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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class DateSerializer.
 *
 * @author des
 */
public class DateSerializer extends JsonSerializer<Date> {
	/**
	 * Serialize.
	 *
	 * @param date the date
	 * @param gen the gen
	 * @param provider the provider
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JsonProcessingException the json processing exception
	 * @see JsonSerializer#serialize(java.lang.Object, JsonGenerator, SerializerProvider)
	 */
	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException {
		// local variable because SimpleDateFormat is not thread safe
		final SimpleDateFormat dateFormat = new MsbDateFormat();
		String formattedDate = date == null ? null : dateFormat.format(date);
		gen.writeString(formattedDate);
	}

}
