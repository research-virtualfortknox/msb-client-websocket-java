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
 * The Class Function.
 *
 * @author des
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, scope = Function.class)
public class Function {

	private String functionId;
	private Map<String,Object> dataFormat;
	private String name;
	private String description;
	private Event[] responseEvents;

	/**
	 * Instantiates a new Function.
	 */
	protected Function(){
		//Default constructor
	}

	/**
	 * Instantiates a new Function.
	 *
	 * @param functionId  the function id
	 * @param name        the name
	 * @param description the description
	 */
	public Function(String functionId,String name,String description){
		this.functionId = functionId;
		this.name = name;
		this.description = description;
	}

	/**
	 * Instantiates a new Function.
	 *
	 * @param functionId  the function id
	 * @param name        the name
	 * @param description the description
	 * @param dataFormat  the data format
	 */
	public Function(String functionId,String name,String description,Map<String,Object> dataFormat){
		this(functionId,name,description);
		this.dataFormat = dataFormat;
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
	 * Gets the response events.
	 *
	 * @return the response events
	 */
	public Event[] getResponseEvents() {
		if (responseEvents != null) {
			return responseEvents.clone();
		}
		return null;
	}

	/**
	 * Sets the response events.
	 *
	 * @param responseEvents the new response events
	 */
	public void setResponseEvents(Event[] responseEvents) {
		if (responseEvents != null) {
			this.responseEvents = responseEvents.clone();
		} else {
			this.responseEvents = null;
		}
	}

}
