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

package de.fhg.ipa.vfk.msb.client.api;

import de.fhg.ipa.vfk.msb.client.util.DataFormatParser;
import io.swagger.models.HttpMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

class EntityTest {

    private static final String SERVICE_UUID = UUID.randomUUID().toString();
    private static final String SERVICE_NAME = "name";
    private static final String SERVICE_DESCRIPTION = "description";
    private static final String SERVICE_TOKEN = "token";

    @Test
    void testApplicationSetter(){
        Connection connection = new Connection();
        connection.setConnectionState(ConnectionState.UNCONNECTED);
        connection.setConnectionType(ConnectionType.OPC_UA);
        connection.setConnectionFormat(ConnectionFormat.JSON);

        Application application = new Application("","","","");
        application.setUuid(SERVICE_UUID);
        application.setName(SERVICE_NAME);
        application.setDescription(SERVICE_DESCRIPTION);
        application.setToken(SERVICE_TOKEN);
        application.setConfiguration(getConfiguration());
        application.setConnection(connection);
        addEvents(application);
        addFunctions(application);
        addEndpoints(application);

        Assertions.assertEquals(SERVICE_UUID,application.getUuid());
        Assertions.assertEquals(SERVICE_NAME,application.getName());
        Assertions.assertEquals(SERVICE_DESCRIPTION,application.getDescription());
        Assertions.assertEquals(SERVICE_TOKEN,application.getToken());
        Assertions.assertNotNull(application.getConfiguration());
        Assertions.assertNotNull(application.getConnection());
        Assertions.assertEquals(ConnectionState.UNCONNECTED, application.getConnection().getConnectionState());
        Assertions.assertEquals(ConnectionType.OPC_UA, application.getConnection().getConnectionType());
        Assertions.assertEquals(ConnectionFormat.JSON, application.getConnection().getConnectionFormat());
        Assertions.assertNotNull(application.getEvents());
        Assertions.assertEquals(2,application.getEvents().size());
        Assertions.assertNotNull(application.getFunctions());
        Assertions.assertEquals(2,application.getFunctions().size());
        Assertions.assertNotNull(application.getEndpoints());
        Assertions.assertEquals(2,application.getEndpoints().size());
        application.getEndpoints().forEach(endpoint -> {
            if(endpoint.getConnectionType().equals(ConnectionType.REST)){
                Assertions.assertEquals("url",endpoint.getUrl());
                Assertions.assertEquals(1,endpoint.getFunctions().size(),"e");
                EndpointFunction function = endpoint.getFunctions().get(0);
                Assertions.assertEquals(ConnectionFormat.JSON, function.getConnectionFormat());
                Assertions.assertEquals(HttpMethod.PUT, function.getHttpMethod());
                Assertions.assertEquals(0, function.getStatusCodes().size(), "f");
                Assertions.assertEquals(application.getFunctions().get(1), function.getFunction());
            }
            if(endpoint.getConnectionType().equals(ConnectionType.SOAP)){
                Assertions.assertEquals("http://url.com",endpoint.getUrl());
                Assertions.assertEquals(2,endpoint.getFunctions().size());
                endpoint.getFunctions().forEach(function -> {
                    if (function.getConnectionFormat().equals(ConnectionFormat.XML)){
                        Assertions.assertEquals(HttpMethod.GET, function.getHttpMethod());
                        Assertions.assertEquals(2, function.getStatusCodes().size());
                        Assertions.assertTrue(function.getStatusCodes().containsKey("200"));
                        Assertions.assertTrue(function.getStatusCodes().containsKey("400"));
                        Assertions.assertEquals(application.getFunctions().get(0), function.getFunction());
                    }
                    if (function.getConnectionFormat().equals(ConnectionFormat.JSON)){
                        Assertions.assertEquals(HttpMethod.PUT, function.getHttpMethod());
                        Assertions.assertEquals(0, function.getStatusCodes().size(), "f");
                        Assertions.assertEquals(application.getFunctions().get(1), function.getFunction());
                    }
                });
            }
        });
    }

    @Test
    void testApplicationConstructor(){
        Application application = new Application(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN, new ArrayList<Endpoint>());
        application.setConfiguration(getConfiguration());
        application.setConnection(new Connection(ConnectionState.CONNECTED, ConnectionType.OPC_UA, ConnectionFormat.JSON, new Date()));
        addEvents(application);
        addFunctions(application);
        addEndpoints(application);

        Assertions.assertEquals(SERVICE_UUID,application.getUuid());
        Assertions.assertEquals(SERVICE_NAME,application.getName());
        Assertions.assertEquals(SERVICE_DESCRIPTION,application.getDescription());
        Assertions.assertEquals(SERVICE_TOKEN,application.getToken());
        Assertions.assertNotNull(application.getConfiguration());
        Assertions.assertNotNull(application.getConnection());
        Assertions.assertEquals(ConnectionState.CONNECTED, application.getConnection().getConnectionState());
        Assertions.assertEquals(ConnectionType.OPC_UA, application.getConnection().getConnectionType());
        Assertions.assertEquals(ConnectionFormat.JSON, application.getConnection().getConnectionFormat());
        Assertions.assertNotNull(application.getEvents());
        Assertions.assertEquals(2,application.getEvents().size());
        Assertions.assertNotNull(application.getFunctions());
        Assertions.assertEquals(2,application.getFunctions().size());
        Assertions.assertNotNull(application.getEndpoints());
        Assertions.assertEquals(2,application.getEndpoints().size());
    }

    @Test
    void testApplicationConstructor2(){
        Application application = new Application(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN,getConfiguration(), new ArrayList<Endpoint>());
        addEvents(application);
        addFunctions(application);
        addEndpoints(application);

        Assertions.assertEquals(SERVICE_UUID,application.getUuid());
        Assertions.assertEquals(SERVICE_NAME,application.getName());
        Assertions.assertEquals(SERVICE_DESCRIPTION,application.getDescription());
        Assertions.assertEquals(SERVICE_TOKEN,application.getToken());
        Assertions.assertNotNull(application.getConfiguration());
        Assertions.assertNotNull(application.getEvents());
        Assertions.assertEquals(2,application.getEvents().size());
        Assertions.assertNotNull(application.getFunctions());
        Assertions.assertEquals(2,application.getFunctions().size());
        Assertions.assertNotNull(application.getEndpoints());
        Assertions.assertEquals(2,application.getEndpoints().size());
    }

    @Test
    void testSmartObjectSetter(){
        Connection connection = new Connection();
        connection.setConnectionState(ConnectionState.UNCONNECTED);
        connection.setConnectionType(ConnectionType.OPC_UA);
        connection.setConnectionFormat(ConnectionFormat.JSON);

        SmartObject smartObject = new SmartObject("","","","");
        smartObject.setUuid(SERVICE_UUID);
        smartObject.setName(SERVICE_NAME);
        smartObject.setDescription(SERVICE_DESCRIPTION);
        smartObject.setToken(SERVICE_TOKEN);
        smartObject.setConfiguration(getConfiguration());
        smartObject.setConnection(connection);
        addEvents(smartObject);
        addFunctions(smartObject);

        Assertions.assertEquals(SERVICE_UUID,smartObject.getUuid());
        Assertions.assertEquals(SERVICE_NAME,smartObject.getName());
        Assertions.assertEquals(SERVICE_DESCRIPTION,smartObject.getDescription());
        Assertions.assertEquals(SERVICE_TOKEN,smartObject.getToken());
        Assertions.assertNotNull(smartObject.getConfiguration());
        Assertions.assertNotNull(smartObject.getConnection());
        Assertions.assertEquals(ConnectionState.UNCONNECTED, smartObject.getConnection().getConnectionState());
        Assertions.assertEquals(ConnectionType.OPC_UA, smartObject.getConnection().getConnectionType());
        Assertions.assertEquals(ConnectionFormat.JSON, smartObject.getConnection().getConnectionFormat());
        Assertions.assertNotNull(smartObject.getEvents());
        Assertions.assertEquals(2,smartObject.getEvents().size());
        Assertions.assertNotNull(smartObject.getFunctions());
        Assertions.assertEquals(2,smartObject.getFunctions().size());
    }

    @Test
    void testSmartObjectConstructor(){
        SmartObject smartObject = new SmartObject(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN);
        smartObject.setConfiguration(getConfiguration());
        smartObject.setConnection(new Connection(ConnectionState.CONNECTED, ConnectionType.OPC_UA, ConnectionFormat.JSON, new Date()));
        addEvents(smartObject);
        addFunctions(smartObject);

        Assertions.assertEquals(SERVICE_UUID,smartObject.getUuid());
        Assertions.assertEquals(SERVICE_NAME,smartObject.getName());
        Assertions.assertEquals(SERVICE_DESCRIPTION,smartObject.getDescription());
        Assertions.assertEquals(SERVICE_TOKEN,smartObject.getToken());
        Assertions.assertNotNull(smartObject.getConfiguration());
        Assertions.assertNotNull(smartObject.getConnection());
        Assertions.assertEquals(ConnectionState.CONNECTED, smartObject.getConnection().getConnectionState());
        Assertions.assertEquals(ConnectionType.OPC_UA, smartObject.getConnection().getConnectionType());
        Assertions.assertEquals(ConnectionFormat.JSON, smartObject.getConnection().getConnectionFormat());
        Assertions.assertNotNull(smartObject.getEvents());
        Assertions.assertEquals(2,smartObject.getEvents().size());
        Assertions.assertNotNull(smartObject.getFunctions());
        Assertions.assertEquals(2,smartObject.getFunctions().size());
    }

    @Test
    void testSmartObjectConstructor2(){
        SmartObject smartObject = new SmartObject(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN,getConfiguration());
        addEvents(smartObject);
        addFunctions(smartObject);

        Assertions.assertEquals(SERVICE_UUID,smartObject.getUuid());
        Assertions.assertEquals(SERVICE_NAME,smartObject.getName());
        Assertions.assertEquals(SERVICE_DESCRIPTION,smartObject.getDescription());
        Assertions.assertEquals(SERVICE_TOKEN,smartObject.getToken());
        Assertions.assertNotNull(smartObject.getConfiguration());
        Assertions.assertNotNull(smartObject.getEvents());
        Assertions.assertEquals(2,smartObject.getEvents().size());
        Assertions.assertNotNull(smartObject.getFunctions());
        Assertions.assertEquals(2,smartObject.getFunctions().size());
    }

    @Test
    void testGatewaySetter(){
        Gateway gateway = new Gateway("","","","");
        gateway.setUuid(SERVICE_UUID);
        gateway.setName(SERVICE_NAME);
        gateway.setDescription(SERVICE_DESCRIPTION);
        gateway.setToken(SERVICE_TOKEN);
        gateway.setConfiguration(getConfiguration());
        addEvents(gateway);
        addFunctions(gateway);
        gateway.setServices(Collections.singleton(new SmartObject("uuid", "name", "description", "token")));

        Assertions.assertEquals(SERVICE_UUID,gateway.getUuid());
        Assertions.assertEquals(SERVICE_NAME,gateway.getName());
        Assertions.assertEquals(SERVICE_DESCRIPTION,gateway.getDescription());
        Assertions.assertEquals(SERVICE_TOKEN,gateway.getToken());
        Assertions.assertNotNull(gateway.getConfiguration());
        Assertions.assertNotNull(gateway.getEvents());
        Assertions.assertEquals(2,gateway.getEvents().size());
        Assertions.assertNotNull(gateway.getFunctions());
        Assertions.assertEquals(2,gateway.getFunctions().size());
        Assertions.assertNotNull(gateway.getServices());
        Assertions.assertEquals(1,gateway.getServices().size());
    }

    @Test
    void testGatewayConstructor(){
        Gateway gateway = new Gateway(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN, new HashSet<>());
        gateway.setConfiguration(getConfiguration());
        addEvents(gateway);
        addFunctions(gateway);
        gateway.addService(new SmartObject("uuid","name","description","token"));

        Assertions.assertEquals(SERVICE_UUID,gateway.getUuid());
        Assertions.assertEquals(SERVICE_NAME,gateway.getName());
        Assertions.assertEquals(SERVICE_DESCRIPTION,gateway.getDescription());
        Assertions.assertEquals(SERVICE_TOKEN,gateway.getToken());
        Assertions.assertNotNull(gateway.getConfiguration());
        Assertions.assertNotNull(gateway.getEvents());
        Assertions.assertEquals(2,gateway.getEvents().size());
        Assertions.assertNotNull(gateway.getFunctions());
        Assertions.assertEquals(2,gateway.getFunctions().size());
        Assertions.assertNotNull(gateway.getServices());
        Assertions.assertEquals(1,gateway.getServices().size());
    }

    @Test
    void testGatewayConstructor2(){
        Gateway gateway = new Gateway(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN,getConfiguration(), new HashSet<Service>());
        addEvents(gateway);
        addFunctions(gateway);
        Set<Service> services = new HashSet<>();
        services.add(new SmartObject("uuid","name","description","token"));
        services.add(new SmartObject("uuid1","name1","description1","token1"));
        gateway.setServices(services);

        Assertions.assertEquals(SERVICE_UUID,gateway.getUuid());
        Assertions.assertEquals(SERVICE_NAME,gateway.getName());
        Assertions.assertEquals(SERVICE_DESCRIPTION,gateway.getDescription());
        Assertions.assertEquals(SERVICE_TOKEN,gateway.getToken());
        Assertions.assertNotNull(gateway.getConfiguration());
        Assertions.assertNotNull(gateway.getEvents());
        Assertions.assertEquals(2,gateway.getEvents().size());
        Assertions.assertNotNull(gateway.getFunctions());
        Assertions.assertEquals(2,gateway.getFunctions().size());
        Assertions.assertNotNull(gateway.getServices());
        Assertions.assertEquals(2,gateway.getServices().size());
    }

    @Test
    void testServiceNotEquals(){
        Service smartObject = new SmartObject(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN);
        Service gateway = new Gateway(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN);
        Assertions.assertNotEquals(smartObject,gateway);
        Assertions.assertEquals(smartObject.hashCode(),gateway.hashCode());
    }

    @Test
    void testServiceEquals(){
        Application application = new Application(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN);
        Assertions.assertEquals(application,application);
        Assertions.assertEquals(application.hashCode(),application.hashCode());
    }

    @Test
    void testServiceEqualsNull(){
        Gateway gateway = new Gateway(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN);
        Assertions.assertNotEquals(gateway,null);
    }

    private Configuration getConfiguration(){
        ParameterValue parameterValue = new ParameterValue();
        parameterValue.setFormat(PrimitiveFormat.BYTE);
        parameterValue.setType(PrimitiveType.STRING);
        parameterValue.setValue("test");
        ParameterValue parameterValue1 = new ParameterValue("value");
        Configuration configuration = new Configuration();
        configuration.setParameters(new HashMap<>());
        configuration.addParameter("param",parameterValue);
        configuration.addParameter("param1",parameterValue1);
        configuration.addParameter("param2",new ParameterValue("value", PrimitiveType.NUMBER, PrimitiveFormat.FLOAT));
        configuration.addParameter("param3",new ParameterValue("value", PrimitiveType.INTEGER, PrimitiveFormat.INT32));
        configuration.addParameter("param4",new ParameterValue("value", PrimitiveType.ARRAY));
        return configuration;
    }

    private Connection getConnection(){
        return new Connection(ConnectionState.CONNECTED, ConnectionType.OPC_UA, ConnectionFormat.JSON, new Date());
    }

    private void addEvents(Service service){
        Event event = new Event();
        event.setEventId("eventId");
        event.setName("name");
        event.setDescription("description");
        event.setDataFormat(DataFormatParser.parse(String.class));
        service.setEvents(null);
        service.setEvents(new ArrayList<Event>());
        List<Event> events = new ArrayList<>();
        events.add(event);
        service.addEvents(events);
        service.addEvent(new Event("eventId1","name1","description1",DataFormatParser.parse(String.class)));
    }

    private void addFunctions(Service service){
        Function function = new Function();
        function.setFunctionId("functionId");
        function.setName("name");
        function.setDescription("description");
        function.setDataFormat(DataFormatParser.parse(String.class));
        function.setResponseEvents(new Event[]{service.getEvents().get(0), service.getEvents().get(1)});
        service.setFunctions(null);
        service.setFunctions(new ArrayList<Function>());
        List<Function> functions = new ArrayList<>();
        functions.add(function);
        service.addFunctions(functions);
        service.addFunction(new Function("functionId1","name1","description1",DataFormatParser.parse(String.class)));
    }

    private void addEndpoints(Application application){
        EndpointFunction endpointFunction = new EndpointFunction();
        endpointFunction.setConnectionFormat(ConnectionFormat.XML);
        endpointFunction.setHttpMethod(HttpMethod.GET);
        Map<String,String> statusCodes = new HashMap<>();
        statusCodes.put("200","eventId");
        endpointFunction.setStatusCodes(statusCodes);
        endpointFunction.addStatusCode("400","eventId1");
        endpointFunction.setFunction(application.getFunctions().get(0));
        List<EndpointFunction> endpointFunctions = new ArrayList<>();
        endpointFunctions.add(new EndpointFunction(HttpMethod.PUT, ConnectionFormat.JSON, application.getFunctions().get(1)));

        Endpoint endpoint = new Endpoint();
        endpoint.setUrl("http://url.com");
        endpoint.setConnectionType(ConnectionType.SOAP);
        endpoint.setFunctions(null);
        endpoint.setFunctions(new ArrayList<>());
        endpoint.addFunctions(endpointFunctions);
        endpoint.addFunction(endpointFunction);
        application.setEndpoints(new ArrayList<>());
        application.setEndpoints(null);
        List<Endpoint> endpoints = new ArrayList<>();
        endpoints.add(endpoint);
        application.addEndpoints(endpoints);
        application.addEndpoint(new Endpoint("url", ConnectionType.REST, endpointFunctions));
    }

}
