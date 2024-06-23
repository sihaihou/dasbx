package com.reyco.dasbx.commons.utils;

public class RandomNumberUtils {
	
	public static String randomNumber() {
		int randomNumber = RandomUtils.randomInt(1000000);
        String formattedNumber = String.format("%06d", randomNumber); // 格式化为六位数字字符串
        return formattedNumber;
	}
	public static void main(String[] args) {
		String randomNumber = randomNumber();
		System.out.println(randomNumber);
	}
}
