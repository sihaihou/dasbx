package com.reyco.dasbx.model.constants;

public interface RabbitConstants {
	
	/**
	 * 账号
	 */
	String ACCOUNT_EXCHANGE = "account_exchange";
	//注册
	String ACCOUNT_REGISTER_QUEUE = "account_register_queue";
	String ACCOUNT_REGISTER_ROUTE_KEY = "account_register_route_key";
	//同步ES
	String ACCOUNT_SYNC_ES_QUEUE = "account_sync_es_queue";
	String ACCOUNT_SYNC_ES_ROUTE_KEY = "account_sync_es_route_key";
	
	/**
	 * 日志
	 */
	String LOG_EXCHANGE = "log_exchange";
	//登录
	String LOG_LOGIN_QUEUE = "login_log_queue";
	String LOG_LOGIN_ROUTE_KEY = "login_log_route_key";
	//登出
	String LOG_LOGOUT_QUEUE = "logout_log_queue";
	String LOG_LOGOUT_ROUTE_KEY = "logout_log_route_key";
	//系统
	String LOG_SYS_QUEUE = "sys_log_queue";
	String LOG_SYS_ROUTE_KEY = "sys_log_route_key";
}
