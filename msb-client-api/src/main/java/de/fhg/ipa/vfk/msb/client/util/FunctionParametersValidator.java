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
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.swagger.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * The type Function parameters validator.
 *
 * @author des
 */
public final class FunctionParametersValidator {

    private static final Logger LOG = LoggerFactory.getLogger(DataFormatValidator.class);

    private static final ObjectMapper JsonMapper = Json.mapper();

    private static final String SCHEME_PREFIX = "{\"title\":\"A JSON Schema for MSB data format API.\"," +
            "   \"$schema\":\"http://json-schema.org/draft-04/schema#\"," +
            "   \"type\":\"object\"," +
            "   \"required\":[" +
            "      \"definitions\"" +
            "   ],\n" +
            "   \"additionalProperties\":false," +
            "   \"properties\":{" +
            "      \"definitions\":{" +
            "         \"$ref\":\"#/definitions/definitions\"" +
            "      }" +
            "   }," +
            "   \"definitions\":{" +
            "      \"definitions\":{" +
            "         \"type\":\"object\"," +
            "         \"additionalProperties\":false," +
            "         \"properties\":{";

    private FunctionParametersValidator() {}

    /**
     * Validate function parameters boolean.
     *
     * @param dataFormat the data format
     * @param parameters the parameters
     * @return the boolean
     */
    public static boolean validateFunctionParameters(String dataFormat, Map<String, Object> parameters) {
        LOG.debug("validate data format: {} against function parameters: {}",dataFormat,parameters);
        try {
            if(dataFormat == null || "".equals(dataFormat) || "null".equals(dataFormat) || "{}".equals(dataFormat)){
                return (parameters == null || parameters.isEmpty());
            }

            JsonNode dataFormatJsonNode = JsonMapper.readTree(dataFormat);
            Iterator<String> it = dataFormatJsonNode.fieldNames();
            StringBuilder properties = new StringBuilder(SCHEME_PREFIX);
            StringBuilder required = new StringBuilder(",\"required\":[");
            while(it.hasNext()){
                String fieldName = it.next();
                if(!dataFormat.contains("#/definitions/"+fieldName)){
                    properties.append("\"").append(fieldName).append("\":{\"$ref\":\"#/definitions/").append(fieldName).append("\"},");
                   required.append("\"").append(fieldName).append("\",");
                }
            }
            String dataFormatSchemaJsonString = removeLastChar(properties.toString())+"}"+removeLastChar(required.toString())+"]},"+dataFormat.substring(1) + "}}";

            JsonNode dataFormatSchemaJson = JsonMapper.readTree(dataFormatSchemaJsonString);
            final JsonSchema dataFormatSchema = JsonSchemaFactory.byDefault().getJsonSchema(dataFormatSchemaJson);
            JsonNode dataObjectJson = JsonMapper.readTree("{\"definitions\":" +JsonMapper.writeValueAsString(parameters)+ "}");
            ProcessingReport report = dataFormatSchema.validateUnchecked(dataObjectJson,true);

            if (report.isSuccess()) {
                return true;
            } else {
                LOG.error("failure report: {}",report);
                return false;
            }

        } catch (IOException e) {
            LOG.error("IOException: ",e);
        } catch (ProcessingException e) {
            LOG.error("ProcessingException: ",e);
        }
        return false;
    }

    private static String removeLastChar(String s){
        return s.substring(0,s.length()-1);
    }

}
