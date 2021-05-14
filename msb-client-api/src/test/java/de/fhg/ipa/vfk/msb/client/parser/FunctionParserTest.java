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

package de.fhg.ipa.vfk.msb.client.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhg.ipa.vfk.msb.client.annotation.ConfigurationParam;
import de.fhg.ipa.vfk.msb.client.annotation.Events;
import de.fhg.ipa.vfk.msb.client.annotation.FunctionCall;
import de.fhg.ipa.vfk.msb.client.annotation.FunctionHandler;
import de.fhg.ipa.vfk.msb.client.annotation.FunctionParam;
import de.fhg.ipa.vfk.msb.client.api.Function;
import de.fhg.ipa.vfk.msb.client.api.MultipleResponseEvent;
import de.fhg.ipa.vfk.msb.client.util.DataFormatParser;
import de.fhg.ipa.vfk.msb.client.api.messages.EventPriority;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Function parser test.
 *
 * @author des
 */
@FunctionHandler(path="FunctionParserTest")
public class FunctionParserTest {

    private static final Logger LOG = LoggerFactory.getLogger(FunctionParserTest.class);

    private final ObjectMapper mapper = DataFormatParser.getObjectMapper();

    /**
     * Test parse function by package scan.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void testParseFunctionByPackageScan() throws JsonProcessingException {
        LOG.info("testParseFunctionByPackageScan");
        Map<String, FunctionCallReference> functionCallbackMap = new HashMap<>();
        Map<String, EventReference> eventMap = new HashMap<>();
        eventMap.put("uuid_START",EventParser.getEventReference("START","start","start description",Void.class, EventPriority.DEFAULT));
        eventMap.put("uuid_WAIT",EventParser.getEventReference("WAIT","wait","wait description",Void.class, EventPriority.DEFAULT));
        List<Function> functions = FunctionParser.parseFunctionHandlers("uuid", "de.fhg.ipa.vfk.msb.client.parser",functionCallbackMap,eventMap);
        Assert.assertEquals("functions size not equals",4, functions.size());
        Assert.assertEquals("function call references size not equals", 4, functionCallbackMap.size());

        FunctionCallReference functionCallReference = functionCallbackMap.get("uuid_/FunctionParserTest/print");
        Assert.assertNotNull("function call reference is null", functionCallReference);
        Assert.assertNotNull("function handler instance of function call reference is null", functionCallReference.getFunctionHandlerInstance());
        Assert.assertNotNull("method of function call reference is null", functionCallReference.getMethod());
        Assert.assertNotNull("parameters of function call reference is null", functionCallReference.getParameters());
        Assert.assertEquals("parameters of function call reference is not empty",0, functionCallReference.getParameters().size());

        Function function = functionCallReference.getFunction();
        Assert.assertNotNull("function of function call reference is null",function);
        Assert.assertTrue("functions not contains function",functions.contains(function));
        Assert.assertEquals("functionId not equals","/FunctionParserTest/print",function.getFunctionId());
        Assert.assertEquals("name not equals","print",function.getName());
        Assert.assertEquals("description not equals","",function.getDescription());
        Assert.assertNull("data format is not null",function.getDataFormat());
        Assert.assertNull("response events is not null",function.getResponseEvents());

        FunctionCallReference functionCallReference2 = functionCallbackMap.get("uuid_/FunctionParserTest/printInt");
        Assert.assertNotNull("function call reference is null", functionCallReference2);
        Assert.assertNotNull("function handler instance of function call reference is null", functionCallReference2.getFunctionHandlerInstance());
        Assert.assertNotNull("method of function call reference is null", functionCallReference2.getMethod());
        Assert.assertNotNull("parameters of function call reference is null", functionCallReference2.getParameters());
        Assert.assertEquals("parameters of function call reference is empty",3, functionCallReference2.getParameters().size());

        Function function2 = functionCallReference2.getFunction();
        Assert.assertNotNull("function of function call reference is null",function2);
        Assert.assertTrue("functions not contains function",functions.contains(function2));
        Assert.assertEquals("functionId not equals","/FunctionParserTest/printInt",function2.getFunctionId());
        Assert.assertEquals("name not equals","printInteger",function2.getName());
        Assert.assertEquals("description not equals","test with different response",function2.getDescription());
        Assert.assertNotNull("data format is null",function2.getDataFormat());
        Assert.assertEquals("{\"int\":{\"type\":\"integer\",\"format\":\"int32\"},\"arg1\":{\"type\":\"string\"},\"arg2\":{\"type\":\"string\"}}",mapper.writeValueAsString(function2.getDataFormat()));
        Assert.assertNotNull("response events is null",function2.getResponseEvents());
        Assert.assertEquals("response events size not equals",2, function2.getResponseEvents().length);

        FunctionCallReference functionCallReference3 = functionCallbackMap.get("uuid_/FunctionParserTest/printFunctions");
        Assert.assertNotNull("function call reference is null", functionCallReference3);
        Assert.assertNotNull("function handler instance of function call reference is null", functionCallReference3.getFunctionHandlerInstance());
        Assert.assertNotNull("method of function call reference is null", functionCallReference3.getMethod());
        Assert.assertNotNull("parameters of function call reference is null", functionCallReference3.getParameters());
        Assert.assertEquals("parameters of function call reference is empty",1, functionCallReference3.getParameters().size());

        Function function3 = functionCallReference3.getFunction();
        Assert.assertNotNull("function of function call reference is null",function3);
        Assert.assertTrue("functions not contains function",functions.contains(function3));
        Assert.assertEquals("functionId not equals","/FunctionParserTest/printFunctions",function3.getFunctionId());
        Assert.assertEquals("name not equals","printFunctions",function3.getName());
        Assert.assertEquals("description not equals","",function3.getDescription());
        Assert.assertNotNull("data format is null",function3.getDataFormat());
        Assert.assertEquals("{\"arg0\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}}}",mapper.writeValueAsString(function3.getDataFormat()));
        Assert.assertNotNull("response events is null",function3.getResponseEvents());
    }

    /**
     * Test parse function by instance.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void testParseFunctionByInstance() throws JsonProcessingException {
        LOG.info("testParseFunctionByInstance");
        Map<String, FunctionCallReference> functionCallbackMap = new HashMap<>();
        Map<String, EventReference> eventMap = new HashMap<>();
        eventMap.put("uuid_START",EventParser.getEventReference("START","start","start description",Void.class, EventPriority.DEFAULT));
        eventMap.put("uuid_WAIT",EventParser.getEventReference("WAIT","wait","wait description",Void.class, EventPriority.DEFAULT));
        List<Function> functions = FunctionParser.parseFunctionHandlers("uuid", new Object[]{this},functionCallbackMap,eventMap);

        Assert.assertEquals("functions size not equals",4, functions.size());
        Assert.assertEquals("function call references size not equals", 4, functionCallbackMap.size());

        FunctionCallReference functionCallReference = functionCallbackMap.get("uuid_/FunctionParserTest/print");
        Assert.assertNotNull("function call reference is null", functionCallReference);
        Assert.assertNotNull("function handler instance of function call reference is null", functionCallReference.getFunctionHandlerInstance());
        Assert.assertNotNull("method of function call reference is null", functionCallReference.getMethod());
        Assert.assertNotNull("parameters of function call reference is null", functionCallReference.getParameters());
        Assert.assertEquals("parameters of function call reference is not empty",0, functionCallReference.getParameters().size());

        Function function = functionCallReference.getFunction();
        Assert.assertNotNull("function of function call reference is null",function);
        Assert.assertTrue("functions not contains function",functions.contains(function));
        Assert.assertEquals("functionId not equals","/FunctionParserTest/print",function.getFunctionId());
        Assert.assertEquals("name not equals","print",function.getName());
        Assert.assertEquals("description not equals","",function.getDescription());
        Assert.assertNull("data format is not null",function.getDataFormat());
        Assert.assertNull("response events is not null",function.getResponseEvents());

        FunctionCallReference functionCallReference2 = functionCallbackMap.get("uuid_/FunctionParserTest/printInt");
        Assert.assertNotNull("function call reference is null", functionCallReference2);
        Assert.assertNotNull("function handler instance of function call reference is null", functionCallReference2.getFunctionHandlerInstance());
        Assert.assertNotNull("method of function call reference is null", functionCallReference2.getMethod());
        Assert.assertNotNull("parameters of function call reference is null", functionCallReference2.getParameters());
        Assert.assertEquals("parameters of function call reference is empty",3, functionCallReference2.getParameters().size());

        Function function2 = functionCallReference2.getFunction();
        Assert.assertNotNull("function of function call reference is null",function2);
        Assert.assertTrue("functions not contains function",functions.contains(function2));
        Assert.assertEquals("functionId not equals","/FunctionParserTest/printInt",function2.getFunctionId());
        Assert.assertEquals("name not equals","printInteger",function2.getName());
        Assert.assertEquals("description not equals","test with different response",function2.getDescription());
        Assert.assertNotNull("data format is null",function2.getDataFormat());
        Assert.assertEquals("{\"int\":{\"type\":\"integer\",\"format\":\"int32\"},\"arg1\":{\"type\":\"string\"},\"arg2\":{\"type\":\"string\"}}",mapper.writeValueAsString(function2.getDataFormat()));
        Assert.assertNotNull("response events is null",function2.getResponseEvents());
        Assert.assertEquals("response events size not equals",2, function2.getResponseEvents().length);

        FunctionCallReference functionCallReference3 = functionCallbackMap.get("uuid_/FunctionParserTest/printFunctions");
        Assert.assertNotNull("function call reference is null", functionCallReference3);
        Assert.assertNotNull("function handler instance of function call reference is null", functionCallReference3.getFunctionHandlerInstance());
        Assert.assertNotNull("method of function call reference is null", functionCallReference3.getMethod());
        Assert.assertNotNull("parameters of function call reference is null", functionCallReference3.getParameters());
        Assert.assertEquals("parameters of function call reference is empty",1, functionCallReference3.getParameters().size());

        Function function3 = functionCallReference3.getFunction();
        Assert.assertNotNull("function of function call reference is null",function3);
        Assert.assertTrue("functions not contains function",functions.contains(function3));
        Assert.assertEquals("functionId not equals","/FunctionParserTest/printFunctions",function3.getFunctionId());
        Assert.assertEquals("name not equals","printFunctions",function3.getName());
        Assert.assertEquals("description not equals","",function3.getDescription());
        Assert.assertNotNull("data format is null",function3.getDataFormat());
        Assert.assertEquals("{\"arg0\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}}}",mapper.writeValueAsString(function3.getDataFormat()));
        Assert.assertNotNull("response events is null",function3.getResponseEvents());
    }

    /**
     * Test parse function with missing response event.
     */
    @Test(expected = IllegalStateException.class)
    public void testParseFunctionWithMissingResponseEvent() {
        Map<String, FunctionCallReference> functionCallbackMap = new HashMap<>();
        Map<String, EventReference> eventMap = new HashMap<>();
        eventMap.put("START",EventParser.getEventReference("START","start","start description",Void.class, EventPriority.DEFAULT));
        FunctionParser.parseFunctionHandlers("uuid", "de.fhg.ipa.vfk.msb.client.parser",functionCallbackMap,eventMap);
    }

    @Test(expected = IllegalStateException.class)
    public void testParseFunctionWithDuplicatePath() {
        Map<String, FunctionCallReference> functionCallbackMap = new HashMap<>();
        Map<String, EventReference> eventMap = new HashMap<>();
        FunctionParser.parseFunctionHandlers("uuid", "de.fhg.ipa.vfk.msb.client.failing.entities",functionCallbackMap,eventMap);
    }

    @Test
    public void testParseFunctionEmptyPath() {
        LOG.info("testParseFunctionByInstance");
        List<Function> functions = FunctionParser.parseFunctionHandlers("uuid", "com.empty", new HashMap<>(), new HashMap<>());
        Assert.assertTrue(functions.isEmpty());
    }

    @Test
    public void testParseFunctionByInstanceAndPackageScan() throws JsonProcessingException {
        LOG.info("testParseFunctionByInstanceAndPackageScan");
        Map<String, FunctionCallReference> functionCallbackMap = new HashMap<>();
        Map<String, EventReference> eventMap = new HashMap<>();
        eventMap.put("uuid_START",EventParser.getEventReference("START","start","start description",Void.class, EventPriority.DEFAULT));
        eventMap.put("uuid_WAIT",EventParser.getEventReference("WAIT","wait","wait description",Void.class, EventPriority.DEFAULT));
        List<Function> functions = FunctionParser.parseFunctionHandlers("uuid","de.fhg.ipa.vfk.msb.client.parser.entities",new Object[]{this},functionCallbackMap,eventMap);

        Assert.assertEquals("functions size not equals",4, functions.size());
        Assert.assertEquals("function call references size not equals", 4, functionCallbackMap.size());
    }

    @Test
    public void testParseFunctionByClass() throws JsonProcessingException {
        LOG.info("testParseFunctionByClass");
        Map<String, FunctionCallReference> functionCallbackMap = new HashMap<>();
        Map<String, EventReference> eventMap = new HashMap<>();
        eventMap.put("uuid_START", EventParser.getEventReference("START", "start", "start description", Void.class, EventPriority.DEFAULT));
        eventMap.put("uuid_WAIT", EventParser.getEventReference("WAIT", "wait", "wait description", Void.class, EventPriority.DEFAULT));
        List<Function> functions = FunctionParser.parseFunctionHandlers("uuid", new Object[]{FunctionParserTest.class}, functionCallbackMap, eventMap);

        Assert.assertEquals("functions size not equals", 4, functions.size());
        Assert.assertEquals("function call references size not equals", 4, functionCallbackMap.size());
    }

    @Test
    public void testAddFunction() throws NoSuchMethodException {
        Map<String, EventReference> eventMap = new HashMap<>();
        eventMap.put("uuid_START", EventParser.getEventReference("START", "start", "start description", Void.class, EventPriority.DEFAULT));
        Map<String, Type> callbackParameters = new HashMap<>();
        callbackParameters.put("arg0",String.class);
        Class[] parameterTypesStringMethod = new Class[1];
        parameterTypesStringMethod[0] = String.class;
        Method stringMethod = FunctionParserTest.class.getMethod("print", parameterTypesStringMethod);

        FunctionCallReference functionCallReference = FunctionParser.addFunction("uuid", "printString", "printString", "print a string",
                this, stringMethod, callbackParameters, new String[]{"START"},eventMap);

        Assert.assertNotNull("response events of function call reference is null", functionCallReference);
        Assert.assertEquals("method of function call reference not equals", stringMethod, functionCallReference.getMethod());
        Assert.assertEquals("functionHandler class of function call reference not equals", this, functionCallReference.getFunctionHandlerInstance());
        Assert.assertEquals("dataFormat of function call reference not equals", "{\"arg0\":{\"type\":\"string\"}}", functionCallReference.getDataFormat());
        Assert.assertNotNull("parameters of function call reference is null", functionCallReference.getParameters());
        Assert.assertEquals("parameters of function call reference is empty",1, functionCallReference.getParameters().size());
        Assert.assertNotNull("response events of function call reference is null", functionCallReference.getResponseEvents());
        Assert.assertEquals("response events of function call reference is empty",1, functionCallReference.getResponseEvents().size());

        Function function = functionCallReference.getFunction();
        Assert.assertNotNull("function of function call reference is null",function);
        Assert.assertEquals("functionId not equals","printString",function.getFunctionId());
        Assert.assertEquals("name not equals","printString",function.getName());
        Assert.assertEquals("description not equals","print a string",function.getDescription());
        Assert.assertNotNull("data format is null",function.getDataFormat());
        Assert.assertNotNull("response events is null",function.getResponseEvents());
    }

    @Test
    public void testAddFunctionWithoutMethod() {
        Map<String, Type> callbackParameters = new HashMap<>();
        callbackParameters.put("arg0",String.class);
        FunctionCallReference functionCallReference = FunctionParser.addFunction("uuid", "printString", "printString", "print a string",
                this, null, callbackParameters, new String[0], new HashMap<String, EventReference>());

        Assert.assertNotNull("response events of function call reference is null", functionCallReference);
        Assert.assertNull("method of function call reference not null", functionCallReference.getMethod());
        Assert.assertEquals("functionHandler class of function call reference not equals", this, functionCallReference.getFunctionHandlerInstance());
        Assert.assertEquals("dataFormat of function call reference not equals", "{\"arg0\":{\"type\":\"string\"}}", functionCallReference.getDataFormat());
        Assert.assertNotNull("parameters of function call reference is null", functionCallReference.getParameters());
        Assert.assertEquals("parameters of function call reference is empty",1,functionCallReference.getParameters().size());
        Assert.assertNotNull("response events of function call reference is null", functionCallReference.getResponseEvents());
        Assert.assertTrue("response events of function call reference is empty",functionCallReference.getResponseEvents().isEmpty());
    }

    @Test
    public void testAddFunctionWitNullResponseEvents() {
        Map<String, Type> callbackParameters = new HashMap<>();
        callbackParameters.put("arg0",String.class);
        FunctionCallReference functionCallReference = FunctionParser.addFunction("uuid", "printString", "printString", "print a string",
                this, null, callbackParameters, null, new HashMap<String, EventReference>());

        Assert.assertNotNull("response events of function call reference is null", functionCallReference);
        Assert.assertNull("method of function call reference not null", functionCallReference.getMethod());
        Assert.assertEquals("functionHandler class of function call reference not equals", this, functionCallReference.getFunctionHandlerInstance());
        Assert.assertEquals("dataFormat of function call reference not equals", "{\"arg0\":{\"type\":\"string\"}}", functionCallReference.getDataFormat());
        Assert.assertNotNull("parameters of function call reference is null", functionCallReference.getParameters());
        Assert.assertEquals("parameters of function call reference is empty",1,functionCallReference.getParameters().size());
        Assert.assertNotNull("response events of function call reference is null", functionCallReference.getResponseEvents());
        Assert.assertTrue("response events of function call reference is empty",functionCallReference.getResponseEvents().isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddFunctionWithNotExistingResponseEvent() {
        Map<String, Type> callbackParameters = new HashMap<>();
        callbackParameters.put("arg0",String.class);
        FunctionCallReference functionCallReference = FunctionParser.addFunction("uuid", "printString", "printString", "print a string",
                this, null, callbackParameters, new String[]{"Not_Existing_Event"}, new HashMap<>());

        Assert.assertNotNull("response events of function call reference is null", functionCallReference);
        Assert.assertNull("method of function call reference not null", functionCallReference.getMethod());
        Assert.assertEquals("functionHandler class of function call reference not equals", this, functionCallReference.getFunctionHandlerInstance());
        Assert.assertEquals("dataFormat of function call reference not equals", "{\"arg0\":{\"type\":\"string\"}}", functionCallReference.getDataFormat());
        Assert.assertNotNull("parameters of function call reference is null", functionCallReference.getParameters());
        Assert.assertEquals("parameters of function call reference is empty",1,functionCallReference.getParameters().size());
        Assert.assertNotNull("response events of function call reference is null", functionCallReference.getResponseEvents());
        Assert.assertTrue("response events of function call reference is empty",functionCallReference.getResponseEvents().isEmpty());
    }

    @Test
    public void testEmpty() {
        FunctionCallReference functionCallReference = FunctionParser.addFunction("uuid", "printString", "printString", "print a string",
                null, null, null, null, new HashMap<>());

        Assert.assertNotNull("response events of function call reference is null", functionCallReference);
        Assert.assertNull("method of function call reference not null", functionCallReference.getMethod());
        Assert.assertNull("functionHandler class of function call reference not equals", functionCallReference.getFunctionHandlerInstance());
        Assert.assertEquals("dataFormat of function call reference not equals", "{}", functionCallReference.getDataFormat());
        Assert.assertNotNull("parameters of function call reference is null", functionCallReference.getParameters());
        Assert.assertTrue("parameters of function call reference is empty", functionCallReference.getParameters().isEmpty());
        Assert.assertNotNull("response events of function call reference is null", functionCallReference.getResponseEvents());
        Assert.assertTrue("response events of function call reference is empty",functionCallReference.getResponseEvents().isEmpty());
    }

    @Test
    public void testParseFunctionFailsByConstructor() throws JsonProcessingException {
        LOG.info("testParseFunctionByInstance");
        Map<String, FunctionCallReference> functionCallbackMap = new HashMap<>();
        Map<String, EventReference> eventMap = new HashMap<>();
        List<Function> functions = FunctionParser.parseFunctionHandlers("uuid", new Class[]{FailingClazz.class}, functionCallbackMap, eventMap);

        Assert.assertEquals("functions size not equals",0, functions.size());
        Assert.assertEquals("function call references size not equals", 0, functionCallbackMap.size());
    }

    @FunctionCall(path="/print")
    public void print() {
        // required for parsing test
    }

    @FunctionCall(path="printString")
    public void print(String string) {
        // required for parsing test
    }

    /**
     * Print int.
     *
     * @param intParam    the int param
     * @param stringParam the string param
     * @return the multiple response event
     */
    @FunctionCall(path="printInt", name = "printInteger", description = "test with different response", responseEvents={"START","WAIT"})
    public MultipleResponseEvent printInt(@FunctionParam(name = "int") int intParam, @FunctionParam String stringParam, String stringParam2) {
        // required for parsing test
        return new MultipleResponseEvent();
    }

    /**
     * Print functions.
     *
     * @param functions the functions
     */
    @FunctionCall(path="/printFunctions", responseEvents={"WAIT"})
    public String printFunctions(List<String> functions) {
        // required for parsing test
        return null;
    }

    @FunctionHandler(path="FunctionParserTest.FailingClazz")
    public static class FailingClazz {

        public FailingClazz(String missingDefaultConstructor){ }

    }
}
