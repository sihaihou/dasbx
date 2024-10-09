package com.reyco.dasbx.portal.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.reyco.dasbx.commons.utils.video.VideoUtils;

public class Test3 {
	public static void main(String[] args) throws IOException {
		p1();
	}
	public static void p1() {
		getLists().stream().forEach(videoMap -> {
			String videoFilename = videoMap.get("name");
			String destPath = "D:\\usr\\local\\file\\mp4\\" + videoFilename + ".mp4";
			String m3u8Url = videoMap.get("url");
			VideoUtils.m3u8ToMp4(m3u8Url, destPath);
		});
	}
	private static List<Map<String, String>> getLists() {
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		getVideos().stream().forEach(videoStr -> {
			String[] video = videoStr.split("---");
			Map<String, String> map1 = new HashMap<>();
			map1.put("name", video[0]);
			map1.put("url", video[1]);
			lists.add(map1);
		});
		return lists;
	}

	private static List<String> getVideos() {
		List<String> videos = new ArrayList<String>();
		videos.add("但行好事-莫问前程---http://oss.subixin.com/videos/hls/821/1629905908533.m3u8");
		return videos;
	}

}
