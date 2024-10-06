package com.reyco.dasbx.commons.utils.random;

import java.util.Random;

public class RandomUtils {
	
	private static final Random random = new Random();
	
	public static int randomInt() {
		return random.nextInt();
	}
	
	public static int randomInt(int max) {
		return random.nextInt(max);
	}
	
}
