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

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * The Class Event.
 *
 * @author des
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, scope = Event.class)
public class Event {

	/** The event id. */
	private String eventId;

	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The data format. */
	private Map<String,Object> dataFormat;

	/**
	 * Instantiates a new Event.
	 */
	protected Event(){
		//Default constructor
	}

	/**
	 * Instantiates a new Event.
	 *
	 * @param eventId     the event id
	 * @param name        the name
	 * @param description the description
	 */
	public Event(String eventId,String name,String description){

		this.eventId = eventId;
		this.name = name;
		this.description = description;
	}

	/**
	 * Instantiates a new Event.
	 *
	 * @param eventId     the event id
	 * @param name        the name
	 * @param description the description
	 * @param dataFormat  the data format
	 */
	public Event(String eventId,String name,String description,Map<String,Object> dataFormat){
		this(eventId,name,description);
		this.dataFormat = dataFormat;
	}

	/**
	 * Gets the event id.
	 *
	 * @return the event id
	 */
	public String getEventId() {
		return eventId;
	}

	/**
	 * Sets the event id.
	 *
	 * @param eventId the new event id
	 */
	public void setEventId(String eventId) {
		this.eventId = eventId;
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
	 * Gets the data format.
	 *
	 * @return the data format
	 */
	public Map<String,Object> getDataFormat() {
		return dataFormat;
	}

	/**
	 * Sets the data format.
	 *
	 * @param dataFormat the data format
	 */
	public void setDataFormat(Map<String,Object> dataFormat) {
		this.dataFormat = dataFormat;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("{eventId:%1$s, name:%2$s, description:%3$s, dataFormat:%4$s}", eventId, name, description, dataFormat);
	}

}
