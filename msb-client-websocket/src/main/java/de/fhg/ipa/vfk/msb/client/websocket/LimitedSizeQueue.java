/*
 * Copyright (c) 2019 Fraunhofer Institute for Manufacturing Engineering and Automation (IPA).
 * Authors: Daniel Schel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package de.fhg.ipa.vfk.msb.client.websocket;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;

/**
 * The Class LimitedSizeQueue.
 * A FIFO queue with a fixed maximum size.
 *
 * @param <T> the generic type
 * @author des
 */
public class LimitedSizeQueue<T> implements Queue<T> {

    private static final int NO_MAX_SIZE = 0;
    private Deque<T> deque = new ArrayDeque<>();
    private int maxSize = Integer.MAX_VALUE;

    /**
     * Instantiates a new limited size queue.
     */
    public LimitedSizeQueue() {
        //Default constructor
    }

    /**
     * Instantiates a new limited size queue.
     *
     * @param maxSize the max size
     */
    public LimitedSizeQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Maximum size.
     *
     * @return the int
     */
    public int maximumSize() {
        return maxSize;
    }

    /**
     * Size.
     *
     * @return the int
     * @see java.util.Collection#size()
     */
    @Override
    public int size() {
        return deque.size();
    }

    /**
     * Checks if is empty.
     *
     * @return true, if is empty
     * @see java.util.Collection#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return deque.isEmpty();
    }

    /**
     * Contains.
     *
     * @param o the o
     * @return true, if successful
     * @see java.util.Collection#contains(java.lang.Object)
     */
    @Override
    public boolean contains(Object o) {
        return deque.contains(o);
    }

    /**
     * Iterator.
     *
     * @return the iterator
     * @see java.util.Collection#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return deque.iterator();
    }

    /**
     * To array.
     *
     * @return the object[]
     * @see java.util.Collection#toArray()
     */
    @Override
    public Object[] toArray() {
        return deque.toArray();
    }

    /**
     * To array.
     *
     * @param <T> the generic type
     * @param a   the a
     * @return the t[]
     * @see java.util.Collection#toArray(java.lang.Object[])
     */
    @Override
    public <T> T[] toArray(T[] a) {
        return deque.toArray(a);
    }

    /**
     * Removes the.
     *
     * @param o the o
     * @return true, if successful
     * @see java.util.Collection#remove(java.lang.Object)
     */
    @Override
    public boolean remove(Object o) {
        return deque.remove(o);
    }

    /**
     * Contains all.
     *
     * @param c the c
     * @return true, if successful
     * @see java.util.Collection#containsAll(java.util.Collection)
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return deque.containsAll(c);
    }

    /**
     * Adds the all.
     *
     * @param c the c
     * @return true, if successful
     * @see java.util.Collection#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c.size() + size() < maxSize || maxSize == NO_MAX_SIZE) {
            return deque.addAll(c);
        } else {
            Iterator<? extends T> i = c.iterator();
            while (i.hasNext()) {
                add(i.next());
            }
            return true;
        }
    }

    /**
     * Removes the all.
     *
     * @param c the c
     * @return true, if successful
     * @see java.util.Collection#removeAll(java.util.Collection)
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return deque.removeAll(c);
    }

    /**
     * Retain all.
     *
     * @param c the c
     * @return true, if successful
     * @see java.util.Collection#retainAll(java.util.Collection)
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return deque.retainAll(c);
    }

    /**
     * Clear.
     *
     * @see java.util.Collection#clear()
     */
    @Override
    public void clear() {
        deque.clear();
    }

    /**
     * Adds the.
     *
     * @param e the e
     * @return true, if successful
     * @see java.util.Queue#add(java.lang.Object)
     */
    @Override
    public boolean add(T e) {
        return offer(e);
    }

    /**
     * Offer.
     *
     * @param e the e
     * @return true, if successful
     * @see java.util.Queue#offer(java.lang.Object)
     */
    @Override
    public boolean offer(T e) {
        if (size() >= maxSize && maxSize != NO_MAX_SIZE) {
            deque.removeFirst();
        }
        return deque.add(e);
    }

    /**
     * Removes the.
     *
     * @return the t
     * @see java.util.Queue#remove()
     */
    @Override
    public T remove() {
        return deque.remove();
    }

    /**
     * Poll.
     *
     * @return the t
     * @see java.util.Queue#poll()
     */
    @Override
    public T poll() {
        return deque.pollFirst();
    }

    /**
     * Element.
     *
     * @return the t
     * @see java.util.Queue#element()
     */
    @Override
    public T element() {
        return deque.element();
    }

    /**
     * Peek.
     *
     * @return the t
     * @see java.util.Queue#peek()
     */
    @Override
    public T peek() {
        return deque.peekFirst();
    }

}
