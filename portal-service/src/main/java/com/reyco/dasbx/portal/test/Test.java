package com.reyco.dasbx.portal.test;

import java.text.ParseException;
import java.util.Arrays;

/**
 * 数组方法引用
 * @author reyco
 *
 */
public class Test {
	public static void main(String[] args) throws ParseException {
		int[] arr2 = IntArrays.createIntArray(10, int[]::new);
		System.out.println(Arrays.toString(arr2));
		long i = 1629906016897L%1024;
		System.out.println(i);
	}
}
@FunctionalInterface
interface Arrayfactory{
	int[] createIntArray(int len);
}
class IntArrays{
	public static int[] createIntArray(int len,Arrayfactory arrayfactory) {
		return arrayfactory.createIntArray(len);
	}
}