package com.reyco.dasbx.user.core.constant;

public class Constants {
	public final static String ACCOUNT_INDEX_NAME = "sys_account";
	public final static String ACCOUNT_SEARCH_FIELD = "all";
	public final static String[] ACCOUNT_SEARCH_MULTIFIELDS = new String[] {"nickname","username","phone","email"};
	public final static String[] ACCOUNT_HIGHLIGHT_FIELDS = new String[] {"nickname","username","phone","email"};
	public final static String ACCOUNT_suggestion_NAME = "suggestion";
	public final static String ACCOUNT_SUGGESTION_FIELD = "suggestion";
	public final static String[] ACCOUNT_AGGREGATION_FIELDS = new String[] {"gender","state","type"};
	public final static String[] ACCOUNT_AGGREGATION_NAMES = new String[] {};
	public final static Integer[] ACCOUNT_AGGREGATION_SIZES = new Integer[] {};
	public final static Boolean[] ACCOUNT_AGGREGATION_FIELD_ORDERS = new Boolean[] {false};
	
	public final static String ROLE_INDEX_NAME = "sys_role";
	public final static String ROLE_SEARCH_FIELD = "all";
	public final static String[] ROLE_SEARCH_MULTIFIELDS = new String[] {"name"};
	public final static String[] ROLE_HIGHLIGHT_FIELDS = new String[] {"name"};
	public final static String ROLE_suggestion_NAME = "suggestion";
	public final static String ROLE_SUGGESTION_FIELD = "suggestion";
	public final static String[] ROLE_AGGREGATION_FIELDS = new String[] {};
	public final static String[] ROLE_AGGREGATION_NAMES = new String[] {};
	public final static Integer[] ROLE_AGGREGATION_SIZES = new Integer[] {};
	public final static Boolean[] ROLE_AGGREGATION_FIELD_ORDERS = new Boolean[] {};
}
