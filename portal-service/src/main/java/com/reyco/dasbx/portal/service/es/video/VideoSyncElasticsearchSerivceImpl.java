package com.reyco.dasbx.portal.service.es.video;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.config.es.sync.AbstractSyncElasticsearchService;
import com.reyco.dasbx.es.core.client.ElasticsearchDocument;
import com.reyco.dasbx.portal.constant.Constants;
import com.reyco.dasbx.portal.dao.VideoDao;
import com.reyco.dasbx.portal.dao.VideoProductionDao;
import com.reyco.dasbx.portal.model.domain.Video;
import com.reyco.dasbx.portal.model.domain.vo.VideoProductionInfoVO;
import com.reyco.dasbx.portal.model.es.po.VideoElasticsearchDocument;

@Service("videoSyncElasticsearchSerivce")
public class VideoSyncElasticsearchSerivceImpl extends AbstractSyncElasticsearchService {
	
	@Autowired
	private VideoDao videoDao;
	@Autowired
	private VideoProductionDao videoProductionDao;
	@Override
	protected String getIndexName() {
		return Constants.VIDEO_INDEX_NAME;
	}
	@Override
	protected String getIndexDSL() {
		return "";
	}
	@Override
	protected Long getDocumentMaxId() {
		return videoDao.getMaxId();
	}
	@Override
	protected List<ElasticsearchDocument> getListByLimit(Long startId, Long endId) {
		List<Video> videos = videoDao.getListByLimit(startId, endId);
		List<ElasticsearchDocument> elasticsearchDocuments = convert(videos);
		return elasticsearchDocuments;
	}
	@Override
	protected ElasticsearchDocument getElasticsearchDocumentByPrimaryKeyId(Long primaryKeyId) {
		Video video = videoDao.getById(primaryKeyId);
		ElasticsearchDocument elasticsearchDocument = convert(video);
		return elasticsearchDocument;
	}
	/**
	 * @param video
	 * @return
	 */
	private List<ElasticsearchDocument> convert(List<Video> videos) {
		if(videos==null || videos.size()==0) {
			return new ArrayList<>();
		}
		List<ElasticsearchDocument> elasticsearchDocuments = new ArrayList<>();
		ElasticsearchDocument elasticsearchDocument = null;
		for (Video video : videos) {
			elasticsearchDocument = convert(video);
			elasticsearchDocuments.add(elasticsearchDocument);
		}
		return elasticsearchDocuments;
	}
	/**
	 * video转Document
	 * @param video
	 * @return
	 */
	private VideoElasticsearchDocument convert(Video video) {
		if(video==null) {
			return null;
		}
		VideoElasticsearchDocument videoElasticsearchDocument = Convert.sourceToTarget(video, VideoElasticsearchDocument.class);
		VideoProductionInfoVO videoProductionInfoVO = videoProductionDao.getByVideoId(video.getId());
		videoElasticsearchDocument.setDirector(videoProductionInfoVO.getDirector());
		videoElasticsearchDocument.setStar(videoProductionInfoVO.getStar());
		videoElasticsearchDocument.setIntroduction(videoProductionInfoVO.getIntroduction());
		Set<String> suggestionSet = new HashSet<>();
		suggestionSet.add(videoElasticsearchDocument.getName());
		suggestionSet.add(videoElasticsearchDocument.getDescription());
		String directors = videoElasticsearchDocument.getDirector();
		if(StringUtils.isNotBlank(directors)) {
			String[] directorArray = directors.split(SPLIT_EXPRESSION);
			Arrays.stream(directorArray).forEach(director->suggestionSet.add(director));
		}
		String stars = videoElasticsearchDocument.getStar();
		if(StringUtils.isNotBlank(stars)) {
			String[] starArray = stars.split(SPLIT_EXPRESSION);
			Arrays.stream(starArray).forEach(star->suggestionSet.add(star));
		}
		videoElasticsearchDocument.setSuggestion(new ArrayList<>(suggestionSet));
		return videoElasticsearchDocument;
	}
}
