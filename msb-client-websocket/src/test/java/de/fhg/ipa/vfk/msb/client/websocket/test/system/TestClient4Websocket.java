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

import com.fasterxml.jackson.databind.node.NullNode;
import de.fhg.ipa.vfk.msb.client.api.Event;
import de.fhg.ipa.vfk.msb.client.api.SmartObject;
import de.fhg.ipa.vfk.msb.client.api.messages.EventPriority;
import de.fhg.ipa.vfk.msb.client.listener.ConnectionAdapter;
import de.fhg.ipa.vfk.msb.client.util.DataFormatParser;
import de.fhg.ipa.vfk.msb.client.websocket.MsbClient;
import de.fhg.ipa.vfk.msb.client.websocket.MsbClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The type Test client 4 websocket.
 *
 * @author des
 */
public class TestClient4Websocket {

    private static final Logger LOG = LoggerFactory.getLogger(TestClient4Websocket.class);

    private final SmartObject selfDescription;
    private TestClient4WebsocketFunctionHandler webSocketFunctionHandler;
    private final MsbClient msbClient;

    /**
     * Gets uuid.
     *
     * @return the uuid
     */
    public String getUuid() {
        return this.selfDescription.getUuid();
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.selfDescription.getName();
    }

    /**
     * Instantiates a new Test client 4 websocket.
     *
     * @param urlToBroker the url to broker
     * @param token       the token
     */
    public TestClient4Websocket(String urlToBroker, String token) {
        this.msbClient = new MsbClient.Builder()
                .url(urlToBroker)
                .trustStore("./src/test/resources/CERTS.trs", "password")
                .disableHostnameVerification()
                .enabledDataFormatValidation().build();
        this.selfDescription = initSelfDescription(UUID.randomUUID().toString(),
                "client-" + UUID.randomUUID().toString(), token, generateEventsList());
    }

    /**
     * Instantiates a new Test client 4 websocket.
     *
     * @param urlToBroker     the url to broker
     * @param token           the token
     * @param functionHandler the function handler
     */
    public TestClient4Websocket(String urlToBroker, String token, TestClient4WebsocketFunctionHandler functionHandler) {
        this(urlToBroker, token);
        this.webSocketFunctionHandler = functionHandler;
    }

    private static SmartObject initSelfDescription(String uuid, String name, String token, List<Event> events) {
        SmartObject selfDesc = new SmartObject(uuid, name, "Test smart object of websocket interface.\n" +
                "Created at: " + new Date(), token);
        selfDesc.setEvents(events);
        return selfDesc;
    }

    private static List<Event> generateEventsList() {
        List<Event> events = new ArrayList<>();
        Event startEvent = createEvent("Start", "START", "Timestamp of start", String.class);
        events.add(startEvent);
        return events;
    }

    private static Event createEvent(String eventName, String eventID, String eventDescription, Class<?> dataType) {
        Event event = new Event(eventID, eventName, eventDescription);
        if (dataType != NullNode.class) {
            event.setDataFormat(DataFormatParser.parse(dataType));
        }
        return event;
    }

    /***
     * Starts the waiting thread for the WebSocket Client.
     * A CountdonwLatch ensures, that the client can successfully register with the handler.
     * @param ioRegistered the io registered
     * @throws ExecutionException the execution exception
     * @throws IOException the io exception
     * @throws InterruptedException the interrupted exception
     */
    public void startClient(final CountDownLatch ioRegistered) throws ExecutionException, IOException, InterruptedException {
        Future<MsbClientHandler> future = msbClient.connect();
        MsbClientHandler handler = future.get();
        handler.addConnectionListener(new ConnectionAdapter() {
            @Override
            public void afterServiceRegistered() {
                ioRegistered.countDown();
            }
        });
        if (webSocketFunctionHandler != null) {
            handler.register(selfDescription, new Object[]{webSocketFunctionHandler});
        } else {
            handler.register(selfDescription);
        }
    }

    /**
     * Publish.
     *
     * @param event      the event
     * @param doneSignal the done signal
     * @param dataObject the data object
     * @throws IOException the io exception
     */
    public void publish(final String event, final CountDownLatch doneSignal, final Object dataObject) throws IOException {
        final Object nuObject = (dataObject == null ? new Date() : dataObject);
        MsbClientHandler handler = msbClient.getClientHandler();
        handler.publish(event, nuObject, EventPriority.MEDIUM);
        doneSignal.countDown();
    }

    /**
     * Close connection.
     *
     * @param closed the closed
     * @throws Exception the exception
     */
    public void closeConnection(final CountDownLatch closed) throws Exception {
        this.msbClient.getClientHandler().addConnectionListener(new ConnectionAdapter() {
            @Override
            public void afterConnectionClosed() {
                closed.countDown();
            }
        });
        this.msbClient.disconnect();
        this.msbClient.close();
    }

}
