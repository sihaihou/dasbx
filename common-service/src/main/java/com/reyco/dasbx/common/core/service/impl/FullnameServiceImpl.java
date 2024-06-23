package com.reyco.dasbx.common.core.service.impl;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.common.core.service.FullnameService;
import com.reyco.dasbx.common.core.service.NameService;
import com.reyco.dasbx.common.core.service.SurnameService;
import com.reyco.dasbx.commons.utils.RandomUtils;
import com.reyco.dasbx.model.domain.Fullname;
import com.reyco.dasbx.model.domain.Name;
import com.reyco.dasbx.model.domain.Surname;

@Service
public class FullnameServiceImpl implements FullnameService{
	@Autowired
	private SurnameService surnameService;
	@Autowired
	private NameService nameService;
	@Override
	public Fullname randomFullname() {
		Surname surname = surnameService.randomSurname();
		return randomFullname(surname.getName());
	}

	@Override
	public Fullname randomFullname(String surname) {
		int len = RandomUtils.randomInt(2)+1;
		return randomFullname(surname, len);
	}

	@Override
	public Fullname randomFullname(Integer len) {
		Surname surname = surnameService.randomSurname();
		return randomFullname(surname.getName(), len);
	}

	@Override
	public Fullname randomFullname(Boolean gender) {
		Surname surname = surnameService.randomSurname();
		int len = RandomUtils.randomInt(2)+1;
		String names = Stream.iterate(0,i->i+1).limit(len).map(i->nameService.randomGirlName().getName()).collect(Collectors.joining());
		return buildFullname(gender, surname.getName(), names);
	}

	@Override
	public Fullname randomFullname(String surname, Integer len) {
		String names = Stream.iterate(0,i->i+1).limit(len).map(i->nameService.randomGirlName().getName()).collect(Collectors.joining());
		int gender = RandomUtils.randomInt(9);
		return buildFullname((gender&1)==0, surname, names);
	}

	@Override
	public Fullname randomFullname(String surname, Integer len, Boolean gender) {
		String names = Stream.iterate(0,i->i+1).limit(len).map(i->nameService.randomGirlName().getName()).collect(Collectors.joining());
		return buildFullname(gender, surname, names);
	}

	@Override
	public Fullname randomMaleFullname() {
		Surname surname = surnameService.randomSurname();
		return randomMaleFullname(surname.getName());
	}

	@Override
	public Fullname randomMaleFullname(String surname) {
		int len = RandomUtils.randomInt(2)+1;
		return randomMaleFullname(surname,len); 
	}

	@Override
	public Fullname randomMaleFullname(Integer len) {
		Surname surname = surnameService.randomSurname();
		return randomMaleFullname(surname.getName(),len); 
	}

	@Override
	public Fullname randomMaleFullname(String surname, String... name) {
		return buildFullname(false, surname, name);
	}

	@Override
	public Fullname randomMaleFullname(String surname, Integer len) {
		String names = Stream.iterate(0,i->i+1).limit(len).map(i->nameService.randomGirlName().getName()).collect(Collectors.joining());
		return buildFullname(false, surname, names);
	}

	@Override
	public Fullname randomGirlFullname() {
		Surname surname = surnameService.randomSurname();
		return randomGirlFullname(surname.getName());
	}

	@Override
	public Fullname randomGirlFullname(String surname) {
		int len = RandomUtils.randomInt(2)+1;
		return randomGirlFullname(surname, len);
	}

	@Override
	public Fullname randomGirlFullname(Integer len) {
		Surname surname = surnameService.randomSurname();
		return randomGirlFullname(surname.getName(),len);
	}

	@Override
	public Fullname randomGirlFullname(String surname, String... name) {
		return buildFullname(true, surname, name);
	}

	@Override
	public Fullname randomGirlFullname(String surname, Integer len) {
		String names = Stream.iterate(0,i->i+1).limit(len).map(i->nameService.randomGirlName().getName()).collect(Collectors.joining());
		return buildFullname(true, surname, names);
	}
	@Override
	public Fullname buildFullname(Boolean gender, String surname, String... names) {
		Surname surnameObj = new Surname();
		surnameObj.setName(surname);
		Name nameObj = new Name();
		nameObj.setGender(gender);
		nameObj.setName(Stream.of(names).collect(Collectors.joining()));
		Fullname fullname = new Fullname();
		fullname.setSurname(surnameObj);
		fullname.setName(nameObj);
		fullname.setGender(gender);
		fullname.setFullname(surnameObj.getName()+nameObj.getName());
		return fullname;
	}
}
