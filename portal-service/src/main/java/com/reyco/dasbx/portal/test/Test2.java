package com.reyco.dasbx.portal.test;

public class Test2 {
	public static void main(String[] args) {
		Person person = Factorys.create(new String("zhangsan"), s->new Person(s));
		System.out.println(person);
	}
}
@FunctionalInterface
interface Factory<T,R> {
	R apply(T t);
}
class Factorys{
	public static <T, R> R create(T t,Factory<T,R> factory) {
		return factory.apply(t);
	}
}
class Person {
	private String name;
	private String age;
	public Person() {
	}
	public Person(String name) {
		super();
		this.name = name;
	}
	public Person(String name, String age) {
		super();
		this.name = name;
		this.age = age;
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + "]";
	}
}