package com.reyco.dasbx.sync.es;

public interface ElasticsearchSync<T, R> extends ElasticsearchFullSync<R>, ElasticsearchIncrementUpdateSync<T, R> {
	
	//字符串分割表达式: '.' 或者 ',' 或者 ';' 或者  '、' 或者 '一个或多个空格' 或者 '-'
	public final static String SPLIT_EXPRESSION = "\\.|,|，|;|；|、|\\s+|-";
	
}
