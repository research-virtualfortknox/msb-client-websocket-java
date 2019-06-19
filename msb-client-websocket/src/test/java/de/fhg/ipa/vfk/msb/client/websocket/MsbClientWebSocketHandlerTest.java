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
import de.fhg.ipa.vfk.msb.client.api.messages.ConfigurationMessage;
import de.fhg.ipa.vfk.msb.client.api.messages.FunctionCallMessage;
import de.fhg.ipa.vfk.msb.client.api.Application;
import de.fhg.ipa.vfk.msb.client.api.Configuration;
import de.fhg.ipa.vfk.msb.client.api.Event;
import de.fhg.ipa.vfk.msb.client.api.Function;
import de.fhg.ipa.vfk.msb.client.api.Gateway;
import de.fhg.ipa.vfk.msb.client.api.ParameterValue;
import de.fhg.ipa.vfk.msb.client.api.SmartObject;
import de.fhg.ipa.vfk.msb.client.listener.ConfigurationListener;
import de.fhg.ipa.vfk.msb.client.listener.ConnectionListener;
import de.fhg.ipa.vfk.msb.client.listener.FunctionCallsListener;
import de.fhg.ipa.vfk.msb.client.listener.PublishingError;
import de.fhg.ipa.vfk.msb.client.listener.RegistrationError;
import de.fhg.ipa.vfk.msb.client.util.WrongDataFormatException;
import de.fhg.ipa.vfk.msb.client.websocket.annotation.AnnotationTestClient;
import de.fhg.ipa.vfk.msb.client.websocket.annotation.TestClientFunctionHandler;
import de.fhg.ipa.vfk.msb.client.api.PrimitiveFormat;
import de.fhg.ipa.vfk.msb.client.api.PrimitiveType;
import de.fhg.ipa.vfk.msb.client.api.messages.EventPriority;
import de.fhg.ipa.vfk.msb.client.util.MsbDateFormat;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The type Msb client mockito test.
 *
 * @author des
 */
@RunWith(MockitoJUnitRunner.class)
public class MsbClientWebSocketHandlerTest {

    @Mock
    private WebSocketSession mockSession;

    @Captor
    private ArgumentCaptor<WebSocketMessage> captor;

    @Spy
    private MsbClientWebSocketHandler msbClientWebSocketHandler = new MsbClientWebSocketHandler();

    /**
     * Test send callback.
     *
     * @throws Exception the exception
     */
    @Test(expected = IllegalStateException.class)
    public void testRegisterUnconnected() throws Exception{
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
    }

    @Test
    public void testRegisterWithAnnotationScanning() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        Mockito.verify(mockSession).sendMessage(captor.capture());
        String message = captor.getValue().getPayload().toString();
        Assert.assertTrue(message.startsWith("R {"));
        Assert.assertTrue(message.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Application application = new ObjectMapper().readValue(message.substring(2), Application.class);
        Assert.assertEquals("AnnotationTestClient",application.getName());
        Assert.assertEquals("Annotation Test client",application.getDescription());
        Assert.assertEquals("token",application.getToken());
        Assert.assertEquals(5, application.getEvents().size());
        Assert.assertEquals(7, application.getFunctions().size());
        Assert.assertEquals(3,application.getConfiguration().getParameters().size());
    }

    @Test
    public void testRegisterWithAnnotationScanningAndFunctionHandlerInstances() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation", new Object[]{new TestClientFunctionHandler()});
        Mockito.verify(mockSession).sendMessage(captor.capture());
        String message = captor.getValue().getPayload().toString();
        Assert.assertTrue(message.startsWith("R {"));
        Assert.assertTrue(message.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Application application = new ObjectMapper().readValue(message.substring(2), Application.class);
        Assert.assertEquals("AnnotationTestClient",application.getName());
        Assert.assertEquals("Annotation Test client",application.getDescription());
        Assert.assertEquals("token",application.getToken());
        Assert.assertEquals(5, application.getEvents().size());
        Assert.assertEquals(7, application.getFunctions().size());
        Assert.assertEquals(3,application.getConfiguration().getParameters().size());
    }

    @Test
    public void testRegisterWithAnnotationScanningAndFunctionHandlerClasses() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation", new Object[]{TestClientFunctionHandler.class});
        Mockito.verify(mockSession).sendMessage(captor.capture());
        String message = captor.getValue().getPayload().toString();
        Assert.assertTrue(message.startsWith("R {"));
        Assert.assertTrue(message.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Application application = new ObjectMapper().readValue(message.substring(2), Application.class);
        Assert.assertEquals("AnnotationTestClient",application.getName());
        Assert.assertEquals("Annotation Test client",application.getDescription());
        Assert.assertEquals("token",application.getToken());
        Assert.assertEquals(5, application.getEvents().size());
        Assert.assertEquals(7, application.getFunctions().size());
        Assert.assertEquals(3,application.getConfiguration().getParameters().size());
    }

    @Test
    public void testUpdateRegister() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.register(new Application("uuid","name","description","token"));
        Mockito.verify(mockSession, Mockito.times(2)).sendMessage(captor.capture());

        String firstMessage = captor.getAllValues().get(0).getPayload().toString();
        Assert.assertTrue(firstMessage.startsWith("R {"));
        Assert.assertTrue(firstMessage.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Application application = new ObjectMapper().readValue(firstMessage.substring(2), Application.class);
        Assert.assertEquals("AnnotationTestClient",application.getName());
        Assert.assertEquals("Annotation Test client",application.getDescription());
        Assert.assertEquals("token",application.getToken());
        Assert.assertEquals(5, application.getEvents().size());
        Assert.assertEquals(7, application.getFunctions().size());
        Assert.assertEquals(3,application.getConfiguration().getParameters().size());

        String secondMessage = captor.getValue().getPayload().toString();
        Assert.assertTrue(secondMessage.startsWith("R {"));
        Assert.assertTrue(secondMessage.contains("\"uuid\":\"uuid\","));
        application = new ObjectMapper().readValue(secondMessage.substring(2), Application.class);
        Assert.assertEquals("name",application.getName());
        Assert.assertEquals("description",application.getDescription());
        Assert.assertEquals("token",application.getToken());
        Assert.assertEquals(0, application.getEvents().size());
        Assert.assertEquals(0, application.getFunctions().size());
        Assert.assertNull(application.getConfiguration());
    }

    @Test
    public void testUpdateRegisterWithAnnotationScanning() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        Map<String, ParameterValue> parameters = new HashMap<>();
        parameters.put("testParam",new ParameterValue("value",PrimitiveType.STRING));
        Application updateApplication = new Application("uuid","name","description","token", new Configuration(parameters));

        msbClientWebSocketHandler.register(updateApplication,"de.fhg.ipa.vfk.msb.client.websocket.annotation");
        Mockito.verify(mockSession, Mockito.times(2)).sendMessage(captor.capture());

        String firstMessage = captor.getAllValues().get(0).getPayload().toString();
        Assert.assertTrue(firstMessage.startsWith("R {"));
        Assert.assertTrue(firstMessage.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Application application = new ObjectMapper().readValue(firstMessage.substring(2), Application.class);
        Assert.assertEquals("AnnotationTestClient",application.getName());
        Assert.assertEquals("Annotation Test client",application.getDescription());
        Assert.assertEquals("token",application.getToken());
        Assert.assertEquals(5, application.getEvents().size());
        Assert.assertEquals(7, application.getFunctions().size());
        Assert.assertEquals(3,application.getConfiguration().getParameters().size());

        String secondMessage = captor.getValue().getPayload().toString();
        Assert.assertTrue(secondMessage.startsWith("R {"));
        Assert.assertTrue(secondMessage.contains("\"uuid\":\"uuid\","));
        application = new ObjectMapper().readValue(secondMessage.substring(2), Application.class);
        Assert.assertEquals("name",application.getName());
        Assert.assertEquals("description",application.getDescription());
        Assert.assertEquals("token",application.getToken());
        Assert.assertEquals(5, application.getEvents().size());
        Assert.assertEquals(7, application.getFunctions().size());
        Assert.assertEquals(4,application.getConfiguration().getParameters().size());
    }

    @Test
    public void testUpdateRegisterWithAnnotationScanningAndFunctionHandlerClasses() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        Application updateApplication = new Application("uuid","name","description","token");
        updateApplication.addEvent(new Event("PULSE","Event pulse",""));
        msbClientWebSocketHandler.register(updateApplication,new Object[]{TestClientFunctionHandler.class});
        Mockito.verify(mockSession, Mockito.times(2)).sendMessage(captor.capture());

        String firstMessage = captor.getAllValues().get(0).getPayload().toString();
        Assert.assertTrue(firstMessage.startsWith("R {"));
        Assert.assertTrue(firstMessage.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Application application = new ObjectMapper().readValue(firstMessage.substring(2), Application.class);
        Assert.assertEquals("AnnotationTestClient",application.getName());
        Assert.assertEquals("Annotation Test client",application.getDescription());
        Assert.assertEquals("token",application.getToken());
        Assert.assertEquals(5, application.getEvents().size());
        Assert.assertEquals(7, application.getFunctions().size());
        Assert.assertEquals(3,application.getConfiguration().getParameters().size());

        String secondMessage = captor.getValue().getPayload().toString();
        Assert.assertTrue(secondMessage.startsWith("R {"));
        Assert.assertTrue(secondMessage.contains("\"uuid\":\"uuid\","));
        application = new ObjectMapper().readValue(secondMessage.substring(2), Application.class);
        Assert.assertEquals("name",application.getName());
        Assert.assertEquals("description",application.getDescription());
        Assert.assertEquals("token",application.getToken());
        Assert.assertEquals(4, application.getEvents().size());
        Assert.assertEquals(7, application.getFunctions().size());
        Assert.assertNull(application.getConfiguration());
    }

    @Test
    public void testUpdateRegisterWithAnnotationScanningAndFunctionHandlerAndEventClasses() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        Map<String, ParameterValue> parameters = new HashMap<>();
        parameters.put("testParam",new ParameterValue("value",PrimitiveType.STRING));
        Application updateApplication = new Application("uuid","name","description","token", new Configuration(parameters));

        msbClientWebSocketHandler.register(updateApplication,new Object[]{TestClientFunctionHandler.class}, new Class<?>[]{AnnotationTestClient.class});
        Mockito.verify(mockSession, Mockito.times(2)).sendMessage(captor.capture());

        String firstMessage = captor.getAllValues().get(0).getPayload().toString();
        Assert.assertTrue(firstMessage.startsWith("R {"));
        Assert.assertTrue(firstMessage.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Application application = new ObjectMapper().readValue(firstMessage.substring(2), Application.class);
        Assert.assertEquals("AnnotationTestClient",application.getName());
        Assert.assertEquals("Annotation Test client",application.getDescription());
        Assert.assertEquals("token",application.getToken());
        Assert.assertEquals(5, application.getEvents().size());
        Assert.assertEquals(7, application.getFunctions().size());
        Assert.assertEquals(3,application.getConfiguration().getParameters().size());

        String secondMessage = captor.getValue().getPayload().toString();
        Assert.assertTrue(secondMessage.startsWith("R {"));
        Assert.assertTrue(secondMessage.contains("\"uuid\":\"uuid\","));
        application = new ObjectMapper().readValue(secondMessage.substring(2), Application.class);
        Assert.assertEquals("name",application.getName());
        Assert.assertEquals("description",application.getDescription());
        Assert.assertEquals("token",application.getToken());
        Assert.assertEquals(5, application.getEvents().size());
        Assert.assertEquals(7, application.getFunctions().size());
        Assert.assertEquals(1,application.getConfiguration().getParameters().size());
    }

    @Test
    public void testRegisterWithAddMethods() throws Exception {
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
        Assert.assertTrue(message.startsWith("R {"));
        Assert.assertTrue(message.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e23\","));
        smartObject = new ObjectMapper().readValue(message.substring(2), SmartObject.class);
        Assert.assertEquals("name", smartObject.getName());
        Assert.assertEquals("description", smartObject.getDescription());
        Assert.assertEquals("token", smartObject.getToken());
        Assert.assertEquals(2, smartObject.getEvents().size());
        Assert.assertEquals(1, smartObject.getFunctions().size());
        Assert.assertEquals(2, smartObject.getConfiguration().getParameters().size());
    }

    @Test
    public void testRegisterGateway() throws Exception {
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
        Application managedApplication = new Application("uuid","name1","description1","token1");
        managedApplication.addEvent(new Event("PULSE","Pulse","Pulse event"));
        managedApplication.addFunction(new Function("helloWorld","Hello world","Print Hello world"));
        msbClientWebSocketHandler.addManagedService(managedApplication);
        msbClientWebSocketHandler.register(gateway);
        Mockito.verify(mockSession,Mockito.times(2)).sendMessage(captor.capture());
        String message = captor.getValue().getPayload().toString();
        Assert.assertTrue(message.startsWith("R {"));
        Assert.assertTrue(message.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e\","));
        gateway = new ObjectMapper().readValue(message.substring(2), Gateway.class);
        Assert.assertEquals("name", gateway.getName());
        Assert.assertEquals("description", gateway.getDescription());
        Assert.assertEquals("token", gateway.getToken());
        Assert.assertEquals(2, gateway.getEvents().size());
        Assert.assertEquals(1, gateway.getFunctions().size());
        Assert.assertEquals(2, gateway.getConfiguration().getParameters().size());
    }

    /**
     * Test publish before register.
     *
     * @throws Exception the exception
     */
    @Test(expected = IOException.class)
    public void testPublishBeforeRegister() throws Exception {
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.publish("PULSE");
    }

    @Test
    public void testPublishEmptyEvent() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        Assert.assertTrue(msbClientWebSocketHandler.isRegistered());
        msbClientWebSocketHandler.publish("PULSE");
        Mockito.verify(mockSession,Mockito.times(2)).sendMessage(captor.capture());
        Assert.assertTrue(captor.getAllValues().get(0).getPayload().toString().startsWith("R {"));
        Assert.assertTrue(captor.getAllValues().get(0).getPayload().toString().contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"PULSE\""));
    }

    @Test
    public void testPublishObject() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        msbClientWebSocketHandler.publish("TEMPERATURE",23.4F);
        Mockito.verify(mockSession,Mockito.times(2)).sendMessage(captor.capture());
        Assert.assertTrue(captor.getAllValues().get(0).getPayload().toString().startsWith("R {"));
        Assert.assertTrue(captor.getAllValues().get(0).getPayload().toString().contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"TEMPERATURE\""));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"dataObject\":23.4"));
    }

    @Test(expected = WrongDataFormatException.class)
    public void testPublishWithWrongDataFormat() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.setDataFormatValidation(true);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        msbClientWebSocketHandler.publish("TEMPERATURE",new Date());
        Mockito.verify(mockSession,Mockito.times(1)).sendMessage(captor.capture());
        Assert.assertTrue(captor.getValue().getPayload().toString().startsWith("R {"));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
    }

    @Test
    public void testPublishObjectWithPriority() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        msbClientWebSocketHandler.publish("TEMPERATURE",23.4F, EventPriority.MEDIUM);
        Mockito.verify(mockSession,Mockito.times(2)).sendMessage(captor.capture());
        Assert.assertTrue(captor.getAllValues().get(0).getPayload().toString().startsWith("R {"));
        Assert.assertTrue(captor.getAllValues().get(0).getPayload().toString().contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"TEMPERATURE\""));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"dataObject\":23.4"));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"priority\":1"));
    }

    @Test
    public void testPublishObjectWithDate() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        Date date = new Date();
        msbClientWebSocketHandler.publish("TEMPERATURE",23.4F, EventPriority.MEDIUM, false, date);
        Mockito.verify(mockSession,Mockito.times(2)).sendMessage(captor.capture());
        Assert.assertTrue(captor.getAllValues().get(0).getPayload().toString().startsWith("R {"));
        Assert.assertTrue(captor.getAllValues().get(0).getPayload().toString().contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"TEMPERATURE\""));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"dataObject\":23.4"));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"priority\":1"));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"postDate\":\""+ new MsbDateFormat().format(date)+"\""));
    }

    @Test
    public void testPublishObjectWithCorrelationId() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        Date date = new Date();
        String correlationId = UUID.randomUUID().toString();
        msbClientWebSocketHandler.publish("TEMPERATURE",23.4F, EventPriority.MEDIUM, false, date, correlationId);
        Mockito.verify(mockSession,Mockito.times(2)).sendMessage(captor.capture());
        Assert.assertTrue(captor.getAllValues().get(0).getPayload().toString().startsWith("R {"));
        Assert.assertTrue(captor.getAllValues().get(0).getPayload().toString().contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"TEMPERATURE\""));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"dataObject\":23.4"));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"priority\":1"));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"postDate\":\""+ new MsbDateFormat().format(date)+"\""));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"correlationId\":\""+ correlationId+"\""));
    }

    @Test
    public void testPublishObjectWithCached() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.publish("TEMPERATURE",23.4F, EventPriority.MEDIUM, true);
        Mockito.verify(mockSession,Mockito.times(1)).sendMessage(captor.capture());
        Assert.assertTrue(captor.getValue().getPayload().toString().startsWith("R {"));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        Mockito.verify(mockSession,Mockito.times(2)).sendMessage(captor.capture());
        Assert.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"TEMPERATURE\""));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"dataObject\":23.4"));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"priority\":1"));
    }

    @Test
    public void testPublishForServiceWithAddedManagedService() throws Exception {
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
        Assert.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"application_uuid\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"PULSE\""));
    }

    @Test
    public void testPublishForServiceInvalidEventWithAddedManagedService() throws Exception {
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
        Assert.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"TEMPERATURE\""));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"dataObject\":23.4"));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"priority\":1"));
    }

    @Test
    public void testPublishForService() throws Exception {
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
        Assert.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"application_uuid\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"PULSE\""));
    }

    @Test
    public void testPublishForServiceInvalidEvent() throws Exception {
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
        Assert.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"TEMPERATURE\""));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"dataObject\":23.4"));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"priority\":1"));
    }

    @Test
    public void testConnectionListener() throws Exception {
        ConnectionListener connectionListener = Mockito.mock(ConnectionListener.class);
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
        //Mockito.verify(connectionListener, Mockito.times(1)).errorAtServiceRegistration(Mockito.any(RegistrationError.class));
        Mockito.verify(connectionListener, Mockito.times(1)).afterConnectionClosed();
        Mockito.verify(connectionListener, Mockito.times(1)).beforeConnectionTryToReconnecting();

        msbClientWebSocketHandler.removeConnectionListener(connectionListener);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
    }

    @Test
    public void testConfigurationListener() throws Exception {
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
    public void testFunctionCallsListener() throws Exception {
        FunctionCallsListener configurationListener = Mockito.mock(FunctionCallsListener.class);
        msbClientWebSocketHandler.addFunctionCallsListener(configurationListener);
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");

        FunctionCallMessage data = new FunctionCallMessage("df61a143-6dab-471a-88b4-8feddb4c9e45","/functionhandler/hello_world","correlationId",new HashMap<String, Object>());
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("C "+new ObjectMapper().writeValueAsString(data)));
        Thread.sleep(100);
        msbClientWebSocketHandler.removeFunctionCallsListener(configurationListener);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("C "+new ObjectMapper().writeValueAsString(data)));

        Mockito.verify(configurationListener, Mockito.after(100).times(1)).onCallback(Mockito.eq("df61a143-6dab-471a-88b4-8feddb4c9e45"), Mockito.eq("/functionhandler/hello_world"), Mockito.eq("correlationId"), Mockito.anyMapOf(String.class, Object.class));
    }

    @Test
    public void testFunctionCallResponseEvent() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        FunctionCallMessage data = new FunctionCallMessage("df61a143-6dab-471a-88b4-8feddb4c9e45","/functionhandler/hello_world","correlationId",new HashMap<String, Object>());
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("C "+new ObjectMapper().writeValueAsString(data)));
        Mockito.verify(mockSession,Mockito.after(100).times(2)).sendMessage(captor.capture());
        Assert.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"PULSE\""));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"correlationId\":\"correlationId\""));
    }

    @Test
    public void testFunctionCallMultiResponseEvent() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("int",10);
        FunctionCallMessage data = new FunctionCallMessage("df61a143-6dab-471a-88b4-8feddb4c9e45","/functionhandler/printInt","correlationId", parameters);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("C "+new ObjectMapper().writeValueAsString(data)));
        Mockito.verify(mockSession,Mockito.after(100).times(2)).sendMessage(captor.capture());
        Assert.assertTrue(captor.getValue().getPayload().toString().startsWith("E {\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"eventId\":\"START\""));
        Assert.assertTrue(captor.getValue().getPayload().toString().contains("\"correlationId\":\"correlationId\""));
    }

    @Test
    public void testPing() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("ping"));
        Mockito.verify(mockSession).sendMessage(captor.capture());
        Assert.assertEquals("pong",captor.getValue().getPayload());
    }

    @Ignore
    @Test
    public void testReconnect() throws Exception {
        Mockito.when(mockSession.isOpen()).thenReturn(true);
        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_CONNECTED"));
        msbClientWebSocketHandler.register("de.fhg.ipa.vfk.msb.client.websocket.annotation");
        Mockito.verify(mockSession).sendMessage(captor.capture());
        String message = captor.getValue().getPayload().toString();
        Assert.assertTrue(message.startsWith("R {"));
        Assert.assertTrue(message.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_REGISTERED"));

        msbClientWebSocketHandler.afterConnectionClosed(mockSession, CloseStatus.NORMAL);

        msbClientWebSocketHandler.afterConnectionEstablished(mockSession);
        msbClientWebSocketHandler.handleTextMessage(mockSession,new TextMessage("IO_CONNECTED"));
        Mockito.verify(mockSession,Mockito.after(100).times(2)).sendMessage(captor.capture());
        String reconnectMessage = captor.getValue().getPayload().toString();
        Assert.assertTrue(reconnectMessage.startsWith("R {"));
        Assert.assertTrue(reconnectMessage.contains("\"uuid\":\"df61a143-6dab-471a-88b4-8feddb4c9e45\","));
        Assert.assertEquals(message,reconnectMessage);
    }

}
