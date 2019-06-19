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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class DateDeserializer.
 *
 * @author des
 */
public class DateDeserializer extends JsonDeserializer<Date> {

	/**
	 * Deserialize.
	 *
	 * @param jp the json parser
	 * @param context the context
	 * @return the date
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JsonProcessingException the json processing exception
	 * @see JsonDeserializer#deserialize(JsonParser, DeserializationContext)
	 */
	@Override
	public Date deserialize(JsonParser jp, DeserializationContext context) throws IOException {
		JsonNode json = jp.getCodec().readTree(jp);
		if (json.asText() != null && !"".equals(json.asText().trim())) {
			String s = json.asText();
			try {
				return new Date(Long.parseLong(s));
			} catch (NumberFormatException nex) {
				return parseDate(s);
			}
		} else {
			return null;
		}
	}

	private static Date parseDate(String s){
		// local variable because SimpleDateFormat is not thread safe
		final SimpleDateFormat dateFormat = new MsbDateFormat();
		try {
			return dateFormat.parse(s);
		} catch (ParseException e) {
			return null;
		}
	}

}
