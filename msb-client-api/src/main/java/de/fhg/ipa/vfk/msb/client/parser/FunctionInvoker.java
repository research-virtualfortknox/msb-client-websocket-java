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

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhg.ipa.vfk.msb.client.api.messages.FunctionCallMessage;
import de.fhg.ipa.vfk.msb.client.util.DataFormatParser;
import de.fhg.ipa.vfk.msb.client.util.NarrowingNumberConversion;
import de.fhg.ipa.vfk.msb.client.util.WrapperTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The Class FunctionInvoker.
 *
 * @author des
 */
public final class FunctionInvoker {

	private static final Logger LOG = LoggerFactory.getLogger(FunctionInvoker.class);

	private static ObjectMapper mapper = DataFormatParser.getObjectMapper();

	private FunctionInvoker() {}

	/**
	 * Call functions.
	 *
	 * @param outData  the out data
	 * @param callback the callback
	 * @return the object
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public static Object callFunctions(FunctionCallMessage outData, FunctionCallReference callback)
			throws IllegalAccessException, InvocationTargetException {
		LOG.debug("call function: {}", outData.getFunctionId());
		if (callback == null) {
			throw new IllegalAccessException("function " + outData.getFunctionId() + " does not exist");
		}
		Object functionHandler = callback.getFunctionHandlerInstance();
		Method method = callback.getMethod();
		if (method != null && functionHandler != null) {
			Map<String, Object> objects = convertToClassType(outData.getFunctionParameters(), callback);
			if (!objects.values().isEmpty()) {
				Object[] objectArray = objects.values().toArray();
				LOG.debug("call {} with parameters {}", outData.getFunctionId(), objectArray);
                return method.invoke(functionHandler, objectArray);
			} else {
				LOG.debug("call {} without parameters", outData.getFunctionId());
                return method.invoke(functionHandler);
			}
		} else {
			LOG.error("method is {} or functionHandler is {}", method, functionHandler);
		}
		return null;
	}
	
	/**
	 * Convert to class type.
	 *
	 * @param parameters
	 *            the parameters
	 * @param callback
	 *            the callback
	 * @return the map
	 */
	private static Map<String, Object> convertToClassType(Map<String, Object> parameters, FunctionCallReference callback) {
		Map<String, Object> objects = new LinkedHashMap<>();
		Map<String, Type> definedParameters = callback.getParameters();
		if((parameters==null || parameters.isEmpty()) && !definedParameters.isEmpty()) {
			LOG.warn("missing parameters");
		}
		fillDefaultParameters(definedParameters,objects);
		if(parameters==null || parameters.isEmpty()) {
			return objects;
		}
		fillParameterizedParameters(parameters,definedParameters,objects);
		return objects;
	}

	private static void fillDefaultParameters(Map<String, Type> definedParameters, Map<String, Object> objects) {
		for (Entry<String, Type> entry : definedParameters.entrySet()) {
			// set default value dependent on parameter type
			if (entry.getValue() instanceof Class<?> && WrapperTypes.isBooleanWrapperType((Class<?>) entry.getValue())) {
				objects.put(entry.getKey(), false);
			} else if (entry.getValue() instanceof Class<?> && WrapperTypes.isNumberWrapperType((Class<?>) entry.getValue())) {
				objects.put(entry.getKey(), NarrowingNumberConversion.convert((Class<?>) entry.getValue(), 0));
			} else {
				objects.put(entry.getKey(), null);
			}
		}
	}

	private static void fillParameterizedParameters(Map<String, Object> parameters, Map<String, Type> definedParameters, Map<String, Object> objects) {
		for (Entry<String,Object> entry : parameters.entrySet()) {
			LOG.debug("name: {} data: {}", entry.getKey(), entry.getValue());
			Type parameterClass = definedParameters.get(entry.getKey());

			// TODO: check if parameter is required
			if (parameterClass == null) {
				throw new IllegalArgumentException("parameter " + entry.getKey() + " does not exist");
			}
			if (parameterClass instanceof Class<?> && (((Class<?>) parameterClass).isPrimitive() || WrapperTypes.isWrapperTypeWithoutConversation((Class<?>) parameterClass))) {
				// TODO: short don't work
				Object entryValue = entry.getValue();
				// required if value is Integer and Long is excepted
				if (!((Class<?>) parameterClass).isInstance(entryValue) && entryValue instanceof Number) {
					entryValue = NarrowingNumberConversion.convert((Class<?>) parameterClass, (Number) entryValue);
				}
				objects.put(entry.getKey(), entryValue);
			} else {
				Object obj = mapper.convertValue(entry.getValue(), mapper.constructType(parameterClass));
				objects.put(entry.getKey(), obj);
			}
		}
	}

}
