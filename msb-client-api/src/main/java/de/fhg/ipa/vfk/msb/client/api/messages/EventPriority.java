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

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The Enum EventPriority.
 *
 * @author des
 */
@JsonFormat(shape= JsonFormat.Shape.NUMBER_INT)
public enum EventPriority {
	
	/** The low. */
	LOW(0),
	
	/** The medium. */
	MEDIUM(1),
	
	/** The high. */
	HIGH(2);
	
	public static final EventPriority DEFAULT = EventPriority.LOW;
	
	/** The value. */
	private int value;

	/**
	 * Instantiates a new event priority.
	 *
	 * @param value the value
	 */
	EventPriority(Integer value) {
		this.value = value;
	}

	/**
	 * Parses the.
	 *
	 * @param value the value
	 * @return the event priority
	 */
	public static EventPriority parse(Integer value) {
		switch (value) {
		case 0:
			return LOW;
		case 1:
			return MEDIUM;
		case 2:
			return HIGH;
		default:
			throw new IllegalArgumentException("Invalid priority, value must between 0 and 2.");
		}
	}
	
	/**
	 * Checks if is event priority.
	 *
	 * @param value the value
	 * @return true, if is event priority
	 */
	public static boolean isEventPriority(int value){
		return value>=LOW.value&&value<=HIGH.value;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return getValue()+"";
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	
}
