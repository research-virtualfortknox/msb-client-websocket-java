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
import de.fhg.ipa.vfk.msb.client.api.AllTypes;
import de.fhg.ipa.vfk.msb.client.api.ComplexType;
import de.fhg.ipa.vfk.msb.client.api.messages.FunctionCallMessage;
import de.fhg.ipa.vfk.msb.client.util.MsbDateFormat;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type Function invoker test.
 *
 * @author des
 */
class FunctionInvokerTest {

    private static Logger LOG = LoggerFactory.getLogger(FunctionInvokerTest.class);

    private Object obj;
    private int number;
    private Long longNumber;
    private boolean bool;
    private String[] array;
    private Date date;
    private byte[] byteArray;
    private BigDecimal bigDecimal;
    private boolean called;
    private ComplexType complexType;

    private FunctionCallReference callback;

    /**
     * Init.
     *
     * @throws NoSuchMethodException the no such method exception
     */
    @BeforeEach
    void init() throws NoSuchMethodException {
        Map<String, Type> functionCallbackParameters = new LinkedHashMap<>();
        functionCallbackParameters.put("obj",Object.class);
        functionCallbackParameters.put("number",int.class);
        functionCallbackParameters.put("longNumber",Long.class);
        functionCallbackParameters.put("bool",Boolean.class);
        functionCallbackParameters.put("array",String[].class);
        functionCallbackParameters.put("date",Date.class);
        functionCallbackParameters.put("byte",byte[].class);
        functionCallbackParameters.put("bigDecimal",BigDecimal.class);
        callback = new FunctionCallReference();
        callback.setFunctionHandlerInstance(this);
        callback.setMethod(this.getClass().getDeclaredMethod("testFunction", Object.class, int.class, Long.class, boolean.class, String[].class, Date.class, byte[].class, BigDecimal.class));
        callback.setParameters(functionCallbackParameters);
    }

    /**
     * Test complex type parameter.
     *
     * @throws NoSuchMethodException     the no such method exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws JsonProcessingException   the json processing exception
     * @throws JSONException             the json exception
     */
    @Test
    void testComplexTypeParameter() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, JsonProcessingException, JSONException {
        Map<String, Type> functionCallbackParameters = new LinkedHashMap<>();
        functionCallbackParameters.put("complexType",ComplexType.class);
        FunctionCallReference callback = new FunctionCallReference();
        callback.setFunctionHandlerInstance(this);
        callback.setMethod(this.getClass().getDeclaredMethod("testFunction", ComplexType.class));
        callback.setParameters(functionCallbackParameters);

        this.complexType = null;

        ComplexType complexType = new ComplexType();
        complexType.setMap(new HashMap<>());
        complexType.getMap().put(1, AllTypes.getTestEntity());
        complexType.getMap().put(2, AllTypes.getTestEntity());
        Map<String,Object> functionParameters = new LinkedHashMap<>();
        functionParameters.put("complexType", complexType);
        FunctionCallMessage functionCall = new FunctionCallMessage("","testFunction",null,functionParameters);
        Object result = FunctionInvoker.callFunctions(functionCall,callback);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ComplexType);
        Assertions.assertEquals(this.complexType, result);
        Assertions.assertNotNull(this.complexType, "is null");
        JSONAssert.assertEquals(new ObjectMapper().writeValueAsString(complexType), new ObjectMapper().writeValueAsString(this.complexType),true);
        LOG.debug(new ObjectMapper().writeValueAsString(this.complexType));
    }

    /**
     * Test call function.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     * @throws ParseException            the parse exception
     */
    @Test
    void testCallFunction() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException, ParseException {
        obj=null;
        number=0;
        longNumber=0L;
        bool=false;
        array=null;
        date = null;
        byteArray = null;
        bigDecimal = null;

        Map<String,Object> functionParameters = new LinkedHashMap<>();
        functionParameters.put("obj",new Object());
        functionParameters.put("number",1);
        functionParameters.put("longNumber",2);
        functionParameters.put("bool",true);
        functionParameters.put("array",new String[]{});
        functionParameters.put("date","2018-03-01T12:48:58.771+01");
        functionParameters.put("byte","test byte string".getBytes());
        functionParameters.put("bigDecimal",BigDecimal.valueOf(1.2));
        FunctionCallMessage functionCall = new FunctionCallMessage("","testFunction",null,functionParameters);
        FunctionInvoker.callFunctions(functionCall,callback);
        Assertions.assertNotNull(obj, "not null");
        Assertions.assertEquals(1,number, "is not 1");
        Assertions.assertEquals(2L, longNumber.longValue(), "is not 2");
        Assertions.assertTrue(bool,"is false");
        Assertions.assertNotNull(array, "not null");
        Assertions.assertEquals(new MsbDateFormat().parse("2018-03-01T12:48:58.771+01"), date, "date not equals");
        Assertions.assertEquals("test byte string",new String(byteArray), "byte array not equals");
        Assertions.assertEquals(BigDecimal.valueOf(1.2), bigDecimal, "bigDecimal not equals");
    }

    /**
     * Test call function with missing some parameters.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     */
    @Test
    void testCallFunctionWithMissingSomeParameters() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        obj=null;
        number=0;
        longNumber=0L;
        bool=false;
        array=null;
        date = null;
        byteArray = null;
        bigDecimal = null;
        Map<String,Object> functionParameters = new LinkedHashMap<>();
        functionParameters.put("number",2);
        FunctionCallMessage functionCall = new FunctionCallMessage("","testFunction",null,functionParameters);
        FunctionInvoker.callFunctions(functionCall,callback);
        Assertions.assertNull(obj, "not null");
        Assertions.assertEquals(2, number, "is not 2");
        Assertions.assertEquals(0L, longNumber.longValue(), "is not 0");
        Assertions.assertFalse(bool,"is true");
        Assertions.assertNull(array, "not null");
        Assertions.assertNull(date, "not null");
        Assertions.assertNull(byteArray, "not null");
        Assertions.assertNull(bigDecimal, "not null");
    }

    /**
     * Test call function with missing all parameters.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     */
    @Test
    void testCallFunctionWithMissingAllParameters() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        obj=null;
        number=0;
        longNumber=0L;
        bool=false;
        array=null;
        date = null;
        byteArray = null;
        bigDecimal = null;
        FunctionCallMessage functionCall = new FunctionCallMessage("","testFunction",null,new HashMap<String, Object>());
        FunctionInvoker.callFunctions(functionCall,callback);
        Assertions.assertNull(obj,"not null");
        Assertions.assertEquals(0,number,"is not 0");
        Assertions.assertEquals(0L, longNumber.longValue(),"is not 0");
        Assertions.assertFalse(bool,"is true");
        Assertions.assertNull(array, "not null");
        Assertions.assertNull(date, "not null");
        Assertions.assertNull(byteArray, "not null");
        Assertions.assertNull(bigDecimal, "not null");
    }

    /**
     * Test fail call function.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     */
    @Test
    void testFailCallFunction() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        obj=null;
        number=0;
        longNumber=0L;
        bool=false;
        array=null;
        date = null;
        byteArray = null;
        Map<String,Object> functionParameters = new LinkedHashMap<>();
        functionParameters.put("number",null);
        functionParameters.put("bool",null);
        FunctionCallMessage functionCall = new FunctionCallMessage("","testFunction",null,functionParameters);
        Assertions.assertThrows(IllegalArgumentException.class, ()->FunctionInvoker.callFunctions(functionCall,callback));
    }

    /**
     * Test fail call function 2.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     */
    @Test
    void testFailCallFunction2() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        FunctionCallMessage functionCall = new FunctionCallMessage("","testFunction",null,new HashMap<String,Object>());
        Assertions.assertThrows(IllegalAccessException.class, ()->FunctionInvoker.callFunctions(functionCall,null));
    }

    /**
     * Test fail call function without parameters.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     * @throws NoSuchMethodException     the no such method exception
     */
    @Test
    void testFailCallFunctionWithoutParameters() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException, NoSuchMethodException {
        called = false;
        FunctionCallMessage functionCall = new FunctionCallMessage("", "testFunction", null, new HashMap<String, Object>());
        FunctionCallReference callback = new FunctionCallReference();
        callback.setFunctionHandlerInstance(this);
        callback.setMethod(this.getClass().getDeclaredMethod("testFunction" ));
        FunctionInvoker.callFunctions(functionCall, callback);
        Assertions.assertTrue(called);
    }

    /**
     * Test fail call function fails.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     */
    @Test
    void testFailCallFunctionFails() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        FunctionCallMessage functionCall = new FunctionCallMessage("", "testFunction", null, new HashMap<String, Object>());
        FunctionCallReference callback = new FunctionCallReference();
        Object result = FunctionInvoker.callFunctions(functionCall, callback);
        Assertions.assertNull(result);
    }

    /**
     * Test fail call function fails cause null parameter.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     * @throws NoSuchMethodException     the no such method exception
     */
    @Test
    void testFailCallFunctionFailsCauseNullParameter() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException, NoSuchMethodException {
        Map<String,Object> functionParameters = new LinkedHashMap<>();
        functionParameters.put("obj",new Object());
        functionParameters.put("number",1);
        functionParameters.put("longNumber",2);
        functionParameters.put("bool",true);
        functionParameters.put("array",new String[]{});
        functionParameters.put("date","2018-03-01T12:48:58.771+01");
        functionParameters.put("byte","test byte string".getBytes());
        functionParameters.put("bigDecimal",BigDecimal.valueOf(1.2));
        FunctionCallMessage functionCall = new FunctionCallMessage("","testFunction",null,functionParameters);
        Map<String, Type> functionCallbackParameters = new LinkedHashMap<>();
        functionCallbackParameters.put("obj",null);
        functionCallbackParameters.put("number",Integer.class);
        functionCallbackParameters.put("longNumber",Long.class);
        functionCallbackParameters.put("bool",Boolean.class);
        functionCallbackParameters.put("array",String[].class);
        functionCallbackParameters.put("date",Date.class);
        functionCallbackParameters.put("byte",byte[].class);
        functionCallbackParameters.put("bigDecimal",BigDecimal.class);
        callback = new FunctionCallReference();
        callback.setFunctionHandlerInstance(this);
        callback.setMethod(this.getClass().getDeclaredMethod("testFunction", Object.class, int.class, Long.class, boolean.class, String[].class, Date.class, byte[].class, BigDecimal.class));
        callback.setParameters(functionCallbackParameters);
        Assertions.assertThrows(IllegalArgumentException.class, ()->FunctionInvoker.callFunctions(functionCall, callback));
    }

    /**
     * Test call function with convertible types.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     * @throws ParseException            the parse exception
     */
    @Test
    void testCallFunctionWithConvertibleTypes() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException, ParseException {
        obj=null;
        number=0;
        longNumber=0L;
        bool=false;
        array=null;
        date = null;
        byteArray = null;
        bigDecimal = null;
        Map<String,Object> functionParameters = new LinkedHashMap<>();
        functionParameters.put("obj",new Object());
        functionParameters.put("number",1.2F);
        functionParameters.put("longNumber",2L);
        functionParameters.put("bool",true);
        functionParameters.put("array",new String[]{});
        functionParameters.put("date","2018-03-01T12:48:58.771+01");
        functionParameters.put("byte","test byte string".getBytes());
        functionParameters.put("bigDecimal",1.2);
        FunctionCallMessage functionCall = new FunctionCallMessage("","testFunction",null,functionParameters);
        FunctionInvoker.callFunctions(functionCall, callback);
        Assertions.assertNotNull(obj,"is null");
        Assertions.assertEquals(1,number,"is not 1");
        Assertions.assertEquals(2L, longNumber.longValue(),"is not 2");
        Assertions.assertTrue(bool,"is false");
        Assertions.assertNotNull(array,"is null");
        Assertions.assertEquals(new MsbDateFormat().parse("2018-03-01T12:48:58.771+01"), date, "date not equals");
        Assertions.assertEquals("test byte string",new String(byteArray), "byte array not equals");
        Assertions.assertEquals(BigDecimal.valueOf(1.2), bigDecimal, "bigDecimal not equals");
    }

    /**
     * Test call function returning null.
     *
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws NoSuchMethodException     the no such method exception
     */
    @Test
    void testCallFunctionReturningNull() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, Type> functionCallbackParameters = new LinkedHashMap<>();
        functionCallbackParameters.put("bool", boolean.class);
        FunctionCallReference callback = new FunctionCallReference();
        callback.setFunctionHandlerInstance(this);
        callback.setMethod(this.getClass().getDeclaredMethod("testFunction", boolean.class));
        callback.setParameters(functionCallbackParameters);

        Map<String,Object> functionParameters = new LinkedHashMap<>();
        functionParameters.put("bool",true);
        FunctionCallMessage functionCall = new FunctionCallMessage("","testFunction",null,functionParameters);
        Object obj = FunctionInvoker.callFunctions(functionCall,callback);
        Assertions.assertNotNull(obj,"is null");
        Assertions.assertTrue((Boolean) obj,"is false");

        functionParameters.put("bool",false);
        functionCall = new FunctionCallMessage("","testFunction",null,functionParameters);
        obj = FunctionInvoker.callFunctions(functionCall,callback);
        Assertions.assertNull(obj,"is not null");
    }

    /**
     * Test function.
     *
     * @param obj        the obj
     * @param number     the number
     * @param longNumber the long number
     * @param bool       the bool
     * @param array      the array
     * @param date       the date
     * @param byteArray  the byte array
     * @param bigDecimal the big decimal
     */
    public void testFunction(Object obj, int number, Long longNumber, boolean bool, String[] array, Date date, byte[] byteArray, BigDecimal bigDecimal){
        this.obj = obj;
        this.number = number;
        this.longNumber = longNumber;
        this.bool = bool;
        this.array = array;
        this.date = date;
        this.byteArray = byteArray;
        this.bigDecimal = bigDecimal;
    }

    /**
     * Test function.
     */
    public void testFunction(){
        this.called = true;
    }

    /**
     * Test function complex type.
     *
     * @param complexType the complex type
     * @return the complex type
     */
    public ComplexType testFunction(ComplexType complexType){
        this.complexType = complexType;
        return complexType;
    }

    /**
     * Test function boolean.
     *
     * @param bool the bool
     * @return the boolean
     */
    public Boolean testFunction(boolean bool){
        if(bool){
            return true;
        }
        return null;
    }

}
