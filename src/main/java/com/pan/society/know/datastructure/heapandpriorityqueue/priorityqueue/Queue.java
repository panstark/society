package com.pan.society.know.datastructure.heapandpriorityqueue.priorityqueue;

public interface Queue<E> {

    int getSize();
    boolean isEmpty();
    void enqueue(E e);
    E dequeue();
    E getFront();
}
