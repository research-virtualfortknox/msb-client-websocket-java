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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * The type Test environment configuration.
 *
 * @author des
 */
public class TestEnvironmentConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(TestEnvironmentConfiguration.class);

    private static final String OWNER_UUID = "7c328ad1-cea5-410e-8dd8-6c7ca5a2e4f5";
    private static final String SERVICE_PATH = "/service";
    private static final String FLOW_PATH = "/integrationFlow";
    private static final String TOKEN_PATH = "/token";
    private static final int RETRY_TIME = 1500;

    private String ownerUuid;
    private final HttpHeaders headers = new HttpHeaders();

    private final String urlSmartObjMgmtHttp;
    private final String urlIntegrationDesignMgmtHttp;
    private final String urlInterfaceWebSocket;

    /**
     * Instantiates a new Test environment configuration.
     */
    public TestEnvironmentConfiguration() {
        LOG.info("Looking for system property testEnvUrl defining the test environment to use.");
        String testEnvUrl = System.getProperty("TESTENV_CUSTOMIP");
        String testEnvOwnerUuid = System.getProperty("TESTENV_OWNER_UUID");
        String testEnvUsername = System.getProperty("TESTENV_USERNAME");
        String testEnvPassword = System.getProperty("TESTENV_PASSWORD");
        String testEnvWebsocketUrl = System.getProperty("MSB_WEBSOCKET_INTERFACE_URL");
        String testEnvSmartObjectUrl = System.getProperty("MSB_SMARTOBJECTMGMT_URL");
        String testEnvIntegrationDesignUrl = System.getProperty("MSB_INTEGRATIONDESIGNMGMT_URL");

        if (testEnvUrl != null && !"".equalsIgnoreCase(testEnvUrl)) {
            LOG.info("Found testEnvUrl: {}", testEnvUrl);
        } else {
            LOG.info("No testEnvUrl set! Using default (localhost)!");
            testEnvUrl = "localhost";
        }

        if (testEnvOwnerUuid != null && !"".equalsIgnoreCase(testEnvOwnerUuid)) {
            this.ownerUuid = testEnvOwnerUuid;
        } else {
            this.ownerUuid = OWNER_UUID;
        }
        LOG.info("Test Env Owner Uuid: {}", ownerUuid);

        if (testEnvUsername != null && !"".equalsIgnoreCase(testEnvUsername)) {
            headers.setBasicAuth(testEnvUsername, testEnvPassword, StandardCharsets.US_ASCII);
        }

        if (testEnvSmartObjectUrl != null && !"".equalsIgnoreCase(testEnvSmartObjectUrl)) {
            this.urlSmartObjMgmtHttp = testEnvSmartObjectUrl;
        } else {
            this.urlSmartObjMgmtHttp = "http://" + testEnvUrl + ":8081";
        }
        LOG.info("Test Env SmartObjMgmt Url: {}", urlSmartObjMgmtHttp);

        if (testEnvIntegrationDesignUrl != null && !"".equalsIgnoreCase(testEnvIntegrationDesignUrl)) {
            this.urlIntegrationDesignMgmtHttp = testEnvIntegrationDesignUrl;
        } else {
            this.urlIntegrationDesignMgmtHttp = "http://" + testEnvUrl + ":8082";
        }
        LOG.info("Test Env IntegrationDesignMgmt Url: {}", urlIntegrationDesignMgmtHttp);

        if (testEnvSmartObjectUrl != null && !"".equalsIgnoreCase(testEnvSmartObjectUrl)) {
            this.urlInterfaceWebSocket = testEnvWebsocketUrl;
        } else {
            this.urlInterfaceWebSocket = "wss://" + testEnvUrl + ":8084";
        }
        LOG.info("Test Env WebSocket Url: {}", urlInterfaceWebSocket);
    }

    private HttpHeaders getHeaders(){
        return headers;
    }

    /**
     * Gets owner uuid.
     *
     * @return the owner uuid
     */
    public String getOwnerUuid() {
        return ownerUuid;
    }

    /**
     * Gets url smart obj mgmt http.
     *
     * @return the url smart obj mgmt http
     */
    public String getUrlSmartObjMgmtHttp() {
        return urlSmartObjMgmtHttp;
    }

    /**
     * Gets url integration design mgmt http.
     *
     * @return the url integration design mgmt http
     */
    public String getUrlIntegrationDesignMgmtHttp() {
        return urlIntegrationDesignMgmtHttp;
    }

    /**
     * Gets url interface web socket.
     *
     * @return the url interface web socket
     */
    public String getUrlInterfaceWebSocket() {
        return urlInterfaceWebSocket;
    }

    /**
     * Load flow string.
     *
     * @param serviceUuid1 the service uuid 1
     * @param serviceName1 the service name 1
     * @param serviceUuid2 the service uuid 2
     * @param serviceName2 the service name 2
     * @return the string
     * @throws IOException the io exception
     */
    public String loadFlow(String serviceUuid1, String serviceName1, String serviceUuid2, String serviceName2) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Files.lines(Paths.get("./src/test/resources/integration_flow.json")).forEach(stringBuilder::append);
        } catch (NoSuchFileException e){
            Files.lines(Paths.get("./msb-client-websocket/src/test/resources/integration_flow.json")).forEach(stringBuilder::append);
        }
        return stringBuilder.toString()
                .replace("%%%%FLOWNAME%%%%", "TestFlow-" + UUID.randomUUID().toString().substring(24, 36))
                .replace("%%%%OWNERUUID%%%%", ownerUuid)
                .replace("%%%%SOUUID1%%%%", serviceUuid1)
                .replace("%%%%SONAME1%%%%", serviceName1)
                .replace("%%%%SOUUID2%%%%", serviceUuid2)
                .replace("%%%%SONAME2%%%%", serviceName2);
    }

    /**
     * Unload test environment.
     *
     * @param everythingOK the everything ok
     */
    public void unloadTestEnvironment(boolean everythingOK) {
        if (!everythingOK) {
            throw new IllegalStateException("Could not setup or connect to services in time.\nSomething else could have gone wrong during test Setup.");
        }
    }

    /**
     * Wait for smart object mgmt rest.
     *
     * @param startSignal the start signal
     * @param doneSignal  the done signal
     */
    public void waitForSmartObjectMgmtRest(final CountDownLatch startSignal, final CountDownLatch doneSignal) {
        RestTemplate restTemplate = new RestTemplate();
        final Object lock = new Object();
        new Thread(() -> {
            try {
                startSignal.await();
                HttpStatus statusCode = HttpStatus.NOT_FOUND;
                do {
                    synchronized (lock) {
                        lock.wait(RETRY_TIME);
                    }
                    LOG.info("Trying to connect to SmartObjectMgmt with URL: {}", urlSmartObjMgmtHttp);
                    try {
                        ResponseEntity<String> entity = restTemplate.exchange(getUrlSmartObjMgmtHttp() + "/actuator/info", HttpMethod.GET, null, String.class);
                        statusCode = entity.getStatusCode();
                    } catch (HttpClientErrorException e) {
                        LOG.error("Connecting to SmartObjectMgmt not yet possible", e);
                        try {
                            ResponseEntity<String> entity = restTemplate.exchange(getUrlSmartObjMgmtHttp() + "/info", HttpMethod.GET, null, String.class);
                            statusCode = entity.getStatusCode();
                        } catch (HttpClientErrorException e1) {
                            LOG.error("Connecting to SmartObjectMgmt not yet possible", e1);
                        }
                    }
                } while (!statusCode.is2xxSuccessful());
                LOG.info("Connected to SmartObjectMgmt.");
                doneSignal.countDown();
            } catch (InterruptedException e) {
                LOG.error("InterruptedException: ", e);
                // Restore interrupted state...
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * Wait for integration design mgmt rest.
     *
     * @param startSignal the start signal
     * @param doneSignal  the done signal
     */
    public void waitForIntegrationDesignMgmtRest(final CountDownLatch startSignal, final CountDownLatch doneSignal) {
        RestTemplate restTemplate = new RestTemplate();
        final Object lock = new Object();
        new Thread(() -> {
            try {
                startSignal.await();
                HttpStatus statusCode = HttpStatus.NOT_FOUND;
                do {
                    synchronized (lock) {
                        lock.wait(RETRY_TIME);
                    }
                    LOG.info("Trying to connect to IntegrationDesignMgmt with URL: {}", urlIntegrationDesignMgmtHttp);
                    try {
                        ResponseEntity<String> entity = restTemplate.exchange(getUrlIntegrationDesignMgmtHttp() + "/actuator/info", HttpMethod.GET, null, String.class);
                        statusCode = entity.getStatusCode();
                    } catch (HttpClientErrorException e) {
                        LOG.error("Connecting to IntegrationDesignMgmt not yet possible", e);
                        try {
                            ResponseEntity<String> entity = restTemplate.exchange(getUrlIntegrationDesignMgmtHttp() + "/info", HttpMethod.GET, null, String.class);
                            statusCode = entity.getStatusCode();
                        } catch (HttpClientErrorException e1) {
                            LOG.error("Connecting to IntegrationDesignMgmt not yet possible", e1);
                        }
                    }
                } while (!statusCode.is2xxSuccessful());
                LOG.info("Connected to IntegrationDesignMgmt.");
            } catch (InterruptedException e) {
                LOG.error("InterruptedException: ", e);
                // Restore interrupted state...
                Thread.currentThread().interrupt();
            }
            doneSignal.countDown();
        }).start();
    }

    /**
     * Wait and get service future.
     *
     * @param serviceUuid the service uuid
     * @return the future
     */
    public Future<ResponseEntity<ObjectNode>> waitAndGetService(String serviceUuid) {
        RestTemplate restTemplate = new RestTemplate();
        final Object lock = new Object();
        return CompletableFuture.supplyAsync(() -> {
            ResponseEntity<ObjectNode> entity = null;
            try {
                HttpStatus statusCode = HttpStatus.NOT_FOUND;
                do {
                    synchronized (lock) {
                        lock.wait(RETRY_TIME);
                    }
                    LOG.debug("getService - uuid: {}", serviceUuid);
                    Map<String, Object> uriVariables = new HashMap<>();
                    uriVariables.put("serviceUuid", serviceUuid);
                    try {
                        entity = restTemplate.exchange(getUrlSmartObjMgmtHttp() + SERVICE_PATH + "/{serviceUuid}", HttpMethod.GET, new HttpEntity<Void>(getHeaders()), ObjectNode.class, uriVariables);
                        statusCode = entity.getStatusCode();
                    } catch (HttpClientErrorException e) {
                        LOG.error("get service not yet possible", e);
                    }
                } while (!statusCode.is2xxSuccessful());
            } catch (InterruptedException e) {
                LOG.error("InterruptedException: ", e);
                // Restore interrupted state...
                Thread.currentThread().interrupt();
            }
            return entity;
        });
    }

    /**
     * Gets service.
     *
     * @param uuid the uuid
     * @return the service
     */
    public ResponseEntity<ObjectNode> getService(String uuid) {
        LOG.debug("getService - uuid: {}", uuid);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("serviceUuid", uuid);
        try {
            return restTemplate.exchange(getUrlSmartObjMgmtHttp() + SERVICE_PATH + "/{serviceUuid}", HttpMethod.GET, new HttpEntity<Void>(getHeaders()), ObjectNode.class, uriVariables);
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete service response entity.
     *
     * @param uuid the uuid
     * @return the response entity
     */
    public ResponseEntity<Void> deleteService(String uuid) {
        LOG.debug("deleteService - uuid: {}", uuid);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("serviceUuid", uuid);
        return restTemplate.exchange(getUrlSmartObjMgmtHttp() + SERVICE_PATH + "/{serviceUuid}", HttpMethod.DELETE, new HttpEntity<Void>(getHeaders()), Void.class, uriVariables);
    }

    /**
     * Gets new token.
     *
     * @param ownerUuid the owner uuid
     * @return the new token
     */
    public ResponseEntity<ObjectNode> getNewToken(String ownerUuid) {
        LOG.debug("getNewToken - ownerUuid: {}", ownerUuid);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("ownerUuid", ownerUuid);
        return restTemplate.exchange(getUrlSmartObjMgmtHttp() + TOKEN_PATH + "/{ownerUuid}", HttpMethod.POST, new HttpEntity<Void>(getHeaders()), ObjectNode.class, uriVariables);
    }

    /**
     * Create and deploy flow response entity.
     *
     * @param integrationFlow the integration flow
     * @return the response entity
     * @throws IOException the io exception
     */
    public ResponseEntity<ObjectNode> createAndDeployFlow(String integrationFlow) throws IOException {
        LOG.debug("createAndDeploy - integrationFlow: {}", integrationFlow);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(getUrlIntegrationDesignMgmtHttp() + FLOW_PATH + "/create/deploy", HttpMethod.POST, new HttpEntity<>(new ObjectMapper().readTree(integrationFlow), getHeaders()), ObjectNode.class);
    }

    /**
     * Gets flow.
     *
     * @param flowId the flow id
     * @return the flow
     */
    public ResponseEntity<ObjectNode> getFlow(long flowId) {
        LOG.debug("get - flowId: {}", flowId);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("flowId", flowId);
        try {
            return restTemplate.exchange(getUrlIntegrationDesignMgmtHttp() + FLOW_PATH + "/{flowId}", HttpMethod.GET, new HttpEntity<Void>(getHeaders()), ObjectNode.class, uriVariables);
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete flow response entity.
     *
     * @param flowId the flow id
     * @return the response entity
     */
    public ResponseEntity<Void> deleteFlow(long flowId) {
        LOG.debug("delete - flowId: {}", flowId);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("flowId", flowId);
        return restTemplate.exchange(getUrlIntegrationDesignMgmtHttp() + FLOW_PATH + "/{flowId}", HttpMethod.DELETE, new HttpEntity<Void>(getHeaders()), Void.class, uriVariables);
    }

}
