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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
class FunctionParserTest {

    private static final Logger LOG = LoggerFactory.getLogger(FunctionParserTest.class);

    private final ObjectMapper mapper = DataFormatParser.getObjectMapper();

    /**
     * Test parse function by package scan.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    void testParseFunctionByPackageScan() throws JsonProcessingException {
        LOG.info("testParseFunctionByPackageScan");
        Map<String, FunctionCallReference> functionCallbackMap = new HashMap<>();
        Map<String, EventReference> eventMap = new HashMap<>();
        eventMap.put("uuid_START",EventParser.getEventReference("START","start","start description",Void.class, EventPriority.DEFAULT));
        eventMap.put("uuid_WAIT",EventParser.getEventReference("WAIT","wait","wait description",Void.class, EventPriority.DEFAULT));
        List<Function> functions = FunctionParser.parseFunctionHandlers("uuid", "de.fhg.ipa.vfk.msb.client.parser",functionCallbackMap,eventMap);

        Assertions.assertEquals(4, functions.size(), "functions size not equals");
        Assertions.assertEquals(4, functionCallbackMap.size(), "function call references size not equals");

        FunctionCallReference functionCallReference = functionCallbackMap.get("uuid_/FunctionParserTest/print");
        Assertions.assertNotNull(functionCallReference, "function call reference is null");
        Assertions.assertNotNull(functionCallReference.getFunctionHandlerInstance(), "function handler instance of function call reference is null");
        Assertions.assertNotNull(functionCallReference.getMethod(), "method of function call reference is null");
        Assertions.assertNotNull(functionCallReference.getParameters(), "parameters of function call reference is null");
        Assertions.assertEquals(0, functionCallReference.getParameters().size(), "parameters of function call reference is not empty");

        Function function = functionCallReference.getFunction();
        Assertions.assertNotNull(function, "function of function call reference is null");
        Assertions.assertTrue(functions.contains(function), "functions not contains function");
        Assertions.assertEquals("/FunctionParserTest/print",function.getFunctionId(), "functionId not equals");
        Assertions.assertEquals("print",function.getName(), "name not equals");
        Assertions.assertEquals("",function.getDescription(), "description not equals");
        Assertions.assertNull(function.getDataFormat(), "data format is not null");
        Assertions.assertNull(function.getResponseEvents(), "response events is not null");

        FunctionCallReference functionCallReference2 = functionCallbackMap.get("uuid_/FunctionParserTest/printInt");
        Assertions.assertNotNull(functionCallReference2, "function call reference is null");
        Assertions.assertNotNull(functionCallReference2.getFunctionHandlerInstance(), "function handler instance of function call reference is null");
        Assertions.assertNotNull(functionCallReference2.getMethod(), "method of function call reference is null");
        Assertions.assertNotNull(functionCallReference2.getParameters(), "parameters of function call reference is null");
        Assertions.assertEquals(3, functionCallReference2.getParameters().size(), "parameters of function call reference is not empty");

        Function function2 = functionCallReference2.getFunction();
        Assertions.assertNotNull(function2, "function of function call reference is null");
        Assertions.assertTrue(functions.contains(function2), "functions not contains function");
        Assertions.assertEquals("/FunctionParserTest/printInt",function2.getFunctionId(), "functionId not equals");
        Assertions.assertEquals("printInteger",function2.getName(), "name not equals");
        Assertions.assertEquals("test with different response",function2.getDescription(), "description not equals");
        Assertions.assertNotNull(function2.getDataFormat(), "data format is null");
        Assertions.assertEquals("{\"int\":{\"type\":\"integer\",\"format\":\"int32\"},\"arg1\":{\"type\":\"string\"},\"arg2\":{\"type\":\"string\"}}",mapper.writeValueAsString(function2.getDataFormat()));
        Assertions.assertNotNull(function2.getResponseEvents(), "response events is null");
        Assertions.assertEquals(2, function2.getResponseEvents().length, "response events size not equals");

        FunctionCallReference functionCallReference3 = functionCallbackMap.get("uuid_/FunctionParserTest/printFunctions");
        Assertions.assertNotNull(functionCallReference3, "function call reference is null");
        Assertions.assertNotNull(functionCallReference3.getFunctionHandlerInstance(), "function handler instance of function call reference is null");
        Assertions.assertNotNull(functionCallReference3.getMethod(), "method of function call reference is null");
        Assertions.assertNotNull(functionCallReference3.getParameters(), "parameters of function call reference is null");
        Assertions.assertEquals(1, functionCallReference3.getParameters().size(), "parameters of function call reference is not empty");

        Function function3 = functionCallReference3.getFunction();
        Assertions.assertNotNull(function3, "function of function call reference is null");
        Assertions.assertTrue(functions.contains(function3), "functions not contains function");
        Assertions.assertEquals("/FunctionParserTest/printFunctions",function3.getFunctionId(),"functionId not equals");
        Assertions.assertEquals("printFunctions",function3.getName(),"name not equals");
        Assertions.assertEquals("",function3.getDescription(),"description not equals");
        Assertions.assertNotNull(function3.getDataFormat(),"data format is null");
        Assertions.assertEquals("{\"arg0\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}}}",mapper.writeValueAsString(function3.getDataFormat()));
        Assertions.assertNotNull(function3.getResponseEvents(),"response events is null");
    }

    /**
     * Test parse function by instance.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    void testParseFunctionByInstance() throws JsonProcessingException {
        LOG.info("testParseFunctionByInstance");
        Map<String, FunctionCallReference> functionCallbackMap = new HashMap<>();
        Map<String, EventReference> eventMap = new HashMap<>();
        eventMap.put("uuid_START",EventParser.getEventReference("START","start","start description",Void.class, EventPriority.DEFAULT));
        eventMap.put("uuid_WAIT",EventParser.getEventReference("WAIT","wait","wait description",Void.class, EventPriority.DEFAULT));
        List<Function> functions = FunctionParser.parseFunctionHandlers("uuid", new Object[]{this},functionCallbackMap,eventMap);

        Assertions.assertEquals(4, functions.size(), "functions size not equals");
        Assertions.assertEquals(4, functionCallbackMap.size(), "function call references size not equals");

        FunctionCallReference functionCallReference = functionCallbackMap.get("uuid_/FunctionParserTest/print");
        Assertions.assertNotNull(functionCallReference, "function call reference is null");
        Assertions.assertNotNull(functionCallReference.getFunctionHandlerInstance(), "function handler instance of function call reference is null");
        Assertions.assertNotNull(functionCallReference.getMethod(), "method of function call reference is null");
        Assertions.assertNotNull(functionCallReference.getParameters(), "parameters of function call reference is null");
        Assertions.assertEquals(0, functionCallReference.getParameters().size(), "parameters of function call reference is not empty");

        Function function = functionCallReference.getFunction();
        Assertions.assertNotNull(function, "function of function call reference is null");
        Assertions.assertTrue(functions.contains(function), "functions not contains function");
        Assertions.assertEquals("/FunctionParserTest/print",function.getFunctionId(), "functionId not equals");
        Assertions.assertEquals("print",function.getName(), "name not equals");
        Assertions.assertEquals("",function.getDescription(), "description not equals");
        Assertions.assertNull(function.getDataFormat(), "data format is not null");
        Assertions.assertNull(function.getResponseEvents(), "response events is not null");

        FunctionCallReference functionCallReference2 = functionCallbackMap.get("uuid_/FunctionParserTest/printInt");
        Assertions.assertNotNull(functionCallReference2, "function call reference is null");
        Assertions.assertNotNull(functionCallReference2.getFunctionHandlerInstance(), "function handler instance of function call reference is null");
        Assertions.assertNotNull(functionCallReference2.getMethod(), "method of function call reference is null");
        Assertions.assertNotNull(functionCallReference2.getParameters(), "parameters of function call reference is null");
        Assertions.assertEquals(3, functionCallReference2.getParameters().size(), "parameters of function call reference is not empty");

        Function function2 = functionCallReference2.getFunction();
        Assertions.assertNotNull(function2, "function of function call reference is null");
        Assertions.assertTrue(functions.contains(function2), "functions not contains function");
        Assertions.assertEquals("/FunctionParserTest/printInt",function2.getFunctionId(), "functionId not equals");
        Assertions.assertEquals("printInteger",function2.getName(), "name not equals");
        Assertions.assertEquals("test with different response",function2.getDescription(), "description not equals");
        Assertions.assertNotNull(function2.getDataFormat(), "data format is null");
        Assertions.assertEquals("{\"int\":{\"type\":\"integer\",\"format\":\"int32\"},\"arg1\":{\"type\":\"string\"},\"arg2\":{\"type\":\"string\"}}",mapper.writeValueAsString(function2.getDataFormat()));
        Assertions.assertNotNull(function2.getResponseEvents(), "response events is null");
        Assertions.assertEquals(2, function2.getResponseEvents().length, "response events size not equals");

        FunctionCallReference functionCallReference3 = functionCallbackMap.get("uuid_/FunctionParserTest/printFunctions");
        Assertions.assertNotNull(functionCallReference3, "function call reference is null");
        Assertions.assertNotNull(functionCallReference3.getFunctionHandlerInstance(), "function handler instance of function call reference is null");
        Assertions.assertNotNull(functionCallReference3.getMethod(), "method of function call reference is null");
        Assertions.assertNotNull(functionCallReference3.getParameters(), "parameters of function call reference is null");
        Assertions.assertEquals(1, functionCallReference3.getParameters().size(), "parameters of function call reference is not empty");

        Function function3 = functionCallReference3.getFunction();
        Assertions.assertNotNull(function3, "function of function call reference is null");
        Assertions.assertTrue(functions.contains(function3), "functions not contains function");
        Assertions.assertEquals("/FunctionParserTest/printFunctions",function3.getFunctionId(),"functionId not equals");
        Assertions.assertEquals("printFunctions",function3.getName(),"name not equals");
        Assertions.assertEquals("",function3.getDescription(),"description not equals");
        Assertions.assertNotNull(function3.getDataFormat(),"data format is null");
        Assertions.assertEquals("{\"arg0\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}}}",mapper.writeValueAsString(function3.getDataFormat()));
        Assertions.assertNotNull(function3.getResponseEvents(),"response events is null");
    }

    /**
     * Test parse function with missing response event.
     */
    @Test
    void testParseFunctionWithMissingResponseEvent() {
        Map<String, FunctionCallReference> functionCallbackMap = new HashMap<>();
        Map<String, EventReference> eventMap = new HashMap<>();
        eventMap.put("START",EventParser.getEventReference("START","start","start description",Void.class, EventPriority.DEFAULT));
        Assertions.assertThrows(IllegalStateException.class, ()->FunctionParser.parseFunctionHandlers("uuid", "de.fhg.ipa.vfk.msb.client.parser",functionCallbackMap,eventMap));
    }

    @Test
    void testParseFunctionWithDuplicatePath() {
        Map<String, FunctionCallReference> functionCallbackMap = new HashMap<>();
        Map<String, EventReference> eventMap = new HashMap<>();
        Assertions.assertThrows(IllegalStateException.class, ()->FunctionParser.parseFunctionHandlers("uuid", "de.fhg.ipa.vfk.msb.client.failing.entities",functionCallbackMap,eventMap));
    }

    @Test
    void testParseFunctionEmptyPath() {
        LOG.info("testParseFunctionByInstance");
        List<Function> functions = FunctionParser.parseFunctionHandlers("uuid", "com.empty", new HashMap<>(), new HashMap<>());
        Assertions.assertTrue(functions.isEmpty());
    }

    @Test
    void testParseFunctionByInstanceAndPackageScan() throws JsonProcessingException {
        LOG.info("testParseFunctionByInstanceAndPackageScan");
        Map<String, FunctionCallReference> functionCallbackMap = new HashMap<>();
        Map<String, EventReference> eventMap = new HashMap<>();
        eventMap.put("uuid_START",EventParser.getEventReference("START","start","start description",Void.class, EventPriority.DEFAULT));
        eventMap.put("uuid_WAIT",EventParser.getEventReference("WAIT","wait","wait description",Void.class, EventPriority.DEFAULT));
        List<Function> functions = FunctionParser.parseFunctionHandlers("uuid","de.fhg.ipa.vfk.msb.client.parser.entities",new Object[]{this},functionCallbackMap,eventMap);

        Assertions.assertEquals(4, functions.size(), "functions size not equals");
        Assertions.assertEquals( 4, functionCallbackMap.size(), "function call references size not equals");
    }

    @Test
    void testParseFunctionByClass() throws JsonProcessingException {
        LOG.info("testParseFunctionByClass");
        Map<String, FunctionCallReference> functionCallbackMap = new HashMap<>();
        Map<String, EventReference> eventMap = new HashMap<>();
        eventMap.put("uuid_START", EventParser.getEventReference("START", "start", "start description", Void.class, EventPriority.DEFAULT));
        eventMap.put("uuid_WAIT", EventParser.getEventReference("WAIT", "wait", "wait description", Void.class, EventPriority.DEFAULT));
        List<Function> functions = FunctionParser.parseFunctionHandlers("uuid", new Object[]{FunctionParserTest.class}, functionCallbackMap, eventMap);

        Assertions.assertEquals(4, functions.size(), "functions size not equals");
        Assertions.assertEquals( 4, functionCallbackMap.size(), "function call references size not equals");
    }

    @Test
    void testAddFunction() throws NoSuchMethodException {
        Map<String, EventReference> eventMap = new HashMap<>();
        eventMap.put("uuid_START", EventParser.getEventReference("START", "start", "start description", Void.class, EventPriority.DEFAULT));
        Map<String, Type> callbackParameters = new HashMap<>();
        callbackParameters.put("arg0",String.class);
        Class[] parameterTypesStringMethod = new Class[1];
        parameterTypesStringMethod[0] = String.class;
        Method stringMethod = FunctionParserTest.class.getMethod("print", parameterTypesStringMethod);

        FunctionCallReference functionCallReference = FunctionParser.addFunction("uuid", "printString", "printString", "print a string",
                this, stringMethod, callbackParameters, new String[]{"START"},eventMap);

        Assertions.assertNotNull(functionCallReference,"response events of function call reference is null");
        Assertions.assertEquals(stringMethod, functionCallReference.getMethod(),"method of function call reference not equals");
        Assertions.assertEquals(this, functionCallReference.getFunctionHandlerInstance(),"functionHandler class of function call reference not equals");
        Assertions.assertEquals("{\"arg0\":{\"type\":\"string\"}}", functionCallReference.getDataFormat(),"dataFormat of function call reference not equals");
        Assertions.assertNotNull(functionCallReference.getParameters(),"parameters of function call reference is null");
        Assertions.assertEquals(1, functionCallReference.getParameters().size(),"parameters of function call reference is empty");
        Assertions.assertNotNull(functionCallReference.getResponseEvents(), "response events of function call reference is null");
        Assertions.assertEquals(1, functionCallReference.getResponseEvents().size(), "response events of function call reference is empty");

        Function function = functionCallReference.getFunction();
        Assertions.assertNotNull(function, "function of function call reference is null");
        Assertions.assertEquals("printString",function.getFunctionId(), "functionId not equals");
        Assertions.assertEquals("printString",function.getName(), "name not equals");
        Assertions.assertEquals("print a string",function.getDescription(), "description not equals");
        Assertions.assertNotNull(function.getDataFormat(), "data format is null");
        Assertions.assertNotNull(function.getResponseEvents(), "response events is null");
    }

    @Test
    void testAddFunctionWithoutMethod() {
        Map<String, Type> callbackParameters = new HashMap<>();
        callbackParameters.put("arg0",String.class);
        FunctionCallReference functionCallReference = FunctionParser.addFunction("uuid", "printString", "printString", "print a string",
                this, null, callbackParameters, new String[0], new HashMap<String, EventReference>());

        Assertions.assertNotNull(functionCallReference,"response events of function call reference is null");
        Assertions.assertNull(functionCallReference.getMethod(), "method of function call reference not null");
        Assertions.assertEquals(this, functionCallReference.getFunctionHandlerInstance(),"functionHandler class of function call reference not equals");
        Assertions.assertEquals("{\"arg0\":{\"type\":\"string\"}}", functionCallReference.getDataFormat(),"dataFormat of function call reference not equals");
        Assertions.assertNotNull(functionCallReference.getParameters(),"parameters of function call reference is null");
        Assertions.assertEquals(1, functionCallReference.getParameters().size(),"parameters of function call reference is empty");
        Assertions.assertNotNull(functionCallReference.getResponseEvents(),"response events of function call reference is null");
        Assertions.assertTrue(functionCallReference.getResponseEvents().isEmpty(), "response events of function call reference is empty");
    }

    @Test
    void testAddFunctionWitNullResponseEvents() {
        Map<String, Type> callbackParameters = new HashMap<>();
        callbackParameters.put("arg0",String.class);
        FunctionCallReference functionCallReference = FunctionParser.addFunction("uuid", "printString", "printString", "print a string",
                this, null, callbackParameters, null, new HashMap<String, EventReference>());

        Assertions.assertNotNull(functionCallReference,"response events of function call reference is null");
        Assertions.assertNull(functionCallReference.getMethod(), "method of function call reference not null");
        Assertions.assertEquals(this, functionCallReference.getFunctionHandlerInstance(),"functionHandler class of function call reference not equals");
        Assertions.assertEquals("{\"arg0\":{\"type\":\"string\"}}", functionCallReference.getDataFormat(),"dataFormat of function call reference not equals");
        Assertions.assertNotNull(functionCallReference.getParameters(),"parameters of function call reference is null");
        Assertions.assertEquals(1, functionCallReference.getParameters().size(),"parameters of function call reference is empty");
        Assertions.assertNotNull(functionCallReference.getResponseEvents(),"response events of function call reference is null");
        Assertions.assertTrue(functionCallReference.getResponseEvents().isEmpty(), "response events of function call reference is empty");
    }

    @Test
    void testAddFunctionWithNotExistingResponseEvent() {
        Map<String, Type> callbackParameters = new HashMap<>();
        callbackParameters.put("arg0",String.class);
        Assertions.assertThrows(IllegalStateException.class, ()-> {
            FunctionCallReference functionCallReference = FunctionParser.addFunction("uuid", "printString", "printString", "print a string",
                    this, null, callbackParameters, new String[]{"Not_Existing_Event"}, new HashMap<>());

            Assertions.assertNotNull(functionCallReference,"response events of function call reference is null");
            Assertions.assertNull(functionCallReference.getMethod(), "method of function call reference not null");
            Assertions.assertEquals(this, functionCallReference.getFunctionHandlerInstance(),"functionHandler class of function call reference not equals");
            Assertions.assertEquals("{\"arg0\":{\"type\":\"string\"}}", functionCallReference.getDataFormat(),"dataFormat of function call reference not equals");
            Assertions.assertNotNull(functionCallReference.getParameters(),"parameters of function call reference is null");
            Assertions.assertEquals(1, functionCallReference.getParameters().size(),"parameters of function call reference is empty");
            Assertions.assertNotNull(functionCallReference.getResponseEvents(),"response events of function call reference is null");
            Assertions.assertTrue(functionCallReference.getResponseEvents().isEmpty(), "response events of function call reference is empty");
        });
    }

    @Test
    void testEmpty() {
        FunctionCallReference functionCallReference = FunctionParser.addFunction("uuid", "printString", "printString", "print a string",
                null, null, null, null, new HashMap<>());

        Assertions.assertNotNull(functionCallReference,"response events of function call reference is null");
        Assertions.assertNull(functionCallReference.getMethod(), "method of function call reference not null");
        Assertions.assertNull(functionCallReference.getFunctionHandlerInstance(),"functionHandler class of function call reference not equals");
        Assertions.assertEquals("{}", functionCallReference.getDataFormat(),"dataFormat of function call reference not equals");
        Assertions.assertNotNull(functionCallReference.getParameters(),"parameters of function call reference is null");
        Assertions.assertTrue(functionCallReference.getParameters().isEmpty(), "parameters of function call reference is empty");
        Assertions.assertNotNull(functionCallReference.getResponseEvents(),"response events of function call reference is null");
        Assertions.assertTrue(functionCallReference.getResponseEvents().isEmpty(), "response events of function call reference is empty");
    }

    @Test
    void testParseFunctionFailsByConstructor() throws JsonProcessingException {
        LOG.info("testParseFunctionByInstance");
        Map<String, FunctionCallReference> functionCallbackMap = new HashMap<>();
        Map<String, EventReference> eventMap = new HashMap<>();
        List<Function> functions = FunctionParser.parseFunctionHandlers("uuid", new Class[]{FailingClazz.class}, functionCallbackMap, eventMap);

        Assertions.assertEquals(0, functions.size(), "functions size not equals");
        Assertions.assertEquals( 0, functionCallbackMap.size(), "function call references size not equals");
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

        FailingClazz(String missingDefaultConstructor){ }

    }
}
