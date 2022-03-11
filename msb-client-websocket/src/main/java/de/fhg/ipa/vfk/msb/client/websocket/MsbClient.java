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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * The type Client to establish a connection with an MSB instance.
 *
 * @author des
 */
public class MsbClient implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(MsbClient.class);

    private static final String DEFAULT_PROTOCOL = "https";
    private static final int MIN_RECONNECT_INTERVAL= 10000;
    private static final int INITIAL = 0;
    private static final int STARTED = 1;
    private static final int STOPPED = 2;
    private static final int CLOSED = 3;

    private final Object lock = new Object();

    private long disconnectTimestamp;
    private int state = INITIAL;

    /**
     * The constant hostnameVerification.
     */
    protected static boolean hostnameVerificationDisabled = false;

    private final MsbClientWebSocketHandler clientHandler;

    /**
     * Instantiates a new Client.
     *
     * @param url the url of MSB websocket interface
     */
    public MsbClient(String url) {
        url = checkUrl(url);
        this.clientHandler = new MsbClientWebSocketHandler(url);
    }

    /**
     * Instantiates a new Msb client.
     *
     * @param url            the url
     * @param eventCacheSize the event cache size
     */
    public MsbClient(String url, int eventCacheSize) {
        url = checkUrl(url);
        this.clientHandler = new MsbClientWebSocketHandler(url, eventCacheSize);
    }

    /**
     * Instantiates a new Msb client.
     *
     * @param url                      the url
     * @param eventCacheSize           the event cache size
     * @param websocketTextMessageSize the websocket text message size
     */
    public MsbClient(String url, int eventCacheSize, int websocketTextMessageSize) {
        url = checkUrl(url);
        this.clientHandler = new MsbClientWebSocketHandler(url, eventCacheSize, websocketTextMessageSize);
    }

    /**
     * Instantiates a new Msb client.
     *
     * @param url                          the url
     * @param eventCacheSize               the event cache size
     * @param websocketTextMessageSize     the websocket text message size
     * @param functionCallExecutorPoolSize the function call executor pool size
     */
    public MsbClient(String url, int eventCacheSize, int websocketTextMessageSize, int functionCallExecutorPoolSize) {
        url = checkUrl(url);
        this.clientHandler = new MsbClientWebSocketHandler(url, eventCacheSize, websocketTextMessageSize,functionCallExecutorPoolSize);
    }

    /**
     * Sets trust store for secure connection over wss/https.
     * Alternatively use the following system property to inject trust store.
     * System.setProperty("javax.net.ssl.trustStore", "[path to trust store].trs");
     *
     * @param trustStorePath the trust store path
     * @param trustStorePwd  the trust store pwd
     */
    public void setTrustStore(String trustStorePath, String trustStorePwd) {
        clientHandler.setTrustStore(trustStorePath, trustStorePwd);
    }

    /**
     * Disable hostname checking when establishing a secure connection,
     * if self-signed certificates are used or an ip is used instead of a domain name.
     *
     * @param disable the hostname verification
     */
    public static void disableHostnameVerification(boolean disable) {
        hostnameVerificationDisabled = disable;
    }


    /**
     * Is hostname verification enabled.
     *
     * @return the boolean
     */
    public boolean isHostnameVerificationDisabled() {
        return hostnameVerificationDisabled;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return clientHandler.getUrl();
    }

    /**
     * Gets event cache size.
     *
     * @return the event cache size
     */
    public int getEventCacheSize() {
        return clientHandler.getEventCacheSize();
    }

    /**
     * Gets websocket text message size.
     *
     * @return the websocket text message size
     */
    public int getWebsocketTextMessageSize() {
        return clientHandler.getWebsocketTextMessageSize();
    }

    /**
     * Get client handler to interact with the connected MSB instance.
     *
     * @return the client handler
     */
    public MsbClientHandler getClientHandler() {
        return clientHandler;
    }

    /**
     * Is invokable function calls boolean.
     *
     * @return the boolean
     */
    public boolean isInvokableFunctionCalls() {
        return clientHandler.isInvokableFunctionCalls();
    }

    /**
     * Sets invokable function calls.
     *
     * @param invokable the invokable
     */
    public void setInvokableFunctionCalls(boolean invokable) {
        clientHandler.setInvokableFunctionCalls(invokable);
    }

    /**
     * Is event cache boolean.
     *
     * @return the boolean
     */
    public boolean isEventCache(){
        return clientHandler.isEventCacheEnabled();
    }

    /**
     * Set event cache.
     *
     * @param eventCacheEnabled the event cache enabled
     */
    public void setEventCache(boolean eventCacheEnabled){
        clientHandler.setEventCacheEnabled(eventCacheEnabled);
    }

    /**
     * Is auto reconnect boolean.
     *
     * @return the boolean
     */
    public boolean isAutoReconnect() {
        return clientHandler.isAutoReconnect();
    }

    /**
     * Sets auto reconnect.
     *
     * @param autoReconnect the auto reconnect
     */
    public void setAutoReconnect(boolean autoReconnect) {
        clientHandler.setAutoReconnect(autoReconnect);
    }

    /**
     * Sets reconnect interval, it must be greater than 3000 milliseconds.
     *
     * @param millis the millis
     */
    public void setReconnectInterval(int millis) {
        if (millis>MIN_RECONNECT_INTERVAL){
            clientHandler.setReconnectInterval(millis);
        } else {
            LOG.error("Ignored, interval for reconnecting must be greater than: "+MIN_RECONNECT_INTERVAL+" milliseconds");
        }
    }

    /**
     * Gets reconnect interval.
     *
     * @return the reconnect interval
     */
    public int getReconnectInterval() {
        return clientHandler.getReconnectInterval();
    }

    /**
     * Is data format validation boolean.
     *
     * @return the boolean
     */
    public boolean isDataFormatValidation(){
        return clientHandler.isDataFormatValidation();
    }

    /**
     * Set data format validation.
     *
     * @param dataFormatValidationEnabled the data format validation enabled
     */
    public void setDataFormatValidation(boolean dataFormatValidationEnabled){
        clientHandler.setDataFormatValidation(dataFormatValidationEnabled);
    }

    /**
     * Start the client, which opens and hold a connection to MSB.
     * After connection is established, this method returns a client handler to interact with the connected MSB instance.
     *
     * @return the client handler
     */
    public synchronized Future<MsbClientHandler> connect() {
        if(state == CLOSED){
            throw new IllegalStateException("The client is closed, create a new instance to connect.");
        }
        LOG.info("connect client");

        long waitingTime = getReconnectInterval() - (System.currentTimeMillis() - disconnectTimestamp);
        if (state==STOPPED && waitingTime > 0 ){
            LOG.debug("Client wait {} ms until connect client again", waitingTime);
            try {
                synchronized(lock) {
                    while (System.currentTimeMillis() - disconnectTimestamp < waitingTime) {
                        lock.wait(waitingTime);
                    }
                }
            } catch (InterruptedException e) {
                LOG.warn("Minimal wait time to restart client interrupted",e);
                // Restore interrupted state...
                Thread.currentThread().interrupt();
            }
        }

        CompletableFuture<MsbClientHandler> future = clientHandler.establishConnection().thenApply(aBoolean -> {
            if (Boolean.TRUE.equals(aBoolean)) {
                LOG.info("client connected");
                return clientHandler;
            } else {
                return null;
            }
        });

        state=STARTED;
        LOG.info("client connecting");
        return future;
    }

    /**
     * Checks if is connected.
     *
     * @return true, if is connected
     */
    public boolean isConnected() {
        return clientHandler.isConnected();
    }

    /**
     * Is registered boolean.
     *
     * @return the boolean
     */
    public synchronized boolean isRegistered() {
        return clientHandler.isRegistered();
    }

    /**
     * Stop client and close connection to MSB.
     */
    public synchronized void disconnect() {
        LOG.info("disconnect client");
        disconnectTimestamp = System.currentTimeMillis();
        clientHandler.closeConnection();
        state=STOPPED;
        LOG.info("client disconnected");
    }

    @Override
    public synchronized void close() throws Exception {
        if (state == STARTED) {
            disconnect();
        }
        clientHandler.close();
        state=CLOSED;
        LOG.info("client terminated");
    }

    private String checkUrl(String url){
        if(!url.startsWith("http://")&&!url.startsWith("https://")&&!url.startsWith("ws://")&&!url.startsWith("wss://")){
            LOG.warn("Missing protocol information at url, set '"+DEFAULT_PROTOCOL+"' as default protocol");
            return DEFAULT_PROTOCOL+ "://" + url;
        }
        return url;
    }

    /**
     * The type Builder to build a client.
     */
    public static class Builder {

        /**
         * Instantiates a new Builder.
         */
        public Builder(){
            //Default constructor
        }

        private String url = "";
        private int eventCacheSize = -1;
        private int websocketTextMessageSize = -1;
        private int functionCallExecutorPoolSize = -1;
        private boolean hostnameVerification = false;
        private String trustStorePath = "";
        private String trustStorePwd = "";
        private boolean invokableFunctionCallsEnabled = true;
        private boolean reconnectEnabled = true;
        private boolean eventCacheEnabled = true;
        private int reconnectInterval = -1;
        private boolean dataFormatValidationEnabled = false;

        /**
         * Set the url of MSB websocket interface
         *
         * @param url the url
         * @return the builder
         */
        public Builder url(String url){
            this.url = url;
            return this;
        }

        /**
         * Event cache size builder.
         *
         * @param eventCacheSize the event cache size
         * @return the builder
         */
        public Builder eventCacheSize(int eventCacheSize){
            this.eventCacheSize = eventCacheSize;
            return this;
        }

        /**
         * Set websocket text message size.
         *
         * @param websocketTextMessageSize the websocket text message size
         * @return the builder
         */
        public Builder websocketTextMessageSize(int websocketTextMessageSize){
            this.websocketTextMessageSize = websocketTextMessageSize;
            return this;
        }

        /**
         * Function call executor pool size builder.
         *
         * @param functionCallExecutorPoolSize the function call executor pool size
         * @return the builder
         */
        public Builder functionCallExecutorPoolSize(int functionCallExecutorPoolSize){
            this.functionCallExecutorPoolSize = functionCallExecutorPoolSize;
            return this;
        }

        /**
         * Disable hostname checking when establishing a secure connection,
         * if self-signed certificates are used or an ip is used instead of a domain name.
         *
         * @return the builder
         */
        public Builder disableHostnameVerification(){
            this.hostnameVerification = true;
            return this;
        }



        /**
         * Sets trust store for secure connection over wss/https.
         * Alternatively use the following system property to inject trust store.
         * System.setProperty("javax.net.ssl.trustStore", "[path to trust store].trs");
         *
         * @param trustStorePath the trust store path
         * @param trustStorePwd  the trust store pwd
         * @return the builder
         */
        public Builder trustStore(String trustStorePath, String trustStorePwd){
            this.trustStorePath = trustStorePath;
            this.trustStorePwd = trustStorePwd;
            return this;
        }

        /**
         * Disable function calls invocation of client.
         *
         * @return the builder
         */
        public Builder disableFunctionCallsInvocation(){
            this.invokableFunctionCallsEnabled = false;
            return this;
        }

        /**
         * Disable event cache builder.
         *
         * @return the builder
         */
        public Builder disableEventCache(){
            this.eventCacheEnabled = false;
            return this;
        }

        /**
         * Disable auto reconnect builder.
         *
         * @return the builder
         */
        public Builder disableAutoReconnect(){
            this.reconnectEnabled = false;
            return this;
        }

        /**
         * Sets reconnect interval, it must be greater than 3000 milliseconds.
         *
         * @param reconnectInterval the reconnect interval
         * @return the builder
         */
        public Builder reconnectInterval(int reconnectInterval){
            this.reconnectInterval = reconnectInterval;
            return this;
        }

        /**
         * Enabled data format validation builder.
         *
         * @return the builder
         */
        public Builder enabledDataFormatValidation(){
            this.dataFormatValidationEnabled = true;
            return this;
        }

        /**
         * Build client.
         *
         * @return the client
         */
        public MsbClient build(){
            MsbClient msbClient;
            if ("".equals(url)){
                throw new IllegalStateException("url is required");
            } else if(eventCacheSize >-1 && websocketTextMessageSize > -1 && functionCallExecutorPoolSize > -1) {
                msbClient = new MsbClient(url, eventCacheSize,websocketTextMessageSize,functionCallExecutorPoolSize);
            } else if(eventCacheSize >-1 && websocketTextMessageSize > -1) {
                msbClient = new MsbClient(url, eventCacheSize,websocketTextMessageSize);
            } else if(eventCacheSize >-1){
                msbClient = new MsbClient(url, eventCacheSize);
            } else {
                msbClient = new MsbClient(url);
            }
            if(!"".equals(trustStorePath) && !"".equals(trustStorePwd)){
               msbClient.setTrustStore(trustStorePath,trustStorePwd);
            }
            MsbClient.disableHostnameVerification(hostnameVerification);
            msbClient.setInvokableFunctionCalls(invokableFunctionCallsEnabled);
            msbClient.setEventCache(eventCacheEnabled);
            msbClient.setAutoReconnect(reconnectEnabled);
            msbClient.setDataFormatValidation(dataFormatValidationEnabled);
            if(reconnectInterval>-1) {
                msbClient.setReconnectInterval(reconnectInterval);
            }
            return msbClient;
        }

    }

}
