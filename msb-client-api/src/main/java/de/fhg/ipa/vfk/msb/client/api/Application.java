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

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Application.
 *
 * @author des
 */
@JsonTypeName("Application")
public class Application extends Service {
	
	private List<Endpoint> endpoints = new ArrayList<>();

    /**
     * Instantiates a new Application.
     */
    protected Application(){
        //Default constructor
    }

    /**
     * Instantiates a new Application.
     *
     * @param uuid        the uuid
     * @param name        the name
     * @param description the description
     * @param token       the token
     */
    public Application(String uuid, String name, String description, String token){
		super(uuid,name,description,token);
	}

    /**
     * Instantiates a new Application.
     *
     * @param uuid          the uuid
     * @param name          the name
     * @param description   the description
     * @param token         the token
     * @param configuration the configuration
     */
    public Application(String uuid, String name, String description, String token, Configuration configuration){
		super(uuid,name,description,token,configuration);
	}

    /**
     * Instantiates a new Application.
     *
     * @param uuid        the uuid
     * @param name        the name
     * @param description the description
     * @param token       the token
     * @param endpoints   the endpoints
     */
    public Application(String uuid, String name, String description, String token, List<Endpoint> endpoints){
		this(uuid,name,description,token);
		this.endpoints.addAll(endpoints);
	}

    /**
     * Instantiates a new Application.
     *
     * @param uuid          the uuid
     * @param name          the name
     * @param description   the description
     * @param token         the token
     * @param configuration the configuration
     * @param endpoints     the endpoints
     */
    public Application(String uuid, String name, String description, String token, Configuration configuration, List<Endpoint> endpoints){
		this(uuid,name,description,token,configuration);
		this.endpoints.addAll(endpoints);
	}

    /**
     * Gets endpoints.
     *
     * @return the endpoints
     */
    public List<Endpoint> getEndpoints() {
		return new ArrayList<>(endpoints);
	}

    /**
     * Sets endpoints.
     *
     * @param endpoints the endpoints
     */
    public void setEndpoints(List<Endpoint> endpoints) {
        if (endpoints != null){
            this.endpoints = new ArrayList<>(endpoints);
        } else {
            this.endpoints = new ArrayList<>();
        }
	}

    /**
     * Add endpoints.
     *
     * @param endpoints the endpoints
     */
    public void addEndpoints(List<Endpoint> endpoints) {
        if (endpoints != null){
            this.endpoints.addAll(endpoints);
        }
    }

    /**
     * Add endpoint.
     *
     * @param endpoint the endpoint
     */
    public void addEndpoint(Endpoint endpoint) {
        if (endpoint != null){
            this.endpoints.add(endpoint);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
