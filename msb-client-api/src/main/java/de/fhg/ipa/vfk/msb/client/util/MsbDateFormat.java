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

import java.text.SimpleDateFormat;

/**
 * The Class MsbDateFormat.
 * Is a helper class which extends SimpleDateFormat for ISO8601 conform date formatting/parsing.
 *
 * @author des
 */
public class MsbDateFormat extends SimpleDateFormat {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1765461322902712120L;

	/**
	 * Instantiates a new msb date format.
	 */
	public MsbDateFormat(){
		super("yyyy-MM-dd'T'HH:mm:ss.SSSX");
	}
	
}
