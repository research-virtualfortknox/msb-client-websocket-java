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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ListProcessingReport;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.swagger.parser.SwaggerParser;
import io.swagger.parser.util.SwaggerDeserializationResult;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The Class de.fhg.ipa.vfk.msb.client.util.DataFormatValidator.
 * Inspiration from https://github.com/swagger-api/validator-badge/blob/master/src/main/java/io/swagger/validator/services/ValidatorService.java
 *
 * @author des
 */
public final class DataFormatValidator {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(DataFormatValidator.class);
	
	/** The Constant JsonMapper. */
	private static final ObjectMapper JsonMapper = Json.mapper();
	
	/** The Constant YamlMapper. */
	private static final ObjectMapper YamlMapper = Yaml.mapper();

	private DataFormatValidator(){}

	private static final String FUNCTION_DATA_FORMAT_JSON_SCHEMA = "{\"title\": \"A JSON Schema for MSB data format API.\",\"$schema\": \"http://json-schema.org/draft-04/schema#\",\"type\": \"object\",\"required\": [\"definitions\"],\"additionalProperties\": false,\"properties\": {\"definitions\": {\"$ref\": \"#/definitions/definitions\"}},\"definitions\": {"
			+ "\"definitions\": {\"type\": \"object\",\"additionalProperties\": {\"$ref\": \"#/definitions/schema\"},\"description\": \"One or more JSON objects describing the schemas being consumed and produced by the API.\"},"
			+ "\"schema\": {\"type\": \"object\",\"description\": \"A deterministic version of a JSON Schema object.\",\"properties\": {\"$ref\": {\"type\": \"string\"},\"format\": {\"type\": \"string\"},\"title\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/title\"},\"description\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/description\"},\"default\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/default\"},\"multipleOf\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/multipleOf\"},\"maximum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/maximum\"},\"exclusiveMaximum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/exclusiveMaximum\"},\"minimum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/minimum\"},\"exclusiveMinimum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/exclusiveMinimum\"},\"maxLength\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveInteger\"},\"minLength\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveIntegerDefault0\"},\"pattern\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/pattern\"},\"maxItems\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveInteger\"},\"minItems\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveIntegerDefault0\"},\"uniqueItems\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/uniqueItems\"},\"maxProperties\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveInteger\"},\"minProperties\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveIntegerDefault0\"},\"required\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/stringArray\"},\"enum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/enum\"},\"additionalProperties\": {\"anyOf\": [{\"$ref\": \"#/definitions/schema\"},{\"type\": \"boolean\"}],\"default\": {}},\"type\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/type\"},\"items\": {\"anyOf\": [{\"$ref\": \"#/definitions/schema\"},{\"type\": \"array\",\"minItems\": 1,\"items\": {\"$ref\": \"#/definitions/schema\"}}],\"default\": {}},\"allOf\": {\"type\": \"array\",\"minItems\": 1,\"items\": {\"$ref\": \"#/definitions/schema\"}},\"properties\": {\"type\": \"object\",\"additionalProperties\": {\"$ref\": \"#/definitions/schema\"},\"default\": {}},\"discriminator\": {\"type\": \"string\"},\"readOnly\": {\"type\": \"boolean\",\"default\": false},\"example\": {}},\"additionalProperties\": false},"
			+ "\"title\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/title\"},\"description\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/description\"},\"default\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/default\"},\"multipleOf\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/multipleOf\"},\"maximum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/maximum\"},\"exclusiveMaximum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/exclusiveMaximum\"},\"minimum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/minimum\"},\"exclusiveMinimum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/exclusiveMinimum\"},\"maxLength\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveInteger\"},\"minLength\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveIntegerDefault0\"},\"pattern\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/pattern\"},\"maxItems\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveInteger\"},\"minItems\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveIntegerDefault0\"},\"uniqueItems\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/uniqueItems\"},\"enum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/enum\"},\"jsonReference\": {\"type\": \"object\",\"required\": [\"$ref\"],\"additionalProperties\": false,\"properties\": {\"$ref\": {  \"type\": \"string\"}}}"
			+ "}}";

	private static final String EVENT_DATA_FORMAT_JSON_SCHEMA = "{\"title\": \"A JSON Schema for MSB data format API.\",\"$schema\": \"http://json-schema.org/draft-04/schema#\",\"type\": \"object\",\"required\": [\"definitions\"],\"additionalProperties\": false,\"properties\": {\"definitions\": {\"$ref\": \"#/definitions/definitions\"}},\"definitions\": {"
			+ "\"definitions\": {\"type\": \"object\",\"required\": [\"dataObject\"],\"properties\": {\"dataObject\": {\"$ref\": \"#/definitions/dataObject\"}},\"additionalProperties\": {\"$ref\": \"#/definitions/schema\"},\"description\": \"One or more JSON objects describing the schemas being consumed and produced by the API.\"},"
			+ "\"dataObject\": {\"$ref\": \"#/definitions/schema\"},"
			+ "\"schema\": {\"type\": \"object\",\"description\": \"A deterministic version of a JSON Schema object.\",\"properties\": {\"$ref\": {\"type\": \"string\"},\"format\": {\"type\": \"string\"},\"title\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/title\"},\"description\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/description\"},\"default\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/default\"},\"multipleOf\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/multipleOf\"},\"maximum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/maximum\"},\"exclusiveMaximum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/exclusiveMaximum\"},\"minimum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/minimum\"},\"exclusiveMinimum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/exclusiveMinimum\"},\"maxLength\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveInteger\"},\"minLength\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveIntegerDefault0\"},\"pattern\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/pattern\"},\"maxItems\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveInteger\"},\"minItems\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveIntegerDefault0\"},\"uniqueItems\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/uniqueItems\"},\"maxProperties\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveInteger\"},\"minProperties\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveIntegerDefault0\"},\"required\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/stringArray\"},\"enum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/enum\"},\"additionalProperties\": {\"anyOf\": [{\"$ref\": \"#/definitions/schema\"},{\"type\": \"boolean\"}],\"default\": {}},\"type\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/type\"},\"items\": {\"anyOf\": [{\"$ref\": \"#/definitions/schema\"},{\"type\": \"array\",\"minItems\": 1,\"items\": {\"$ref\": \"#/definitions/schema\"}}],\"default\": {}},\"allOf\": {\"type\": \"array\",\"minItems\": 1,\"items\": {\"$ref\": \"#/definitions/schema\"}},\"properties\": {\"type\": \"object\",\"additionalProperties\": {\"$ref\": \"#/definitions/schema\"},\"default\": {}},\"discriminator\": {\"type\": \"string\"},\"readOnly\": {\"type\": \"boolean\",\"default\": false},\"example\": {}},\"additionalProperties\": false},"
			+ "\"title\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/title\"},\"description\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/description\"},\"default\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/default\"},\"multipleOf\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/multipleOf\"},\"maximum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/maximum\"},\"exclusiveMaximum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/exclusiveMaximum\"},\"minimum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/minimum\"},\"exclusiveMinimum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/exclusiveMinimum\"},\"maxLength\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveInteger\"},\"minLength\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveIntegerDefault0\"},\"pattern\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/pattern\"},\"maxItems\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveInteger\"},\"minItems\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/definitions/positiveIntegerDefault0\"},\"uniqueItems\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/uniqueItems\"},\"enum\": {\"$ref\": \"http://json-schema.org/draft-04/schema#/properties/enum\"},\"jsonReference\": {\"type\": \"object\",\"required\": [\"$ref\"],\"additionalProperties\": false,\"properties\": {\"$ref\": {  \"type\": \"string\"}}}"
			+ "}}";

	/**
	 * Validate event dataformat.
	 *
	 * @param content
	 *            the content
	 * @return the list processing report
	 */
	public static ListProcessingReport validateEventDataformat(String content) {
		return validateAgainstSchema(EVENT_DATA_FORMAT_JSON_SCHEMA, content);
	}

	/**
	 * Validate function dataformat.
	 *
	 * @param content
	 *            the content
	 * @return the list processing report
	 */
	public static ListProcessingReport validateFunctionDataformat(String content) {
		return validateAgainstSchema(FUNCTION_DATA_FORMAT_JSON_SCHEMA, content);
	}

	/**
	 * Validate against schema.
	 *
	 * @param schemaJSON
	 *            the schema JSON
	 * @param content
	 *            the content
	 * @return the list processing report
	 */
	private static ListProcessingReport validateAgainstSchema(String schemaJSON, String content) {
		ListProcessingReport lp = new ListProcessingReport();
		try {
			JsonNode schemaObject = JsonMapper.readTree(schemaJSON);
			content = "{\"definitions\":" + content + "}";

			JsonNode spec = readNode(content);
			if (spec == null) {
				ProcessingMessage pm = new ProcessingMessage();
				pm.setLogLevel(LogLevel.ERROR);
				pm.setMessage("Unable to read content.  It may be invalid JSON or YAML");
				lp.error(pm);
				return lp;
			}

			JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
			JsonSchema schema = factory.getJsonSchema(schemaObject);
			ProcessingReport report = schema.validate(spec);

			lp.mergeWith(report);

			if (report.isSuccess()) {
				readSwagger(content);
			}
		} catch (Exception e) {
			LOG.error("can't read content", e);
			ProcessingMessage pm = new ProcessingMessage();
			pm.setLogLevel(LogLevel.ERROR);
			pm.setMessage("unable to parse swagger");
			try {
				lp.error(pm);
			} catch (ProcessingException e1) {
				LOG.error("ProcessingException during generate list report", e1);
			}
		}	
		return lp;
	}

	/**
	 * Read swagger.
	 *
	 * @param content
	 *            the content
	 * @return the swagger deserialization result
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 */
	private static SwaggerDeserializationResult readSwagger(String content) {
		SwaggerParser parser = new SwaggerParser();
		return parser.readWithInfo(content);
	}

	/**
	 * Read node.
	 *
	 * @param text
	 *            the text
	 * @return the json node
	 */
	private static JsonNode readNode(String text) {
		try {
			if (text.trim().startsWith("{")) {
				return JsonMapper.readTree(text);
			} else {
				return YamlMapper.readTree(text);
			}
		} catch (IOException e) {
			LOG.warn("IOException: ",e);
			return null;
		}
	}

}
