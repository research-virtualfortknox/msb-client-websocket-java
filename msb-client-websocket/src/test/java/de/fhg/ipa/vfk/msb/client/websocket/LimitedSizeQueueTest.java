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

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Limited size queue test.
 *
 * @author des
 */
public class LimitedSizeQueueTest {

    /**
     * Test limited size queue poll.
     */
    @Test
    public void testLimitedSizeQueuePoll(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assert.assertTrue("is false",limitedSizeQueue.add(new Object()));
        Assert.assertEquals("size not 1",1,limitedSizeQueue.size());
        Assert.assertNotNull("queue return null",limitedSizeQueue.poll());
        Assert.assertTrue("queue is not empty",limitedSizeQueue.isEmpty());
    }

    /**
     * Test limited size queue peek.
     */
    @Test
    public void testLimitedSizeQueuePeek(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assert.assertTrue("is false",limitedSizeQueue.add(new Object()));
        Assert.assertEquals("size not 1",1,limitedSizeQueue.size());
        Assert.assertNotNull("queue return null",limitedSizeQueue.peek());
        Assert.assertFalse("queue is empty",limitedSizeQueue.isEmpty());
    }

    /**
     * Test limited size queue remove.
     */
    @Test
    public void testLimitedSizeQueueRemove(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assert.assertTrue("is false",limitedSizeQueue.add(new Object()));
        Assert.assertEquals("size not 1",1,limitedSizeQueue.size());
        Assert.assertNotNull("queue return null",limitedSizeQueue.remove());
        Assert.assertTrue("queue is not empty",limitedSizeQueue.isEmpty());
    }

    /**
     * Test limited size queue element.
     */
    @Test
    public void testLimitedSizeQueueElement(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assert.assertTrue("is false",limitedSizeQueue.add(new Object()));
        Assert.assertEquals("size not 1",1,limitedSizeQueue.size());
        Assert.assertNotNull("queue return null",limitedSizeQueue.element());
        Assert.assertFalse("queue is empty",limitedSizeQueue.isEmpty());
    }

    /**
     * Test limited size queue max size.
     */
    @Test
    public void testLimitedSizeQueueMaxSize(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>(2);
        Assert.assertEquals("max size not 2",2,limitedSizeQueue.maximumSize());
        Assert.assertTrue("is false",limitedSizeQueue.add(new Object()));
        Assert.assertEquals("size not 1", 1,limitedSizeQueue.size());
        Assert.assertTrue("is false",limitedSizeQueue.add(new Object()));
        Assert.assertEquals("size not 1",2,limitedSizeQueue.size());
        Assert.assertTrue("is false",limitedSizeQueue.add(new Object()));
        Assert.assertEquals("size not 1", 2,limitedSizeQueue.size());
    }

    /**
     * Test limited size queue clear.
     */
    @Test
    public void testLimitedSizeQueueClear(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>(2);
        Assert.assertEquals("max size not 2",2,limitedSizeQueue.maximumSize());
        Assert.assertTrue("is false",limitedSizeQueue.add(new Object()));
        Assert.assertTrue("is false",limitedSizeQueue.add(new Object()));
        Assert.assertTrue("is false",limitedSizeQueue.add(new Object()));
        limitedSizeQueue.clear();
        Assert.assertTrue("queue is not empty",limitedSizeQueue.isEmpty());
    }

    /**
     * Test limited size queue add all.
     */
    @Test
    public void testLimitedSizeQueueAddAll(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assert.assertTrue("is false",limitedSizeQueue.addAll(getList()));
        Assert.assertEquals("queue is not empty",3,limitedSizeQueue.size());
    }

    /**
     * Test limited size queue add all to many.
     */
    @Test
    public void testLimitedSizeQueueAddAllToMany(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>(2);
        Assert.assertTrue("is false",limitedSizeQueue.addAll(getList()));
        Assert.assertEquals("queue is not empty",2,limitedSizeQueue.size());
    }

    /**
     * Test limited size queue remove entry.
     */
    @Test
    public void testLimitedSizeQueueRemoveEntry(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Object obj = new Object();
        List<Object> objects = getList();
        objects.add(obj);
        Assert.assertTrue("is false",limitedSizeQueue.addAll(objects));
        Assert.assertTrue("is false",limitedSizeQueue.remove(obj));
        Assert.assertFalse("is true",limitedSizeQueue.contains(obj));
    }

    /**
     * Test limited size queue remove all.
     */
    @Test
    public void testLimitedSizeQueueRemoveAll(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        List<Object> objects = getList();
        Assert.assertTrue("is false",limitedSizeQueue.addAll(objects));
        Assert.assertTrue("is false",limitedSizeQueue.removeAll(objects));
        Assert.assertTrue("queue is not empty",limitedSizeQueue.isEmpty());
    }

    /**
     * Test limited size queue retain all.
     */
    @Test
    public void testLimitedSizeQueueRetainAll(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        List<Object> objects = getList();
        Assert.assertTrue("is false",limitedSizeQueue.addAll(objects));
        objects.remove(0);
        Assert.assertTrue("is false",limitedSizeQueue.retainAll(objects));
        Assert.assertEquals("queue size not 2",2,limitedSizeQueue.size());
    }

    /**
     * Test limited size queue contains.
     */
    @Test
    public void testLimitedSizeQueueContains(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Object obj = new Object();
        List<Object> objects = getList();
        objects.add(obj);
        Assert.assertTrue("is false",limitedSizeQueue.addAll(objects));
        Assert.assertTrue("is false",limitedSizeQueue.contains(obj));
    }

    /**
     * Test limited size queue contains all.
     */
    @Test
    public void testLimitedSizeQueueContainsAll(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assert.assertTrue("is false",limitedSizeQueue.addAll(getList()));
        Assert.assertTrue("is false",limitedSizeQueue.containsAll(getList()));
    }

    /**
     * Test limited size queue to array.
     */
    @Test
    public void testLimitedSizeQueueToArray(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assert.assertTrue("is false",limitedSizeQueue.addAll(getList()));
        Object[] obj = limitedSizeQueue.toArray();
        Assert.assertNotNull("is null",obj);
        Assert.assertEquals("length not 3",3,obj.length);
    }

    /**
     * Test limited size queue to typed array.
     */
    @Test
    public void testLimitedSizeQueueToTypedArray(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assert.assertTrue("is false",limitedSizeQueue.addAll(getList()));
        String[] strings = limitedSizeQueue.toArray(new String[0]);
        Assert.assertNotNull("is null",strings);
        Assert.assertEquals("length not 3",3,strings.length);
    }

    /**
     * Test limited size queue to iterator.
     */
    @Test
    public void testLimitedSizeQueueToIterator(){
        LimitedSizeQueue<Object> limitedSizeQueue = new LimitedSizeQueue<>();
        Assert.assertTrue("is false",limitedSizeQueue.addAll(getList()));
        Assert.assertTrue("is false",limitedSizeQueue.iterator().hasNext());
    }

    private List<Object> getList(){
        List<Object> objects = new ArrayList<>();
        objects.add("1");
        objects.add("2");
        objects.add("3");
        return objects;
    }

}
