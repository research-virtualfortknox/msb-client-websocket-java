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

package de.fhg.ipa.vfk.msb.client.websocket.example;

import de.fhg.ipa.vfk.msb.client.api.Configuration;
import de.fhg.ipa.vfk.msb.client.api.Event;
import de.fhg.ipa.vfk.msb.client.api.Function;
import de.fhg.ipa.vfk.msb.client.api.ParameterValue;
import de.fhg.ipa.vfk.msb.client.api.PrimitiveFormat;
import de.fhg.ipa.vfk.msb.client.api.PrimitiveType;
import de.fhg.ipa.vfk.msb.client.api.SmartObject;
import de.fhg.ipa.vfk.msb.client.api.messages.EventPriority;
import de.fhg.ipa.vfk.msb.client.listener.ConfigurationListener;
import de.fhg.ipa.vfk.msb.client.listener.FunctionCallsListener;
import de.fhg.ipa.vfk.msb.client.util.DataFormatParser;
import de.fhg.ipa.vfk.msb.client.websocket.MsbClient;
import de.fhg.ipa.vfk.msb.client.websocket.MsbClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The type Programmatic test client.
 *
 * @author des
 */
public class ProgrammaticTestClient {

    private static final Logger LOG = LoggerFactory.getLogger(ProgrammaticTestClient.class);

    private static final String PULSE_EVENT = "PULSE";
    private static final String TEMPERATURE_EVENT = "TEMPERATURE";

    private String uuid;

    private String name;

    private String token;

    private MsbClient msbClient;

    /**
     * Instantiates a new Programmatic test client.
     *
     * @param uuid  the uuid
     * @param name  the name
     * @param token the token
     */
    public ProgrammaticTestClient(String uuid, String name, String token) {
        this.uuid = uuid;
        this.name = name;
        this.token = token;
        this.msbClient = new MsbClient.Builder().url("wss://localhost:8084").disableHostnameVerification().enabledDataFormatValidation().build();
    }

    /**
     * Start client.
     *
     * @throws ExecutionException   the execution exception
     * @throws InterruptedException the interrupted exception
     * @throws IOException          the io exception
     */
    public void startClient() throws ExecutionException, InterruptedException, IOException {
        Future<MsbClientHandler> future = msbClient.connect();
        MsbClientHandler handler = future.get();
        handler.addFunctionCallsListener(functionCallsListener);
        handler.addConfigurationListener(configurationListener);

        SmartObject smartObject = new SmartObject(uuid, name, "Test client", token);

        Map<String, ParameterValue> parameters = new HashMap<>();
        parameters.put("drillingSpeed", new ParameterValue(100, PrimitiveType.INTEGER, PrimitiveFormat.INT32));
        parameters.put("scheduled", new ParameterValue(true, PrimitiveType.BOOLEAN));

        smartObject.setConfiguration(new Configuration(parameters));
        smartObject.addEvent(new Event(TEMPERATURE_EVENT, "Temperature", "Current temperature", DataFormatParser.parse(float.class)));
        smartObject.addFunction(new Function("hello", "helloWorld", "Hello World", DataFormatParser.parse("msg", String.class)));

        handler.addConfigParam("drillingSpeed", "100", PrimitiveFormat.INT32);
        handler.addEvent(smartObject, PULSE_EVENT, "Pulse", "Pulse event", Void.class, EventPriority.LOW);
        try {
            Method stringMethod = TestClientFunctionHandler.class.getMethod("printString", String.class);
            handler.addFunction(smartObject, "printString", "print string", "print a string", new String[]{PULSE_EVENT}, new TestClientFunctionHandler(), stringMethod);
        } catch (NoSuchMethodException e) {
            LOG.error("NoSuchMethodException", e);
        }

        handler.register(smartObject);

        final CountDownLatch count = new CountDownLatch(2);
        new Thread(() -> {
            try {
                while (count.getCount() > 0) {
                    Thread.sleep(1000);
                    msbClient.getClientHandler().publish(PULSE_EVENT);
                    msbClient.getClientHandler().publish(TEMPERATURE_EVENT, Float.valueOf(count.getCount()));
                    count.countDown();
                }
            } catch (Exception e) {
                LOG.error("Exception: ", e);
                Thread.currentThread().interrupt();
            } finally {
                msbClient.disconnect();
                try {
                    msbClient.close();
                } catch (Exception e) {
                    LOG.error("Exception during client close: ", e);
                }
            }
        }).start();
    }

    private FunctionCallsListener functionCallsListener = (serviceUuid, functionId, correlationId, functionParameters) -> LOG.debug("call: {}, {}, {}, {}", serviceUuid, functionId, correlationId, functionParameters);

    private ConfigurationListener configurationListener = configuration -> LOG.debug("New configuration: {}", configuration);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        ProgrammaticTestClient client = new ProgrammaticTestClient("df61a143-6dab-471a-88b4-8feddb4c9e70", "TestClient0", "57e721c9bcdf");
        try {
            client.startClient();
        } catch (ExecutionException | IOException e) {
            LOG.error("Exception: ", e);
        } catch (InterruptedException e){
            LOG.error("InterruptedException: ", e);
            Thread.currentThread().interrupt();
        }
    }
}
