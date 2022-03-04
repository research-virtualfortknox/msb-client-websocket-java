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

import de.fhg.ipa.vfk.msb.client.api.AllTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Function parameters validator test.
 *
 * @author des
 */
class FunctionParametersValidatorTest {

    /**
     * Validate correct none function parameter.
     */
    @Test
    void validateCorrectNoneFunctionParameter() {
        String jsonDataFormatString = "{}";
        Assertions.assertTrue(FunctionParametersValidator.validateFunctionParameters(jsonDataFormatString, null));
    }

    /**
     * Validate incorrect none function parameter.
     */
    @Test
    void validateIncorrectNoneFunctionParameter() {
        String jsonDataFormatString = "{}";
        Map<String, Object> dataObject = new HashMap<>();
        dataObject.put("param", "");

        Assertions.assertFalse(FunctionParametersValidator.validateFunctionParameters(jsonDataFormatString, dataObject));
    }

    /**
     * Validate correct simple function parameter.
     */
    @Test
    void validateCorrectSimpleFunctionParameter() {
        String jsonDataFormatString = "{\"param\": {\"type\": \"boolean\"}}";
        Map<String, Object> dataObject = new HashMap<>();
        dataObject.put("param", true);

        Assertions.assertTrue(FunctionParametersValidator.validateFunctionParameters(jsonDataFormatString, dataObject));
    }

    /**
     * Validate incorrect simple function parameter type.
     */
    @Test
    void validateIncorrectSimpleFunctionParameterType() {
        String jsonDataFormatString = "{\"param\": {\"type\": \"boolean\"}}";
        Map<String, Object> dataObject = new HashMap<>();
        dataObject.put("param", "");

        Assertions.assertFalse(FunctionParametersValidator.validateFunctionParameters(jsonDataFormatString, dataObject));
    }

    /**
     * Validate incorrect simple function parameter name.
     */
    @Test
    void validateIncorrectSimpleFunctionParameterName() {
        String jsonDataFormatString = "{\"param\": {\"type\": \"boolean\"}}";
        Map<String, Object> dataObject = new HashMap<>();
        dataObject.put("test", true);

        Assertions.assertFalse(FunctionParametersValidator.validateFunctionParameters(jsonDataFormatString, dataObject));
    }

    /**
     * Validate correct simple function parameters.
     */
    @Test
    void validateCorrectSimpleFunctionParameters() {
        String jsonDataFormatString = "{\"param\": {\"type\": \"boolean\"}, \"param2\": {\"type\": \"string\"}}";
        Map<String, Object> dataObject = new HashMap<>();
        dataObject.put("param", true);
        dataObject.put("param2", "test");

        Assertions.assertTrue(FunctionParametersValidator.validateFunctionParameters(jsonDataFormatString, dataObject));
    }

    /**
     * Validate incorrect simple function parameters type.
     */
    @Test
    void validateIncorrectSimpleFunctionParametersType() {
        String jsonDataFormatString = "{\"param\": {\"type\": \"boolean\"}, \"param2\": {\"type\": \"string\"}}";
        Map<String, Object> dataObject = new HashMap<>();
        dataObject.put("param", true);
        dataObject.put("param2", true);

        Assertions.assertFalse(FunctionParametersValidator.validateFunctionParameters(jsonDataFormatString, dataObject));
    }

    /**
     * Validate incorrect simple function parameters name.
     */
    @Test
    void validateIncorrectSimpleFunctionParametersName() {
        String jsonDataFormatString = "{\"param\": {\"type\": \"boolean\"}, \"param2\": {\"type\": \"string\"}}";
        Map<String, Object> dataObject = new HashMap<>();
        dataObject.put("param", true);
        dataObject.put("test", "");

        Assertions.assertFalse(FunctionParametersValidator.validateFunctionParameters(jsonDataFormatString, dataObject));
    }

    /**
     * Validate incorrect simple function parameters count.
     */
    @Test
    void validateIncorrectSimpleFunctionParametersCount() {
        String jsonDataFormatString = "{\"param\": {\"type\": \"boolean\"}, \"param2\": {\"type\": \"string\"}}";
        Map<String, Object> dataObject = new HashMap<>();
        dataObject.put("param", true);

        Assertions.assertFalse(FunctionParametersValidator.validateFunctionParameters(jsonDataFormatString, dataObject));
    }


    /**
     * Validate correct complex data object.
     */
    @Test
    void validateCorrectComplexDataObject() {
        String jsonDataFormatString = "{ \"param\": { \"type\": \"object\", \"properties\": { \"ns=3;s=StructVar.SubStruct1\": { \"type\": \"object\", \"properties\": { \"ns=3;s=StructVar.SubStruct1.SubField1_1\": { \"type\": \"integer\", \"format\": \"int32\", \"minimum\": 0, \"maximum\": 4.2949673E9 } } }, \"ns=3;s=StructVar.Field2\": { \"type\": \"array\", \"items\": { \"type\": \"number\", \"format\": \"float\" }, \"maxItems\": 5 }, \"ns=3;s=StructVar.SubStruct2\": { \"type\": \"object\", \"properties\": { \"ns=3;s=StructVar.SubStruct2.SubField2_1\": { \"type\": \"boolean\" }, \"ns=3;s=StructVar.SubStruct2.SubField2_2\": { \"type\": \"number\", \"format\": \"float\" } } }, \"ns=3;s=StructVar.Field1\": { \"type\": \"boolean\" } } } }";

        Map<String, Object> SubStruct1 = new HashMap<>();
        SubStruct1.put("ns=3;s=StructVar.SubStruct1.SubField1_1", 123);
        Map<String, Object> SubStruct2 = new HashMap<>();
        SubStruct2.put("ns=3;s=StructVar.SubStruct2.SubField2_1", true);
        SubStruct2.put("ns=3;s=StructVar.SubStruct2.SubField2_2", 123);
        Map<String, Object> struct = new HashMap<>();
        struct.put("ns=3;s=StructVar.Field1", true);
        struct.put("ns=3;s=StructVar.Field2", new float[]{10, 20, 30, 40});
        struct.put("ns=3;s=StructVar.SubStruct1", SubStruct1);
        struct.put("ns=3;s=StructVar.SubStruct2", SubStruct2);
        Map<String, Object> dataObject = new HashMap<>();
        dataObject.put("param", struct);

        Assertions.assertTrue(FunctionParametersValidator.validateFunctionParameters(jsonDataFormatString, dataObject));
    }

    /**
     * Validate incorrect complex data object.
     */
    @Test
    void validateIncorrectComplexDataObject() {
        String jsonDataFormatString = "{ \"param\": { \"type\": \"object\", \"properties\": { \"ns=3;s=StructVar.SubStruct1\": { \"type\": \"object\", \"properties\": { \"ns=3;s=StructVar.SubStruct1.SubField1_1\": { \"type\": \"integer\", \"format\": \"int32\", \"minimum\": 0, \"maximum\": 4.2949673E9 } } }, \"ns=3;s=StructVar.Field2\": { \"type\": \"array\", \"items\": { \"type\": \"number\", \"format\": \"float\" }, \"maxItems\": 5 }, \"ns=3;s=StructVar.SubStruct2\": { \"type\": \"object\", \"properties\": { \"ns=3;s=StructVar.SubStruct2.SubField2_1\": { \"type\": \"boolean\" }, \"ns=3;s=StructVar.SubStruct2.SubField2_2\": { \"type\": \"number\", \"format\": \"float\" } } }, \"ns=3;s=StructVar.Field1\": { \"type\": \"boolean\" } } } }";

        Map<String, Object> SubStruct1 = new HashMap<>();
        SubStruct1.put("ns=3;s=StructVar.SubStruct1.SubField1_1", true);
        Map<String, Object> SubStruct2 = new HashMap<>();
        SubStruct2.put("ns=3;s=StructVar.SubStruct2.SubField2_1", true);
        SubStruct2.put("ns=3;s=StructVar.SubStruct2.SubField2_2", 123);
        Map<String, Object> struct = new HashMap<>();
        struct.put("ns=3;s=StructVar.Field1", true);
        struct.put("ns=3;s=StructVar.Field2", new float[]{10, 20, 30, 40});
        struct.put("ns=3;s=StructVar.SubStruct1", SubStruct1);
        struct.put("ns=3;s=StructVar.SubStruct2", SubStruct2);
        Map<String, Object> dataObject = new HashMap<>();
        dataObject.put("param", struct);

        Assertions.assertFalse(FunctionParametersValidator.validateFunctionParameters(jsonDataFormatString, dataObject));
    }

    /**
     * Validate referenced complex data object.
     */
    @Test
    void validateReferencedComplexDataObject() {
        String jsonDataFormatString = "{\"param\":{\"$ref\":\"#/definitions/AllTypes\"},\"AllTypes\":{\"type\":\"object\",\"properties\":{\"double_type\":{\"type\":\"number\",\"format\":\"double\"},\"float_type\":{\"type\":\"number\",\"format\":\"float\"},\"bool_type\":{\"type\":\"boolean\",\"default\":false},\"string_type\":{\"type\":\"string\"},\"count_type\":{\"type\":\"integer\",\"format\":\"int32\"},\"date_type\":{\"type\":\"string\",\"format\":\"date-time\"}}}}";
        Map<String, Object> dataObject = new HashMap<>();
        dataObject.put("param", AllTypes.getTestEntity());

        Assertions.assertTrue(FunctionParametersValidator.validateFunctionParameters(jsonDataFormatString, dataObject));
    }

}
