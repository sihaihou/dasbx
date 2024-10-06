package com.reyco.dasbx.commons.domain;

import com.reyco.dasbx.commons.utils.serializable.ToString;

public class R<T> extends ToString {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7706853366746848391L;
	//请求成功
	public final static String REQUEST_SUCCESS = "请求成功";
	public final static String REQUEST_SUCCESS_DESC = "Success";
	public final static int REQUEST_SUCCESS_CODE = 200;
	//请求失败
	public final static String REQUEST_FAIL = "请求失败";
	public final static String REQUEST_FAIL_DESC = "Fail";
	public final static int REQUEST_FAIL_CODE = 201;
	//认证失败
	public final static String AUTH_FAIL = "认证失败";
	public final static String AUTH_FAIL_DESC = "Authentication";
	public final static int AUTH_FAIL_CODE = 211;
	//请求失败
	public final static String PARAM_ERROR = "参数错误";
	public final static String PARAM_ERROR_DESC = "Parameter_error";
	public final static int PARAM_ERROR_CODE = 201;
	
	/**
	 * 成功或失败
	 */
	private Boolean success = true;
	/**
	 * 状态码
	 */
	private Integer code;
	/**
	 * 数据
	 */
	private T data;
	/**
	 * 失败原因/前端看的
	 */
	private String dataMsg;
	/**
	 * 失败原因/后端看的
	 */
	private String msg;

	public R() {
		
	}
	public static R<?> noData(){
		return success(null, R.REQUEST_SUCCESS_DESC, R.REQUEST_SUCCESS);
	}
	public static R<?> success(){
		return success(null, R.REQUEST_SUCCESS_DESC, R.REQUEST_SUCCESS);
	}
	public static R<?> success(Object data){
		return success(data, R.REQUEST_SUCCESS_DESC, R.REQUEST_SUCCESS);
	}
	public static R<?> success(Object data,String msg){
		return success(data, R.REQUEST_SUCCESS_DESC, msg);
	}
	public static R<?> success(Object data,String dataMsg,String msg){
		return success(R.REQUEST_SUCCESS_CODE, data, dataMsg, msg);
	}
	public static R<?> success(Integer code,Object data,String dataMsg,String msg){
		return build(true, code, data, dataMsg, msg);
	}
	public static R<?> authFail(String msg){
		return authFail(null, msg);
	}
	public static R<?> authFail(Object data){
		return authFail(data, R.AUTH_FAIL);
	}
	public static R<?> authFail(Object data,String msg){
		return authFail(data, R.AUTH_FAIL_DESC, msg);
	}
	public static R<?> authFail(Object data,String dataMsg,String msg){
		return authFail(R.AUTH_FAIL_CODE, data, dataMsg, msg);
	}
	public static R<?> authFail(Integer code,Object data,String dataMsg,String msg){
		return build(true, code, data, dataMsg, msg);
	}
	public static R<?> fail(String msg){
		return fail(R.REQUEST_FAIL_DESC, msg);
	}
	public static R<?> fail(String dataMsg,String msg){
		return fail(null, dataMsg, msg);
	}
	public static R<?> fail(Object data,String dataMsg,String msg){
		return fail(R.REQUEST_FAIL_CODE, data, dataMsg, msg);
	}
	public static R<?> fail(Integer code,Object data,String dataMsg,String msg){
		return build(false, code, data, dataMsg, msg);
	}
	public static R<?> build(Boolean success,Integer code,Object data,String dataMsg,String msg){
		R r = new R();
		r.setSuccess(success);
		r.setCode(code);
		r.setData(data);
		r.setDataMsg(dataMsg);
		r.setMsg(msg);
		return r;
	}
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getDataMsg() {
		return dataMsg;
	}

	public void setDataMsg(String dataMsg) {
		this.dataMsg = dataMsg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
