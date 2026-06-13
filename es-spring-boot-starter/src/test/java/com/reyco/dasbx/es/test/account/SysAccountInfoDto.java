package com.reyco.dasbx.es.test.account;

import com.reyco.dasbx.es.support.annotation.EsIndex;
import com.reyco.dasbx.es.support.annotation.aggregation.EsAgg;
import com.reyco.dasbx.es.support.annotation.highlight.EsHighlightField;
import com.reyco.dasbx.es.support.result.facet.GenderLabelProvider;
import com.reyco.dasbx.es.test.domain.StateFacetLabelProvider;
import com.reyco.dasbx.es.test.domain.TypeFacetLabelProvider;

@EsIndex("sys_account")
public class SysAccountInfoDto extends BaseDto {
	
	@EsHighlightField
	private String nickname;
	@EsHighlightField
	private String username;
	@EsHighlightField
	private String phone;
	@EsHighlightField
	private String email;
	
	@EsAgg(provider=GenderLabelProvider.class)
	private Byte gender;
	
	@EsAgg(provider=StateFacetLabelProvider.class)
	private Byte state;
	
	@EsAgg(provider=TypeFacetLabelProvider.class)
	private Byte type;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	
}
