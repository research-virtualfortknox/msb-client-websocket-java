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

package de.fhg.ipa.vfk.msb.client.parser;

import de.fhg.ipa.vfk.msb.client.annotation.ConfigurationParam;
import de.fhg.ipa.vfk.msb.client.annotation.Events;
import de.fhg.ipa.vfk.msb.client.api.Configuration;
import de.fhg.ipa.vfk.msb.client.api.ParameterValue;
import de.fhg.ipa.vfk.msb.client.api.PrimitiveFormat;
import de.fhg.ipa.vfk.msb.client.api.PrimitiveType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Config param parser test.
 *
 * @author des
 */
class ConfigParamParserTest {

    @Test
    void testParseConfigParamByPackageScan() {
        Configuration configuration = ConfigParamParser.parse("de.fhg.ipa.vfk.msb.client.parser");

        Assertions.assertNotNull(configuration, "configuration is null");
        Assertions.assertEquals( 10, configuration.getParameters().size(), "configuration parameter size not equals");

        ParameterValue parameterValue = configuration.getParameters().get("helloParam");
        Assertions.assertNotNull(parameterValue,"config param is null");
        Assertions.assertEquals(PrimitiveType.STRING, parameterValue.getType(), "type of config param");
        Assertions.assertNull(parameterValue.getFormat(), "format of config param");
        Assertions.assertEquals("hello", parameterValue.getValue(), "value of config param");

        ParameterValue parameterValue2 = configuration.getParameters().get("testParam2");
        Assertions.assertNotNull(parameterValue2, "config param is null");
        Assertions.assertEquals(PrimitiveType.NUMBER, parameterValue2.getType(), "type of config param");
        Assertions.assertEquals(PrimitiveFormat.DOUBLE, parameterValue2.getFormat(), "format of config param");
        Assertions.assertEquals(1.234D, parameterValue2.getValue(), "value of config param");
    }

    @Test
    void testParseConfigParamByInstance() {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        annotatedClasses.add(SuccessfulClazz.class);
        Configuration configuration = ConfigParamParser.parseConfig(annotatedClasses);

        Assertions.assertNotNull(configuration, "configuration is null");
        Assertions.assertEquals( 10, configuration.getParameters().size(), "configuration parameter size not equals");

        ParameterValue parameterValue = configuration.getParameters().get("helloParam");
        Assertions.assertNotNull(parameterValue,"config param is null");
        Assertions.assertEquals(PrimitiveType.STRING, parameterValue.getType(), "type of config param");
        Assertions.assertNull(parameterValue.getFormat(), "format of config param");
        Assertions.assertEquals("hello", parameterValue.getValue(), "value of config param");

        ParameterValue parameterValue2 = configuration.getParameters().get("testParam2");
        Assertions.assertNotNull(parameterValue2, "config param is null");
        Assertions.assertEquals(PrimitiveType.NUMBER, parameterValue2.getType(), "type of config param");
        Assertions.assertEquals(PrimitiveFormat.DOUBLE, parameterValue2.getFormat(), "format of config param");
        Assertions.assertEquals(1.234D, parameterValue2.getValue(), "value of config param");
    }

    @Test
    void testParseConfigParamFailsByConstructor() {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        annotatedClasses.add(FailingClazz.class);
        Configuration configuration = ConfigParamParser.parseConfig(annotatedClasses);

        Assertions.assertNotNull(configuration, "configuration is null");
        Assertions.assertEquals( 0, configuration.getParameters().size(), "configuration parameter size not equals");
    }

    @Events({})
    public static class SuccessfulClazz {

        @ConfigurationParam(name = "helloParam")
        String testParam1 = "hello";
        @ConfigurationParam
        protected double testParam2 = 1.234D;
        @ConfigurationParam
        private int testParam3 = 1234;
        @ConfigurationParam
        public int testParam4 = 100;
        @ConfigurationParam
        public long testParam5 = 200L;
        @ConfigurationParam
        public float testParam6 = 0.1F;
        @ConfigurationParam
        public boolean testParam7 = true;
        @ConfigurationParam
        public Date testParam8 = new Date();
        @ConfigurationParam
        public String[] testParam9 = new String[]{"hello"};
        @ConfigurationParam
        public List<String> testParam10 = new ArrayList<>();
        @ConfigurationParam
        public Map<String,String> testParam11 = new HashMap<>();

        public SuccessfulClazz(){
            testParam10.add("hello");
            testParam11.put("hello","test");
        }

        public int getTestParam3(){
            return testParam3;
        }

        public void setTestParam3(int testParam3){
            this.testParam3 = testParam3;
        }
    }

    @Events({})
    public static class FailingClazz {

        public FailingClazz(String missingDefaultConstructor){ }

    }



}
