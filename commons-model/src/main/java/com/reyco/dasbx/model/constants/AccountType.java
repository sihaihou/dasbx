package com.reyco.dasbx.model.constants;

/**
 * 用户/账号类型
 * @author reyco
 *
 */
public enum AccountType {
	/**
	 * QQ类型
	 */
	ACCOUNT_TYPE_QQ((byte)1,"qq","QQ用户"),
	/**
	 * 微博类型
	 */
	ACCOUNT_TYPE_WEIBO((byte)2,"sina","微博用户"),
	/**
	 * 百度类型
	 */
	ACCOUNT_TYPE_BAIDU((byte)3,"baidu","百度用户"),
	/**
	 * 后台
	 */
	ACCOUNT_TYPE_BACK((byte)4,"back","系统用户"),
	/**
	 * 前台类型
	 */
	ACCOUNT_TYPE_PORTAL((byte)5,"portal","系统用户"),
	/**
	 * portal类型
	 */
	ACCOUNT_TYPE_OPEN((byte)6,"open","系统用户");
	
	private Byte type;
	private String name;
	private String remark;
	
	AccountType(Byte type, String name,String remark) {
        this.type = type;
        this.name = name;
        this.remark = remark;
    }

    public static AccountType getAccountType(Byte type){
        for(AccountType userType: AccountType.values()){
            if(userType.type.equals(type)){
                return userType;
            }
        }
        return null;
    }
    public static AccountType getAccountType(String name){
        for(AccountType userType: AccountType.values()){
            if(userType.name.equalsIgnoreCase(name)){
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
