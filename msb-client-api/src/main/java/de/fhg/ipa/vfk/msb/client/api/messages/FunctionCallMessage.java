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
package de.fhg.ipa.vfk.msb.client.api.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.models.HttpMethod;

import java.util.Map;

/**
 * The Class OutgoingData.
 *
 * @author des
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FunctionCallMessage extends Message {

	private String functionId;
	private HttpMethod httpMethod;
	private Map<String, Object> functionParameters;

    /**
     * Instantiates a new outgoing data new.
     */
    public FunctionCallMessage() {
		// empty constructor (p.e. for reflection)
	}

    /**
     * Instantiates a new outgoing data new.
     *
     * @param uuid               the uuid
     * @param functionId         the function id
     * @param correlationId      the correlation id
     * @param functionParameters the function parameters
     */
    public FunctionCallMessage(String uuid, String functionId, String correlationId, Map<String, Object> functionParameters) {
        super(uuid,correlationId);
		this.functionId = functionId;
		this.functionParameters = functionParameters;
	}

    /**
     * Instantiates a new outgoing data new.
     *
     * @param uuid               the uuid
     * @param functionId         the function id
     * @param correlationId      the correlation id
     * @param httpMethod         the http method
     * @param functionParameters the function parameters
     */
    public FunctionCallMessage(String uuid, String functionId, String correlationId, HttpMethod httpMethod, Map<String, Object> functionParameters) {
	    this(uuid,functionId,correlationId,functionParameters);
		this.httpMethod = httpMethod;
	}

    /**
     * Gets the function id.
     *
     * @return the function id
     */
    public String getFunctionId() {
		return functionId;
	}

    /**
     * Sets the function id.
     *
     * @param functionId the new function id
     */
    public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

    /**
     * Gets the http method.
     *
     * @return the http method
     */
    public HttpMethod getHttpMethod() {
		return httpMethod;
	}

    /**
     * Sets the http method.
     *
     * @param httpMethod the new http method
     */
    public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

    /**
     * Gets the function parameters.
     *
     * @return the function parameters
     */
    public Map<String, Object> getFunctionParameters() {
		return functionParameters;
	}

    /**
     * Sets the function parameters.
     *
     * @param functionParameters the function parameters
     */
    public void setFunctionParameters(Map<String, Object> functionParameters) {
		this.functionParameters = functionParameters;
	}

    /**
     * To string.
     *
     * @return the string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{\"uuid\":\"" + getUuid() + "\",\"functionId\":\"" + functionId + "\",\"correlationId\":\"" + getCorrelationId() + "\",\"httpMethod\":" + httpMethod
                + "\",\"functionParameters\":" + functionParameters + "}";
    }

}
