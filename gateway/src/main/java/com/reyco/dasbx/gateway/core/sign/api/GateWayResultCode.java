package com.reyco.dasbx.gateway.core.sign.api;

public enum GateWayResultCode {
    DASBX_ERROR("非法请求，未知异常","600"),
    DASBX_TIMESTAMP("非法请求，时间戳不合法","601"),
    DASBX_KEY("非法请求，key不合法","602"),
    DASBX_NONCE("非法请求，Nonce不合法","603"),
    DASBX_SIGNATURE("非法请求，Signature不合法","604"),
    DASBX_CONTENT_SIGNATURE("非法请求，Content_Signature不合法","605"),
    DASBX_VERSION("非法请求，版本为空","606"),
    DASBX_BUILD_FAIL("非法请求，签名生成失败","607"),
    DASBX_KEY_SIGN("非法请求，签名不匹配","608")
            ;
    private String msg;
    private String code;

    GateWayResultCode(String msg, String code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }

}