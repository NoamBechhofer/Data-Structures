/**
 * LLNode.java
 * A node in a doubly linked list.
 * 
 * @author Noam Bechhofer
 */

package com.noambechhofer.datastructures.utils;

public class LLNode<E> {
    /**
     * The previous node in the list.
     */
    private LLNode<E> prev;
    /**
     * The data stored in the node.
     */
    private E data;
    /**
     * The next node in the list.
     */
    private LLNode<E> next;

    /**
     * Constructs a new node with the given data.
     * 
     * @param prev the previous node in the list (null if none)
     * @param data the data to be stored in the node
     * @param next the next node in the list (null if none)
     */
    public LLNode(LLNode<E> prev, E data, LLNode<E> next) {
        this.prev = prev;
        this.data = data;
        this.next = next;
    }

    /**
     * Constructs a new node with the given data and no previous or next node.
     */
    public LLNode(E data) {
        this(null, data, null);
    }

    /**
     * retrieves the previous node in the list.
     * @return the previous node in the list, or null if this is the head of the list
     */
    public LLNode<E> getPrev() {
        return prev;
    }

    /**
     * retrieves the next node in the list.
     * @return the next node in the list, or null if this is the tail of the list
     */
    public LLNode<E> getNext() {
        return next;
    }

    /**
     * retrieves the data stored in the node.
     * @return the data stored in the node
     */
    public E getData() {
        return data;
    }

    /**
     * Replace this node's {@code prev}-link with the given node.
     * 
     * @param prev the new previous node
     */
    public void setPrev(LLNode<E> prev) {
        this.prev = prev;
        prev.setNext(this);
    }

    /**
     * Replace this node's {@code next}-link with the given node.
     * 
     * @param next the new next node
     */
    public void setNext(LLNode<E> next) {
        this.next = next;
        next.setPrev(this);
    }

    /**
     * Replace this node's data with the given data.
     * 
     * @param data the new data
     */
    public void setData(E data) {
        this.data = data;
    }
}