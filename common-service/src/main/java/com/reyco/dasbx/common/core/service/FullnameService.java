package com.reyco.dasbx.common.core.service;

import com.reyco.dasbx.model.domain.Fullname;

public interface FullnameService {
	/**
	 * 随机生产一个名字
	 * @return
	 */
	Fullname randomFullname();
	/**
	 * 根据姓氏随机生产一个名字
	 * @param surname   姓氏
	 * @return
	 */
	Fullname randomFullname(String surname);
	/**
	 * 根据名字长度随机生产一个名字
	 * @param len    	名长度
	 * @return
	 */
	Fullname randomFullname(Integer len);
	/**
	 * 根据性别随机生产一个名字
	 * @param gender    性别  false:男   true:女
	 * @return
	 */
	Fullname randomFullname(Boolean gender);
	/**
	 * 根据姓氏和名字长度随机生产一个名字
	 * @param surname   姓氏
	 * @param len       名长度
	 * @return
	 */
	Fullname randomFullname(String surname,Integer len);
	/**
	 * 根据姓氏+名字长度+性别随机生产一个名字
	 * @param surname   姓氏
	 * @param len       名长度
	 * @return
	 */
	Fullname randomFullname(String surname,Integer len,Boolean gender);
	/**
	 * 随机生产一个男名字
	 * @return
	 */
	Fullname randomMaleFullname();
	/**
	 * 根据姓氏随机生产一个男名字
	 * @param surname   姓氏
	 * @return
	 */
	Fullname randomMaleFullname(String surname);
	/**
	 * 根据名字长度随机生产一个男名字
	 * @param len    	名长度
	 * @return
	 */
	Fullname randomMaleFullname(Integer len);
	/**
	 * 根据姓氏+名字随机生产一个男名字
	 * @param surname    姓氏
	 * @param name    	   名字
	 * @return
	 */
	Fullname randomMaleFullname(String surname,String...name);
	/**
	 * 根据姓氏+名字长度随机生产一个男名字
	 * @param surname   姓氏
	 * @param len   	名字长度
	 * @return
	 */
	Fullname randomMaleFullname(String surname,Integer len);
	/**
	 * 随机生产一个女名字
	 * @return
	 */
	Fullname randomGirlFullname();
	/**
	 * 根据姓氏随机生产一个女名字
	 * @param surname    姓氏
	 * @return
	 */
	Fullname randomGirlFullname(String surname);
	/**
	 * 根据名字长度随机生产一个女名字
	 * @param len    	名长度
	 * @return
	 */
	Fullname randomGirlFullname(Integer len);
	/**
	 * 根据姓氏+名字随机生产一个女名字
	 * @param surname    姓氏
	 * @param name    	  名字
	 * @return
	 */
	Fullname randomGirlFullname(String surname,String...name);
	/**
	 * 根据姓氏+名字长度随机生产一个女名字
	 * @param surname    姓氏
	 * @param len     	   名字长度
	 * @return
	 */
	Fullname randomGirlFullname(String surname,Integer len);
	/**
	 * 根据姓氏随机生产一个男/女名字
	 * @param gender   false:男   true:女
	 * @param surname  姓氏
	 * @param name...  名字
	 * @return
	 */
	Fullname buildFullname(Boolean gender,String surname,String... name);
}
