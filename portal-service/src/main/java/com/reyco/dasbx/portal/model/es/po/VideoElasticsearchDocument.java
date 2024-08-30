package com.reyco.dasbx.portal.model.es.po;

import java.util.List;

import com.reyco.dasbx.es.core.client.ElasticsearchDocument;

/**
 * 视频Document对象
 * @author reyco
 *
 */
public class VideoElasticsearchDocument implements ElasticsearchDocument {
	private Long id;
	//用于搜索的4个字段
	private String name;
	private String description;
	private String director;
	private String star;
	//用于聚合的7个字段
	private Long categoryId;
	private Long countryId;
	private Long typeId;
	private Long yearId;
	private Long vipId;
	private Byte hls;
	private Byte state;
	//字符串
	private String introduction;
	private String sourceUrl;
	private String playUrl;
	private String portraitCoverUrl;
	private String landscapeCoverUrl;
	//用于排序的5个字段
	private Integer playQuantity;
	private Integer commentQuantity;
	private Integer collectionQuantity;
	private Integer shareQuantity;
	private Integer heatQuantity;
	//
	private Long uploadBy;
	private String remark;
	private Long gmtCreate;
	private Long createBy;
	private Long gmtModified;
	private Long modifiedBy;
	//字段补全的字段
	private List<String> suggestion;
	@Override
	public String getPrimaryKeyId() {
		return id.toString();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public Long getYearId() {
		return yearId;
	}
	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}
	public Long getVipId() {
		return vipId;
	}
	public void setVipId(Long vipId) {
		this.vipId = vipId;
	}
	public Byte getHls() {
		return hls;
	}
	public void setHls(Byte hls) {
		this.hls = hls;
	}
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	public String getPlayUrl() {
		return playUrl;
	}
	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}
	public String getPortraitCoverUrl() {
		return portraitCoverUrl;
	}
	public void setPortraitCoverUrl(String portraitCoverUrl) {
		this.portraitCoverUrl = portraitCoverUrl;
	}
	public String getLandscapeCoverUrl() {
		return landscapeCoverUrl;
	}
	public void setLandscapeCoverUrl(String landscapeCoverUrl) {
		this.landscapeCoverUrl = landscapeCoverUrl;
	}
	public Integer getPlayQuantity() {
		return playQuantity;
	}
	public void setPlayQuantity(Integer playQuantity) {
		this.playQuantity = playQuantity;
	}
	public Integer getCommentQuantity() {
		return commentQuantity;
	}
	public void setCommentQuantity(Integer commentQuantity) {
		this.commentQuantity = commentQuantity;
	}
	public Integer getCollectionQuantity() {
		return collectionQuantity;
	}
	public void setCollectionQuantity(Integer collectionQuantity) {
		this.collectionQuantity = collectionQuantity;
	}
	public Integer getShareQuantity() {
		return shareQuantity;
	}
	public void setShareQuantity(Integer shareQuantity) {
		this.shareQuantity = shareQuantity;
	}
	public Integer getHeatQuantity() {
		return heatQuantity;
	}
	public void setHeatQuantity(Integer heatQuantity) {
		this.heatQuantity = heatQuantity;
	}
	public Long getUploadBy() {
		return uploadBy;
	}
	public void setUploadBy(Long uploadBy) {
		this.uploadBy = uploadBy;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Long gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Long getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Long gmtModified) {
		this.gmtModified = gmtModified;
	}
	public Long getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public List<String> getSuggestion() {
		return suggestion;
	}
	public void setSuggestion(List<String> suggestion) {
		this.suggestion = suggestion;
	}
}
