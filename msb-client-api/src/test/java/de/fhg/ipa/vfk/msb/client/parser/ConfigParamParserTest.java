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
import org.junit.Assert;
import org.junit.Test;

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
public class ConfigParamParserTest {

    @Test
    public void testParseConfigParamByPackageScan() {
        Configuration configuration = ConfigParamParser.parse("de.fhg.ipa.vfk.msb.client.parser");

        Assert.assertNotNull("configuration is null",configuration);
        Assert.assertEquals("configuration parameter size not equals", 10, configuration.getParameters().size());

        ParameterValue parameterValue = configuration.getParameters().get("helloParam");
        Assert.assertNotNull("config param is null", parameterValue);
        Assert.assertEquals("type of config param", PrimitiveType.STRING, parameterValue.getType());
        Assert.assertNull("format of config param", parameterValue.getFormat());
        Assert.assertEquals("value of config param", "hello", parameterValue.getValue());

        ParameterValue parameterValue2 = configuration.getParameters().get("testParam2");
        Assert.assertNotNull("config param is null", parameterValue2);
        Assert.assertEquals("type of config param", PrimitiveType.NUMBER, parameterValue2.getType());
        Assert.assertEquals("format of config param", PrimitiveFormat.DOUBLE, parameterValue2.getFormat());
        Assert.assertEquals("value of config param", 1.234D, parameterValue2.getValue());
    }

    @Test
    public void testParseConfigParamByInstance() {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        annotatedClasses.add(SuccessfulClazz.class);
        Configuration configuration = ConfigParamParser.parseConfig(annotatedClasses);

        Assert.assertNotNull("configuration is null",configuration);
        Assert.assertEquals("configuration parameter size not equals", 10, configuration.getParameters().size());

        ParameterValue parameterValue = configuration.getParameters().get("helloParam");
        Assert.assertNotNull("config param is null", parameterValue);
        Assert.assertEquals("type of config param", PrimitiveType.STRING, parameterValue.getType());
        Assert.assertNull("format of config param", parameterValue.getFormat());
        Assert.assertEquals("value of config param", "hello", parameterValue.getValue());

        ParameterValue parameterValue2 = configuration.getParameters().get("testParam2");
        Assert.assertNotNull("config param is null", parameterValue2);
        Assert.assertEquals("type of config param", PrimitiveType.NUMBER, parameterValue2.getType());
        Assert.assertEquals("format of config param", PrimitiveFormat.DOUBLE, parameterValue2.getFormat());
        Assert.assertEquals("value of config param", 1.234D, parameterValue2.getValue());
    }

    @Test
    public void testParseConfigParamFailsByConstructor() {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        annotatedClasses.add(FailingClazz.class);
        Configuration configuration = ConfigParamParser.parseConfig(annotatedClasses);

        Assert.assertNotNull("configuration is null",configuration);
        Assert.assertEquals("configuration parameter size not equals", 0, configuration.getParameters().size());
    }

    @Events({})
    public static class SuccessfulClazz {

        @ConfigurationParam(name = "helloParam")
        public String testParam1 = "hello";
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
