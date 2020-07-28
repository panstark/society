package com.pan.society.know.datastructure.heapandpriorityqueue.heapbasics;





/**
 * 堆：
 * 一般指具有特殊性质的二叉树
 * 1、二叉堆是一个完全二叉树
 *      满二叉树：除了叶子节点，所有的节点都具有左孩子和右孩子
 *      完全二叉树：可以不是满二叉树，但是所有的缺失部分都必须在整棵树的右下侧。
 *                即把元素顺序排列成树的形状
 * 2、堆中的某个节点的值，总是不大于其父节点的值。 （根节点是最大的元素）
 *
 *
 * 可以用数组来标识
 * parent(i)=i/2
 * left child = 2*i
 * right child = 2*i+1
 * @param <E>
 */
public class MaxHeap<E extends Comparable<E>> {

    private Array<E> data;

    public MaxHeap(int capacity){
        data = new Array<>(capacity);
    }

    public MaxHeap(){
        data = new Array<>();
    }

    // 返回堆中的元素个数
    public int size(){
        return data.getSize();
    }

    // 返回一个布尔值, 表示堆中是否为空
    public boolean isEmpty(){
        return data.isEmpty();
    }

    // 返回完全二叉树的数组表示中，一个索引所表示的元素的父亲节点的索引
    private int parent(int index){
        if(index == 0)
            throw new IllegalArgumentException("index-0 doesn't have parent.");
        return (index - 1) / 2;
    }

    // 返回完全二叉树的数组表示中，一个索引所表示的元素的左孩子节点的索引
    private int leftChild(int index){
        return index * 2 + 1;
    }

    // 返回完全二叉树的数组表示中，一个索引所表示的元素的右孩子节点的索引
    private int rightChild(int index){
        return index * 2 + 2;
    }
}
