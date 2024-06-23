package com.reyco.dasbx.portal.model.domain;

import com.reyco.dasbx.model.Base;

public class VideoComment extends Base {
	/**
	 * 视频
	 */
	private Long videoId;
	/**
	 * 根评论
	 */
	private Long rootId;
	/**
	 * 父评论
	 */
	private Long parentId;
	/**
	 * 评论人
	 */
	private Long userId;
	private String nickname;
	private String userFaceUri;
	/**
	 * 评论内容
	 */
	private String content;
	/**
	 * 评论时间
	 */
	private Long time;
	/**
	 * 地址
	 */
	private String ip;
	private String province;
	private String city;
	private String district;
	private String address;
	/**
	 * 点赞数
	 */
	private Integer likeQuantity;
	/**
	 * 评论数
	 */
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
