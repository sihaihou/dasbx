package com.reyco.dasbx.model.constants;

/**
 * 扫码类型
 * @author reyco
 *
 */
public enum ScanQRCodeType {
	/**
	 * 登录
	 */
	LOGIN((byte)0,"登录");
	
	private Byte type;
	private String remark;
	
	ScanQRCodeType(Byte type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    public static ScanQRCodeType getScanQRCodeType(Byte type){
        for(ScanQRCodeType userType: ScanQRCodeType.values()){
            if(userType.type.equals(type)){
                return userType;
            }
        }
        return null;
    }
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
