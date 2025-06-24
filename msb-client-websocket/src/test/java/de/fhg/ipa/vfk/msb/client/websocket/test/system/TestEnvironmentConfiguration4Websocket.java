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

package de.fhg.ipa.vfk.msb.client.websocket.test.system;

import de.fhg.ipa.vfk.msb.client.websocket.MsbClient;
import de.fhg.ipa.vfk.msb.client.websocket.MsbClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * The type Test environment configuration for websocket.
 *
 * @author des
 */
public class TestEnvironmentConfiguration4Websocket extends TestEnvironmentConfiguration {

    private static Logger LOG = LoggerFactory.getLogger(TestEnvironmentConfiguration4Websocket.class);

    /**
     * Instantiates a new Test environment configuration 4 websocket.
     */
    public TestEnvironmentConfiguration4Websocket() {
        super();
    }

    /**
     * Initialize test environment.
     *
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    public void initializeTestEnvironment() throws IOException, InterruptedException {

        final CountDownLatch startSig = new CountDownLatch(1);
        final CountDownLatch doneSig = new CountDownLatch(3);
        final int timeOutMinutes = 4;

        waitForInterfaceWebsocket(startSig, doneSig);
        waitForSmartObjectMgmtRest(startSig, doneSig);
        waitForIntegrationDesignMgmtRest(startSig, doneSig);

        startSig.countDown();
        boolean success = doneSig.await(timeOutMinutes, TimeUnit.MINUTES);
        if (!success) {
            unloadTestEnvironment(false);
        }
    }

    /**
     * Wait for interface websocket.
     *
     * @param startSignal the start signal
     * @param doneSignal  the done signal
     */
    public void waitForInterfaceWebsocket(final CountDownLatch startSignal, final CountDownLatch doneSignal) {
        new Thread(() -> {
            try {
                startSignal.await();
            } catch (InterruptedException e) {
                LOG.error("InterruptedException: ", e);
            }
            MsbClientHandler handler = returnClientHandlerForWebSocket();
            LOG.info("Connected to InterfaceWebsocket.");
            try {
                handler.close();
            } catch (Exception e) {
                LOG.info("Exception: {}", e.getMessage());
            }
            LOG.info("Closed connection to InterfaceWebsocket.");
            doneSignal.countDown();
        }).start();
    }

    /**
     * Return client handler for web socket msb client handler.
     *
     * @return the msb client handler
     */
    public MsbClientHandler returnClientHandlerForWebSocket() {
        LOG.info("Trying to connect to WebSocket Interface with URL: {}", getUrlInterfaceWebSocket());
        MsbClient msbClient = new MsbClient.Builder().url(getUrlInterfaceWebSocket()).trustStore("./src/test/resources/CERTS.trs", "password").disableHostnameVerification().enabledDataFormatValidation().build();
        Future<MsbClientHandler> future = msbClient.connect();
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Exception: ", e);
        }
        return null;
    }

}
