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

import com.fasterxml.jackson.annotation.JsonIdentityReference;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Endpoint.
 *
 * @author des
 */
public class Endpoint {

	/** The url. */
	private String url;

	/** The connection type. */
	private ConnectionType connectionType;

	/** The functions. */
	@JsonIdentityReference(alwaysAsId=true)
	private List<EndpointFunction> functions = new ArrayList<>();

    /**
     * Instantiates a new Endpoint.
     */
    public Endpoint(){
		// empty constructor (p.e. for reflection)
	}

    /**
     * Instantiates a new Endpoint.
     *
     * @param url            the url
     * @param connectionType the connection type
     * @param functions      the functions
     */
    public Endpoint(String url, ConnectionType connectionType, List<EndpointFunction> functions){
		this.url = url;
		this.connectionType = connectionType;
		this.functions.addAll(functions);
	}

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
		return url;
	}

    /**
     * Sets the url.
     *
     * @param url the new url
     */
    public void setUrl(String url) {
		this.url = url;
	}

    /**
     * Gets the connection type.
     *
     * @return the connection type
     */
    public ConnectionType getConnectionType() {
		return connectionType;
	}

    /**
     * Sets the connection type.
     *
     * @param connectionType the new connection type
     */
    public void setConnectionType(ConnectionType connectionType) {
		this.connectionType = connectionType;
	}

    /**
     * Gets the functions.
     *
     * @return the functions
     */
    public List<EndpointFunction> getFunctions() {
		return new ArrayList<>(functions);
	}

    /**
     * Sets the functions.
     *
     * @param functions the new functions
     */
    public void setFunctions(List<EndpointFunction> functions) {
		if (functions != null){
			this.functions = new ArrayList<>(functions);
		} else {
			this.functions = new ArrayList<>();
		}
	}

    /**
     * Add functions.
     *
     * @param functions the functions
     */
    public void addFunctions(List<EndpointFunction> functions){
		if (functions != null) {
			this.functions.addAll(functions);
		}
	}

    /**
     * Add function.
     *
     * @param function the function
     */
    public void addFunction(EndpointFunction function){
		if (function != null) {
			this.functions.add(function);
		}
	}

}
