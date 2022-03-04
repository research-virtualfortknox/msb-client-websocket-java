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
 * Tests for {@link DataObjectValidator}.
 *
 * @author maes
 */
class DataObjectValidatorTest {

    @Test
    void validateInvalidJson() {
        String jsonDataFormatString = "wrong_json";
        Map<String, Object> dataObject = new HashMap<>();
        dataObject.put("dataObject", "");
        Assertions.assertFalse(DataObjectValidator.validateDataObject(jsonDataFormatString, dataObject));
    }

    @Test
    void validateCorrectNoneDataObject() {
        String jsonDataFormatString = "{}";
        Assertions.assertTrue(DataObjectValidator.validateDataObject(jsonDataFormatString, null));
    }

    @Test
    void validateIncorrectNoneDataObject() {
        String jsonDataFormatString = "{}";
        Map<String, Object> dataObject = new HashMap<>();
        dataObject.put("dataObject", "");

        Assertions.assertFalse(DataObjectValidator.validateDataObject(jsonDataFormatString, dataObject));
    }

    @Test
    void validateCorrectSimpleDataObject() {
        String jsonDataFormatString = "{\"dataObject\": {\"type\": \"boolean\"}}";
        Map<String, Object> dataObject = new HashMap<>();
        dataObject.put("dataObject", true);

        Assertions.assertTrue(DataObjectValidator.validateDataObject(jsonDataFormatString, dataObject));
    }

    @Test
    void validateIncorrectSimpleDataObject() {
        String jsonDataFormatString = "{\"dataObject\": {\"type\": \"boolean\"}}";
        Map<String, Object> dataObject = new HashMap<>();
        dataObject.put("dataObject2", true);

        Assertions.assertFalse(DataObjectValidator.validateDataObject(jsonDataFormatString, dataObject));
        Assertions.assertFalse(DataObjectValidator.validateDataObject(jsonDataFormatString, true));
    }

    @Test
    void validateCorrectComplexDataObject() {
        String jsonDataFormatString = "{ \"dataObject\": { \"type\": \"object\", \"properties\": { \"ns=3;s=StructVar.SubStruct1\": { \"type\": \"object\", \"properties\": { \"ns=3;s=StructVar.SubStruct1.SubField1_1\": { \"type\": \"integer\", \"format\": \"int32\", \"minimum\": 0, \"maximum\": 4.2949673E9 } } }, \"ns=3;s=StructVar.Field2\": { \"type\": \"array\", \"items\": { \"type\": \"number\", \"format\": \"float\" }, \"maxItems\": 5 }, \"ns=3;s=StructVar.SubStruct2\": { \"type\": \"object\", \"properties\": { \"ns=3;s=StructVar.SubStruct2.SubField2_1\": { \"type\": \"boolean\" }, \"ns=3;s=StructVar.SubStruct2.SubField2_2\": { \"type\": \"number\", \"format\": \"float\" } } }, \"ns=3;s=StructVar.Field1\": { \"type\": \"boolean\" } } } }";

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
        dataObject.put("dataObject", struct);

        Assertions.assertTrue(DataObjectValidator.validateDataObject(jsonDataFormatString, dataObject));
    }

    @Test
    void validateIncorrectComplexDataObject() {
        String jsonDataFormatString = "{ \"dataObject\": { \"type\": \"object\", \"properties\": { \"ns=3;s=StructVar.SubStruct1\": { \"type\": \"object\", \"properties\": { \"ns=3;s=StructVar.SubStruct1.SubField1_1\": { \"type\": \"integer\", \"format\": \"int32\", \"minimum\": 0, \"maximum\": 4.2949673E9 } } }, \"ns=3;s=StructVar.Field2\": { \"type\": \"array\", \"items\": { \"type\": \"number\", \"format\": \"float\" }, \"maxItems\": 5 }, \"ns=3;s=StructVar.SubStruct2\": { \"type\": \"object\", \"properties\": { \"ns=3;s=StructVar.SubStruct2.SubField2_1\": { \"type\": \"boolean\" }, \"ns=3;s=StructVar.SubStruct2.SubField2_2\": { \"type\": \"number\", \"format\": \"float\" } } }, \"ns=3;s=StructVar.Field1\": { \"type\": \"boolean\" } } } }";

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
        dataObject.put("dataObject", struct);

        Assertions.assertFalse(DataObjectValidator.validateDataObject(jsonDataFormatString, dataObject));
    }

    @Test
    void validateReferencedComplexDataObject() {
        String jsonDataFormatString = "{\"dataObject\":{\"$ref\":\"#/definitions/AllTypes\"},\"AllTypes\":{\"type\":\"object\",\"properties\":{\"double_type\":{\"type\":\"number\",\"format\":\"double\"},\"float_type\":{\"type\":\"number\",\"format\":\"float\"},\"bool_type\":{\"type\":\"boolean\",\"default\":false},\"string_type\":{\"type\":\"string\"},\"count_type\":{\"type\":\"integer\",\"format\":\"int32\"},\"date_type\":{\"type\":\"string\",\"format\":\"date-time\"}}}}";
        Map<String, Object> dataObject = new HashMap<>();
        dataObject.put("dataObject", AllTypes.getTestEntity());

        Assertions.assertTrue(DataObjectValidator.validateDataObject(jsonDataFormatString, dataObject));
    }
}
