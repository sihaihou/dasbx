package com.reyco.dasbx.portal.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.commons.utils.convert.Convert;
import com.reyco.dasbx.commons.utils.serializable.ToString;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.es.core.client.ElasticsearchClient;
import com.reyco.dasbx.es.core.search.SearchVO;
import com.reyco.dasbx.es.core.search.Sort;
import com.reyco.dasbx.lock.annotation.Lock;
import com.reyco.dasbx.model.constants.CachePrefixInfoConstants;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.vo.SysAccountToken;
import com.reyco.dasbx.portal.constant.Constants;
import com.reyco.dasbx.portal.dao.VideoDao;
import com.reyco.dasbx.portal.dao.VideoProductionDao;
import com.reyco.dasbx.portal.model.domain.Video;
import com.reyco.dasbx.portal.model.domain.dto.VideoInsertDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoPlayEventDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoProductionInsertDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoReviewDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoSearchDto;
import com.reyco.dasbx.portal.model.domain.po.VideoInsertPO;
import com.reyco.dasbx.portal.model.domain.po.VideoPlayPO;
import com.reyco.dasbx.portal.model.domain.po.VideoProductionInsertPO;
import com.reyco.dasbx.portal.model.domain.po.VideoReviewPO;
import com.reyco.dasbx.portal.model.domain.vo.CategoryListVO;
import com.reyco.dasbx.portal.model.domain.vo.CountryListVO;
import com.reyco.dasbx.portal.model.domain.vo.TypeListVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoInfoVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoListDetailVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoListVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoProductionInfoVO;
import com.reyco.dasbx.portal.model.domain.vo.YearListVO;
import com.reyco.dasbx.portal.model.es.po.VideoElasticsearchDocument;
import com.reyco.dasbx.portal.model.msg.VideoMessage;
import com.reyco.dasbx.portal.monitor.HybridLoadMonitor;
import com.reyco.dasbx.portal.service.CategoryService;
import com.reyco.dasbx.portal.service.CountryService;
import com.reyco.dasbx.portal.service.TypeService;
import com.reyco.dasbx.portal.service.VideoProductionService;
import com.reyco.dasbx.portal.service.VideoService;
import com.reyco.dasbx.portal.service.YearService;
import com.reyco.dasbx.portal.service.es.video.VideoListSearch;
import com.reyco.dasbx.portal.service.es.video.VideoSearch;
import com.reyco.dasbx.rabbitmq.service.RabbitProducrService;
import com.reyco.dasbx.sync.es.AbstractElasticsearchSync;
import com.reyco.dasbx.sync.exception.SyncException;

@Service
public class VideoServiceImpl implements VideoService {

	private static Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);
	
	private static final int MAX_BLOCKING_QUEUE_SIZE = 10000;
	
	private AtomicInteger roundRobinIndex = new AtomicInteger(0);
	
	@Autowired
	private VideoDao videoDao;
	@Autowired
	private VideoService videoService;
	@Autowired
	private VideoSearch videoSearch;
	@Autowired
	private VideoListSearch videoListSearch;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CountryService countryService;
	@Autowired
	private TypeService typeService;
	@Autowired
	private YearService yearService;
	@Autowired
	private VideoProductionDao videoProductionDao;
	@Autowired
	private VideoProductionService videoProductionService;
	@Autowired
	private RabbitProducrService rabbitProducrService;
	
	@Resource(name="videoSyncElasticsearchSerivce")
	private AbstractElasticsearchSync<Long,VideoElasticsearchDocument> syncElasticsearchService;
	@Autowired
	private ElasticsearchClient<VideoElasticsearchDocument> elasticsearchClient;
	
	private ExecutorService executor;
	private int corePoolSize;
	
	private List<PlayEventNotify> playEventNotifys = new ArrayList<>();
	private int playEventNotifySize;
	
	private HybridLoadMonitor loadMonitor;
	
	public VideoServiceImpl() {
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		this.corePoolSize = availableProcessors;
		this.playEventNotifySize = corePoolSize;
		this.loadMonitor = new HybridLoadMonitor(playEventNotifys, MAX_BLOCKING_QUEUE_SIZE);
		executor = new ThreadPoolExecutor(corePoolSize,corePoolSize,60,TimeUnit.SECONDS, 
				new ArrayBlockingQueue<>(50),
				new ThreadFactory() {
					LongAdder count = new LongAdder();
					@Override
					public Thread newThread(Runnable r) {
						count.increment();
						Thread t = new Thread(r);
						t.setDaemon(true);
						t.setName("com.reyco.dasbx.portal.playEventNotify-" + count.intValue());
						return t;
					}
				}
		);
		
	}
	@PostConstruct
	public void init() {
		for(int i=0;i<playEventNotifySize;i++) {
			PlayEventNotify playEventNotify = new PlayEventNotify();
			playEventNotifys.add(playEventNotify);
			executor.execute(playEventNotify);
		}
	}
	@Override
	@Lock(name="video:init")
	public int initElasticsearchVideo() throws SyncException {
		return syncElasticsearchService.fullSync();
	}
	@Override
	@Cacheable(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.PORTAL_VIDEO_INFO_PREFIX,key="#id")
	public VideoInfoVO get(Long id) {
		Video video = videoDao.getById(id);
		if (video == null) {
			return null;
		}
		VideoInfoVO videoInfoVO = buildIfNecessary(video);
		return videoInfoVO;
	}
	@Override
	public List<String> getSuggestion(String keyword) throws Exception {
		List<String> suggestion = elasticsearchClient.getSuggestion(Constants.VIDEO_INDEX_NAME, keyword);
		if (suggestion == null) {
			suggestion = new ArrayList<>();
		}
		return suggestion;
	}
	@Override
	public SearchVO<VideoListVO> search(VideoSearchDto videoSearchDto) throws IOException {
		buildSortIfNecessary(videoSearchDto);
		return videoSearch.search(videoSearchDto);
	}
	@Override
	public SearchVO<VideoListDetailVO> searchBack(VideoSearchDto videoSearchDto) throws IOException {
		buildSortIfNecessary(videoSearchDto);
		return videoListSearch.search(videoSearchDto);
	}
	
	@Override
	@Lock(name="video:add:name:",key="#videoInsertDto.name")
	public void add(VideoInsertDto videoInsertDto) throws AuthenticationException {
		logger.debug("Video Add,videoInsertDto:{}",videoInsertDto);
		VideoInsertPO videoInsertPO = Convert.sourceToTarget(videoInsertDto, VideoInsertPO.class);
		SysAccountToken sysAccountToken = TokenUtils.getToken();
		videoInsertPO.setUploadBy(sysAccountToken.getId());
		videoDao.insert(videoInsertPO);
		VideoProductionInsertDto videoProductionInsertDto = videoInsertDto.getVideoProductionInsertDto();
		if(videoProductionInsertDto==null) {
			videoProductionInsertDto = new VideoProductionInsertDto();
		}
		VideoProductionInsertPO videoProductionInsertPO = Convert.sourceToTarget(videoProductionInsertDto, VideoProductionInsertPO.class);
		videoProductionInsertPO.setVideoId(videoInsertPO.getId());
		videoProductionDao.insert(videoProductionInsertPO);
		Video video = Convert.sourceToTarget(videoInsertPO, Video.class);
		publishVideoAddEvent(video.getId());
	}
	
	@Override
	@CacheEvict(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.PORTAL_VIDEO_INFO_PREFIX,key="#videoReviewDto.id")
	public void review(VideoReviewDto videoReviewDto) {
		VideoReviewPO videoReviewPO = Convert.sourceToTarget(videoReviewDto, VideoReviewPO.class);
		videoReviewPO.setState(videoReviewDto.getReview()?(byte)1:(byte)2);
		videoDao.review(videoReviewPO);
		publishVideoAddEvent(videoReviewPO.getId());
	}
	@Override
	@CacheEvict(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.PORTAL_VIDEO_INFO_PREFIX,key="#videoPlayEventDto.id")
	public void addPlayEvent(VideoPlayEventDto videoPlayEventDto) {
		// 记录请求
        loadMonitor.recordRequest();
        
		PlayEvent playEvent = new PlayEvent();
		playEvent.setVideoId(videoPlayEventDto.getId());
		playEvent.setUserId(videoPlayEventDto.getUserId());
		playEvent.setPlayTime(Dasbx.getCurrentTime());
		PlayEventNotify playEventNotify = getLeastLoadedPlayEventNotify();
		playEventNotify.addTask(playEvent);
	}
	
	@Override
	public void processPlay(List<PlayEvent> playEvents) {
		Map<Long, Long> playTimeMap = playEvents.stream().collect(Collectors.groupingBy(PlayEvent::getVideoId,Collectors.counting()));
		
		/*Lists.partition(new ArrayList<>(playTimeMap.entrySet()), 20).forEach(batch -> {
        	List<VideoPlayPO> videoPlayPOs = new ArrayList<>();
        	batch.forEach(entry -> {
        		VideoPlayPO videoPlayPO = new VideoPlayPO();
    			videoPlayPO.setId(entry.getKey());
    			videoPlayPO.setPlayQuantity(entry.getValue().intValue());
    			videoPlayPOs.add(videoPlayPO);
        	});
        	videoDao.updatePlay(videoPlayPOs);
        	
        	videoPlayPOs.stream().forEach(VideoPlayPO->{
    			publishVideoAddEvent(VideoPlayPO.getId());
    		});
        });*/
		
		// 一次性批量更新，而不是分片
	    List<VideoPlayPO> videoPlayPOs = playTimeMap.entrySet().stream()
	        .map(entry -> {
	            VideoPlayPO po = new VideoPlayPO();
	            po.setId(entry.getKey());
	            po.setPlayQuantity(entry.getValue().intValue());
	            return po;
	        })
	        .collect(Collectors.toList());
	    
	    // 使用MyBatis批量更新
	    videoDao.updatePlay(videoPlayPOs);
	    
	    videoPlayPOs.stream().forEach(VideoPlayPO->{
			publishVideoAddEvent(VideoPlayPO.getId());
		});
	}
	public class PlayEventNotify implements Runnable {
		private int maxBlockingQueueSize = 10000;
		private BlockingQueue<PlayEvent> blockingQueue = new ArrayBlockingQueue<PlayEvent>(maxBlockingQueueSize);
		private Long intervalTime = 1000L;
		public PlayEventNotify() {
		}
		public boolean addTask(PlayEvent playEvent) {
			try {
		        return blockingQueue.offer(playEvent, 50, TimeUnit.MILLISECONDS);
		    } catch (InterruptedException e) {
		        Thread.currentThread().interrupt();
		        return false;
		    }
		}
		@Override
		public void run() {
			while (true) {
				try {
					List<PlayEvent> playEvents = new ArrayList<>(100);
					long startTime = System.currentTimeMillis();
					while (playEvents.size()<100 && System.currentTimeMillis() - startTime < intervalTime) {
						PlayEvent playEvent = blockingQueue.poll(1000-(System.currentTimeMillis()-startTime),TimeUnit.MILLISECONDS);
						if (playEvent != null) {
							playEvents.add(playEvent);
						}
					}
					if (playEvents.size() > 0) {
						process(playEvents);
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		public void process(List<PlayEvent> playEvents) {
			videoService.processPlay(playEvents);
		}
		public BlockingQueue<PlayEvent> getBlockingQueue() {
			return blockingQueue;
		}
		public int getMaxBlockingQueueSize() {
			return maxBlockingQueueSize;
		}
	}
	/**
	 * 使用轮询+负载检查选择一个PlayEventNotify
	 * @return
	 */
	public PlayEventNotify getLeastLoadedPlayEventNotify() {
	    int startIndex = roundRobinIndex.getAndIncrement() % playEventNotifys.size();
	    PlayEventNotify selected = playEventNotifys.get(startIndex);
	    
	    // 动态阈值：系统负载高时降低阈值
	    double loadFactor = loadMonitor.getSystemLoadFactor(); // 0.0-1.0
	    double dynamicThresholdRatio = 0.75 - (loadFactor * 0.25); // 75% ~ 50%
	    int threshold = (int) (selected.getMaxBlockingQueueSize() * dynamicThresholdRatio);
	    
	    // 快速检查，如果队列不太满就直接返回
	    if (selected.getBlockingQueue().size() < threshold) {
	        return selected;
	    }
	    
	    // 快速查找：检查接下来几个队列，避免全局遍历
	    for (int i = 1; i < Math.min(3, playEventNotifys.size()); i++) {
	        PlayEventNotify next = playEventNotifys.get((startIndex + i) % playEventNotifys.size());
	        if (next.getBlockingQueue().size() < threshold) {
	            return next;
	        }
	    }
	    
	    // 否则找真正的最小队列
	    return playEventNotifys.stream()
	            .min(Comparator.comparingInt(p -> p.getBlockingQueue().size()))
	            .orElse(selected);
	}
	
	public class PlayEvent extends ToString {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6406219741912969199L;
		private Long videoId;
		private Long userId;
		private Long playTime;
		public Long getVideoId() {
			return videoId;
		}
		public void setVideoId(Long videoId) {
			this.videoId = videoId;
		}
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public Long getPlayTime() {
			return playTime;
		}
		public void setPlayTime(Long playTime) {
			this.playTime = playTime;
		}
	}
	/**
	 * 发布视频事件
	 * @param sysAccountToken
	 */
	private void publishVideoAddEvent(Long videoId) {
		VideoMessage videoDecodeMessage = new VideoMessage();
		videoDecodeMessage.setVideoId(videoId);
		rabbitProducrService.send(RabbitConstants.VIDEO_FANOUT_EXCHANGE, null, videoDecodeMessage);
	}
	private VideoInfoVO buildIfNecessary(Video video){
		VideoInfoVO videoInfoVO = Convert.sourceToTarget(video, VideoInfoVO.class);
		buildIfNecessary(videoInfoVO);
		return videoInfoVO;
	}
	private void buildIfNecessary(VideoInfoVO videoInfoVO){
		CategoryListVO categoryListVO = categoryService.get(videoInfoVO.getCategoryId());
		videoInfoVO.setCategoryName(categoryListVO.getName());
		CountryListVO countryListVO = countryService.get(videoInfoVO.getCountryId());
		videoInfoVO.setCountryName(countryListVO.getName());
		TypeListVO typeListVO = typeService.get(videoInfoVO.getTypeId());
		videoInfoVO.setTypeName(typeListVO.getName());
		YearListVO yearListVO = yearService.get(videoInfoVO.getYearId());
		videoInfoVO.setYearName(yearListVO.getName());
		VideoProductionInfoVO videoProductionInfoVO = videoProductionService.getByVideoId(videoInfoVO.getId());
		videoInfoVO.setVideoProductionInfoVO(videoProductionInfoVO);
	}
	private void buildSortIfNecessary(VideoSearchDto videoSearchDto) {
		Byte sortValue = videoSearchDto.getSort();
		List<Sort> sorts = videoSearchDto.getSorts();
		if(sortValue.intValue()==-1) {
			if(sorts==null) {
				sorts = new ArrayList<>();
				videoSearchDto.setSorts(sorts);
			}
			Sort playQuantitySort = new Sort();
			playQuantitySort.setField("playQuantity");
			playQuantitySort.setSortOrder(SortOrder.DESC);
			sorts.add(playQuantitySort);
			Sort heatQuantitySort = new Sort();
			heatQuantitySort.setField("heatQuantity");
			heatQuantitySort.setSortOrder(SortOrder.DESC);
			sorts.add(heatQuantitySort);
		}
		if(sortValue.intValue()==1) {
			if(sorts==null) {
				sorts = new ArrayList<>();
				videoSearchDto.setSorts(sorts);
			}
			Sort heatQuantitySort = new Sort();
			heatQuantitySort.setField("heatQuantity");
			heatQuantitySort.setSortOrder(SortOrder.DESC);
			sorts.add(heatQuantitySort);
		}
		if(sortValue.intValue()==2) {
			if(sorts==null) {
				sorts = new ArrayList<>();
				videoSearchDto.setSorts(sorts);
			}
			Sort playQuantitySort = new Sort();
			playQuantitySort.setField("playQuantity");
			playQuantitySort.setSortOrder(SortOrder.DESC);
			sorts.add(playQuantitySort);
		}
	}
}
