package com.reyco.dasbx.portal.rabbitmq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;
import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.commons.utils.MultipartFileUtils;
import com.reyco.dasbx.commons.utils.VpsUtils;
import com.reyco.dasbx.config.rabbitmq.service.AbstractRabbitConsumerService;
import com.reyco.dasbx.config.rabbitmq.service.RabbitMessageType;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.dto.SysMessageInsertDto;
import com.reyco.dasbx.model.msg.RabbitMessage;
import com.reyco.dasbx.model.msg.VideoMessage;
import com.reyco.dasbx.portal.dao.VideoDao;
import com.reyco.dasbx.portal.model.domain.Video;
import com.reyco.dasbx.portal.model.domain.po.VideoDecodePO;
import com.reyco.dasbx.portal.service.SysMessageService;

@Service
public class VideoDecodeRabbitConsumerService extends AbstractRabbitConsumerService{

	private static final Logger logger = LoggerFactory.getLogger(VideoDecodeRabbitConsumerService.class);

	@Autowired
	private VideoDao videoDao;
	@Autowired
	private SysMessageService sysMessageService;

	@RabbitHandler
	@RabbitListener(bindings = @QueueBinding(
			exchange = @Exchange(value = RabbitConstants.VIDEO_FANOUT_EXCHANGE, type = ExchangeTypes.FANOUT, durable = "true", autoDelete = "false"), 
			value = @Queue(value = RabbitConstants.VIDEO_FANOUT_DECODE_QUEUE, durable = "true", exclusive = "false", autoDelete = "false")),
			containerFactory="rabbitListenerContainerFactory")
	public void log(VideoMessage videoMessage, Channel channel, Message message) {
		execute(videoMessage, channel, message);
	}
	@Override
	protected void handler(RabbitMessage rabbitMessage) throws Exception {
		VideoMessage videoMessage = (VideoMessage)rabbitMessage;
		Video video = videoDao.getById(videoMessage.getVideoId());
		decode(video);
	}
	@Override
	protected void handlerExceptionRabbitMessage(RabbitMessage rabbitMessage,Exception e) throws Exception {
		SysMessageInsertDto sysMessageInsertDto = new SysMessageInsertDto();
		sysMessageInsertDto.setType(1L);
		sysMessageInsertDto.setUserId(1L);
		sysMessageInsertDto.setBuzId(rabbitMessage.getCorrelationDataId());
		sysMessageInsertDto.setBuzType((byte)2);
		sysMessageInsertDto.setBuzName("视频解码失败");
		sysMessageInsertDto.setContent("视频解码失败,需要人工介入处理...");
		sysMessageService.insert(sysMessageInsertDto);
	}
	@Override
	protected RabbitMessageType getRabbitMessageType() {
		return new RabbitMessageType() {
			@Override
			public byte getType() {
				return 2;
			}
			
			@Override
			public String getRetryKey() {
				return CachePrefixConstants.VIDEO_DECODE_RETRY;
			}
		};
	}
	private void decode(Video video) throws Exception {
		if(video.getHls().intValue()==1) {
			return;
		}
		try {
			// http://oss.dasbx.com/videos/409/新喜剧之王-隐藏了一个重要情节-202408042212049.mp4
			URL url = new URL(video.getSourceUrl());
			// /videos/409/新喜剧之王-隐藏了一个重要情节-202408042212049.mp4
			String path = url.getPath();
			// /usr/local/dasbx/web/work_store_file/videos/409/新喜剧之王-隐藏了一个重要情节-202408042212049.mp4
			String sourceVideoPath = ("/usr/local/dasbx/web/work_store_file/"+path).replace("//", "/");
			// /usr/local/dasbx/web/work_store_file/hls/409
			String baseHlsPath = getHlsPath(path);
			String ffmpegCommand = getFFmpegCommand(sourceVideoPath, baseHlsPath);
			logger.info("ffmpegCommand:"+ffmpegCommand);
			Runtime run = Runtime.getRuntime();
			Process p = run.exec(ffmpegCommand);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), Charset.forName("UTF-8")));
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
			String playUrl = getPlayUrl(url.getProtocol()+"://"+url.getHost(), sourceVideoPath, baseHlsPath);
			videoDecodePO.setPlayUrl(playUrl);
			videoDao.decode(videoDecodePO);
		} catch (IOException e) {
			logger.error(video+"解码失败,msg:"+e.getMessage());
			throw new RuntimeException("解码失败");
		}
	}
	/**
	 * 获取FFmpegCommand
	 * @param sourceVideoPath	/usr/local/dasbx/web/work_store_file/videos/409/新喜剧之王-隐藏了一个重要情节-202408042212049.mp4
	 * @param baseHlsPath		/usr/local/dasbx/web/work_store_file/hls/409
	 * @return
	 */
	private static String getFFmpegCommand(String sourceVideoPath,String baseHlsPath) {
		// 新喜剧之王-隐藏了一个重要情节-202408042212049
		String filename = getFilename(sourceVideoPath);
		// /usr/local/dasbx/web/work_store_file/hls/409/新喜剧之王-隐藏了一个重要情节-202408042212049-%d.ts
		String tsVideoPath = getVideoTsPath(baseHlsPath, filename);
		// /usr/local/dasbx/web/work_store_file/hls/409/新喜剧之王-隐藏了一个重要情节-202408042212049.m3u8
		String m3u8VideoPath = getVideoM3u8Path(baseHlsPath, filename);
		String ffmpegCommand = "ffmpeg -re -i " + sourceVideoPath + " -c copy" + " -hls_time 1"
				+ " -hls_playlist_type vod" + " -hls_segment_filename " + tsVideoPath + " " + m3u8VideoPath;
		return ffmpegCommand;
	}
	/**
	 * 
	 * @param baseHlsPath
	 * @param filename
	 * @return
	 */
	private static String getVideoTsPath(String baseHlsPath,String filename) {
		// /usr/local/dasbx/web/work_store_file/hls/409/新喜剧之王-隐藏了一个重要情节-202408042212049-%d.ts
		String tsVideoPath = baseHlsPath+ "/" + filename + "-%d.ts";
		return tsVideoPath;
	}
	/**
	 * 
	 * @param baseHlsPath
	 * @param filename
	 * @return
	 */
	private static String getVideoM3u8Path(String baseHlsPath,String filename) {
		// /usr/local/dasbx/web/work_store_file/hls/409/新喜剧之王-隐藏了一个重要情节-202408042212049.m3u8
		String m3u8VideoPath = baseHlsPath + "/" + filename + ".m3u8";
		return m3u8VideoPath;
	}
	/**
	 * 获取解码目录
	 * @param path	/videos/409/新喜剧之王-隐藏了一个重要情节-202408042212049.mp4
	 * @return
	 */
	private static String getHlsPath(String path) {
		// filenameToVps=新喜剧之王-隐藏了一个重要情节
		String filenameToVps = path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("-"));
		int vps = VpsUtils.getVps(filenameToVps);
		// /usr/local/dasbx/web/work_store_file/hls/409
		String baseHlsPath = "/usr/local/dasbx/web/work_store_file/hls/"+vps;
		MultipartFileUtils.mkdirs(baseHlsPath);
		return baseHlsPath;
	}
	private static String getFilename(String sourceVideoPath) {
		// 新喜剧之王-隐藏了一个重要情节-202408042212049
		String filename = sourceVideoPath.substring(sourceVideoPath.lastIndexOf("/")+1, sourceVideoPath.lastIndexOf("."));
		return filename;
	}
	private static String getPlayUrl(String host,String sourceVideoPath,String baseHlsPath) {
		// 新喜剧之王-隐藏了一个重要情节-202408042212049
		String filename = getFilename(sourceVideoPath);
		// /usr/local/dasbx/web/work_store_file/hls/409/新喜剧之王-隐藏了一个重要情节-202408042212049.m3u8
		String m3u8VideoPath = getVideoM3u8Path(baseHlsPath, filename);
		String playUrl = host+m3u8VideoPath.replace("/usr/local/dasbx/web/work_store_file", "");
		return playUrl;
	}
}
