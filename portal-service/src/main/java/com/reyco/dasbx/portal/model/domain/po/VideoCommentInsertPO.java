package com.reyco.dasbx.portal.model.domain.po;

import com.reyco.dasbx.model.po.SimpleInsertPO;

public class VideoCommentInsertPO extends SimpleInsertPO{
	private Long videoId;
	private Long rootId;
	private Long parentId;
	
	private Long userId;
	private String nickname;
	private String userFaceUri;
	
	private String content;
	
	private Long time;
	private String ip;
	private String province;
	private String city;
	private String district;
	private String address;
	
	private Integer likeQuantity;
	private Integer commentQuantity;
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	public Long getRootId() {
		return rootId;
	}
	public void setRootId(Long rootId) {
		this.rootId = rootId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getUserFaceUri() {
		return userFaceUri;
	}
	public void setUserFaceUri(String userFaceUri) {
		this.userFaceUri = userFaceUri;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getLikeQuantity() {
		return likeQuantity;
	}
	public void setLikeQuantity(Integer likeQuantity) {
		this.likeQuantity = likeQuantity;
	}
	public Integer getCommentQuantity() {
		return commentQuantity;
	}
	public void setCommentQuantity(Integer commentQuantity) {
		this.commentQuantity = commentQuantity;
	}
}
