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

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhg.ipa.vfk.msb.client.api.Application;
import de.fhg.ipa.vfk.msb.client.api.Configuration;
import de.fhg.ipa.vfk.msb.client.api.Connection;
import de.fhg.ipa.vfk.msb.client.api.ConnectionFormat;
import de.fhg.ipa.vfk.msb.client.api.ConnectionState;
import de.fhg.ipa.vfk.msb.client.api.ConnectionType;
import de.fhg.ipa.vfk.msb.client.api.Event;
import de.fhg.ipa.vfk.msb.client.api.Function;
import de.fhg.ipa.vfk.msb.client.api.Gateway;
import de.fhg.ipa.vfk.msb.client.api.ParameterValue;
import de.fhg.ipa.vfk.msb.client.api.PrimitiveFormat;
import de.fhg.ipa.vfk.msb.client.api.PrimitiveType;
import de.fhg.ipa.vfk.msb.client.api.Service;
import de.fhg.ipa.vfk.msb.client.api.SmartObject;
import de.fhg.ipa.vfk.msb.client.api.messages.ConfigurationMessage;
import de.fhg.ipa.vfk.msb.client.api.messages.EventMessage;
import de.fhg.ipa.vfk.msb.client.api.messages.EventPriority;
import de.fhg.ipa.vfk.msb.client.api.messages.FunctionCallMessage;
import de.fhg.ipa.vfk.msb.client.listener.ConfigurationListener;
import de.fhg.ipa.vfk.msb.client.listener.ConnectionAdapter;
import de.fhg.ipa.vfk.msb.client.listener.FunctionCallsListener;
import de.fhg.ipa.vfk.msb.client.listener.PublishingError;
import de.fhg.ipa.vfk.msb.client.listener.RegistrationError;
import de.fhg.ipa.vfk.msb.client.util.MsbDateFormat;
import de.fhg.ipa.vfk.msb.client.util.WrongDataFormatException;
import de.fhg.ipa.vfk.msb.client.websocket.annotation.AnnotationTestClient;
import de.fhg.ipa.vfk.msb.client.websocket.annotation.TestClientFunctionHandler;
import io.swagger.models.HttpMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.concurrent.SettableListenableFuture;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.sockjs.client.SockJsClient;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * The type Msb client mockito test.
 *
 * @author des
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class MsbClientWebSocketHandlerTest {

    @Mock
    private WebSocketSession mockSession;
    @Mock
    private SockJsClient sockJsClient;
    @Captor
    private ArgumentCaptor<WebSocketMessage<?>> captor;
    @Spy
    private MsbClientWebSocketHandler msbClientWebSocketHandler = new MsbClientWebSocketHandler("http://localhost");

    @BeforeEach
    void setUp() throws Exception {
        //MockitoAnnotations.openMocks(this);
    }

    /**
     * Test send callback.
     *
     * @throws Exception the exception
     */
    @Test
    void testRegisterUnconnected() throws Exception{
        Assertions.assertThrows(IllegalStateException.class, ()->msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation"));
    }

    @Test
    void testRegisterWithAnnotationScanning() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        Mockito.verify(mockSession).sendMessage(captor.capture());
        String message = captor.getValue().getPayload().toString();
        Assertions.assertTrue(message.startsWith("R {"));
        Assertions.assertTrue(message.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Application application = new ObjectMapper().readValue(message.substring(2), Application.class);
        Assertions.assertEquals("AnnotationTestClient",application.getName());
        Assertions.assertEquals("Annotation Test client",application.getDescription());
        Assertions.assertEquals("token",application.getToken());
        Assertions.assertEquals(5, application.getEvents().size());
        Assertions.assertEquals(8, application.getFunctions().size());
        Assertions.assertEquals(3,application.getConfiguration().getParameters().size());
    }

    @Test
    void testRegisterWithAnnotationScanningAndFunctionHandlerInstances() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation", new Object[]{new TestClientFunctionHandler()});
        Mockito.verify(mockSession).sendMessage(captor.capture());
        String message = captor.getValue().getPayload().toString();
        Assertions.assertTrue(message.startsWith("R {"));
        Assertions.assertTrue(message.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Application application = new ObjectMapper().readValue(message.substring(2), Application.class);
        Assertions.assertEquals("AnnotationTestClient",application.getName());
        Assertions.assertEquals("Annotation Test client",application.getDescription());
        Assertions.assertEquals("token",application.getToken());
        Assertions.assertEquals(5, application.getEvents().size());
        Assertions.assertEquals(8, application.getFunctions().size());
        Assertions.assertEquals(3,application.getConfiguration().getParameters().size());
    }

    @Test
    void testRegisterWithAnnotationScanningAndFunctionHandlerClasses() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation", new Object[]{TestClientFunctionHandler.class});
        Mockito.verify(mockSession).sendMessage(captor.capture());
        String message = captor.getValue().getPayload().toString();
        Assertions.assertTrue(message.startsWith("R {"));
        Assertions.assertTrue(message.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Application application = new ObjectMapper().readValue(message.substring(2), Application.class);
        Assertions.assertEquals("AnnotationTestClient",application.getName());
        Assertions.assertEquals("Annotation Test client",application.getDescription());
        Assertions.assertEquals("token",application.getToken());
        Assertions.assertEquals(5, application.getEvents().size());
        Assertions.assertEquals(8, application.getFunctions().size());
        Assertions.assertEquals(3,application.getConfiguration().getParameters().size());
    }

    @Test
    void testUpdateRegister() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.register(new Application("uuid","name","description","token"));
        Mockito.verify(mockSession, Mockito.times(2)).sendMessage(captor.capture());

        String firstMessage = captor.getAllValues().get(0).getPayload().toString();
        Assertions.assertTrue(firstMessage.startsWith("R {"));
        Assertions.assertTrue(firstMessage.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Application application = new ObjectMapper().readValue(firstMessage.substring(2), Application.class);
        Assertions.assertEquals("AnnotationTestClient",application.getName());
        Assertions.assertEquals("Annotation Test client",application.getDescription());
        Assertions.assertEquals("token",application.getToken());
        Assertions.assertEquals(5, application.getEvents().size());
        Assertions.assertEquals(8, application.getFunctions().size());
        Assertions.assertEquals(3,application.getConfiguration().getParameters().size());

        String secondMessage = captor.getValue().getPayload().toString();
        Assertions.assertTrue(secondMessage.startsWith("R {"));
        Assertions.assertTrue(secondMessage.contains("\"uuid\":\"uuid\","));
        application = new ObjectMapper().readValue(secondMessage.substring(2), Application.class);
        Assertions.assertEquals("name",application.getName());
        Assertions.assertEquals("description",application.getDescription());
        Assertions.assertEquals("token",application.getToken());
        Assertions.assertEquals(0, application.getEvents().size());
        Assertions.assertEquals(0, application.getFunctions().size());
        Assertions.assertNull(application.getConfiguration());
    }

    @Test
    void testUpdateRegisterWithAnnotationScanning() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        Map<String, ParameterValue> parameters = new HashMap<>();
        parameters.put("testParam",new ParameterValue("value",PrimitiveType.STRING));
        Application updateApplication = new Application("uuid","name","description","token", new Configuration(parameters));

        msbClientWebSocketHandler.register(updateApplication,"de.fhg.ipa.vfk.msb.client.websocket.annotation");
        Mockito.verify(mockSession, Mockito.times(2)).sendMessage(captor.capture());

        String firstMessage = captor.getAllValues().get(0).getPayload().toString();
        Assertions.assertTrue(firstMessage.startsWith("R {"));
        Assertions.assertTrue(firstMessage.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Application application = new ObjectMapper().readValue(firstMessage.substring(2), Application.class);
        Assertions.assertEquals("AnnotationTestClient",application.getName());
        Assertions.assertEquals("Annotation Test client",application.getDescription());
        Assertions.assertEquals("token",application.getToken());
        Assertions.assertEquals(5, application.getEvents().size());
        Assertions.assertEquals(8, application.getFunctions().size());
        Assertions.assertEquals(3,application.getConfiguration().getParameters().size());

        String secondMessage = captor.getValue().getPayload().toString();
        Assertions.assertTrue(secondMessage.startsWith("R {"));
        Assertions.assertTrue(secondMessage.contains("\"uuid\":\"uuid\","));
        application = new ObjectMapper().readValue(secondMessage.substring(2), Application.class);
        Assertions.assertEquals("name",application.getName());
        Assertions.assertEquals("description",application.getDescription());
        Assertions.assertEquals("token",application.getToken());
        Assertions.assertEquals(5, application.getEvents().size());
        Assertions.assertEquals(8, application.getFunctions().size());
        Assertions.assertEquals(4,application.getConfiguration().getParameters().size());
    }

    @Test
    void testUpdateRegisterWithAnnotationScanningAndFunctionHandlerClasses() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        Application updateApplication = new Application("uuid","name","description","token");
        updateApplication.addEvent(new Event("PULSE","Event pulse",""));
        msbClientWebSocketHandler.register(updateApplication,new Object[]{TestClientFunctionHandler.class});
        Mockito.verify(mockSession, Mockito.times(2)).sendMessage(captor.capture());

        String firstMessage = captor.getAllValues().get(0).getPayload().toString();
        Assertions.assertTrue(firstMessage.startsWith("R {"));
        Assertions.assertTrue(firstMessage.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Application application = new ObjectMapper().readValue(firstMessage.substring(2), Application.class);
        Assertions.assertEquals("AnnotationTestClient",application.getName());
        Assertions.assertEquals("Annotation Test client",application.getDescription());
        Assertions.assertEquals("token",application.getToken());
        Assertions.assertEquals(5, application.getEvents().size());
        Assertions.assertEquals(8, application.getFunctions().size());
        Assertions.assertEquals(3,application.getConfiguration().getParameters().size());

        String secondMessage = captor.getValue().getPayload().toString();
        Assertions.assertTrue(secondMessage.startsWith("R {"));
        Assertions.assertTrue(secondMessage.contains("\"uuid\":\"uuid\","));
        application = new ObjectMapper().readValue(secondMessage.substring(2), Application.class);
        Assertions.assertEquals("name",application.getName());
        Assertions.assertEquals("description",application.getDescription());
        Assertions.assertEquals("token",application.getToken());
        Assertions.assertEquals(4, application.getEvents().size());
        Assertions.assertEquals(8, application.getFunctions().size());
        Assertions.assertNull(application.getConfiguration());
    }

    @Test
    void testUpdateRegisterWithAnnotationScanningAndFunctionHandlerAndEventClasses() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        Map<String, ParameterValue> parameters = new HashMap<>();
        parameters.put("testParam",new ParameterValue("value",PrimitiveType.STRING));
        Application updateApplication = new Application("uuid","name","description","token", new Configuration(parameters));

        msbClientWebSocketHandler.register(updateApplication,new Object[]{TestClientFunctionHandler.class}, new Class<?>[]{AnnotationTestClient.class});
        Mockito.verify(mockSession, Mockito.times(2)).sendMessage(captor.capture());

        String firstMessage = captor.getAllValues().get(0).getPayload().toString();
        Assertions.assertTrue(firstMessage.startsWith("R {"));
        Assertions.assertTrue(firstMessage.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Application application = new ObjectMapper().readValue(firstMessage.substring(2), Application.class);
        Assertions.assertEquals("AnnotationTestClient",application.getName());
        Assertions.assertEquals("Annotation Test client",application.getDescription());
        Assertions.assertEquals("token",application.getToken());
        Assertions.assertEquals(5, application.getEvents().size());
        Assertions.assertEquals(8, application.getFunctions().size());
        Assertions.assertEquals(3,application.getConfiguration().getParameters().size());

        String secondMessage = captor.getValue().getPayload().toString();
        Assertions.assertTrue(secondMessage.startsWith("R {"));
        Assertions.assertTrue(secondMessage.contains("\"uuid\":\"uuid\","));
        application = new ObjectMapper().readValue(secondMessage.substring(2), Application.class);
        Assertions.assertEquals("name",application.getName());
        Assertions.assertEquals("description",application.getDescription());
        Assertions.assertEquals("token",application.getToken());
        Assertions.assertEquals(5, application.getEvents().size());
        Assertions.assertEquals(8, application.getFunctions().size());
        Assertions.assertEquals(1,application.getConfiguration().getParameters().size());
    }

    @Test
    void testRegisterWithAddMethods() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        SmartObject smartObject = new SmartObject("df61a143-6dab-471a-88b4-8feddb4c9e23","name","description","token");
        msbClientWebSocketHandler.addEvent(smartObject, "TEMPERATUR", "Temperatur", "Current temperatur", float.class, EventPriority.MEDIUM);
        msbClientWebSocketHandler.addEvent(smartObject, "STOP", "Stop", "Timestamp of stop", Date.class, EventPriority.MEDIUM);
        Method stringMethod = TestClientFunctionHandler.class.getMethod("printString", new Class[]{String.class});
        msbClientWebSocketHandler.addFunction(smartObject, "printString", "printString", "print a string", new String[]{"STOP"}, new TestClientFunctionHandler(), stringMethod);
        msbClientWebSocketHandler.addConfigParam("test1", "testValue", PrimitiveType.STRING);
        msbClientWebSocketHandler.addConfigParam("test2", 123, PrimitiveFormat.INT32);
        msbClientWebSocketHandler.register(smartObject);
        Mockito.verify(mockSession).sendMessage(captor.capture());
        String message = captor.getValue().getPayload().toString();
        Assertions.assertTrue(message.startsWith("R {"));
        Assertions.assertTrue(message.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e23\","));
        smartObject = new ObjectMapper().readValue(message.substring(2), SmartObject.class);
        Assertions.assertEquals("name", smartObject.getName());
        Assertions.assertEquals("description", smartObject.getDescription());
        Assertions.assertEquals("token", smartObject.getToken());
        Assertions.assertEquals(2, smartObject.getEvents().size());
        Assertions.assertEquals(1, smartObject.getFunctions().size());
        Assertions.assertEquals(2, smartObject.getConfiguration().getParameters().size());
    }

    @Test
    void testRegisterAndUpdateGateway() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        Gateway gateway = new Gateway("df61a143-6dab-471a-88b4-8feddb4c9e","name","description","token");
        msbClientWebSocketHandler.addEvent(gateway, "TEMPERATUR", "Temperatur", "Current temperatur", float.class, EventPriority.MEDIUM);
        msbClientWebSocketHandler.addEvent(gateway, "STOP", "Stop", "Timestamp of stop", Date.class, EventPriority.MEDIUM);
        Method stringMethod = TestClientFunctionHandler.class.getMethod("printString", new Class[]{String.class});
        msbClientWebSocketHandler.addFunction(gateway, "printString", "printString", "print a string", new String[]{"STOP"}, new TestClientFunctionHandler(), stringMethod);
        msbClientWebSocketHandler.addConfigParam("test1", "testValue", PrimitiveType.STRING);
        msbClientWebSocketHandler.addConfigParam("test2", 123, PrimitiveFormat.INT32);
        msbClientWebSocketHandler.register(gateway);
        Application managedApplication = new Application("uuid1","name1","description1","token1");
        managedApplication.setConnection(new Connection(ConnectionState.CONNECTED, ConnectionType.MQTT, ConnectionFormat.JSON, new Date()));
        managedApplication.addEvent(new Event("PULSE","Pulse","Pulse event"));
        managedApplication.addFunction(new Function("helloWorld","Hello world","Print Hello world"));
        msbClientWebSocketHandler.addManagedService(managedApplication);
        msbClientWebSocketHandler.register(gateway);
        Mockito.verify(mockSession,Mockito.times(2)).sendMessage(captor.capture());

        String firstMessage = captor.getAllValues().get(0).getPayload().toString();
        Assertions.assertTrue(firstMessage.startsWith("R {"));
        Assertions.assertTrue(firstMessage.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e\","));
        Gateway firstMessageGateway = new ObjectMapper().readValue(firstMessage.substring(2), Gateway.class);
        Assertions.assertEquals("name", firstMessageGateway.getName());
        Assertions.assertEquals("description", firstMessageGateway.getDescription());
        Assertions.assertEquals("token", firstMessageGateway.getToken());
        Assertions.assertEquals(2, firstMessageGateway.getEvents().size());
        Assertions.assertEquals(1, firstMessageGateway.getFunctions().size());
        Assertions.assertEquals(2, firstMessageGateway.getConfiguration().getParameters().size());
        Assertions.assertEquals(0, firstMessageGateway.getServices().size());

        String secondMessage = captor.getValue().getPayload().toString();
        Assertions.assertTrue(secondMessage.startsWith("R {"));
        Assertions.assertTrue(secondMessage.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e\","));
        Gateway secondMessageGateway = new ObjectMapper().readValue(secondMessage.substring(2), Gateway.class);
        Assertions.assertEquals("name", secondMessageGateway.getName());
        Assertions.assertEquals("description", secondMessageGateway.getDescription());
        Assertions.assertEquals("token", secondMessageGateway.getToken());
        Assertions.assertEquals(2, secondMessageGateway.getEvents().size());
        Assertions.assertEquals(1, secondMessageGateway.getFunctions().size());
        Assertions.assertEquals(2, secondMessageGateway.getConfiguration().getParameters().size());
        Assertions.assertEquals(1, secondMessageGateway.getServices().size());
        Service secondMessageSubService = secondMessageGateway.getServices().iterator().next();
        Assertions.assertEquals("uuid1", secondMessageSubService.getUuid());
        Assertions.assertEquals("name1", secondMessageSubService.getName());
        Assertions.assertEquals("description1", secondMessageSubService.getDescription());
        Assertions.assertEquals("token1", secondMessageSubService.getToken());
        Assertions.assertEquals(1, secondMessageSubService.getEvents().size());
        Assertions.assertEquals(1, secondMessageSubService.getFunctions().size());
        Assertions.assertNull(secondMessageSubService.getConfiguration());
        Assertions.assertNotNull(secondMessageSubService.getConnection());
        Assertions.assertEquals(managedApplication.getConnection().getConnectionState(),secondMessageSubService.getConnection().getConnectionState());
        Assertions.assertEquals(managedApplication.getConnection().getConnectionType(),secondMessageSubService.getConnection().getConnectionType());
        Assertions.assertEquals(managedApplication.getConnection().getConnectionFormat(),secondMessageSubService.getConnection().getConnectionFormat());
        Assertions.assertEquals(managedApplication.getConnection().getLastContact(),secondMessageSubService.getConnection().getLastContact());

    }

    /**
     * Test publish before register.
     *
     * @throws Exception the exception
     */
    @Test
    void testPublishBeforeRegister() throws Exception {
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        Assertions.assertThrows(IOException.class, ()->msbClientWebSocketHandler.publish("PULSE"));
    }

    @Test
    void testPublishEmptyEvent() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        Assertions.assertTrue(msbClientWebSocketHandler.isRegistered());
        msbClientWebSocketHandler.publish("PULSE");
        Mockito.verify(mockSession,Mockito.times(2)).sendMessage(captor.capture());
        Assertions.assertTrue(captor.getAllValues().get(0).getPayload().toString().startsWith("R {"));
        Assertions.assertTrue(captor.getAllValues().get(0).getPayload().toString().contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assertions.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"PULSE\""));
    }

    @Test
    void testPublishObject() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        msbClientWebSocketHandler.publish("TEMPERATURE",23.4F);
        Mockito.verify(mockSession,Mockito.times(2)).sendMessage(captor.capture());
        Assertions.assertTrue(captor.getAllValues().get(0).getPayload().toString().startsWith("R {"));
        Assertions.assertTrue(captor.getAllValues().get(0).getPayload().toString().contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assertions.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        EventMessage eventMessage = new ObjectMapper().readValue(captor.getValue().getPayload().toString().substring(2), EventMessage.class);
        Assertions.assertNotNull(eventMessage);
        Assertions.assertEquals("TEMPERATURE",eventMessage.getEventId());
        Assertions.assertEquals(23.4,eventMessage.getDataObject());
        Assertions.assertEquals("df61a143-6dab-471a-88b4-8feddb4c9e45",eventMessage.getUuid());
        Assertions.assertEquals(EventPriority.DEFAULT,eventMessage.getPriority());
    }

    @Test
    void testPublishWithWrongDataFormat() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.setDataFormatValidation(true);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        Assertions.assertThrows(WrongDataFormatException.class,()->msbClientWebSocketHandler.publish("TEMPERATURE",new Date()));
        Mockito.verify(mockSession,Mockito.times(1)).sendMessage(captor.capture());
        Assertions.assertTrue(captor.getValue().getPayload().toString().startsWith("R {"));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
    }

    @Test
    void testPublishObjectWithPriority() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        msbClientWebSocketHandler.publish("TEMPERATURE",23.4F, EventPriority.MEDIUM);
        Mockito.verify(mockSession,Mockito.times(2)).sendMessage(captor.capture());
        Assertions.assertTrue(captor.getAllValues().get(0).getPayload().toString().startsWith("R {"));
        Assertions.assertTrue(captor.getAllValues().get(0).getPayload().toString().contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assertions.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"TEMPERATURE\""));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"dataObject\":23.4"));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"priority\":1"));
    }

    @Test
    void testPublishObjectWithDate() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        Date date = new Date();
        msbClientWebSocketHandler.publish("TEMPERATURE",23.4F, EventPriority.MEDIUM, false, date);
        Mockito.verify(mockSession,Mockito.times(2)).sendMessage(captor.capture());
        Assertions.assertTrue(captor.getAllValues().get(0).getPayload().toString().startsWith("R {"));
        Assertions.assertTrue(captor.getAllValues().get(0).getPayload().toString().contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assertions.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"TEMPERATURE\""));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"dataObject\":23.4"));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"priority\":1"));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"postDate\":\""+ new MsbDateFormat().format(date)+"\""));
    }

    @Test
    void testPublishObjectWithCorrelationId() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        Date date = new Date();
        String correlationId = UUID.randomUUID().toString();
        msbClientWebSocketHandler.publish("TEMPERATURE",23.4F, EventPriority.MEDIUM, false, date, correlationId);
        Mockito.verify(mockSession,Mockito.times(2)).sendMessage(captor.capture());
        Assertions.assertTrue(captor.getAllValues().get(0).getPayload().toString().startsWith("R {"));
        Assertions.assertTrue(captor.getAllValues().get(0).getPayload().toString().contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assertions.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"TEMPERATURE\""));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"dataObject\":23.4"));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"priority\":1"));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"postDate\":\""+ new MsbDateFormat().format(date)+"\""));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"correlationId\":\""+ correlationId+"\""));
    }

    @Test
    void testPublishObjectWithCached() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.publish("TEMPERATURE",23.4F, EventPriority.MEDIUM, true);
        Mockito.verify(mockSession,Mockito.times(1)).sendMessage(captor.capture());
        Assertions.assertTrue(captor.getValue().getPayload().toString().startsWith("R {"));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        Mockito.verify(mockSession,Mockito.times(2)).sendMessage(captor.capture());
        Assertions.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"TEMPERATURE\""));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"dataObject\":23.4"));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"priority\":1"));
    }

    @Test
    void testPublishForServiceWithAddedManagedService() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        Gateway gateway = new Gateway("df61a143-6dab-471a-88b4-8feddb4c9e","name","description","token");
        msbClientWebSocketHandler.addEvent(gateway, "TEMPERATURE", "Temperature", "Current temperature", float.class, EventPriority.MEDIUM);
        msbClientWebSocketHandler.register(gateway);
        Application managedApplication = new Application("application_uuid","name1","description1","token1");
        managedApplication.addEvent(new Event("PULSE","Event pulse",""));
        msbClientWebSocketHandler.addManagedService(managedApplication);
        msbClientWebSocketHandler.register(gateway);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        msbClientWebSocketHandler.publish("TEMPERATURE",23.4F, EventPriority.MEDIUM, true);
        msbClientWebSocketHandler.publishForService("application_uuid","PULSE");
        Mockito.verify(mockSession,Mockito.times(4)).sendMessage(captor.capture());
        Assertions.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"application_uuid\","));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"PULSE\""));
    }

    @Test
    void testPublishForServiceInvalidEventWithAddedManagedService() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        Gateway gateway = new Gateway("df61a143-6dab-471a-88b4-8feddb4c9e","name","description","token");
        msbClientWebSocketHandler.addEvent(gateway, "TEMPERATURE", "Temperature", "Current temperature", float.class, EventPriority.MEDIUM);
        msbClientWebSocketHandler.register(gateway);
        msbClientWebSocketHandler.addManagedService(new Application("application_uuid","name1","description1","token1"));
        msbClientWebSocketHandler.register(gateway);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        msbClientWebSocketHandler.publish("TEMPERATURE",23.4F, EventPriority.MEDIUM, true);
        msbClientWebSocketHandler.publishForService("application_uuid","TEMPERATURE",4F);
        Mockito.verify(mockSession,Mockito.times(3)).sendMessage(captor.capture());
        Assertions.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e\","));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"TEMPERATURE\""));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"dataObject\":23.4"));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"priority\":1"));
    }

    @Test
    void testPublishForService() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        Application managedApplication = new Application("application_uuid","name1","description1","token1");
        managedApplication.addEvent(new Event("PULSE","Event pulse",""));
        Gateway gateway = new Gateway("df61a143-6dab-471a-88b4-8feddb4c9e","name","description","token");
        gateway.addService(managedApplication);
        msbClientWebSocketHandler.addEvent(gateway, "TEMPERATURE", "Temperature", "Current temperature", float.class, EventPriority.MEDIUM);
        msbClientWebSocketHandler.register(gateway);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        msbClientWebSocketHandler.publish("TEMPERATURE",23.4F, EventPriority.MEDIUM, true);
        msbClientWebSocketHandler.publishForService("application_uuid","PULSE");
        Mockito.verify(mockSession,Mockito.times(3)).sendMessage(captor.capture());
        Assertions.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"application_uuid\","));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"PULSE\""));
    }

    @Test
    void testPublishForServiceInvalidEvent() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        Gateway gateway = new Gateway("df61a143-6dab-471a-88b4-8feddb4c9e","name","description","token");
        gateway.addService(new Application("application_uuid","name1","description1","token1"));
        msbClientWebSocketHandler.addEvent(gateway, "TEMPERATURE", "Temperature", "Current temperature", float.class, EventPriority.MEDIUM);
        msbClientWebSocketHandler.register(gateway);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        msbClientWebSocketHandler.publish("TEMPERATURE",23.4F, EventPriority.MEDIUM, true);
        msbClientWebSocketHandler.publishForService("application_uuid","TEMPERATURE",4F);
        Mockito.verify(mockSession,Mockito.times(2)).sendMessage(captor.capture());
        Assertions.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e\","));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"TEMPERATURE\""));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"dataObject\":23.4"));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"priority\":1"));
    }

    @Test
    void testConnectionListener() throws Exception {
        Mockito.when(sockJsClient.doHandshake(Mockito.same(msbClientWebSocketHandler),Mockito.anyString(), Mockito.any())).thenReturn(new AsyncResult<>(mockSession));
        Mockito.when(msbClientWebSocketHandler.createClient(Mockito.anyString(),Mockito.any(),Mockito.any(),Mockito.anyInt())).thenReturn(sockJsClient);

        ConnectionAdapter connectionListener = Mockito.mock(ConnectionAdapter.class);
        msbClientWebSocketHandler.addConnectionListener(connectionListener);
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_CONNECTED"));
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.publish("TEMPERATURE",23.4F);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_PUBLISHED"));

        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("NIO_EVENT_FORWARDING_ERROR"));
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("NIO_UNAUTHORIZED_CONNECTION"));
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("NIO_UNEXPECTED_EVENT_FORWARDING_ERROR"));
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("NIO_REGISTRATION_ERROR"));
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("NIO_UNEXPECTED_REGISTRATION_ERROR"));
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("NIO_ALREADY_CONNECTED"));

        msbClientWebSocketHandler.afterConnectionClosed(mockSession, CloseStatus.NORMAL);

        Mockito.verify(connectionListener, Mockito.times(1)).afterConnectionEstablished();
        Mockito.verify(connectionListener, Mockito.times(1)).afterEventCached();
        Mockito.verify(connectionListener, Mockito.times(1)).afterServiceRegistered();
        Mockito.verify(connectionListener, Mockito.times(1)).afterEventPublished();
        Mockito.verify(connectionListener, Mockito.times(3)).errorAtEventPublishing(Mockito.any(PublishingError.class));
        Mockito.verify(connectionListener, Mockito.times(1)).errorAtServiceRegistration(RegistrationError.NIO_ALREADY_CONNECTED);
        Mockito.verify(connectionListener, Mockito.times(1)).errorAtServiceRegistration(RegistrationError.NIO_REGISTRATION_ERROR);
        Mockito.verify(connectionListener, Mockito.times(1)).errorAtServiceRegistration(RegistrationError.NIO_UNEXPECTED_REGISTRATION_ERROR);
        Mockito.verify(connectionListener, Mockito.times(1)).afterConnectionClosed();
        Mockito.verify(connectionListener, Mockito.times(1)).beforeConnectionTryToReconnecting();

        msbClientWebSocketHandler.removeConnectionListener(connectionListener);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
    }

    @Test
    void testConfigurationListener() throws Exception {
        ConfigurationListener configurationListener = Mockito.mock(ConfigurationListener.class);
        msbClientWebSocketHandler.addConfigurationListener(configurationListener);
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");

        Map<String, Object> configurationParameters = new HashMap<>();
        configurationParameters.put("test","value");
        ConfigurationMessage data = new ConfigurationMessage("df61a143-6dab-471a-88b4-8feddb4c9e45",configurationParameters);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("K "+new ObjectMapper().writeValueAsString(data)));
        Mockito.verify(configurationListener, Mockito.times(1)).configurationRemoteChanged(Mockito.any(ConfigurationMessage.class));
        msbClientWebSocketHandler.removeConfigurationListener(configurationListener);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("K "+new ObjectMapper().writeValueAsString(data)));

    }

    @Test
    void testFunctionCallsListener() throws Exception {
        FunctionCallsListener configurationListener = Mockito.mock(FunctionCallsListener.class);
        msbClientWebSocketHandler.addFunctionCallsListener(configurationListener);
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");

        FunctionCallMessage data = new FunctionCallMessage();
        data.setUuid("df61a143-6dab-471a-88b4-8feddb4c9e45");
        data.setFunctionId("/functionhandler/hello_world");
        data.setCorrelationId("correlationId");
        data.setHttpMethod(HttpMethod.GET);
        data.setFunctionParameters(new HashMap<>());
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("C "+new ObjectMapper().writeValueAsString(data)));
        Thread.sleep(100);
        msbClientWebSocketHandler.removeFunctionCallsListener(configurationListener);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("C "+new ObjectMapper().writeValueAsString(data)));

        Mockito.verify(configurationListener, Mockito.after(1000).times(1)).onCallback(Mockito.eq("df61a143-6dab-471a-88b4-8feddb4c9e45"), Mockito.eq("/functionhandler/hello_world"), Mockito.eq("correlationId"), Mockito.anyMap());
    }

    @Test
    void testFunctionCallResponseEvent() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        FunctionCallMessage data = new FunctionCallMessage("df61a143-6dab-471a-88b4-8feddb4c9e45","/functionhandler/hello_world","correlationId", new HashMap<>());
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("C "+new ObjectMapper().writeValueAsString(data)));
        Mockito.verify(mockSession,Mockito.after(1000).times(2)).sendMessage(captor.capture());
        Assertions.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"PULSE\""));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"correlationId\":\"correlationId\""));
    }

    @Test
    void testFunctionCallMultiResponseEvent() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("int",10);
        FunctionCallMessage data = new FunctionCallMessage("df61a143-6dab-471a-88b4-8feddb4c9e45","/functionhandler/printInt","correlationId", parameters);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("C "+new ObjectMapper().writeValueAsString(data)));
        Mockito.verify(mockSession,Mockito.after(1000).times(2)).sendMessage(captor.capture());
        Assertions.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"START\""));
        Assertions.assertTrue(captor.getValue().getPayload().toString().contains("\"correlationId\":\"correlationId\""));
    }

    @Test
    void testFunctionCallNullResponseEvent() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        FunctionCallMessage data = new FunctionCallMessage("df61a143-6dab-471a-88b4-8feddb4c9e45","/functionhandler/helloNull","correlationId", new HashMap<>());
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("C "+new ObjectMapper().writeValueAsString(data)));
        Mockito.verify(mockSession,Mockito.after(1000).times(1)).sendMessage(captor.capture());
        Assertions.assertFalse(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assertions.assertFalse(captor.getValue().getPayload().toString().contains("\"correlationId\":\"correlationId\""));
    }

    @Test
    void testFunctionCallsListenerWithEmptyReference() throws Exception {
        FunctionCallsListener configurationListener = Mockito.mock(FunctionCallsListener.class);
        msbClientWebSocketHandler.addFunctionCallsListener(configurationListener);
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register(new Application("df61a143-6dab-471a-88b4-8feddb4c9e45","name","description","token"));

        FunctionCallMessage data = new FunctionCallMessage();
        data.setUuid("df61a143-6dab-471a-88b4-8feddb4c9e45");
        data.setFunctionId("/functionhandler/hello_world");
        data.setCorrelationId("correlationId");
        data.setHttpMethod(HttpMethod.GET);
        data.setFunctionParameters(new HashMap<>());
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("C "+new ObjectMapper().writeValueAsString(data)));
        Thread.sleep(100);
        msbClientWebSocketHandler.removeFunctionCallsListener(configurationListener);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("C "+new ObjectMapper().writeValueAsString(data)));

        Mockito.verify(configurationListener, Mockito.after(1000).times(1)).onCallback(Mockito.eq("df61a143-6dab-471a-88b4-8feddb4c9e45"), Mockito.eq("/functionhandler/hello_world"), Mockito.eq("correlationId"), Mockito.anyMap());
    }

    @Test
    void testPing() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("ping"));
        Mockito.verify(mockSession).sendMessage(captor.capture());
        Assertions.assertEquals("pong",captor.getValue().getPayload());
    }

    @Test
    void testReconnect() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        SettableListenableFuture<WebSocketSession> connectFuture = new SettableListenableFuture<>();
        Mockito.when(sockJsClient.doHandshake(Mockito.same(msbClientWebSocketHandler),Mockito.anyString(), Mockito.any())).thenReturn(connectFuture);
        Mockito.when(msbClientWebSocketHandler.createClient(Mockito.anyString(),Mockito.any(),Mockito.any(),Mockito.anyInt())).thenReturn(sockJsClient);

        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_CONNECTED"));
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        Mockito.verify(mockSession).sendMessage(captor.capture());
        String message = captor.getValue().getPayload().toString();
        Assertions.assertTrue(message.startsWith("R {"));
        Assertions.assertTrue(message.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));

        msbClientWebSocketHandler.setConnectTimeout(100);
        msbClientWebSocketHandler.setReconnectInterval(100);
        Mockito.when(mockSession.isOpen()).thenReturn(false);
        msbClientWebSocketHandler.afterConnectionClosed(mockSession, CloseStatus.NORMAL);

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(450);
                Mockito.when(mockSession.isOpen()).thenReturn(true);
                connectFuture.set(mockSession);
                Thread.sleep(100);
                msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
                Thread.sleep(100);
                msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_CONNECTED"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Mockito.verify(sockJsClient,Mockito.after(1000).times(3)).doHandshake(Mockito.same(msbClientWebSocketHandler),Mockito.anyString(), Mockito.any());
        Mockito.verify(mockSession,Mockito.after(1000).times(2)).sendMessage(captor.capture());
        String reconnectMessage = captor.getValue().getPayload().toString();
        Assertions.assertTrue(reconnectMessage.startsWith("R {"));
        Assertions.assertTrue(reconnectMessage.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assertions.assertEquals(message,reconnectMessage);
    }

}
