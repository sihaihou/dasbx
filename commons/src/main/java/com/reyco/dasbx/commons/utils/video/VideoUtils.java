package com.reyco.dasbx.commons.utils.video;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取视频封面
 * @author reyco
 *
 */
public class VideoUtils {
	public static final int DEFAULT_INVALID = 5;
	public static final int DEFAULT_FRAME_NUMBER = 10;
	public static final int DEFAULT_COVER_SIZE = 128;
	public static final int DEFAULT_COVER_WIDTH = 128;
	public static final int DEFAULT_COVER_HEIGHT = 128;
	private static final Logger logger = LoggerFactory.getLogger(VideoUtils.class);
	/**
	 * 根据视频URL地址获取视频封面
	 * @param url			视频url地址
	 * @return
	 */
	public static BufferedImage getCoverImageByUrl(String url) {
		return getCoverImageByUrl(DEFAULT_FRAME_NUMBER, url);
	}
	/**
	 * 根据视频URL地址获取视频封面
	 * @param frameNumber	针
	 * @param url			视频url地址
	 * @return
	 */
	public static BufferedImage getCoverImageByUrl(Integer frameNumber, String url) {
		try {
			URL u = new URL(url);
			return getCoverImage(u.openStream(), frameNumber, DEFAULT_INVALID, DEFAULT_COVER_WIDTH, DEFAULT_COVER_HEIGHT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	/**
	 * 根据视频目录获取视频封面
	 * @param path			本地视频目录
	 * @return
	 */
	public static BufferedImage getCoverImage(String path) {
		return getCoverImage(DEFAULT_FRAME_NUMBER,path);
	}
	/**
	 * 根据视频目录获取视频封面
	 * @param frameNumber	针
	 * @param path			本地视频目录
	 * @return
	 */
	public static BufferedImage getCoverImage(Integer frameNumber, String path) {
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(path));
			return getCoverImage(fileInputStream,frameNumber,DEFAULT_INVALID,DEFAULT_COVER_WIDTH,DEFAULT_COVER_HEIGHT);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据视频流获取视频封面
	 * @param is 视频流
	 * @return
	 */
	public static BufferedImage getCoverImage(InputStream is) {
		return getCoverImage(is,DEFAULT_COVER_SIZE);
	}
	/**
	 * 根据视频流获取视频封面
	 * @param is 视频流
	 * @param size 图片尺寸大小
	 * @return
	 */
	public static BufferedImage getCoverImage(InputStream is,int size) {
		return getCoverImage(is,size,size);
	}
	/**
	 * 根据视频流获取视频封面
	 * @param is 视频流
	 * @param width 图片尺寸宽
	 * @param height 图片尺寸高
	 * @return
	 */
	public static BufferedImage getCoverImage(InputStream is,int width,int height) {
		return getCoverImage(is,DEFAULT_FRAME_NUMBER,DEFAULT_INVALID,width,height);
	}
	/**
	 * 根据视频流获取视频封面
	 * @param frameNumber	针
	 * @param is			视频流
	 * @return
	 */
	@SuppressWarnings("resource")
	public static BufferedImage getCoverImage(InputStream is,Integer frameNumber,int invalid,int width,int height) {
		FFmpegFrameGrabber grabber = null;
		try {
			grabber = new FFmpegFrameGrabber(is);
			grabber.start();
			int ftp = grabber.getLengthInFrames() - invalid;
			frameNumber = ftp < frameNumber ? ftp : frameNumber;
			grabber.setFrameNumber(frameNumber);
			BufferedImage bufferedImage = new Java2DFrameConverter().getBufferedImage(grabber.grabImage());
			String rotate = grabber.getVideoMetadata("rotate");
			if (StringUtils.isNotEmpty(rotate)) {
				bufferedImage = rotate(bufferedImage, Integer.parseInt(rotate));
			}
			return getSubimage(bufferedImage, width, height);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(grabber!=null) {
					grabber.stop();
				}
			} catch (org.bytedeco.javacv.FFmpegFrameGrabber.Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 图片转Base64
	 * @param image	图片
	 * @return
	 */
	public static String bufferedImageToBase64(BufferedImage image) {
		return bufferedImageToBase64(image, null);
	}
	/**
	 * 图片转Base64
	 * @param image    	图片    
	 * @param extension	扩展名
	 * @return
	 */
	public static String bufferedImageToBase64(BufferedImage image,String extension) {
		if(StringUtils.isBlank(extension)) {
			extension = "png";
		}
		if(extension.startsWith(".")) {
			extension = extension.replace(".", "");
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, extension, bos);
			byte[] byteArray = bos.toByteArray();
			String base64Str = Base64.getEncoder().encodeToString(byteArray);
			String base64 = "data:image/"+extension+";base64," + base64Str;
			return base64;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @param image
	 * @param filename
	 * @param coverPath
	 */
	public static void writeCoverImage(BufferedImage image,String filename,String coverPath) {
		String targetFileName = filename.substring(0, filename.lastIndexOf(".")) + ".jpg";
		File imageFile = new File(coverPath + targetFileName);
		try {
			ImageIO.write(image, "jpg", imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取截图:在不变形的情况下尽可能保证图片完整性
	 * @param srcImage
	 * @param targetWidth
	 * @param targetHeight
	 * @return
	 */
	private static BufferedImage getSubimage(BufferedImage srcImage,int targetWidth,int targetHeight) {
		int srcWidth = srcImage.getWidth();
		int srcHeight = srcImage.getHeight();
		//计数缩放比例
		double scale = 1;
		if((srcWidth>targetWidth && srcHeight>targetHeight) || srcWidth<targetWidth && srcHeight<targetHeight) {
			double scaleWidth = (double)srcWidth / targetWidth;    
			double scaleHeight = (double)srcHeight / targetHeight;
			scale = Math.min(scaleWidth, scaleHeight);
		}else if(srcWidth<targetWidth && srcHeight>targetHeight ) {
			scale = (double)srcWidth / targetWidth;
		}else if(srcWidth>targetWidth && srcHeight<targetHeight ) {
			scale = (double)srcHeight / targetHeight;
		}
		//计数真实的宽高
		int realWidth = (int)(targetWidth*scale);
		int realHeight = (int)(targetHeight*scale);
		//宽高差
		int diffWidth = srcWidth-realWidth;
		int diffHeight = srcHeight-realHeight;
		//截取x轴y轴的开始位置
		int x = diffWidth/2;
		int y = diffHeight/2;
		//截取图片
		BufferedImage bufferedImage = srcImage.getSubimage(x, y, realWidth, realHeight);
		//等比缩放到目标大小
		int type = bufferedImage.getType()==0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
		BufferedImage resultImage = new BufferedImage(targetWidth, targetHeight, type);
	    Graphics2D graphics2D = resultImage.createGraphics();
	    graphics2D.drawImage(bufferedImage.getScaledInstance(targetWidth, targetHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
	    graphics2D.dispose();
	    return resultImage;
	}
	/**
	 * 旋转
	 * @param src
	 * @param angel
	 * @return
	 */
	private static BufferedImage rotate(BufferedImage src, int angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        int type = src.getColorModel().getTransparency();
        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);
        BufferedImage bi = new BufferedImage(rect_des.width, rect_des.height, type);
        Graphics2D g2 = bi.createGraphics();
        g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return bi;
    }
	/**
	 * 计算旋转大小
	 * @param src
	 * @param angel
	 * @return
	 */
	private static Rectangle calcRotatedSize(Rectangle src, int angel) {
        if (angel >= 90) {
            if(angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);
        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new java.awt.Rectangle(new Dimension(des_width, des_height));
    }
	/**
	 * m3u8转mp4
	 * @param m3u8Url
	 * @param destPath
	 */
	public static void m3u8ToMp4(String m3u8Url, String destPath) {
		//
		String totalTime = "";
		BigDecimal total = BigDecimal.ONE;
		BigDecimal progress = BigDecimal.ZERO;
		
		String ffmpegCmd = "ffmpeg -i %s -c copy %s";
		String cmd = String.format(ffmpegCmd, m3u8Url,destPath);
		logger.info("转码开始："+cmd);
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), Charset.forName("UTF-8")));
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				if(line.contains("Duration")) {            
					totalTime = parseLineDurationToTime(line);
					total = getDuration(totalTime);
					logger.info("总时间:"+totalTime+",总时长:"+total.doubleValue());
				}
				if(line.contains("frame") && line.contains("time")) {
					String currentTime = parseLineframeToTime(line);
					BigDecimal position = getDuration(currentTime);
					BigDecimal currProgress = computeProgress(total, position);
					if(progress == BigDecimal.ZERO 
							|| currProgress.subtract(progress).compareTo(BigDecimal.ONE) > 0
							|| currProgress.compareTo(new BigDecimal(100))>=0) {
						progress = currProgress;
						logger.debug("当前进度,总时间："+totalTime+",当前时间："+currentTime+",当前进度："+progress + "%");
					}
				}
			}
			logger.info("转码成功："+cmd);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("转码失败："+cmd);
		}
	}
	/**
	 * 计数进度
	 * @param total
	 * @param position
	 * @return
	 */
	private static BigDecimal computeProgress(BigDecimal total, BigDecimal position) {
		BigDecimal progress = position.multiply(new BigDecimal(100)).divide(total, 2, RoundingMode.HALF_UP).setScale(2,RoundingMode.HALF_UP);
		return progress;
	}
	/**
	 * frame=   50 fps= 22 q=-1.0 size=     256kB time=00:00:01.92 bitrate=1088.4kbits/s speed=0.857x-->00:00:01.92
	 * @return
	 */
	private static String parseLineframeToTime(String lineframe) {
		String startChar = "time=";
		String endChar = "bitrate=";
		String time = lineframe.substring(lineframe.indexOf(startChar)+startChar.length(),lineframe.indexOf(endChar)-1); //00:00:01.92
		return time;
	}
	/**
	 * Duration: 00:08:13.24, start: 1.457000, bitrate: 0 kb/s --> 00:08:13.24
	 * @param line
	 * @return
	 */
	private static String parseLineDurationToTime(String lineDuration) {
		String startChar = "Duration:";
		String endChar = ",";
		String duration = lineDuration.substring(lineDuration.indexOf(startChar)+startChar.length()+2,lineDuration.indexOf(endChar)); //00:08:13.24
		return duration;
	}
	/**
	 * 00:08:13.24 --> 493.02
	 * @param timeStr
	 * @return
	 */
	private static BigDecimal getDuration(String timeStr) {
		String HHmmss = timeStr.substring(0, timeStr.indexOf("."));
		String SSS = timeStr.substring(timeStr.indexOf(".")+1);
		String[] arr = HHmmss.split(":");
		BigDecimal duration = BigDecimal.ZERO;
		int t = 1;
		for(int i=arr.length-1;i>=0;i--) {
			int num = Integer.parseInt(arr[i]);
			duration = duration.add(new BigDecimal(t*num));
			t *= 60;
		}
		int SSSDuration = Integer.parseInt(SSS);
		BigDecimal SSBigDecimal = new BigDecimal(SSSDuration);
		SSBigDecimal = SSBigDecimal.divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP);
		duration = duration.add(SSBigDecimal);
		return duration;
	}
	public static void main(String[] args) throws Exception {
		BufferedImage coverImage = getCoverImage(10, "D:/usr/local/dasbx/web/work_store_file/videos/1.mp4");
		writeCoverImage(coverImage, "1.jpg", "D:\\usr\\local\\dasbx\\web\\work_store_file\\videos\\");
	}
}
