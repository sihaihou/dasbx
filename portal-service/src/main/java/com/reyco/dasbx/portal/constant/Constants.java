package com.reyco.dasbx.portal.constant;

public class Constants {
	public final static String VIDEO_INDEX_NAME = "video";
	public final static String VIDEO_SEARCH_FIELD = "all";
	public final static String[] VIDEO_SEARCH_MULTIFIELDS = new String[] {"name","description","director","star"};
	public final static String[] VIDEO_HIGHLIGHT_FIELDS = new String[] {"name","description"};
	public final static String VIDEO_suggestion_NAME = "suggestion";
	public final static String VIDEO_SUGGESTION_FIELD = "suggestion";
	public final static String[] VIDEO_AGGREGATION_FIELDS = new String[] {"countryId","typeId","yearId"};
	public final static String[] VIDEO_AGGREGATION_NAMES = new String[] {"country","type","year"};
	public final static Integer[] VIDEO_AGGREGATION_SIZES = new Integer[] {};
	public final static Boolean[] VIDEO_AGGREGATION_FIELD_ORDERS = new Boolean[] {false};
}
