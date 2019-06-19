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
import de.fhg.ipa.vfk.msb.client.annotation.FunctionCall;
import de.fhg.ipa.vfk.msb.client.annotation.FunctionHandler;
import de.fhg.ipa.vfk.msb.client.annotation.FunctionParam;
import de.fhg.ipa.vfk.msb.client.api.Event;
import de.fhg.ipa.vfk.msb.client.api.Function;
import de.fhg.ipa.vfk.msb.client.api.MultipleResponseEvent;
import de.fhg.ipa.vfk.msb.client.util.DataFormatParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The Class FunctionParser.
 *
 * @author des
 */
public final class FunctionParser {

    private static final Logger LOG = LoggerFactory.getLogger(FunctionParser.class);
    private static final String PATH_DELIMITER = "/";

    private FunctionParser() {
    }

    /**
     * Parse function handlers list.
     *
     * @param serviceUuid         the service uuid
     * @param packagePath         the package path
     * @param functionHandlers    the function handlers
     * @param functionCallbackMap the function callback map
     * @param eventMap            the event map
     * @return the list
     * @throws JsonProcessingException the json processing exception
     */
    public static List<Function> parseFunctionHandlers(String serviceUuid, String packagePath, Object[] functionHandlers,
                                                       Map<String, FunctionCallReference> functionCallbackMap,
                                                       Map<String, EventReference> eventMap) throws JsonProcessingException {
        //TODO: filter duplicate function definitions
        List<Function> functionHandlerResult = parseFunctionHandlers(serviceUuid, functionHandlers, functionCallbackMap, eventMap);
        List<Function> packageScanResult = parseFunctionHandlers(serviceUuid, packagePath, functionCallbackMap, eventMap);
        packageScanResult.addAll(functionHandlerResult);
        return packageScanResult;
    }

    /**
     * Parses the functions.
     *
     * @param serviceUuid         the service uuid
     * @param packagePath         the package path
     * @param functionCallbackMap the function callback map
     * @param eventMap            the event map
     * @return the list
     */
    public static List<Function> parseFunctionHandlers(String serviceUuid, String packagePath,
                                                       Map<String, FunctionCallReference> functionCallbackMap,
                                                       Map<String, EventReference> eventMap) {
        try {
            List<Class<?>> functionHandlerClasses = PackageScanner.findMyTypes(packagePath, new Class[]{FunctionHandler.class});
            return parseFunctionHandlers(serviceUuid, functionHandlerClasses, functionCallbackMap, eventMap);
        } catch (ClassNotFoundException | IOException e) {
            LOG.warn(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    /**
     * Parses the function handlers.
     *
     * @param serviceUuid         the service uuid
     * @param functionHandlers    the function handlers
     * @param functionCallbackMap the function callback map
     * @param eventMap            the event map
     * @return the list
     * @throws JsonProcessingException the json processing exception
     */
    public static List<Function> parseFunctionHandlers(String serviceUuid, Object[] functionHandlers,
                                                       Map<String, FunctionCallReference> functionCallbackMap,
                                                       Map<String, EventReference> eventMap) throws JsonProcessingException {
        List<Function> functionList = new ArrayList<>();
        if (functionHandlers != null) {
            for (Object functionHandler : functionHandlers) {
                if (functionHandler instanceof Class) {
                    Class<?> annotatedClass = (Class<?>) functionHandler;
                    try {
                        parseFunctionHandler(serviceUuid, annotatedClass, functionCallbackMap, functionList, eventMap);
                    } catch (InstantiationException | IllegalAccessException e) {
                        LOG.error("Instantiation error, check if a default constructor is defined at " + annotatedClass.getName(), e);
                    }
                } else {
                    parseFunctionHandler(serviceUuid, functionHandler, functionCallbackMap, functionList, eventMap);
                }
            }
        }
        return functionList;
    }

    /**
     * Parses the function handler.
     *
     * @param functionHandlerClass the function handler class
     * @param functionCallbackMap  the function callback map
     * @param functionList         the function list
     * @param eventMap             the event map
     * @return the list
     * @throws JsonProcessingException the json processing exception
     * @throws InstantiationException  the instantiation exception
     * @throws IllegalAccessException  the illegal access exception
     */
    private static List<Function> parseFunctionHandler(String serviceUuid, Class<?> functionHandlerClass,
                                                       Map<String, FunctionCallReference> functionCallbackMap, List<Function> functionList,
                                                       Map<String, EventReference> eventMap)
            throws JsonProcessingException, InstantiationException, IllegalAccessException {
        Object functionHandler = functionHandlerClass.newInstance();
        return parseFunctionHandler(serviceUuid, functionHandler, functionCallbackMap, functionList, eventMap);
    }

    /**
     * Parses the function handler.
     *
     * @param functionHandler     the function handler
     * @param functionCallbackMap the function callback map
     * @param functionList        the function list
     * @param eventMap            the event map
     * @return the list
     * @throws JsonProcessingException the json processing exception
     */
    private static List<Function> parseFunctionHandler(String serviceUuid, Object functionHandler,
                                                       Map<String, FunctionCallReference> functionCallbackMap, List<Function> functionList,
                                                       Map<String, EventReference> eventMap) throws JsonProcessingException {
        Class<?> functionHandlerClass = functionHandler.getClass();
        if (functionHandlerClass.isAnnotationPresent(FunctionHandler.class)) {
            FunctionHandler functionHandlerAnnotation = functionHandlerClass.getAnnotation(FunctionHandler.class);
            String functionHandlerPath = functionHandlerAnnotation.path();

            parseFunctionHandler(serviceUuid, functionHandler, functionHandlerClass, functionHandlerPath, functionCallbackMap,
                    functionList, eventMap);
        }
        return functionList;
    }

    /**
     * Parses the function handlers.
     *
     * @param functionHandlerClasses the function handler classes
     * @param functionCallbackMap    the function callback map
     * @param eventMap               the event map
     * @return the list
     * @throws JsonProcessingException the json processing exception
     */
    private static List<Function> parseFunctionHandlers(String serviceUuid, List<Class<?>> functionHandlerClasses,
                                                        Map<String, FunctionCallReference> functionCallbackMap,
                                                        Map<String, EventReference> eventMap)
            throws JsonProcessingException {
        List<Function> functionList = new ArrayList<>();
        for (Class<?> functionHandlerClass : functionHandlerClasses) {
            if (functionHandlerClass.isAnnotationPresent(FunctionHandler.class)) {
                FunctionHandler functionHandler = functionHandlerClass.getAnnotation(FunctionHandler.class);
                String functionHandlerPath = functionHandler.path();
                Object functionHandlerInstance = null;
                try {
                    functionHandlerInstance = functionHandlerClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    LOG.error("Instantiation error, check if a default constructor is defined at " + functionHandlerClass.getName(), e);
                }
                parseFunctionHandler(serviceUuid, functionHandlerInstance, functionHandlerClass, functionHandlerPath,
                        functionCallbackMap, functionList, eventMap);
            }
        }
        return functionList;
    }

    /**
     * Parses the function handler.
     *
     * @param functionHandlerInstance the function handler instance
     * @param functionHandlerClass    the function handler class
     * @param functionHandlerPath     the function handler path
     * @param functionCallbackMap     the function callback map
     * @param functionList            the function list
     * @param eventMap                the event map
     * @return the list
     * @throws JsonProcessingException the json processing exception
     */
    private static List<Function> parseFunctionHandler(String serviceUuid, Object functionHandlerInstance, Class<?> functionHandlerClass,
                                                       String functionHandlerPath, Map<String, FunctionCallReference> functionCallbackMap,
                                                       List<Function> functionList, Map<String, EventReference> eventMap) throws JsonProcessingException {

        if ("".equals(functionHandlerPath)) {
            functionHandlerPath = functionHandlerClass.getSimpleName();
        }
        if (!functionHandlerPath.startsWith(PATH_DELIMITER)) {
            functionHandlerPath = PATH_DELIMITER + functionHandlerPath;
        }

        Method[] methods = functionHandlerClass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(FunctionCall.class)) {
                if (Modifier.isPublic(method.getModifiers())) {
                    parseMethod(serviceUuid, functionHandlerInstance, method, functionHandlerPath, functionCallbackMap, functionList, eventMap);
                } else {
                    LOG.error("annotated method '{}' at '{}' is not public, so it is not accessible.", method.getName(), functionHandlerInstance.getClass().getName());
                }
            }
        }
        return functionList;
    }

    private static void parseMethod(String serviceUuid, Object functionHandlerInstance, Method method, String functionHandlerPath, Map<String, FunctionCallReference> functionCallbackMap,
                                    List<Function> functionList, Map<String, EventReference> eventMap) throws JsonProcessingException {
        FunctionCall function = method.getAnnotation(FunctionCall.class);

        String functionDescription = function.description();

        String functionPath = function.path();
        if ("".equals(functionPath)) {
            functionPath = method.getName();
        }
        if (!functionPath.startsWith(PATH_DELIMITER)) {
            functionPath = PATH_DELIMITER + functionPath;
        }

        String functionName = function.name();
        if ("".equals(functionName)) {
            functionName = method.getName();
        }
        String functionId = functionHandlerPath + functionPath;
        String[] responseEventsArray = function.responseEvents();

        if (function.responseEvents().length > 1 && !method.getReturnType().equals(MultipleResponseEvent.class)) {
            LOG.error("use '{}' as method return type to publish response events for function call: {}", MultipleResponseEvent.class.getName(), functionPath);
        }
        if (function.responseEvents().length == 1) {
            String responseEvent = function.responseEvents()[0];
            String returnTypeDataFormat = DataFormatParser.getObjectMapper().writeValueAsString(DataFormatParser.parse(method.getReturnType()));
            EventReference eventReference = eventMap.get(serviceUuid + "_" + responseEvent);
            if (eventReference == null) {
                throw new IllegalStateException("Unknown response event : " + responseEvent + " for function: " + functionId);
            }
            if (!returnTypeDataFormat.equals(eventReference.getDataFormat())
                    && !(method.getReturnType().equals(Void.TYPE) && (eventReference.getDataFormat() == null || eventReference.getDataFormat().equals("null")))) {
                LOG.error("give method return type '{}' don't equals registered data format of response event '{}': {}",
                        method.getReturnType(), responseEvent, eventReference.getDataFormat());
            }
        }

        addFunction(serviceUuid, functionCallbackMap, functionList, functionHandlerInstance, method, functionId,
                functionName, functionDescription, responseEventsArray, eventMap);

    }

    /**
     * Adds the function.
     *
     * @param serviceUuid             the service uuid
     * @param functionCallbackMap     the function callback map
     * @param functionList            the function list
     * @param functionHandlerInstance the function handler instance
     * @param method                  the method
     * @param functionId              the function id
     * @param functionName            the function name
     * @param functionDescription     the function description
     * @param responseEventsArray     the response events array
     * @param eventMap                the event map
     * @throws IllegalStateException the illegal state exception
     */
    public static void addFunction(String serviceUuid, Map<String, FunctionCallReference> functionCallbackMap,
                                   List<Function> functionList, Object functionHandlerInstance, Method method, String functionId,
                                   String functionName, String functionDescription, String[] responseEventsArray,
                                   Map<String, EventReference> eventMap) {
        // throw exception for duplicate functionIds
        if (!functionCallbackMap.containsKey(serviceUuid + "_" + functionId)) {
            FunctionCallReference callback = getFunctionCallbackReference(functionHandlerInstance, method);
            callback.setResponseEvents(responseEventsArray);
            functionCallbackMap.put(serviceUuid + "_" + functionId, callback);

            Map<String, Object> map = getFunctionDataFormat(method, callback.getParameters());
            try {
                ObjectMapper mapper = DataFormatParser.getObjectMapper();
                callback.setDataFormat(mapper.writeValueAsString(map));
            } catch (JsonProcessingException e) {
                LOG.error("JsonProcessingException at data format parsing", e);
            }

            Function f = getFunction(serviceUuid, functionId, functionName, functionDescription, map, responseEventsArray, eventMap);
            callback.setFunction(f);
            functionList.add(f);
        } else {
            throw new IllegalStateException("Duplicate function path for: " + functionId);
        }
    }

    /**
     * Adds the function.
     *
     * @param serviceUuid             the service uuid
     * @param functionId              the function id
     * @param name                    the name
     * @param description             the description
     * @param functionHandlerInstance the function handler instance
     * @param method                  the method
     * @param callbackParameters      the callback parameters
     * @param responseEventsArray     the response events array
     * @param eventMap                the event map
     * @return the function callback reference
     */
    public static FunctionCallReference addFunction(String serviceUuid, String functionId, String name, String description,
                                                    Object functionHandlerInstance, Method method, Map<String, Type> callbackParameters,
                                                    String[] responseEventsArray, Map<String, EventReference> eventMap) {
        FunctionCallReference callback = getFunctionCallbackReference(functionHandlerInstance, method);
        callback.setResponseEvents(responseEventsArray);
        Map<String, Object> map;
        if (method != null) {
            map = getFunctionDataFormat(method, callback.getParameters());
        } else {
            callback.setParameters(callbackParameters);
            map = new LinkedHashMap<>();
            for (Entry<String, Type> entry : callbackParameters.entrySet()) {
                Type parameterClass = entry.getValue();
                String parameterName = entry.getKey();
                DataFormatParser.parse(parameterName, parameterClass, map);
            }
        }

        try {
            ObjectMapper mapper = DataFormatParser.getObjectMapper();
            callback.setDataFormat(mapper.writeValueAsString(map));
        } catch (JsonProcessingException e) {
            LOG.error("JsonProcessingException at data format parsing", e);
        }

        Function f = getFunction(serviceUuid, functionId, name, description, map, responseEventsArray, eventMap);
        callback.setFunction(f);

        return callback;
    }

    /**
     * Gets the function callback reference.
     *
     * @param functionHandlerInstance the function handler instance
     * @param method                  the method
     * @return the function callback reference
     */
    private static FunctionCallReference getFunctionCallbackReference(Object functionHandlerInstance,
                                                                      Method method) {
        FunctionCallReference callback = new FunctionCallReference();
        callback.setFunctionHandlerInstance(functionHandlerInstance);
        callback.setMethod(method);
        return callback;
    }

    /**
     * Gets the function data format.
     *
     * @param method             the method
     * @param callbackParameters the callback parameters
     * @return the function data format
     */
    private static Map<String, Object> getFunctionDataFormat(Method method, Map<String, Type> callbackParameters) {
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        Type[] parameterClasses = method.getGenericParameterTypes();

        Map<String, Object> map = new LinkedHashMap<>();

        for (int i = 0; i < parameterClasses.length; i++) {
            Annotation[] paramA = paramAnnotations[i];
            String name = null;
            for (Annotation a : paramA) {
                if (a instanceof FunctionParam) {
                    name = ((FunctionParam) a).name();
                }
            }
            // workaround, with java 8 method parameter names are accessible
            if (name == null || name.isEmpty()) {
                name = "arg" + i;
            }
            Type parameterClass = parameterClasses[i];
            callbackParameters.put(name, parameterClass);
            DataFormatParser.parse(name, parameterClass, map);
        }
        return map;
    }

    /**
     * Gets the function.
     *
     * @param functionId          the function id
     * @param name                the name
     * @param description         the description
     * @param dataFormat          the data format
     * @param responseEventsArray the response events array
     * @param eventMap            the event map
     * @return the function
     */
    private static Function getFunction(String serviceUuid, String functionId, String name, String description,
                                        Map<String, Object> dataFormat, String[] responseEventsArray, Map<String, EventReference> eventMap) {
        LOG.trace("parse function: {}", functionId);

        Function function = new Function(functionId, name, description);
        if (dataFormat.size() > 0) {
            function.setDataFormat(dataFormat);
        }

        if (responseEventsArray != null && responseEventsArray.length > 0) {
            List<Event> responseEvents = new ArrayList<>();
            for (String s : responseEventsArray) {
                EventReference event = eventMap.get(serviceUuid + "_" + s);
                if (event != null) {
                    responseEvents.add(event.getEvent());
                } else {
                    throw new IllegalStateException("Unknown response event : " + s + " for function: " + functionId);
                }
            }
            function.setResponseEvents(responseEvents.toArray(new Event[]{}));
        }
        return function;
    }

}
