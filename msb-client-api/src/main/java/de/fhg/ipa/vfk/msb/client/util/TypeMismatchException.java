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
package de.fhg.ipa.vfk.msb.client.util;

/**
 * The Class TypeMismatchException.
 *
 * @author des
 */
public class TypeMismatchException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6624515002675631896L;

	/** The required type. */
	private final Class<?> requiredType;

	/** The value. */
	private final transient Object value;

	/**
	 * Instantiates a new type mismatch exception.
	 *
	 * @param value
	 *            the value
	 * @param requiredType
	 *            the required type
	 */
	public TypeMismatchException(Object value, Class<?> requiredType) {
		super(String.format("Mismatching type: %s is not from type %s?", value.getClass().getName(),
				requiredType.getName()));
		this.value = value;
		this.requiredType = requiredType;
	}

	/**
	 * Instantiates a new type mismatch exception.
	 *
	 * @param value
	 *            the value
	 * @param requiredType
	 *            the required type
	 * @param cause
	 *            the cause
	 */
	public TypeMismatchException(Object value, Class<?> requiredType, Throwable cause) {
		super(String.format("Mismatching type: %s is not from type %s?", value.getClass().getName(),
				requiredType.getName()), cause);
		this.value = value;
		this.requiredType = requiredType;
	}

	/**
	 * Gets the required type.
	 *
	 * @return the required type
	 */
	public Class<?> getRequiredType() {
		return requiredType;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
}
