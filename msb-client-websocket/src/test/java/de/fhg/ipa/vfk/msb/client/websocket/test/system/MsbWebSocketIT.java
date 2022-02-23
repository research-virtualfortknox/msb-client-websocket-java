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

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * The type Msb web socket it.
 *
 * @author des
 */
public class MsbWebSocketIT {

    private static final Logger LOG = LoggerFactory.getLogger(MsbWebSocketIT.class);
    private static TestEnvironmentConfiguration4Websocket testEnv;

    /**
     * Inits the test environment.
     */
    @BeforeClass
    public static void initTestEnvironment() {
        LOG.info("Loading test environment configuration ....");
        testEnv = new TestEnvironmentConfiguration4Websocket();
        try {
            testEnv.initializeTestEnvironment();
        } catch (IOException | InterruptedException e) {
            LOG.warn(e.getMessage(), e);
            testEnv.unloadTestEnvironment(false);
        }
    }

    /**
     * Test register smart object via websocket interface and verify via smart object mgmt rest.
     *
     * @throws Exception the exception
     */
    @Test
    public void testRegisterSmartObjectViaWebsocketInterfaceAndVerifyViaSmartObjectMgmtRest() throws Exception {
        LOG.info("start test register SmartObject via WebsocketInterface and verify via SmartObjectMgmtRest");
        // Setup self-description for a smart object.
        TestClient4Websocket testClient = new TestClient4Websocket(testEnv.getUrlInterfaceWebSocket(), UUID.randomUUID().toString());
        // Start the client - it connects to the WebSocket interface.
        CountDownLatch registered = new CountDownLatch(1);
        testClient.startClient(registered);
        assertTrue("client not registered", registered.await(6, TimeUnit.SECONDS));

        // Assert for correct name, UUID and registration of our test client.
        Future<ResponseEntity<ObjectNode>> responseEntityFuture = testEnv.waitAndGetService(testClient.getUuid());
        ResponseEntity<ObjectNode> responseEntity = responseEntityFuture.get(6, TimeUnit.SECONDS);
        assertTrue("get smart object has no response body", responseEntity.hasBody());
        assertEquals("get smart object status code is not 2xx", HttpStatus.OK, responseEntity.getStatusCode());

        ObjectNode returnedSelfDescription = responseEntity.getBody();
        LOG.info("json: {}", returnedSelfDescription);
        assertNotNull("returned self-description is null", returnedSelfDescription);
        assertEquals("returned self-description name not equals", testClient.getName(), returnedSelfDescription.get("name").asText());
        assertEquals("returned self-description uuid not equals", testClient.getUuid(), returnedSelfDescription.get("uuid").asText());
        assertEquals("returned self-description lifecycle state is not registered", "REGISTRATED", returnedSelfDescription.get("lifecycleState").asText());

        // Remove our client from the MSB.
        CountDownLatch closed = new CountDownLatch(1);
        testClient.closeConnection(closed);
        assertTrue("client not closed", closed.await(3, TimeUnit.SECONDS));

        testEnv.deleteService(testClient.getUuid());
        // Assert successful removal.
        assertEquals("Did not successfully delete client", HttpStatus.NOT_FOUND, testEnv.getService(testClient.getUuid()).getStatusCode());
    }

    /**
     * Test simple integration flow.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSimpleIntegrationFlow() throws Exception {
        LOG.info("start test simple IntegrationFlow");
        int publishTimes = 2;

        // Setup clients for smart objects.
        ResponseEntity<ObjectNode> token1 = testEnv.getNewToken(testEnv.getOwnerUuid());
        assertTrue("get token has no response body", token1.hasBody());
        TestClient4Websocket client1 = new TestClient4Websocket(testEnv.getUrlInterfaceWebSocket(), token1.getBody().get("token").asText());
        // Client2 needs a function handler, that signals with a CountDownLatch after the method was invoked.
        CountDownLatch client2DidExecuteMethod = new CountDownLatch(publishTimes);
        TestClient4WebsocketFunctionHandler functionHandlerClient2 = new TestClient4WebsocketFunctionHandler(client2DidExecuteMethod);
        ResponseEntity<ObjectNode> token2 = testEnv.getNewToken(testEnv.getOwnerUuid());
        assertTrue("get token has no response body", token2.hasBody());
        TestClient4Websocket client2 = new TestClient4Websocket(testEnv.getUrlInterfaceWebSocket(), token2.getBody().get("token").asText(), functionHandlerClient2);
        CountDownLatch registered = new CountDownLatch(2);
        client1.startClient(registered);
        client2.startClient(registered);
        assertTrue("clients not registered", registered.await(6, TimeUnit.SECONDS));

        // Connect to SmartObjectMgmt to get the endPoint information
        for (TestClient4Websocket testClient : new TestClient4Websocket[]{client1, client2}) {
            Future<ResponseEntity<ObjectNode>> responseEntityFuture = testEnv.waitAndGetService(testClient.getUuid());
            ResponseEntity<ObjectNode> responseEntity = responseEntityFuture.get(6, TimeUnit.SECONDS);
            assertTrue("get smart object has no response body", responseEntity.hasBody());
            ObjectNode returnedSelfDescription = responseEntity.getBody();
            assertNotNull("returned self-description is null", returnedSelfDescription);
            assertEquals("returned self-description name not equals", testClient.getName(), returnedSelfDescription.get("name").asText());
            assertEquals("returned self-description uuid not equals", testClient.getUuid(), returnedSelfDescription.get("uuid").asText());
            assertEquals("returned self-description lifecycle state is not verified", "VERIFIED", returnedSelfDescription.get("lifecycleState").asText());
        }

        // Create a new integration flow
        String flow = testEnv.loadFlow(client1.getUuid(), client1.getName(), client2.getUuid(), client2.getName());
        ResponseEntity<ObjectNode> response = testEnv.createAndDeployFlow(flow);
        assertTrue("The response entity for the integration flow has no body.", response.hasBody());
        assertNotNull("The response entity for the integration flow has null body.", response.getBody());
        long flowId = response.getBody().get("id").asLong();
        LOG.info("created flow with id " + flowId);
        assertTrue("The response entity for the integration flow does not have a valid id.", flowId > 0);
        assertTrue("The integration flow should be deployed, but it is not.", response.getBody().get("deployed").asBoolean());

        // Let Client1 publish an Event, such that the method handler for client2 gets some input.
        CountDownLatch publishTimesLatch = new CountDownLatch(publishTimes);
        client1.publish("START", publishTimesLatch, "");
        client1.publish("START", publishTimesLatch, "");
        boolean successFullyPublishedWithClient1 = publishTimesLatch.await(2, TimeUnit.MINUTES);
        assertTrue("client not published " + publishTimes + " events", successFullyPublishedWithClient1);

        // Assert incoming method calls for client2
        boolean successFullyExecutedMethodOnClient2 = client2DidExecuteMethod.await(2, TimeUnit.MINUTES);
        assertTrue("client not received " + publishTimes + " function calls", successFullyExecutedMethodOnClient2);

        // now cleanup the integration flow.
        testEnv.deleteFlow(flowId);
        assertFalse("Did not successfully delete the integration flow.", testEnv.getFlow(flowId).hasBody());

        // now cleanup the clients.
        CountDownLatch closed = new CountDownLatch(2);
        client1.closeConnection(closed);
        client2.closeConnection(closed);
        assertTrue("clients not closed", closed.await(3, TimeUnit.SECONDS));

        testEnv.deleteService(client1.getUuid());
        testEnv.deleteService(client2.getUuid());
        assertEquals("Did not successfully delete client1.", HttpStatus.NOT_FOUND, testEnv.getService(client1.getUuid()).getStatusCode());
        assertEquals("Did not successfully delete client2.", HttpStatus.NOT_FOUND, testEnv.getService(client2.getUuid()).getStatusCode());
    }

    /**
     * Unload test.
     */
    @AfterClass
    public static void unloadTest() {
        testEnv.unloadTestEnvironment(true);
    }
}
