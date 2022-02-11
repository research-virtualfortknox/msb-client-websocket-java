/*
 * Copyright (c) 2020.
 * See the file "LICENSE" for the full license governing this code.
 */

/*
 * See the file "LICENSE" for the full license governing this code.
 */
package de.fhg.ipa.vfk.msb.client.websocket.example;

import de.fhg.ipa.vfk.msb.client.api.Configuration;
import de.fhg.ipa.vfk.msb.client.api.Connection;
import de.fhg.ipa.vfk.msb.client.api.ConnectionFormat;
import de.fhg.ipa.vfk.msb.client.api.ConnectionState;
import de.fhg.ipa.vfk.msb.client.api.ConnectionType;
import de.fhg.ipa.vfk.msb.client.api.Event;
import de.fhg.ipa.vfk.msb.client.api.Gateway;
import de.fhg.ipa.vfk.msb.client.api.ParameterValue;
import de.fhg.ipa.vfk.msb.client.api.SmartObject;
import de.fhg.ipa.vfk.msb.client.websocket.MsbClient;
import de.fhg.ipa.vfk.msb.client.websocket.MsbClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The type Gateway test client.
 *
 * @author des
 */
public class GatewayTestClient {

    private static final Logger LOG = LoggerFactory.getLogger(GatewayTestClient.class);

    private static final String PULSE_EVENT = "PULSE";
    private static final String SERVICE_UUID = "3f54e77b-0697-4793-a378-57e721c9bcd5";

    private final String uuid;
    private final String name;
    private final String token;
    private final MsbClient msbClient;

    public GatewayTestClient(String uuid, String name, String token) {
        this.uuid = uuid;
        this.name = name;
        this.token = token;
        this.msbClient = new MsbClient.Builder().url("wss://localhost:8084").disableHostnameVerification().enabledDataFormatValidation().build();
    }

    public void startClient() throws ExecutionException, InterruptedException, IOException {
        Future<MsbClientHandler> future = msbClient.connect();
        MsbClientHandler handler = future.get();
        // add a function call listener to handle received function calls for gateway and managed services
        handler.addFunctionCallsListener((serviceUuid, functionId, correlationId, functionParameters) -> LOG.debug("call: {}, {}, {}, {}", serviceUuid, functionId, correlationId, functionParameters));
        // add a configuration listener to handle received configuration updates for gateway and managed services
        handler.addConfigurationListener(configuration -> LOG.debug("New configuration: {}", configuration));
        // create gateway self-description
        Gateway gateway = new Gateway(uuid, name, "Test gateway", token);
        // add a configuration parameter to the gateway
        Map<String, ParameterValue> parameters = new HashMap<>();
        parameters.put("test", new ParameterValue("testValue"));
        Configuration configuration = new Configuration();
        configuration.setParameters(parameters);
        gateway.setConfiguration(configuration);
        // add the self-description of a sub service to the gateway
        SmartObject smartObject1 = new SmartObject(SERVICE_UUID, "Test client", "Test Client", "55e741c9bcd8");
        smartObject1.setConnection(new Connection(ConnectionState.CONNECTED, ConnectionType.REST, ConnectionFormat.JSON, new Date()));
        gateway.addService(smartObject1);
        // register the gateway
        handler.register(gateway);
        // update existing service and add a new one to the gateway
        SmartObject smartObject2 = new SmartObject(SERVICE_UUID, "Test client", "Test Client", "55e741c9bcd10");
        smartObject2.setConnection(new Connection(ConnectionState.CONNECTED, ConnectionType.SOAP, ConnectionFormat.JSON, new Date()));
        smartObject2.addEvent(new Event(PULSE_EVENT, "Pulse Event", "A periodic trigger event"));
        SmartObject smartObject3 = new SmartObject("3f54e77b-0697-4793-a378-57e721c9bcd6", "Test client", "Test Client", "55e741c9bcd11");
        handler.addManagedService(smartObject2);
        handler.addManagedService(smartObject3);
        // update the gateway
        handler.register(gateway);
        // publish events of managed services
        final CountDownLatch count = new CountDownLatch(2);
        new Thread(() -> {
            try {
                while (count.getCount() > 0) {
                    Thread.sleep(1000);
                    msbClient.getClientHandler().publishForService(SERVICE_UUID, PULSE_EVENT);
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

    public static void main(String[] args) {
        GatewayTestClient client = new GatewayTestClient("3f54e77b-0697-4793-a378-57e721c9bczv", "TestGateway9", "57e721c9bcd9");
        try {
            client.startClient();
        } catch (ExecutionException | IOException e) {
            LOG.error("Exception: ", e);
        } catch (InterruptedException e) {
            LOG.error("InterruptedException: ", e);
            Thread.currentThread().interrupt();
        }
    }
}
