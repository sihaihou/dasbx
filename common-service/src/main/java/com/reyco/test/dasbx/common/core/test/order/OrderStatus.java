package com.reyco.test.dasbx.common.core.test.order;

public enum OrderStatus {
    
    PROCESSING(0, "处理中"),
    CREATED(1, "已创建"),
    PAID(2, "已支付"),
    COMPLETED(3, "已完成"),
    CANCELLED(4, "已取消"),
    PAYMENT_TIMEOUT(5, "支付超时"),
    FAILED(6, "失败"),
    NOT_FOUND(7, "未找到");
    
    private final Integer code;
    private final String desc;
    
    OrderStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public static OrderStatus getByCode(Integer code) {
        for (OrderStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}