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
package de.fhg.ipa.vfk.msb.client.listener;

import de.fhg.ipa.vfk.msb.client.api.messages.ConfigurationMessage;

/**
 * The listener interface for receiving configuration events.
 * The class that is interested in processing a configuration
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addConfigurationListener</code> method. When
 * the configuration event occurs, that object's appropriate
 * method is invoked.
 *
 * @author des
 */
@FunctionalInterface
public interface ConfigurationListener {

	
	/**
	 * Configuration remote changed.
	 *
	 * @param configuration the configuration
	 */
	void configurationRemoteChanged(ConfigurationMessage configuration);
	
}
