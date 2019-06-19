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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Class Gateway.
 *
 * @author des
 */
@JsonTypeName("Gateway")
public class Gateway  extends Service {

	private Map<String, Service> services  = new HashMap<>();

    /**
     * Instantiates a new Gateway.
     */
    protected Gateway(){
        //Default constructor
    }

    /**
     * Instantiates a new Gateway.
     *
     * @param uuid        the uuid
     * @param name        the name
     * @param description the description
     * @param token       the token
     */
    public Gateway(String uuid,String name,String description,String token){
		super(uuid,name,description,token);
	}

    /**
     * Instantiates a new Gateway.
     *
     * @param uuid          the uuid
     * @param name          the name
     * @param description   the description
     * @param token         the token
     * @param configuration the configuration
     */
    public Gateway(String uuid,String name,String description,String token, Configuration configuration){
        super(uuid,name,description,token,configuration);
    }

    /**
     * Instantiates a new Gateway.
     *
     * @param uuid        the uuid
     * @param name        the name
     * @param description the description
     * @param token       the token
     * @param services    the services
     */
    public Gateway(String uuid,String name,String description,String token, Set<Service> services){
        this(uuid,name,description,token);
        this.setServices(services);
    }

    /**
     * Instantiates a new Gateway.
     *
     * @param uuid          the uuid
     * @param name          the name
     * @param description   the description
     * @param token         the token
     * @param configuration the configuration
     * @param services      the services
     */
    public Gateway(String uuid,String name,String description,String token,Configuration configuration, Set<Service> services){
        this(uuid,name,description,token,configuration);
        this.setServices(services);
    }

    /**
     * Gets the services.
     *
     * @return the services
     */
    @JsonGetter("services")
    public Set<Service> getServices() {
		return new HashSet<>(services.values());
	}

    /**
     * Add service.
     *
     * @param service the service
     */
    public final void addService(Service service){
        //TODO: add check for duplicate token
        if (services != null) {
            services.put(service.getUuid(), service);
        }
    }

    /**
     * Sets the services.
     *
     * @param services the new services
     */
    @JsonSetter("services")
    protected final void setServices(Set<Service> services) {
        this.services = new HashMap<>();
        for(Service service : services){
            addService(service);
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
