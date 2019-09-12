package com.pan.society.vo.type;

import org.apache.commons.collections4.map.LinkedMap;

import java.util.*;

public class DiffrentList {


    private List<String> arrayList = new ArrayList<>();

    private List<String> linkedList = new LinkedList<>();

    /**
     * 不允许出现重复因素；
     * 允许插入Null值；
     * 元素无序（添加顺序和遍历顺序不一致）；
     * 线程不安全，若2个线程同时操作HashSet，必须通过代码实现同步
     */
    private Set<String> hashSet = new HashSet<>();

    /**
     * 对插入的元素进行排序，是一个有序的集合（主要与HashSet的区别）;
     * 底层使用红黑树结构，而不是哈希表结构；
     * 允许插入Null值；
     * 不允许插入重复元素；
     * 线程不安全；
     */
    private Set<String> treeSet = new TreeSet<>();

    private Map<String,String>  hashmap = new HashMap<>();

    private Map<String,String> linkedHashMap = new LinkedHashMap<>();

    private Map<String,String> linkedMap = new LinkedMap<>();

    private Map<String,String> hashtable = new Hashtable<>();

    private Map<String,String> treeMap = new TreeMap<>();

}
