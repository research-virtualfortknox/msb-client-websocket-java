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

import java.util.HashMap;
import java.util.Map;

/**
 * The Class Configuration.
 *
 * @author des
 */
public class Configuration {

	/** The parameters. */
	private Map<String,ParameterValue> parameters = new HashMap<>();

	/**
	 * Instantiates a new Configuration.
	 */
	public Configuration(){
		// empty constructor (p.e. for reflection)
	}

	/**
	 * Instantiates a new Configuration.
	 *
	 * @param parameters the parameters
	 */
	public Configuration(Map<String,ParameterValue> parameters){
		this.parameters = parameters;
	}

	/**
	 * Gets the parameters.
	 *
	 * @return the parameters
	 */
	public Map<String,ParameterValue> getParameters() {
		return parameters;
	}

	/**
	 * Sets the parameters.
	 *
	 * @param parameters the parameters
	 */
	public void setParameters(Map<String,ParameterValue> parameters) {
		this.parameters = parameters;
	}

	/**
	 * Add parameter.
	 *
	 * @param parameterName  the parameter name
	 * @param parameterValue the parameter value
	 */
	public void addParameter(String parameterName,ParameterValue parameterValue) {
		this.parameters.put(parameterName,parameterValue);
	}

}
