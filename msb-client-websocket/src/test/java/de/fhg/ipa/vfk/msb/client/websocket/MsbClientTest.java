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

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The type Msb client test.
 *
 * @author des
 */
public class MsbClientTest {

    /**
     * Test build msb client without url.
     */
    @Test(expected=IllegalStateException.class)
    public void testBuildMsbClientWithoutUrl(){
        new MsbClient.Builder()
                .trustStore("/path","password")
                .enabledDataFormatValidation()
                .disableHostnameVerification()
                .disableFunctionCallsInvocation()
                .disableAutoReconnect()
                .disableEventCache()
                .build();
    }

    /**
     * Test build msb client default.
     */
    @Test
    public void testBuildMsbClientDefault(){
        MsbClient msbClient = new MsbClient.Builder()
                .url("url").build();
        Assert.assertNotNull(msbClient.getClientHandler());
        Assert.assertFalse(msbClient.isConnected());
        Assert.assertFalse(msbClient.isRegistered());
        Assert.assertEquals("https://url/websocket/data",msbClient.getUrl());
        Assert.assertFalse(msbClient.isDataFormatValidation());
        Assert.assertFalse(msbClient.isHostnameVerificationDisabled());
        Assert.assertTrue(msbClient.isInvokableFunctionCalls());
        Assert.assertTrue(msbClient.isAutoReconnect());
        Assert.assertTrue(msbClient.isEventCache());
        Assert.assertEquals(1000,msbClient.getEventCacheSize());
        Assert.assertEquals(10000,msbClient.getReconnectInterval());
        Assert.assertEquals(1000000,msbClient.getWebsocketTextMessageSize());
    }

    /**
     * Test build msb client only url.
     */
    @Test
    public void testBuildMsbClientOnlyUrl(){
        MsbClient msbClient = new MsbClient.Builder()
                .url("url")
                .trustStore("/path","password")
                .enabledDataFormatValidation()
                .disableHostnameVerification()
                .disableFunctionCallsInvocation()
                .disableAutoReconnect()
                .disableEventCache()
                .build();
        Assert.assertNotNull(msbClient.getClientHandler());
        Assert.assertEquals("https://url/websocket/data",msbClient.getUrl());
        Assert.assertTrue(msbClient.isDataFormatValidation());
        Assert.assertTrue(msbClient.isHostnameVerificationDisabled());
        Assert.assertFalse(msbClient.isInvokableFunctionCalls());
        Assert.assertFalse(msbClient.isAutoReconnect());
        Assert.assertFalse(msbClient.isEventCache());
        Assert.assertEquals(1000,msbClient.getEventCacheSize());
        Assert.assertEquals(10000,msbClient.getReconnectInterval());
        Assert.assertEquals(1000000,msbClient.getWebsocketTextMessageSize());
    }

    /**
     * Test build msb client event cache size.
     */
    @Test
    public void testBuildMsbClientEventCacheSize(){
        MsbClient msbClient = new MsbClient.Builder()
                .url("http://url")
                .trustStore("/path","password")
                .enabledDataFormatValidation()
                .disableHostnameVerification()
                .disableFunctionCallsInvocation()
                .disableAutoReconnect()
                .disableEventCache()
                .eventCacheSize(1)
                .build();
        Assert.assertNotNull(msbClient.getClientHandler());
        Assert.assertEquals("http://url/websocket/data",msbClient.getUrl());
        Assert.assertTrue(msbClient.isDataFormatValidation());
        Assert.assertTrue(msbClient.isHostnameVerificationDisabled());
        Assert.assertFalse(msbClient.isInvokableFunctionCalls());
        Assert.assertFalse(msbClient.isAutoReconnect());
        Assert.assertFalse(msbClient.isEventCache());
        Assert.assertEquals(1,msbClient.getEventCacheSize());
        Assert.assertEquals(10000,msbClient.getReconnectInterval());
        Assert.assertEquals(1000000,msbClient.getWebsocketTextMessageSize());
    }

    /**
     * Test build msb client websocket text message size.
     */
    @Test
    public void testBuildMsbClientWebsocketTextMessageSize(){
        MsbClient msbClient = new MsbClient.Builder()
                .url("https://url/websocket/data")
                .trustStore("/path","password")
                .enabledDataFormatValidation()
                .disableHostnameVerification()
                .disableFunctionCallsInvocation()
                .disableAutoReconnect()
                .disableEventCache()
                .eventCacheSize(1)
                .websocketTextMessageSize(3)
                .reconnectInterval(4)
                .build();
        Assert.assertNotNull(msbClient.getClientHandler());
        Assert.assertEquals("https://url/websocket/data",msbClient.getUrl());
        Assert.assertTrue(msbClient.isDataFormatValidation());
        Assert.assertTrue(msbClient.isHostnameVerificationDisabled());
        Assert.assertFalse(msbClient.isInvokableFunctionCalls());
        Assert.assertFalse(msbClient.isAutoReconnect());
        Assert.assertFalse(msbClient.isEventCache());
        Assert.assertEquals(1,msbClient.getEventCacheSize());
        Assert.assertEquals(10000,msbClient.getReconnectInterval());
        Assert.assertEquals(3,msbClient.getWebsocketTextMessageSize());
    }

    /**
     * Test build msb client all.
     */
    @Test
    public void testBuildMsbClientAll(){
        MsbClient msbClient = new MsbClient.Builder()
                .url("url")
                .trustStore("/path","password")
                .enabledDataFormatValidation()
                .disableHostnameVerification()
                .disableFunctionCallsInvocation()
                .disableAutoReconnect()
                .disableEventCache()
                .eventCacheSize(1)
                .reconnectInterval(20000)
                .websocketTextMessageSize(3)
                .functionCallExecutorPoolSize(1)
                .build();
        Assert.assertNotNull(msbClient.getClientHandler());
        Assert.assertEquals("https://url/websocket/data",msbClient.getUrl());
        Assert.assertTrue(msbClient.isDataFormatValidation());
        Assert.assertTrue(msbClient.isHostnameVerificationDisabled());
        Assert.assertFalse(msbClient.isInvokableFunctionCalls());
        Assert.assertFalse(msbClient.isAutoReconnect());
        Assert.assertFalse(msbClient.isEventCache());
        Assert.assertEquals(1,msbClient.getEventCacheSize());
        Assert.assertEquals(20000,msbClient.getReconnectInterval());
        Assert.assertEquals(3,msbClient.getWebsocketTextMessageSize());
    }

    /**
     * Test closed msb client connect.
     *
     * @throws Exception the exception
     */
    @Test(expected = IllegalStateException.class)
    public void testClosedMsbClientConnect() throws Exception {
        MsbClient msbClient = new MsbClient.Builder().url("url").build();
        msbClient.disconnect();
        msbClient.close();
        msbClient.connect();
    }

    /**
     * Test connect msb client.
     *
     * @throws Exception the exception
     */
    @Test
    public void testConnectMsbClient() throws Exception {
        MsbClient msbClient = new MsbClient.Builder()
                .url("url")
                .trustStore("/path","password")
                .disableHostnameVerification()
                .build();
        Future<MsbClientHandler> future = msbClient.connect();
        try {
            future.get(1000,TimeUnit.MILLISECONDS);
        } catch (TimeoutException e){}
        Assert.assertFalse(future.isDone());
        msbClient.disconnect();
        Assert.assertFalse(msbClient.isConnected());
        msbClient.close();
    }

    /**
     * Test reconnect msb client.
     *
     * @throws Exception the exception
     */
    @Test
    public void testReconnectMsbClient() throws Exception {
        MsbClient msbClient = new MsbClient.Builder().url("url").disableAutoReconnect().build();
        msbClient.connect();
        Assert.assertFalse(msbClient.isConnected());
        msbClient.disconnect();
        Assert.assertFalse(msbClient.isConnected());
        msbClient.connect();
        Assert.assertFalse(msbClient.isConnected());
        msbClient.close();
    }

}
