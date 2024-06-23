package com.reyco.dasbx.portal.model.domain.vo;

import java.util.List;

import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.model.vo.ListVO;

public class VideoCommentListVO implements ListVO {
	private Long id;
	
	private Long videoId;
	
	private Long rootId;
	private Long parentId;
	private String parentName;
	
	private Long userId;
	private String nickname;
	private String userFaceUri;
	
	private Long answerId;
	private String answerNickname;
	
	private String content;
	
	private Long time;
	private String timeDesc;
	private String province;
	
	private Integer likeQuantity;
	private Integer commentQuantity;
	private Boolean isLike = false;
	
	private List<VideoCommentListVO> children;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
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
	public Long getAnswerId() {
		return answerId;
	}
	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}
	public String getAnswerNickname() {
		return answerNickname;
	}
	public void setAnswerNickname(String answerNickname) {
		this.answerNickname = answerNickname;
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
		if(time!=null) {
			this.timeDesc = Dasbx.getDateByTimeZone(time,Dasbx.FORMAT_YYYY_MM_DD);
		}
	}
	public String getTimeDesc() {
		return timeDesc;
	}
	public void setTimeDesc(String timeDesc) {
		this.timeDesc = timeDesc;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
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
	public Boolean getIsLike() {
		return isLike;
	}
	public void setIsLike(Boolean isLike) {
		this.isLike = isLike;
	}
	public List<VideoCommentListVO> getChildren() {
		return children;
	}
	public void setChildren(List<VideoCommentListVO> children) {
		this.children = children;
	}
}
