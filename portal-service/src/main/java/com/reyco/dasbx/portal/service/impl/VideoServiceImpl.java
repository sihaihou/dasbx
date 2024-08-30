package com.reyco.dasbx.portal.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.commons.utils.ToString;
import com.reyco.dasbx.config.es.sync.SyncElasticsearchService;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.rabbitmq.service.RabbitProducrService;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.es.core.client.ElasticsearchClient;
import com.reyco.dasbx.es.core.search.SearchVO;
import com.reyco.dasbx.es.core.search.Sort;
import com.reyco.dasbx.lock.annotation.Lock;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.msg.VideoMessage;
import com.reyco.dasbx.model.vo.SysAccountToken;
import com.reyco.dasbx.portal.constant.Constants;
import com.reyco.dasbx.portal.dao.VideoDao;
import com.reyco.dasbx.portal.dao.VideoProductionDao;
import com.reyco.dasbx.portal.model.domain.Video;
import com.reyco.dasbx.portal.model.domain.dto.VideoInsertDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoPageDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoPlayEventDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoProductionInsertDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoReviewDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoSearchDto;
import com.reyco.dasbx.portal.model.domain.po.VideoInsertPO;
import com.reyco.dasbx.portal.model.domain.po.VideoPlayPO;
import com.reyco.dasbx.portal.model.domain.po.VideoProductionInsertPO;
import com.reyco.dasbx.portal.model.domain.po.VideoReviewPO;
import com.reyco.dasbx.portal.model.domain.po.VideoSelectPO;
import com.reyco.dasbx.portal.model.domain.vo.VideoInfoVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoListVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoProductionInfoVO;
import com.reyco.dasbx.portal.model.es.po.VideoElasticsearchDocument;
import com.reyco.dasbx.portal.service.VideoService;
import com.reyco.dasbx.portal.service.es.video.VideoSearch;

@Service
public class VideoServiceImpl implements VideoService {

	private static Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);
	@Autowired
	private VideoDao videoDao;
	@Autowired
	private VideoService videoService;
	@Autowired
	private VideoSearch videoSearch;
	@Autowired
	private VideoProductionDao videoProductionDao;
	@Autowired
	private RabbitProducrService rabbitProducrService;
	
	@Resource(name="videoSyncElasticsearchSerivce")
	private SyncElasticsearchService syncElasticsearchService;
	@Autowired
	private ElasticsearchClient<VideoElasticsearchDocument> elasticsearchClient;
	
	private ExecutorService executor = new ThreadPoolExecutor(5,10,60,TimeUnit.SECONDS, 
			new LinkedBlockingQueue<>(),
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
	private List<PlayEventNotify> playEventNotifys = new ArrayList<>();

	@PostConstruct
	public void init() {
		for(int i=0;i<5;i++) {
			PlayEventNotify playEventNotify = new PlayEventNotify();
			playEventNotifys.add(playEventNotify);
			executor.execute(playEventNotify);
		}
	}
	@Override
	public int initElasticsearchVideo() throws IOException {
		return syncElasticsearchService.initElasticsearch();
	}
	@Override
	public VideoInfoVO get(Long id) {
		Video video = videoDao.getById(id);
		if (video == null) {
			return null;
		}
		VideoInfoVO videoInfoVO = Convert.sourceToTarget(video, VideoInfoVO.class);
		VideoProductionInfoVO videoProductionInfoVO = videoProductionDao.getByVideoId(video.getId());
		videoInfoVO.setVideoProductionInfoVO(videoProductionInfoVO);
		return videoInfoVO;
	}

	@Override
	public PageInfo<VideoListVO> list(VideoPageDto videoPageDto) {
		VideoSelectPO videoSelectPO = new VideoSelectPO();
		videoSelectPO.setCategoryId(videoPageDto.getCategoryId());
		videoSelectPO.setName(videoPageDto.getKeyword());
		PageHelper.startPage(videoPageDto.getPageNum(), videoPageDto.getPageSize());
		List<VideoListVO> videoListVOs = videoDao.list(videoSelectPO);
		PageInfo<VideoListVO> videoListVOPageInfo = new PageInfo<>(videoListVOs);
		return videoListVOPageInfo;
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
		return videoSearch.search(videoSearchDto);
	}
	
	@Override
	@Lock(value="#videoInsertDto.name")
	public void add(VideoInsertDto videoInsertDto) throws AuthenticationException {
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
		publishVideoAddEvent(video);
	}
	
	@Override
	public void review(VideoReviewDto videoReviewDto) {
		VideoReviewPO videoReviewPO = Convert.sourceToTarget(videoReviewDto, VideoReviewPO.class);
		videoReviewPO.setState(videoReviewDto.getReview()?(byte)1:(byte)2);
		videoDao.review(videoReviewPO);
	}
	@Override
	public void addPlayEvent(VideoPlayEventDto videoPlayEventDto) {
		PlayEvent playEvent = new PlayEvent();
		playEvent.setVideoId(videoPlayEventDto.getId());
		playEvent.setUserId(videoPlayEventDto.getUserId());
		playEvent.setPlayTime(Dasbx.getCurrentTime());
		playEventNotifys.get(new Random().nextInt(playEventNotifys.size())).addTask(playEvent);
	}
	
	@Override
	public void processPlay(List<PlayEvent> playEvents) {
		Map<Long, Long> playTimeMap = playEvents.stream().collect(Collectors.groupingBy(PlayEvent::getVideoId,Collectors.counting()));
		List<VideoPlayPO> videoPlayPOs = new ArrayList<>();
		VideoPlayPO videoPlayPO = null;
		for(Map.Entry<Long, Long> enter: playTimeMap.entrySet()) {
			videoPlayPO = new VideoPlayPO();
			videoPlayPO.setId(enter.getKey());
			videoPlayPO.setPlayQuantity(enter.getValue().intValue());
			videoPlayPOs.add(videoPlayPO);
		}
		videoDao.updatePlay(videoPlayPOs);
	}
	
	public class PlayEventNotify implements Runnable {
		private BlockingQueue<PlayEvent> blockingQueue = new LinkedBlockingQueue<PlayEvent>();
		public void addTask(PlayEvent playEvent) {
			blockingQueue.add(playEvent);
		}
		@Override
		public void run() {
			while (true) {
				try {
					long currentTimeMillis = System.currentTimeMillis();//1000
					List<PlayEvent> playEvents = new ArrayList<>(100);
					while ((System.currentTimeMillis() - 1000 < currentTimeMillis) && playEvents.size() < 100) {
						PlayEvent playEvent = blockingQueue.poll(1000-(System.currentTimeMillis()-currentTimeMillis),TimeUnit.MILLISECONDS);
						if (playEvent != null) {
							playEvents.add(playEvent);
						}
					}
					if (playEvents.size() > 0) {
						process(playEvents);
					}
				} catch (Exception e) {

				}
			}
		}
		public void process(List<PlayEvent> playEvents) {
			videoService.processPlay(playEvents);
		}
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
	private void publishVideoAddEvent(Video video) {
		VideoMessage videoDecodeMessage = new VideoMessage();
		videoDecodeMessage.setVideoId(video.getId());
		rabbitProducrService.send(RabbitConstants.VIDEO_FANOUT_EXCHANGE, null, videoDecodeMessage);
	}
}
