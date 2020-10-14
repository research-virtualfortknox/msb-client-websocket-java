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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * The Class Service.
 *
 * @author des
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes({ @JsonSubTypes.Type(value = SmartObject.class, name = "SmartObject"),
		@JsonSubTypes.Type(value = Application.class, name = "Application"), @JsonSubTypes.Type(value = Gateway.class, name = "Gateway") })
public class Service {

	private String uuid;
	private String name;
	private String description;
	private Configuration configuration;
	private Connection connection;
	private List<Event> events = new ArrayList<>();
	private List<Function> functions = new ArrayList<>();
	private String token;

    /**
     * Instantiates a new Service.
     */
    protected Service(){
		//Default constructor
    }

	/**
	 * Instantiates a new Service.
	 *
	 * @param uuid        the uuid
	 * @param name        the name
	 * @param description the description
	 * @param token       the token
	 */
	protected Service(String uuid,String name,String description,String token){
		this.uuid = uuid;
		this.name = name;
		this.description = description;
		this.token = token;
	}

	/**
	 * Instantiates a new Service.
	 *
	 * @param uuid          the uuid
	 * @param name          the name
	 * @param description   the description
	 * @param token         the token
	 * @param configuration the configuration
	 */
	protected Service(String uuid,String name,String description,String token, Configuration configuration){
		this(uuid,name,description,token);
		this.configuration = configuration;
	}

	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Sets the uuid.
	 *
	 * @param uuid the new uuid
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets configuration.
	 *
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Sets configuration.
	 *
	 * @param configuration the configuration
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Sets the connection.
	 *
	 * @param connection the new connection
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Gets the events.
	 *
	 * @return the events
	 */
	public List<Event> getEvents() {
		return new ArrayList<>(events);
	}

	/**
	 * Sets the events.
	 *
	 * @param events the new events
	 */
	public void setEvents(List<Event> events) {
		if (events != null) {
			this.events = new ArrayList<>(events);
		} else {
			this.events = new ArrayList<>();
		}
	}

	/**
	 * Add events.
	 *
	 * @param events the events
	 */
	public void addEvents(List<Event> events){
        if (events != null) {
            this.events.addAll(events);
        }
    }

	/**
	 * Add event.
	 *
	 * @param event the event
	 */
	public void addEvent(Event event){
        if (event != null) {
            this.events.add(event);
        }
    }

	/**
	 * Gets the functions.
	 *
	 * @return the functions
	 */
	public List<Function> getFunctions() {
		return new ArrayList<>(functions);
	}

	/**
	 * Sets the functions.
	 *
	 * @param functions the new functions
	 */
	public void setFunctions(List<Function> functions) {
		if (functions != null) {
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
	public void addFunctions(List<Function> functions){
        if (functions != null) {
            this.functions.addAll(functions);
        }
    }

	/**
	 * Add function.
	 *
	 * @param function the function
	 */
	public void addFunction(Function function){
		if (function != null) {
			this.functions.add(function);
		}
	}

	/**
	 * Gets the token.
	 *
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the token.
	 *
	 * @param token the new token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Sets type.
	 *
	 * @param className the s
	 */
	@JsonSetter(value = "@class")
	public void setType(String className) {
		if("Service".equals(className) && !this.getClass().getSimpleName().equals(className)){
			throw new IllegalArgumentException("The give type "+ className +" not usable for "+this.getClass().getSimpleName());
		}
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	@JsonGetter(value = "@class")
	public String getType() {
	    return this.getClass().getSimpleName();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() == obj.getClass()) {
			Service service = (Service) obj;
			return uuid != null && uuid.equals(service.getUuid());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return uuid != null ? uuid.hashCode() : 0;
	}

}
