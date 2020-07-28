package com.pan.society.know.datastructure.binarysearchtree.basics;


/**
 * 二分搜索树
 * 1、是一棵二叉树
 * 2、二分搜索树每一个节点的值：
 *     大于其左子树的所有节点值
 *     小于其右子树的所有节点值
 * 3、每一棵子树也是一个二分搜索树
 *
 * 存储的元素必须有可比较性
 *
 * @param <E>
 */
public class BST<E extends Comparable<E>> {

    private class Node {
        public E e;
        public Node left, right;

        public Node(E e) {
            this.e = e;
            left = null;
            right = null;
        }
    }

    private Node root;
    private int size;

    public BST(){
        root = null;
        size = 0;
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }
}
