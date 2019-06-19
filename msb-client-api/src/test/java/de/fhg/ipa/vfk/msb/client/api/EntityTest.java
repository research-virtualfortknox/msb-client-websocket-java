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
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EntityTest {

    private static final String SERVICE_UUID = UUID.randomUUID().toString();
    private static final String SERVICE_NAME = "name";
    private static final String SERVICE_DESCRIPTION = "description";
    private static final String SERVICE_TOKEN = "token";

    @Test
    public void testApplicationSetter(){
        Application application = new Application("","","","");
        application.setUuid(SERVICE_UUID);
        application.setName(SERVICE_NAME);
        application.setDescription(SERVICE_DESCRIPTION);
        application.setToken(SERVICE_TOKEN);
        application.setConfiguration(getConfiguration());
        addEvents(application);
        addFunctions(application);
        addEndpoints(application);

        Assert.assertEquals(SERVICE_UUID,application.getUuid());
        Assert.assertEquals(SERVICE_NAME,application.getName());
        Assert.assertEquals(SERVICE_DESCRIPTION,application.getDescription());
        Assert.assertEquals(SERVICE_TOKEN,application.getToken());
        Assert.assertNotNull(application.getConfiguration());
        Assert.assertNotNull(application.getEvents());
        Assert.assertEquals(2,application.getEvents().size());
        Assert.assertNotNull(application.getFunctions());
        Assert.assertEquals(2,application.getFunctions().size());
        Assert.assertNotNull(application.getEndpoints());
        Assert.assertEquals(2,application.getEndpoints().size());
    }

    @Test
    public void testApplicationConstructor(){
        Application application = new Application(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN, new ArrayList<Endpoint>());
        application.setConfiguration(getConfiguration());
        addEvents(application);
        addFunctions(application);
        addEndpoints(application);

        Assert.assertEquals(SERVICE_UUID,application.getUuid());
        Assert.assertEquals(SERVICE_NAME,application.getName());
        Assert.assertEquals(SERVICE_DESCRIPTION,application.getDescription());
        Assert.assertEquals(SERVICE_TOKEN,application.getToken());
        Assert.assertNotNull(application.getConfiguration());
        Assert.assertNotNull(application.getEvents());
        Assert.assertEquals(2,application.getEvents().size());
        Assert.assertNotNull(application.getFunctions());
        Assert.assertEquals(2,application.getFunctions().size());
        Assert.assertNotNull(application.getEndpoints());
        Assert.assertEquals(2,application.getEndpoints().size());
    }

    @Test
    public void testApplicationConstructor2(){
        Application application = new Application(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN,getConfiguration(), new ArrayList<Endpoint>());
        addEvents(application);
        addFunctions(application);
        addEndpoints(application);

        Assert.assertEquals(SERVICE_UUID,application.getUuid());
        Assert.assertEquals(SERVICE_NAME,application.getName());
        Assert.assertEquals(SERVICE_DESCRIPTION,application.getDescription());
        Assert.assertEquals(SERVICE_TOKEN,application.getToken());
        Assert.assertNotNull(application.getConfiguration());
        Assert.assertNotNull(application.getEvents());
        Assert.assertEquals(2,application.getEvents().size());
        Assert.assertNotNull(application.getFunctions());
        Assert.assertEquals(2,application.getFunctions().size());
        Assert.assertNotNull(application.getEndpoints());
        Assert.assertEquals(2,application.getEndpoints().size());
    }

    @Test
    public void testSmartObjectSetter(){
        SmartObject smartObject = new SmartObject("","","","");
        smartObject.setUuid(SERVICE_UUID);
        smartObject.setName(SERVICE_NAME);
        smartObject.setDescription(SERVICE_DESCRIPTION);
        smartObject.setToken(SERVICE_TOKEN);
        smartObject.setConfiguration(getConfiguration());
        addEvents(smartObject);
        addFunctions(smartObject);

        Assert.assertEquals(SERVICE_UUID,smartObject.getUuid());
        Assert.assertEquals(SERVICE_NAME,smartObject.getName());
        Assert.assertEquals(SERVICE_DESCRIPTION,smartObject.getDescription());
        Assert.assertEquals(SERVICE_TOKEN,smartObject.getToken());
        Assert.assertNotNull(smartObject.getConfiguration());
        Assert.assertNotNull(smartObject.getEvents());
        Assert.assertEquals(2,smartObject.getEvents().size());
        Assert.assertNotNull(smartObject.getFunctions());
        Assert.assertEquals(2,smartObject.getFunctions().size());
    }

    @Test
    public void testSmartObjectConstructor(){
        SmartObject smartObject = new SmartObject(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN);
        smartObject.setConfiguration(getConfiguration());
        addEvents(smartObject);
        addFunctions(smartObject);

        Assert.assertEquals(SERVICE_UUID,smartObject.getUuid());
        Assert.assertEquals(SERVICE_NAME,smartObject.getName());
        Assert.assertEquals(SERVICE_DESCRIPTION,smartObject.getDescription());
        Assert.assertEquals(SERVICE_TOKEN,smartObject.getToken());
        Assert.assertNotNull(smartObject.getConfiguration());
        Assert.assertNotNull(smartObject.getEvents());
        Assert.assertEquals(2,smartObject.getEvents().size());
        Assert.assertNotNull(smartObject.getFunctions());
        Assert.assertEquals(2,smartObject.getFunctions().size());
    }

    @Test
    public void testSmartObjectConstructor2(){
        SmartObject smartObject = new SmartObject(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN,getConfiguration());
        addEvents(smartObject);
        addFunctions(smartObject);

        Assert.assertEquals(SERVICE_UUID,smartObject.getUuid());
        Assert.assertEquals(SERVICE_NAME,smartObject.getName());
        Assert.assertEquals(SERVICE_DESCRIPTION,smartObject.getDescription());
        Assert.assertEquals(SERVICE_TOKEN,smartObject.getToken());
        Assert.assertNotNull(smartObject.getConfiguration());
        Assert.assertNotNull(smartObject.getEvents());
        Assert.assertEquals(2,smartObject.getEvents().size());
        Assert.assertNotNull(smartObject.getFunctions());
        Assert.assertEquals(2,smartObject.getFunctions().size());
    }

    @Test
    public void testGatewaySetter(){
        Gateway gateway = new Gateway("","","","");
        gateway.setUuid(SERVICE_UUID);
        gateway.setName(SERVICE_NAME);
        gateway.setDescription(SERVICE_DESCRIPTION);
        gateway.setToken(SERVICE_TOKEN);
        gateway.setConfiguration(getConfiguration());
        addEvents(gateway);
        addFunctions(gateway);

        Assert.assertEquals(SERVICE_UUID,gateway.getUuid());
        Assert.assertEquals(SERVICE_NAME,gateway.getName());
        Assert.assertEquals(SERVICE_DESCRIPTION,gateway.getDescription());
        Assert.assertEquals(SERVICE_TOKEN,gateway.getToken());
        Assert.assertNotNull(gateway.getConfiguration());
        Assert.assertNotNull(gateway.getEvents());
        Assert.assertEquals(2,gateway.getEvents().size());
        Assert.assertNotNull(gateway.getFunctions());
        Assert.assertEquals(2,gateway.getFunctions().size());
    }

    @Test
    public void testGatewayConstructor(){
        Gateway gateway = new Gateway(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN, new HashSet<Service>());
        gateway.setConfiguration(getConfiguration());
        addEvents(gateway);
        addFunctions(gateway);
        gateway.addService(new SmartObject("uuid","name","description","token"));

        Assert.assertEquals(SERVICE_UUID,gateway.getUuid());
        Assert.assertEquals(SERVICE_NAME,gateway.getName());
        Assert.assertEquals(SERVICE_DESCRIPTION,gateway.getDescription());
        Assert.assertEquals(SERVICE_TOKEN,gateway.getToken());
        Assert.assertNotNull(gateway.getConfiguration());
        Assert.assertNotNull(gateway.getEvents());
        Assert.assertEquals(2,gateway.getEvents().size());
        Assert.assertNotNull(gateway.getFunctions());
        Assert.assertEquals(2,gateway.getFunctions().size());
        Assert.assertNotNull(gateway.getServices());
        Assert.assertEquals(1,gateway.getServices().size());
    }

    @Test
    public void testGatewayConstructor2(){
        Gateway gateway = new Gateway(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN,getConfiguration(), new HashSet<Service>());
        addEvents(gateway);
        addFunctions(gateway);
        Set<Service> services = new HashSet<>();
        services.add(new SmartObject("uuid","name","description","token"));
        services.add(new SmartObject("uuid1","name1","description1","token1"));
        gateway.setServices(services);

        Assert.assertEquals(SERVICE_UUID,gateway.getUuid());
        Assert.assertEquals(SERVICE_NAME,gateway.getName());
        Assert.assertEquals(SERVICE_DESCRIPTION,gateway.getDescription());
        Assert.assertEquals(SERVICE_TOKEN,gateway.getToken());
        Assert.assertNotNull(gateway.getConfiguration());
        Assert.assertNotNull(gateway.getEvents());
        Assert.assertEquals(2,gateway.getEvents().size());
        Assert.assertNotNull(gateway.getFunctions());
        Assert.assertEquals(2,gateway.getFunctions().size());
        Assert.assertNotNull(gateway.getServices());
        Assert.assertEquals(2,gateway.getServices().size());
    }

    @Test
    public void testServiceEquals(){
        SmartObject smartObject = new SmartObject(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN);
        Gateway gateway = new Gateway(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN);
        Assert.assertNotEquals(smartObject,gateway);
        Assert.assertEquals(smartObject.hashCode(),gateway.hashCode());
    }

    @Test
    public void testServiceEqualsSame(){
        Application application = new Application(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN);
        Assert.assertEquals(application,application);
        Assert.assertEquals(application.hashCode(),application.hashCode());
    }

    @Test
    public void testServiceEqualsNull(){
        Gateway gateway = new Gateway(SERVICE_UUID,SERVICE_NAME,SERVICE_DESCRIPTION,SERVICE_TOKEN);
        Assert.assertNotEquals(gateway,null);
    }

    private Configuration getConfiguration(){
        ParameterValue parameterValue = new ParameterValue();
        parameterValue.setFormat(PrimitiveFormat.BYTE);
        parameterValue.setType(PrimitiveType.STRING);
        parameterValue.setValue("test");
        ParameterValue parameterValue1 = new ParameterValue("value");
        Configuration configuration = new Configuration();
        configuration.setParameters(new HashMap<String, ParameterValue>());
        configuration.addParameter("param",parameterValue);
        configuration.addParameter("param1",parameterValue1);
        configuration.addParameter("param2",new ParameterValue("value", PrimitiveType.NUMBER, PrimitiveFormat.FLOAT));
        configuration.addParameter("param3",new ParameterValue("value", PrimitiveType.INTEGER, PrimitiveFormat.INT32));
        configuration.addParameter("param4",new ParameterValue("value", PrimitiveType.ARRAY));
        return configuration;
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
        endpoint.setFunctions(new ArrayList<EndpointFunction>());
        endpoint.addFunctions(endpointFunctions);
        endpoint.addFunction(endpointFunction);
        application.setEndpoints(new ArrayList<Endpoint>());
        application.setEndpoints(null);
        List<Endpoint> endpoints = new ArrayList<>();
        endpoints.add(endpoint);
        application.addEndpoints(endpoints);
        application.addEndpoint(new Endpoint("url", ConnectionType.REST, endpointFunctions));
    }

}
