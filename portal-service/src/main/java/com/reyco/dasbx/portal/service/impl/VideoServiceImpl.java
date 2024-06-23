package com.reyco.dasbx.portal.service.impl;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.commons.utils.ToString;
import com.reyco.dasbx.portal.dao.VideoDao;
import com.reyco.dasbx.portal.dao.VideoProductionDao;
import com.reyco.dasbx.portal.model.domain.Video;
import com.reyco.dasbx.portal.model.domain.dto.VideoPageDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoPlayEventDto;
import com.reyco.dasbx.portal.model.domain.po.VideoPlayPO;
import com.reyco.dasbx.portal.model.domain.po.VideoSelectPO;
import com.reyco.dasbx.portal.model.domain.vo.VideoInfoVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoListVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoProductionInfoVO;
import com.reyco.dasbx.portal.service.VideoService;

@Service
public class VideoServiceImpl implements VideoService {

	private static Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);
	@Autowired
	private VideoDao videoDao;
	@Autowired
	private VideoService videoService;
	@Autowired
	private VideoProductionDao videoProductionDao;

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
	public VideoInfoVO get(Long id) {
		logger.info("id:"+id);
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

}
