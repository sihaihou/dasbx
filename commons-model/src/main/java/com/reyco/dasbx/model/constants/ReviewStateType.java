package com.reyco.dasbx.model.constants;

/**
 * 用户/账号类型
 * @author reyco
 *
 */
public enum ReviewStateType {
	
	REVIEW_STATE_0((byte)0,"审批中"),
	
	REVIEW_STATE_1((byte)1,"审核已通过"),
	
	REVIEW_STATE_2((byte)2,"审批未通过");
	
	private Byte state;
	private String desc;
	
	ReviewStateType(Byte state, String desc) {
        this.state = state;
        this.desc = desc;
    }

    public static ReviewStateType getReviewStateType(Byte state){
        for(ReviewStateType reviewStateType: ReviewStateType.values()){
            if(reviewStateType.state.equals(state)){
                return reviewStateType;
            }
        }
        return null;
    }
    public static ReviewStateType getReviewStateType(String desc){
        for(ReviewStateType userType: ReviewStateType.values()){
            if(userType.desc.equalsIgnoreCase(desc)){
                return userType;
            }
        }
        return null;
    }
    
	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
