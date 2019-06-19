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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import de.fhg.ipa.vfk.msb.client.util.DataFormatParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The Class de.fhg.ipa.vfk.msb.client.api.DataFormat.
 *
 * @author des
 */
public class DataFormat extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = 1841764693622182933L;
	private static final Logger LOG = LoggerFactory.getLogger(DataFormat.class);
	private static final transient ObjectMapper OBJECT_MAPPER = DataFormatParser.getObjectMapper();
	private final transient ObjectWriter writer = OBJECT_MAPPER.writer();

	/**
	 * Instantiates a new data format.
	 */
	public DataFormat() {
		// empty constructor (p.e. for reflection)
	}

	/**
	 * Instantiates a new data format.
	 *
	 * @param s the s
	 */
	public DataFormat(String s) {
		super(DataFormatParser.parse(s));
	}

	/**
	 * Instantiates a new data format.
	 *
	 * @param map the map
	 */
	public DataFormat(Map<String, Object> map) {
		super(map);
	}

	/**
	 * Instantiates a new Data format.
	 *
	 * @param parameterClass the parameter class
	 */
	public DataFormat(Class<?> parameterClass) {
		super(DataFormatParser.parse(parameterClass));
	}

	@Override
	public String toString() {
		try {
			return writer.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			LOG.error("JsonProcessingException: ", e);
		}
		return "";
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}
}
