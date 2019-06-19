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

import de.fhg.ipa.vfk.msb.client.annotation.FunctionCall;
import de.fhg.ipa.vfk.msb.client.annotation.FunctionHandler;
import de.fhg.ipa.vfk.msb.client.annotation.FunctionParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * The type Test client 4 websocket function handler.
 *
 * @author des
 */
@FunctionHandler(path = "/functionhandler")
public class TestClient4WebsocketFunctionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(TestClient4WebsocketFunctionHandler.class);

    private final CountDownLatch doneSignal;

    /**
     * Instantiates a new Test client 4 websocket function handler.
     *
     * @param didExecuteLatch the did execute latch
     */
    public TestClient4WebsocketFunctionHandler(final CountDownLatch didExecuteLatch) {
        this.doneSignal = didExecuteLatch;
    }

    /**
     * Test call.
     *
     * @param name the name
     */
    @FunctionCall(path = "/testCall", description = "test call")
    public void testCall(@FunctionParam(name = "param") String name) {
        LOG.info("I just produced tires for: '" + name + "'.");
        if (this.doneSignal != null) {
            LOG.info("Now counting down the latch!");
            doneSignal.countDown();
            LOG.info("Times left until success: " + doneSignal.getCount());
        }
    }

}
