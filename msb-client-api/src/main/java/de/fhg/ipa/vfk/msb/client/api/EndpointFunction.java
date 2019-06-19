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

import io.swagger.models.HttpMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class EndpointFunction.
 *
 * @author des
 */
public class EndpointFunction {

	private HttpMethod httpMethod;
	private ConnectionFormat connectionFormat;
	private Function function;
	private Map<String, String> statusCodes = new HashMap<>();

    /**
     * Instantiates a new endpoint function.
     */
    public EndpointFunction(){
		// empty constructor (p.e. for reflection)
	}

    /**
     * Instantiates a new endpoint function.
     *
     * @param httpMethod       the http method
     * @param connectionFormat the connection format
     * @param function         the function
     */
    public EndpointFunction(HttpMethod httpMethod, ConnectionFormat connectionFormat, Function function){
		this.httpMethod = httpMethod;
		this.connectionFormat = connectionFormat;
		this.function = function;
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
     * Gets the connection format.
     *
     * @return the connection format
     */
    public ConnectionFormat getConnectionFormat() {
		return connectionFormat;
	}

    /**
     * Sets the connection format.
     *
     * @param connectionFormat the new connection format
     */
    public void setConnectionFormat(ConnectionFormat connectionFormat) {
		this.connectionFormat = connectionFormat;
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
     * Gets status codes.
     *
     * @return the status codes
     */
    public Map<String, String> getStatusCodes() {
		return statusCodes;
	}

    /**
     * Sets status codes.
     *
     * @param statusCodes the status codes
     */
    public void setStatusCodes(Map<String, String> statusCodes) {
		this.statusCodes = statusCodes;
	}

    /**
     * Add status code.
     *
     * @param statusCode the status code
     * @param eventId    the event id
     */
    public void addStatusCode(String statusCode, String eventId){
	    statusCodes.put(statusCode,eventId);
    }

}
