package com.pan.society.know.datastructure.linkedlist.implementqueueinlinkedlist;

public interface Queue<E> {

    int getSize();
    boolean isEmpty();
    void enqueue(E e);
    E dequeue();
    E getFront();
}
