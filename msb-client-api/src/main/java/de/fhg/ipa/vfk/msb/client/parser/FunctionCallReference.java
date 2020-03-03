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

import de.fhg.ipa.vfk.msb.client.api.Function;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class FunctionCallReference.
 *
 * @author des
 */
public class FunctionCallReference {

	private Object functionHandlerInstance;
	private Method method;
	private Map<String, Type> parameters = new LinkedHashMap<>();
	private String dataFormat;
	private Function function;
	private List<String> responseEvents;

	/**
	 * Gets the function handler instance.
	 *
	 * @return the function handler instance
	 */
	public Object getFunctionHandlerInstance() {
		return functionHandlerInstance;
	}

	/**
	 * Sets the function handler instance.
	 *
	 * @param functionHandlerInstance the new function handler instance
	 */
	public void setFunctionHandlerInstance(Object functionHandlerInstance) {
		this.functionHandlerInstance = functionHandlerInstance;
	}

	/**
	 * Gets the method.
	 *
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * Sets the method.
	 *
	 * @param method the new method
	 */
	public void setMethod(Method method) {
		this.method = method;
	}

	/**
	 * Gets the parameters.
	 *
	 * @return the parameters
	 */
	public Map<String, Type> getParameters() {
		return parameters;
	}

	/**
	 * Sets the parameters.
	 *
	 * @param parameters the parameters
	 */
	public void setParameters(Map<String, Type> parameters) {
		this.parameters = parameters;
	}

	/**
	 * Gets the data format.
	 *
	 * @return the data format
	 */
	public String getDataFormat() {
        return dataFormat;
    }

	/**
	 * Sets the data format.
	 *
	 * @param dataFormat the new data format
	 */
	public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

	/**
	 * Gets the function.
	 *
	 * @return the function
	 */
	public Function getFunction() {
		return function;
	}

	/**
	 * Sets the function.
	 *
	 * @param function the new function
	 */
	public void setFunction(Function function) {
		this.function = function;
	}

	/**
	 * Gets response events.
	 *
	 * @return the response events
	 */
	public List<String> getResponseEvents() {
		if(responseEvents == null) {
			return new ArrayList<>();
		} else {
			return new ArrayList<>(this.responseEvents);
		}
	}

	/**
	 * Sets response events.
	 *
	 * @param responseEvents the response events
	 */
	public void setResponseEvents(String[] responseEvents) {
		if(responseEvents == null) {
			this.responseEvents = new ArrayList<>();
		} else {
			this.responseEvents = Arrays.asList(responseEvents);
		}

	}
}
