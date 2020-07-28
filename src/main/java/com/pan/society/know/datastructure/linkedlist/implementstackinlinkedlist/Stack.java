package com.pan.society.know.datastructure.linkedlist.implementstackinlinkedlist;

public interface Stack<E> {

    int getSize();
    boolean isEmpty();
    void push(E e);
    E pop();
    E peek();
}
