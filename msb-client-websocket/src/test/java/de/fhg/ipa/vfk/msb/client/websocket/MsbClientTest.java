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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The type Msb client test.
 *
 * @author des
 */
class MsbClientTest {

    /**
     * Test build msb client without url.
     */
    @Test
    void testBuildMsbClientWithoutUrl(){
        MsbClient.Builder builder = new MsbClient.Builder()
                .trustStore("/path","password")
                .enabledDataFormatValidation()
                .disableHostnameVerification()
                .disableFunctionCallsInvocation()
                .disableAutoReconnect()
                .disableEventCache();
        Assertions.assertThrows(IllegalStateException.class , builder::build);
    }

    /**
     * Test build msb client default.
     */
    @Test
    void testBuildMsbClientDefault(){
        MsbClient msbClient = new MsbClient.Builder()
                .url("url").build();
        Assertions.assertNotNull(msbClient.getClientHandler());
        Assertions.assertFalse(msbClient.isConnected());
        Assertions.assertFalse(msbClient.isRegistered());
        Assertions.assertEquals("https://url/websocket/data",msbClient.getUrl());
        Assertions.assertFalse(msbClient.isDataFormatValidation());
        Assertions.assertFalse(msbClient.isHostnameVerificationDisabled());
        Assertions.assertTrue(msbClient.isInvokableFunctionCalls());
        Assertions.assertTrue(msbClient.isAutoReconnect());
        Assertions.assertTrue(msbClient.isEventCache());
        Assertions.assertEquals(1000,msbClient.getEventCacheSize());
        Assertions.assertEquals(10000,msbClient.getReconnectInterval());
        Assertions.assertEquals(1000000,msbClient.getWebsocketTextMessageSize());
    }

    /**
     * Test build msb client only url.
     */
    @Test
    void testBuildMsbClientOnlyUrl(){
        MsbClient msbClient = new MsbClient.Builder()
                .url("url")
                .trustStore("/path","password")
                .enabledDataFormatValidation()
                .disableHostnameVerification()
                .disableFunctionCallsInvocation()
                .disableAutoReconnect()
                .disableEventCache()
                .build();
        Assertions.assertNotNull(msbClient.getClientHandler());
        Assertions.assertEquals("https://url/websocket/data",msbClient.getUrl());
        Assertions.assertTrue(msbClient.isDataFormatValidation());
        Assertions.assertTrue(msbClient.isHostnameVerificationDisabled());
        Assertions.assertFalse(msbClient.isInvokableFunctionCalls());
        Assertions.assertFalse(msbClient.isAutoReconnect());
        Assertions.assertFalse(msbClient.isEventCache());
        Assertions.assertEquals(1000,msbClient.getEventCacheSize());
        Assertions.assertEquals(10000,msbClient.getReconnectInterval());
        Assertions.assertEquals(1000000,msbClient.getWebsocketTextMessageSize());
    }

    /**
     * Test build msb client event cache size.
     */
    @Test
    void testBuildMsbClientEventCacheSize(){
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
        Assertions.assertNotNull(msbClient.getClientHandler());
        Assertions.assertEquals("http://url/websocket/data",msbClient.getUrl());
        Assertions.assertTrue(msbClient.isDataFormatValidation());
        Assertions.assertTrue(msbClient.isHostnameVerificationDisabled());
        Assertions.assertFalse(msbClient.isInvokableFunctionCalls());
        Assertions.assertFalse(msbClient.isAutoReconnect());
        Assertions.assertFalse(msbClient.isEventCache());
        Assertions.assertEquals(1,msbClient.getEventCacheSize());
        Assertions.assertEquals(10000,msbClient.getReconnectInterval());
        Assertions.assertEquals(1000000,msbClient.getWebsocketTextMessageSize());
    }

    /**
     * Test build msb client websocket text message size.
     */
    @Test
    void testBuildMsbClientWebsocketTextMessageSize(){
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
        Assertions.assertNotNull(msbClient.getClientHandler());
        Assertions.assertEquals("https://url/websocket/data",msbClient.getUrl());
        Assertions.assertTrue(msbClient.isDataFormatValidation());
        Assertions.assertTrue(msbClient.isHostnameVerificationDisabled());
        Assertions.assertFalse(msbClient.isInvokableFunctionCalls());
        Assertions.assertFalse(msbClient.isAutoReconnect());
        Assertions.assertFalse(msbClient.isEventCache());
        Assertions.assertEquals(1,msbClient.getEventCacheSize());
        Assertions.assertEquals(10000,msbClient.getReconnectInterval());
        Assertions.assertEquals(3,msbClient.getWebsocketTextMessageSize());
    }

    /**
     * Test build msb client all.
     */
    @Test
    void testBuildMsbClientAll(){
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
        Assertions.assertNotNull(msbClient.getClientHandler());
        Assertions.assertEquals("https://url/websocket/data",msbClient.getUrl());
        Assertions.assertTrue(msbClient.isDataFormatValidation());
        Assertions.assertTrue(msbClient.isHostnameVerificationDisabled());
        Assertions.assertFalse(msbClient.isInvokableFunctionCalls());
        Assertions.assertFalse(msbClient.isAutoReconnect());
        Assertions.assertFalse(msbClient.isEventCache());
        Assertions.assertEquals(1,msbClient.getEventCacheSize());
        Assertions.assertEquals(20000,msbClient.getReconnectInterval());
        Assertions.assertEquals(3,msbClient.getWebsocketTextMessageSize());
    }

    /**
     * Test closed msb client connect.
     *
     * @throws Exception the exception
     */
    @Test
    void testClosedMsbClientConnect() throws Exception {
        MsbClient msbClient = new MsbClient.Builder().url("url").build();
        msbClient.disconnect();
        msbClient.close();
        Assertions.assertThrows(IllegalStateException.class, msbClient::connect);
    }

    /**
     * Test connect msb client.
     *
     * @throws Exception the exception
     */
    @Test
    void testConnectMsbClient() throws Exception {
        MsbClient msbClient = new MsbClient.Builder()
                .url("url")
                .trustStore("/path","password")
                .disableHostnameVerification()
                .build();
        Assertions.assertThrows(TimeoutException.class, ()-> msbClient.connect().get(1000,TimeUnit.MILLISECONDS));
        msbClient.disconnect();
        Assertions.assertFalse(msbClient.isConnected());
        msbClient.close();
    }

    /**
     * Test reconnect msb client.
     *
     * @throws Exception the exception
     */
    @Test
    void testReconnectMsbClient() throws Exception {
        MsbClient msbClient = new MsbClient.Builder().url("ws://localhost:8085").disableAutoReconnect().build();
        Assertions.assertThrows(TimeoutException.class, ()-> msbClient.connect().get(1000,TimeUnit.MILLISECONDS));
        Assertions.assertFalse(msbClient.isConnected());
        msbClient.disconnect();
        Assertions.assertFalse(msbClient.isConnected());
        Assertions.assertThrows(TimeoutException.class, ()-> msbClient.connect().get(1000,TimeUnit.MILLISECONDS));
        Assertions.assertFalse(msbClient.isConnected());
        msbClient.close();
        Assertions.assertFalse(msbClient.isConnected());
    }

}
