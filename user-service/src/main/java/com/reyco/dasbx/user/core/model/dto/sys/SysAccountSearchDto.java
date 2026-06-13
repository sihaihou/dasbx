package com.reyco.dasbx.user.core.model.dto.sys;

import com.reyco.dasbx.es.dto.SearchPageDto;
import com.reyco.dasbx.es.support.annotation.EsIndex;
import com.reyco.dasbx.es.support.annotation.aggregation.EsAgg;
import com.reyco.dasbx.es.support.annotation.highlight.EsHighlightField;
import com.reyco.dasbx.es.support.annotation.query.EsQuery;
import com.reyco.dasbx.es.support.annotation.query.EsQueryType;
import com.reyco.dasbx.es.support.result.facet.FacetLabelProvider;
import com.reyco.dasbx.es.support.result.facet.GenderLabelProvider;

@EsIndex("sys_account")
public class SysAccountSearchDto extends SearchPageDto{
	
	@EsQuery(type=EsQueryType.MATCH,field="all")
	private String keyword;
	
	@EsQuery
	@EsAgg(name="性别",provider=GenderLabelProvider.class)
	private Byte gender;
	
	@EsQuery
	@EsAgg(name="状态",provider=StateFacetLabelProvider.class)
	private Byte state;
	
	@EsQuery
	@EsAgg(name="平台",provider=TypeFacetLabelProvider.class)
	private Byte type;
	

	@EsHighlightField
	private String nickname;
	@EsHighlightField
	private String username;
	@EsHighlightField
	private String phone;
	@EsHighlightField
	private String email;
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Byte getGender() {
		return gender;
	}
	public void setGender(Byte gender) {
		this.gender = gender;
	}
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public static class StateFacetLabelProvider implements FacetLabelProvider{
		public StateFacetLabelProvider() {
		}
		@Override
		public String getLabel(Object key) {
			byte type = Byte.parseByte(key.toString());
			switch (type) {
			case 1:
				return "禁用";

			case 0:
				return "正常";

			default:
				return "未知";
			}
		}

	}
	public static class TypeFacetLabelProvider implements FacetLabelProvider{
		public TypeFacetLabelProvider() {
		}
		@Override
		public String getLabel(Object key) {
			byte type = Byte.parseByte(key.toString());
			switch (type) {
			case (byte)2:
				return "QQ用户";
			case (byte)3:
				return "微博用户";
			case (byte)4:
				return "百度用户";
			default:
				return "系统用户";
			}
		}

	}

}
