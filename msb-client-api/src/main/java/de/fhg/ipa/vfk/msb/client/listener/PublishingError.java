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

/**
 * The Enum PublishingError.
 *
 * @author des
 */
public enum PublishingError {
	
	/** The nio unauthorized connection. */
	NIO_UNAUTHORIZED_CONNECTION,

	/** The nio event forwarding error. */
	NIO_EVENT_FORWARDING_ERROR,
	
	/** The nio unexpected event forwarding error. */
	NIO_UNEXPECTED_EVENT_FORWARDING_ERROR;
	
}
