package com.reyco.dasbx.commons.top;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 排行榜
 * @author reyco
 *
 * @param <T>
 */
public class Top<T> {
	/**
	 * 默认堆大小
	 */
	private final static int CAPACITY = 16;
	/**
	 * 元素是否存在
	 */
	private Map<T,Node<T>> elementMap;
	/**
	 * 是否在堆上,
	 */
	private Map<Node<T>,Integer> indexMap;
	/**
	 * 小根堆,0位置无效
	 */
	private Node<T>[] heap;
	/**
	 * 堆大小,默认等于1
	 */
	private Integer heapSize = 1;
	
	public Top() {
		this(CAPACITY);
	}
	public Top(Integer capacity) {
		this(capacity,null);
	}
	public Top(Integer capacity,List<T> record) {
		super();
		int c = capacity(capacity);
		this.elementMap = new HashMap<T,Node<T>>(c);;
		this.indexMap = new HashMap<Node<T>,Integer>(c);
		this.heap = new Node[capacity+1];
		this.init(record);
	}
	private void init(List<T> record) {
		if(record!=null) {
			record.stream().forEach(element->add(element));
		}
	}
	/**
	 * 添加元素
	 * @param element	元素
	 */
	public void add(T element) {
		add(element, 1);
	}
	/**
	 * 添加元素
	 * @param element	元素
	 * @param time		分值、词频
	 */
	public void add(T element,int time) {
		Node<T> currNode;
		int currIndex=-1;
		if((currNode = elementMap.get(element))==null) {
			currNode = new Node<T>(element);
			elementMap.put(element, currNode);
			indexMap.put(currNode, currIndex);
		}else {
			currNode.times += time;
			currIndex = indexMap.get(currNode);
		}
		if(currIndex==-1) {
			if(heapSize==heap.length) {
				if(currNode.times>heap[1].times) {
					indexMap.put(heap[1], -1);
					indexMap.put(currNode, 1);
					heap[1] = currNode;
					heapify(1,heapSize);
				}
			}else {
				indexMap.put(currNode, heapSize);
				heap[heapSize] = currNode;
				heapInsert(heapSize++);
			}
		}else {
			heapify(currIndex, heapSize);
		}
	}
	/**
	 * 获取排行榜
	 * @return
	 */
	public List<T> getTop() {
		List<Node<T>> list = new ArrayList<Node<T>>();
		for (int i=1;i<heap.length;i++) {
			if(heap[i]==null) {
				break;
			}
			list.add(heap[i]);
		}
		Collections.sort(list,(s1,s2)->{
			return s2.getTimes()-s1.getTimes();
		});
		return list.stream().map( e -> {
			return e.element;
		}).collect(Collectors.toList());
	}
	/**
	 * 向上heapify
	 * heapInsert
	 * @param index
	 */
	private void heapInsert(Integer index) {
		while (index!=1 && heap[index].times < heap[index>>1].times) {
			swap(index, index>>1);
			index = index>>1;
		}
	}
	/**
	 * 向下heapify
	 * @param index
	 * @param size
	 */
	private void heapify(int index, int size) {
		int left = index<<1;
		while (left < size) {
			int less = left + 1 < size && heap[left + 1].times < heap[left].times ? left + 1 : left;
			less = heap[less].times < heap[index].times ? less : index;
			if (less == index) {
				break;
			}
			swap(less, index);
			index = less;
			left = index<<1;
		}
	}
	private void swap(int i,int j) {
		indexMap.put(heap[i], j);
		indexMap.put(heap[j], i);
		Node temp = heap[i];
		heap[i]=heap[j];
		heap[j]=temp;
	}
	/**
	 * 获取容量
	 * @param capacity
	 * @return
	 */
	private int capacity(Integer capacity) {
		int c = 1;
		while(c<capacity) {
			c <<= 1;
		}
		return c;
	}
	public static class Node<T>{
		private T element;
		private int times;
		public Node(T element) {
			super();
			this.element = element;
			this.times = 1;
		}
		public T getElement() {
			return element;
		}
		public void setElement(T element) {
			this.element = element;
		}
		public int getTimes() {
			return times;
		}
		public void setTimes(int times) {
			this.times = times;
		}
		@Override
		public int hashCode() {
			return element.hashCode();
		}
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Node) {
				Node<T> o1 = (Node)obj;
				return o1.element.equals(this.element);
			}
			return false;
		}
	}
	public static void main(String[] args) {
		Top<String> top = new Top<String>(10);
		Random random = new Random();
		String s = "0123456789";
		//String s = "abcdefghijklmnopqrstwvuxyz";
		//String s = "ABCDEFGHIJKLMNOPQRSTWVUXYZ";
		//String s = "0123456789abcdefghijklmnopqrstwvuxyz";
		//String s = "0123456789ABCDEFGHIJKLMNOPQRSTWVUXYZ";
		//String s = "abcdefghijklmnopqrstwvuxyzABCDEFGHIJKLMNOPQRSTWVUXYZ";
		//String s = "0123456789abcdefghijklmnopqrstwvuxyzABCDEFGHIJKLMNOPQRSTWVUXYZ";
		for(int i=0;i<1000000;i++) {
			StringBuilder sb = new StringBuilder();
			int j = 0;
			int nextInt = random.nextInt(3)+3;
			while(j++<nextInt) {
				String word = s.charAt(random.nextInt(s.length()))+"";
				sb.append(word);
			}
			top.add(sb.toString());
		}
		List<String> tops = top.getTop();
		tops.stream().forEach(System.out::println);
	}
}
