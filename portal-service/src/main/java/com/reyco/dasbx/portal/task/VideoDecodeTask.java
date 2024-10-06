package com.reyco.dasbx.portal.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.commons.utils.net.MultipartFileUtils;
import com.reyco.dasbx.commons.utils.tools.vps.VpsUtils;
import com.reyco.dasbx.portal.dao.VideoDao;
import com.reyco.dasbx.portal.model.domain.Video;
import com.reyco.dasbx.portal.model.domain.po.VideoDecodePO;

public class VideoDecodeTask implements Runnable{
	private static Logger logger = LoggerFactory.getLogger(VideoDecodeTask.class);
	public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMddHHmmsss");
	public static final String LINES = "-";
	private VideoDao videoDao;
	private Video video;
	public VideoDecodeTask() {
	}
	public VideoDecodeTask(VideoDao videoDao, Video video) {
		super();
		this.videoDao = videoDao;
		this.video = video;
	}
	public VideoDao getVideoDao() {
		return videoDao;
	}
	public void setVideoDao(VideoDao videoDao) {
		this.videoDao = videoDao;
	}
	public Video getVideo() {
		return video;
	}
	public void setVideo(Video video) {
		this.video = video;
	}
	@Override
	public void run() {
		try {
			// http://oss.dasbx.com/videos/409/新喜剧之王-隐藏了一个重要情节-202408042212049.mp4
			URL url = new URL(video.getSourceUrl());
			// /videos/409/新喜剧之王-隐藏了一个重要情节-202408042212049.mp4
			String path = url.getPath();
			// /usr/local/dasbx/web/work_store_file/videos/409/新喜剧之王-隐藏了一个重要情节-202408042212049.mp4
			String sourceVideoPath = ("/usr/local/dasbx/web/work_store_file/"+path).replace("//", "/");
			// 新喜剧之王-隐藏了一个重要情节
			String filenameVps = path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("-"));
			int vps = VpsUtils.getVps(filenameVps);
			// 新喜剧之王-隐藏了一个重要情节-202408042212049
			String filename = path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."));
			// /usr/local/dasbx/web/work_store_file/videos/hls/409
			String baseHlsPath = "/usr/local/dasbx/web/work_store_file/videos/hls/"+vps;
			MultipartFileUtils.mkdirs(baseHlsPath);
			// /usr/local/dasbx/web/work_store_file/videos/hls/409/新喜剧之王-隐藏了一个重要情节-202408042212049-%d.ts
			String tsVideoPath = baseHlsPath+ "/" + filename + "-%d.ts";
			// /usr/local/dasbx/web/work_store_file/videos/hls/409/新喜剧之王-隐藏了一个重要情节-202408042212049.m3u8
			String m3u8VideoPath = baseHlsPath + "/" + filename + ".m3u8";
			String ffmpegCommand = "ffmpeg -re -i " + sourceVideoPath + " -c copy" + " -hls_time 1"
					+ " -hls_playlist_type vod" + " -hls_segment_filename " + tsVideoPath + " " + m3u8VideoPath;
			logger.info("ffmpegCommand:"+ffmpegCommand);
			Runtime run = Runtime.getRuntime();
			Process p;
			try {
				p = run.exec(ffmpegCommand);
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(p.getErrorStream(), Charset.forName("UTF-8")));
				String line = "";
				while ((line = bufferedReader.readLine()) != null) {
					logger.debug(line);
				}
				logger.info(video+",解码成功");
				VideoDecodePO videoDecodePO = new VideoDecodePO();
				videoDecodePO.setId(video.getId());
				videoDecodePO.setGmtModified(Dasbx.getCurrentTime());
				videoDecodePO.setModifiedBy(1L);
				videoDecodePO.setHls((byte)1);
				videoDao.decode(videoDecodePO);
			} catch (IOException e) {
				logger.error(video+"解码失败,msg:"+e.getMessage());
				e.printStackTrace();
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

	}
}
