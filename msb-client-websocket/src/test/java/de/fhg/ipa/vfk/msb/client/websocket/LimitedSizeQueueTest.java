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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Limited size queue test.
 *
 * @author des
 */
class LimitedSizeQueueTest {

    /**
     * Test limited size queue poll.
     */
    @Test
    void testLimitedSizeQueuePoll(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assertions.assertTrue(limitedSizeQueue.add(new Object()), "is false");
        Assertions.assertEquals(1,limitedSizeQueue.size(),"size not 1");
        Assertions.assertNotNull(limitedSizeQueue.poll(),"queue return null");
        Assertions.assertTrue(limitedSizeQueue.isEmpty(), "queue is not empty");
    }

    /**
     * Test limited size queue peek.
     */
    @Test
    void testLimitedSizeQueuePeek(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assertions.assertTrue(limitedSizeQueue.add(new Object()), "is false");
        Assertions.assertEquals(1,limitedSizeQueue.size(),"size not 1");
        Assertions.assertNotNull(limitedSizeQueue.peek(),"queue return null");
        Assertions.assertFalse(limitedSizeQueue.isEmpty(), "queue is empty");
    }

    /**
     * Test limited size queue remove.
     */
    @Test
    void testLimitedSizeQueueRemove(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assertions.assertTrue(limitedSizeQueue.add(new Object()), "is false");
        Assertions.assertEquals(1,limitedSizeQueue.size(),"size not 1");
        Assertions.assertNotNull(limitedSizeQueue.remove(),"queue return null");
        Assertions.assertTrue(limitedSizeQueue.isEmpty(), "queue is not empty");
    }

    /**
     * Test limited size queue element.
     */
    @Test
    void testLimitedSizeQueueElement(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assertions.assertTrue(limitedSizeQueue.add(new Object()), "is false");
        Assertions.assertEquals(1,limitedSizeQueue.size(),"size not 1");
        Assertions.assertNotNull(limitedSizeQueue.element(),"queue return null");
        Assertions.assertFalse(limitedSizeQueue.isEmpty(), "queue is empty");
    }

    /**
     * Test limited size queue max size.
     */
    @Test
    void testLimitedSizeQueueMaxSize(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>(2);
        Assertions.assertEquals(2,limitedSizeQueue.maximumSize(),"max size not 2");
        Assertions.assertTrue(limitedSizeQueue.add(new Object()), "is false");
        Assertions.assertEquals(1,limitedSizeQueue.size(),"size not 1");
        Assertions.assertTrue(limitedSizeQueue.add(new Object()), "is false");
        Assertions.assertEquals(2,limitedSizeQueue.size(),"size not 1");
        Assertions.assertTrue(limitedSizeQueue.add(new Object()), "is false");
        Assertions.assertEquals(2,limitedSizeQueue.size(),"size not 1");
    }

    /**
     * Test limited size queue clear.
     */
    @Test
    void testLimitedSizeQueueClear(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>(2);
        Assertions.assertEquals(2,limitedSizeQueue.maximumSize(),"max size not 2");
        Assertions.assertTrue(limitedSizeQueue.add(new Object()), "is false");
        Assertions.assertTrue(limitedSizeQueue.add(new Object()), "is false");
        Assertions.assertTrue(limitedSizeQueue.add(new Object()), "is false");
        limitedSizeQueue.clear();
        Assertions.assertTrue(limitedSizeQueue.isEmpty(), "queue is not empty");
    }

    /**
     * Test limited size queue add all.
     */
    @Test
    void testLimitedSizeQueueAddAll(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assertions.assertTrue(limitedSizeQueue.addAll(getList()), "is false");
        Assertions.assertEquals(3,limitedSizeQueue.size(),"queue is not empty");
    }

    /**
     * Test limited size queue add all to many.
     */
    @Test
    void testLimitedSizeQueueAddAllToMany(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>(2);
        Assertions.assertTrue(limitedSizeQueue.addAll(getList()), "is false");
        Assertions.assertEquals(2,limitedSizeQueue.size(),"queue is not empty");
    }

    /**
     * Test limited size queue remove entry.
     */
    @Test
    void testLimitedSizeQueueRemoveEntry(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Object obj = new Object();
        List<Object> objects = getList();
        objects.add(obj);
        Assertions.assertTrue(limitedSizeQueue.addAll(objects), "is false");
        Assertions.assertTrue(limitedSizeQueue.remove(obj), "is false");
        Assertions.assertFalse(limitedSizeQueue.contains(obj), "is true");
    }

    /**
     * Test limited size queue remove all.
     */
    @Test
    void testLimitedSizeQueueRemoveAll(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        List<Object> objects = getList();
        Assertions.assertTrue(limitedSizeQueue.addAll(objects), "is false");
        Assertions.assertTrue(limitedSizeQueue.removeAll(objects), "is false");
        Assertions.assertTrue(limitedSizeQueue.isEmpty(), "queue is not empty");
    }

    /**
     * Test limited size queue retain all.
     */
    @Test
    void testLimitedSizeQueueRetainAll(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        List<Object> objects = getList();
        Assertions.assertTrue(limitedSizeQueue.addAll(objects), "is false");
        objects.remove(0);
        Assertions.assertTrue(limitedSizeQueue.retainAll(objects), "is false");
        Assertions.assertEquals(2,limitedSizeQueue.size(),"queue size not 2");
    }

    /**
     * Test limited size queue contains.
     */
    @Test
    void testLimitedSizeQueueContains(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Object obj = new Object();
        List<Object> objects = getList();
        objects.add(obj);
        Assertions.assertTrue(limitedSizeQueue.addAll(objects), "is false");
        Assertions.assertTrue(limitedSizeQueue.contains(obj), "is false");
    }

    /**
     * Test limited size queue contains all.
     */
    @Test
    void testLimitedSizeQueueContainsAll(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assertions.assertTrue(limitedSizeQueue.addAll(getList()), "is false");
        Assertions.assertTrue(limitedSizeQueue.containsAll(getList()), "is false");
    }

    /**
     * Test limited size queue to array.
     */
    @Test
    void testLimitedSizeQueueToArray(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assertions.assertTrue(limitedSizeQueue.addAll(getList()), "is false");
        Object[] obj = limitedSizeQueue.toArray();
        Assertions.assertNotNull(obj, "is null");
        Assertions.assertEquals(3,obj.length, "length not 3");
    }

    /**
     * Test limited size queue to typed array.
     */
    @Test
    void testLimitedSizeQueueToTypedArray(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assertions.assertTrue(limitedSizeQueue.addAll(getList()), "is false");
        String[] strings = limitedSizeQueue.toArray(new String[0]);
        Assertions.assertNotNull(strings, "is null");
        Assertions.assertEquals(3,strings.length, "length not 3");
    }

    /**
     * Test limited size queue to iterator.
     */
    @Test
    void testLimitedSizeQueueToIterator(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assertions.assertTrue(limitedSizeQueue.addAll(getList()), "is false");
        Assertions.assertTrue(limitedSizeQueue.iterator().hasNext(), "is false");
    }

    private List<Object> getList(){
        List<Object> objects = new ArrayList<>();
        objects.add("1");
        objects.add("2");
        objects.add("3");
        return objects;
    }

}
