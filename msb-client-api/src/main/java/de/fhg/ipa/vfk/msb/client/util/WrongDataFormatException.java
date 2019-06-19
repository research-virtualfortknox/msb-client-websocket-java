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

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Class de.fhg.ipa.vfk.msb.client.util.WrongDataFormatException.
 *
 * @author des
 */
@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Wrong data format")
public class WrongDataFormatException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1078812229175518343L;

	/**
	 * Instantiates a new wrong dataformat exception.
	 *
	 * @param message
	 *            the message
	 */
	public WrongDataFormatException(String message){
		super(message);
	}
		
	/**
	 * Instantiates a new wrong dataformat exception.
	 *
	 * @param message
	 *            the message
	 * @param throwable
	 *            the throwable
	 */
	public WrongDataFormatException(String message, Throwable throwable){
		super(message, throwable);
	}
	
}

