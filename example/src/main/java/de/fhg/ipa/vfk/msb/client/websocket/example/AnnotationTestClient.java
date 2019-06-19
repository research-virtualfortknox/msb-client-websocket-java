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

import de.fhg.ipa.vfk.msb.client.annotation.ConfigurationParam;
import de.fhg.ipa.vfk.msb.client.annotation.EventDeclaration;
import de.fhg.ipa.vfk.msb.client.annotation.Events;
import de.fhg.ipa.vfk.msb.client.annotation.SelfDescription;
import de.fhg.ipa.vfk.msb.client.listener.ConfigurationListener;
import de.fhg.ipa.vfk.msb.client.websocket.MsbClient;
import de.fhg.ipa.vfk.msb.client.websocket.MsbClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The type Annotation test client.
 *
 * @author des
 */
@Events({
        @EventDeclaration(description = "periodical transmitted event", name = "Event pulse", eventId = "PULSE"),
        @EventDeclaration(dataType = float.class, description = "Current temperature", name = "Temperature", eventId = "TEMPERATURE"),
})
@SelfDescription(uuid = "df61a143-6dab-471a-88b4-8feddb4c9e71", name = "TestClient1", description = "Test client", token = "57e721c9bbdf", type = SelfDescription.Type.SMART_OBJECT)
public class AnnotationTestClient {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationTestClient.class);

    /**
     * The Drilling speed.
     */
    @ConfigurationParam(name = "drilling_speed")
    public int drillingSpeed = 100;

    /**
     * Start client.
     *
     * @throws ExecutionException   the execution exception
     * @throws InterruptedException the interrupted exception
     * @throws IOException          the io exception
     */
    public void startClient() throws ExecutionException, InterruptedException, IOException {
        MsbClient msbClient = new MsbClient.Builder().url("ws://localhost:8085").enabledDataFormatValidation().build();
        Future<MsbClientHandler> future = msbClient.connect();
        MsbClientHandler handler = future.get();

        handler.addConfigurationListener(configurationListener);
        handler.register("de.fhg.ipa.vfk.msb.client.websocket.example");

        final CountDownLatch count = new CountDownLatch(2);
        new Thread(() -> {
            try {
                while (count.getCount() > 0) {
                    Thread.sleep(1000);
                    msbClient.getClientHandler().publish("PULSE");
                    msbClient.getClientHandler().publish("TEMPERATURE", Float.valueOf(count.getCount()));
                    count.countDown();
                }
            } catch (Exception e) {
                LOG.error("Exception: ", e);
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

    private ConfigurationListener configurationListener = configuration -> {
        LOG.debug("New configuration: {}", configuration);
        if (configuration.getConfigurationParameters().containsKey("drilling_speed")) {
            drillingSpeed = Integer.valueOf(String.valueOf(configuration.getConfigurationParameters().get("drilling_speed")));
        }
    };

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        AnnotationTestClient client = new AnnotationTestClient();
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
