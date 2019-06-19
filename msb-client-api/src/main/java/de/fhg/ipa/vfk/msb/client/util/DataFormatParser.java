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

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.converter.ModelConverters;
import io.swagger.models.Model;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.util.ReferenceSerializationConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The Class de.fhg.ipa.vfk.msb.client.util.DataFormatParser is a parser, that helps to generate the data
 * format of a event or function based on a defined class.
 *
 * @author des
 */
public final class DataFormatParser {

	private static final Logger LOG = LoggerFactory.getLogger(DataFormatParser.class);

	private static ObjectMapper mapper;

	private DataFormatParser() {}

	/**
	 * Gets the data format string.
	 *
	 * @param parameterClass the parameter class
	 * @return the data format string
	 * @throws JsonProcessingException the json processing exception
	 */
	public static String parseToString(Type parameterClass)
			throws JsonProcessingException {
		LOG.trace("parse class to string: {}", parameterClass);
		Map<String, Object> map = parse(parameterClass);
		return getObjectMapper().writeValueAsString(map);
	}

	public static Map<String, Object> readFromString(String dataformatString) throws IOException {
		ObjectMapper mapper = getObjectMapper();
		JavaType type = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
		return mapper.readValue(dataformatString, type);
	}

	/**
	 * Gets a configured object mapper. It not fails on empty or unknown fields,
	 * it use for date de-/serialization a ISO8601 conform de-/serializer and it
	 * not include null values.
	 *
	 * @return the object mapper
	 */
	public static ObjectMapper getObjectMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			mapper.setDateFormat(new MsbDateFormat());
			mapper.setSerializationInclusion(Include.NON_NULL);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			SimpleModule module = new SimpleModule();
			module.addSerializer(Date.class, new DateSerializer());
			module.addDeserializer(Date.class, new DateDeserializer());
			mapper.registerModule(module);
			ReferenceSerializationConfigurer.serializeAsComputedRef(mapper);
		}
		return mapper;
	}

	/**
	 * Parse map.
	 *
	 * @param s the s
	 * @return the map
	 */
	public static Map<String, Object> parse(String s) {
		if(s!=null&&!"".equals(s)&&s.trim().startsWith("{")&&s.trim().endsWith("}")){
			try {
				return getObjectMapper().readerFor(LinkedHashMap.class).readValue(s);
			} catch (JsonParseException e) {
				LOG.error("JsonProcessingException: ", e);
			} catch (JsonMappingException e) {
				LOG.error("JsonMappingException: ", e);
			} catch (IOException e) {
				LOG.error("IOException: ", e);
			}
		}
		return new HashMap<>();
	}

	/**
	 * Parse map.
	 *
	 * @param parameterClass the parameter class
	 * @return the map
	 */
	public static Map<String, Object> parse(Type parameterClass) {
		LOG.trace("parse class: {}", parameterClass);
		return parse("dataObject", parameterClass, new LinkedHashMap<String, Object>());
	}

	/**
	 * Parse map.
	 *
	 * @param name           the name
	 * @param parameterClass the parameter class
	 * @return the map
	 */
	public static Map<String, Object> parse(String name, Type parameterClass) {
        return parse(name, parameterClass, new LinkedHashMap<String, Object>());
    }

	/**
	 * Parse map.
	 *
	 * @param name           the name
	 * @param parameterClass the parameter class
	 * @param map            the map
	 * @return the map
	 */
	public static Map<String, Object> parse(String name, Type parameterClass,
                                            Map<String, Object> map) {
		LOG.trace("parse class: {} for parameter: {}", parameterClass, name);
		Property p = ModelConverters.getInstance().readAsProperty(parameterClass);
		map.put(name, p);
		if (p instanceof RefProperty || p instanceof ArrayProperty || p instanceof MapProperty) {
			Map<String, Model> map2 = ModelConverters.getInstance().readAll(parameterClass);
			map.putAll(map2);
		} else if (p instanceof IntegerProperty && parameterClass == Short.class) {
			((IntegerProperty)p).setMinimum(new BigDecimal(Short.MIN_VALUE));
			((IntegerProperty)p).setMaximum(new BigDecimal(Short.MAX_VALUE));
		}
		return map;
	}
	
}
