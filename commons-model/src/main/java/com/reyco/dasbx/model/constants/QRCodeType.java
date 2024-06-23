package com.reyco.dasbx.model.constants;

/**
 * 二维码状态
 * @author reyco
 *
 */
public enum QRCodeType {
	/**
	 * 未扫描
	 */
	UNSCAN((byte)0,"未扫描"),
	/**
	 * 已扫码待确认
	 */
	TO_BE_CONFIRMED((byte)1,"待确认"),
	/**
	 * 已确认
	 */
	CONFIRMED((byte)2,"已确认"),
	/**
	 * 已取消
	 */
	CANCEL((byte)3,"已取消"),
	/**
	 * 已已过期
	 */
	EXPIRED((byte)4,"已过期");
	
	private Byte state;
	private String remark;
	
	QRCodeType(Byte state, String remark) {
        this.state = state;
        this.remark = remark;
    }

    public static QRCodeType getAccountType(Byte state){
        for(QRCodeType userType: QRCodeType.values()){
            if(userType.state.equals(state)){
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
