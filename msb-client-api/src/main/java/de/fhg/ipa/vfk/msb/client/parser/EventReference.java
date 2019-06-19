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

import de.fhg.ipa.vfk.msb.client.api.Event;
import de.fhg.ipa.vfk.msb.client.api.messages.EventPriority;

import java.lang.reflect.Type;

/**
 * The Class EventReference.
 *
 * @author des
 */
public class EventReference {

	private String name;
	private Type dataType;
	private String dataFormat;
	private Event event;
	private EventPriority priority;

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
	 * Gets the data type.
	 *
	 * @return the data type
	 */
	public Type getDataType() {
		return dataType;
	}

	/**
	 * Sets the data type.
	 *
	 * @param dataType the new data type
	 */
	public void setDataType(Type dataType) {
		this.dataType = dataType;
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
	 * Gets the event.
	 *
	 * @return the event
	 */
	public Event getEvent() {
		return event;
	}

	/**
	 * Sets the event.
	 *
	 * @param event the new event
	 */
	public void setEvent(Event event) {
		this.event = event;
	}

	/**
	 * Gets priority.
	 *
	 * @return the priority
	 */
	public EventPriority getPriority() {
		return priority;
	}

	/**
	 * Sets priority.
	 *
	 * @param priority the priority
	 */
	public void setPriority(EventPriority priority) {
		this.priority = priority;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("{name:%1$s, dataType:%2$s, dataFormat:%3$s, event:%4$s}", name, dataType.toString(), dataFormat, event.toString());
	}

}
