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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ListProcessingReport;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * The Class DataFormatValidatorTest.
 *
 * @author des
 */
class DataFormatValidatorTest {

    /**
     * Test validate event json.
     *
     * @throws JsonProcessingException the json processing exception
     * @throws ProcessingException     the processing exception
     */
    @Test
	void testValidateEventJson() throws JsonProcessingException, ProcessingException{
		String s = "{\"dataObject\":{\"type\":\"integer\",\"format\":\"int32\"}}";
		ListProcessingReport lp = DataFormatValidator.validateEventDataformat(s);
		Assertions.assertTrue(lp.isSuccess(), lp.toString());
	}

    /**
     * Test validate event object json.
     *
     * @throws JsonProcessingException the json processing exception
     * @throws ProcessingException     the processing exception
     */
    @Test
	void testValidateEventObjectJson() throws JsonProcessingException, ProcessingException, JSONException {
		String s = DataFormatParser.parseToString(DataFormatParserTest.InnerTestObject.class);
		JSONAssert.assertEquals("{\"dataObject\":{\"$ref\":\"#/definitions/InnerTestObject\"},\"InnerTestObject\":{\"type\":\"object\",\"properties\":{\"integer\":{\"type\":\"integer\",\"format\":\"int32\"},\"list\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}},\"map\":{\"type\":\"object\",\"additionalProperties\":{\"type\":\"string\"}}}}}", s,true);
		ListProcessingReport lp = DataFormatValidator.validateEventDataformat(s);
		Assertions.assertTrue(lp.isSuccess(), lp.toString());
	}

    /**
     * Test validate event failing json.
     *
     * @throws JsonProcessingException the json processing exception
     * @throws ProcessingException     the processing exception
     */
    @Test
	void testValidateEventFailingJson() throws JsonProcessingException, ProcessingException{
		String s = "{\"failing schema\"}";
		ListProcessingReport lp = DataFormatValidator.validateEventDataformat(s);
		Assertions.assertFalse(lp.isSuccess(), lp.toString());
	}

    /**
     * Test validate event json without data object.
     *
     * @throws JsonProcessingException the json processing exception
     * @throws ProcessingException     the processing exception
     */
    @Test
	void testValidateEventJsonWithoutDataObject() throws JsonProcessingException, ProcessingException{
		String s = "{\"xyz\":{\"$ref\":\"#/definitions/InnerTestObject\"},\"InnerTestObject\":{\"type\":\"object\",\"properties\":{\"integer\":{\"type\":\"integer\",\"format\":\"int32\"},\"list\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}},\"map\":{\"type\":\"object\",\"additionalProperties\":{\"type\":\"string\"}}}}}";
		ListProcessingReport lp = DataFormatValidator.validateEventDataformat(s);
		Assertions.assertFalse(lp.isSuccess(), lp.toString());
	}

    /**
     * Test validate mirrored event object json.
     *
     * @throws JsonProcessingException the json processing exception
     * @throws ProcessingException     the processing exception
     */
    @Test
	void testValidateMirroredEventObjectJson() throws JsonProcessingException, ProcessingException{
        String s = "{\"InnerTestObject\":{\"type\":\"object\",\"properties\":{\"integer\":{\"type\":\"integer\",\"format\":\"int32\"},\"list\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}},\"map\":{\"type\":\"object\",\"additionalProperties\":{\"type\":\"string\"}}}},\"dataObject\":{\"$ref\":\"#/definitions/InnerTestObject\"}}";
		ListProcessingReport lp = DataFormatValidator.validateEventDataformat(s);
		Assertions.assertTrue(lp.isSuccess(), lp.toString());
	}

    /**
     * Test validate function json without data object.
     *
     * @throws JsonProcessingException the json processing exception
     * @throws ProcessingException     the processing exception
     */
    @Test
	void testValidateFunctionJsonWithoutDataObject() throws JsonProcessingException, ProcessingException{
		String s = "{\"xyz\":{\"$ref\":\"#/definitions/InnerTestObject\"},\"InnerTestObject\":{\"type\":\"object\",\"properties\":{\"integer\":{\"type\":\"integer\",\"format\":\"int32\"},\"list\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}},\"map\":{\"type\":\"object\",\"additionalProperties\":{\"type\":\"string\"}}}}}";
		ListProcessingReport lp = DataFormatValidator.validateFunctionDataformat(s);
		Assertions.assertTrue(lp.isSuccess(), lp.toString());
	}

    /**
     * Test validate function failing json.
     *
     * @throws JsonProcessingException the json processing exception
     * @throws ProcessingException     the processing exception
     */
    @Test
	void testValidateFunctionFailingJson() throws JsonProcessingException, ProcessingException{
		String s = "{\"failing schema\"}";
		ListProcessingReport lp = DataFormatValidator.validateFunctionDataformat(s);
		Assertions.assertFalse(lp.isSuccess(), lp.toString());
	}

    /**
     * Test validate function object json.
     *
     * @throws JsonProcessingException the json processing exception
     * @throws ProcessingException     the processing exception
     */
    @Test
	void testValidateFunctionObjectJson() throws JsonProcessingException, ProcessingException, JSONException {
		String s = DataFormatParser.parseToString(DataFormatParserTest.InnerTestObject.class);
		JSONAssert.assertEquals("{\"dataObject\":{\"$ref\":\"#/definitions/InnerTestObject\"},\"InnerTestObject\":{\"type\":\"object\",\"properties\":{\"integer\":{\"type\":\"integer\",\"format\":\"int32\"},\"list\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}},\"map\":{\"type\":\"object\",\"additionalProperties\":{\"type\":\"string\"}}}}}", s,true);
		ListProcessingReport lp = DataFormatValidator.validateFunctionDataformat(s);
		Assertions.assertTrue(lp.isSuccess(), lp.toString());
	}

}
