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

/**
 * Validates a dataObject against a data format.
 *
 * @author maes
 */
public final class DataObjectValidator {

    private static final Logger LOG = LoggerFactory.getLogger(DataObjectValidator.class);

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
            "         \"properties\":{" +
            "            \"dataObject\":{" +
            "               \"$ref\":\"#/definitions/dataObject\"" +
            "            }" +
            "         }," +
            "         \"required\":[" +
            "            \"dataObject\"" +
            "         ]" +
            "      },";

    private DataObjectValidator() {}

    /**
     * Validate a dataObject against a data format.
     *
     * @param dataFormat The data format for validation.
     * @param dataObject The dataObject to validate.
     * @return True, if the validation was successful.
     */
    public static boolean validateDataObject(String dataFormat, Object dataObject) {
        LOG.debug("validate data format: {} against data object: {}",dataFormat,dataObject);
        try {
            if(dataFormat==null || "".equals(dataFormat) || "null".equals(dataFormat) || "{}".equals(dataFormat)){
                return dataObject==null || "".equals(dataObject.toString()) || "null".equals(dataObject.toString()) || "{dataObject=null}".equals(dataObject.toString());
            }
            JsonNode dataFormatSchemaJson = JsonMapper.readTree(SCHEME_PREFIX + dataFormat.substring(1) + "}}");
            final JsonSchema dataFormatSchema = JsonSchemaFactory.byDefault().getJsonSchema(dataFormatSchemaJson);
            JsonNode dataObjectJson = JsonMapper.readTree("{\"definitions\":" +JsonMapper.writeValueAsString(dataObject)+ "}");
            ProcessingReport report = dataFormatSchema.validate(dataObjectJson);

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
}
