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
import java.util.List;

/**
 * The type Config param parser test.
 *
 * @author des
 */
@Events({})
public class ConfigParamParserTest {

    /**
     * The Test param 1.
     */
    @ConfigurationParam(name = "helloParam")
    public String testParam1 = "hello";

    /**
     * The Test param 2.
     */
    @ConfigurationParam
    protected double testParam2 = 1.234D;

    @ConfigurationParam
    private int testParam3 = 1234;

    /**
     * Test parse config param by package scan.
     */
    @Test
    public void testParseConfigParamByPackageScan() {
        Configuration configuration = ConfigParamParser.parse("de.fhg.ipa.vfk.msb.client.parser");

        Assert.assertNotNull("configuration is null",configuration);
        Assert.assertEquals("configuration parameter size not equals", 2, configuration.getParameters().size());

        ParameterValue parameterValue = configuration.getParameters().get("helloParam");
        Assert.assertNotNull("config param is null", parameterValue);
        Assert.assertEquals("type of config param", PrimitiveType.STRING, parameterValue.getType());
        Assert.assertNull("format of config param", parameterValue.getFormat());
        Assert.assertEquals("value of config param", testParam1, parameterValue.getValue());

        ParameterValue parameterValue2 = configuration.getParameters().get("testParam2");
        Assert.assertNotNull("config param is null", parameterValue2);
        Assert.assertEquals("type of config param", PrimitiveType.NUMBER, parameterValue2.getType());
        Assert.assertEquals("format of config param", PrimitiveFormat.DOUBLE, parameterValue2.getFormat());
        Assert.assertEquals("value of config param", testParam2, parameterValue2.getValue());
    }

    /**
     * Test parse config param by instance.
     */
    @Test
    public void testParseConfigParamByInstance() {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        annotatedClasses.add(ConfigParamParserTest.class);
        Configuration configuration = ConfigParamParser.parseConfig(annotatedClasses);

        Assert.assertNotNull("configuration is null",configuration);
        Assert.assertEquals("configuration parameter size not equals", 2, configuration.getParameters().size());

        ParameterValue parameterValue = configuration.getParameters().get("helloParam");
        Assert.assertNotNull("config param is null", parameterValue);
        Assert.assertEquals("type of config param", PrimitiveType.STRING, parameterValue.getType());
        Assert.assertNull("format of config param", parameterValue.getFormat());
        Assert.assertEquals("value of config param", testParam1, parameterValue.getValue());

        ParameterValue parameterValue2 = configuration.getParameters().get("testParam2");
        Assert.assertNotNull("config param is null", parameterValue2);
        Assert.assertEquals("type of config param", PrimitiveType.NUMBER, parameterValue2.getType());
        Assert.assertEquals("format of config param", PrimitiveFormat.DOUBLE, parameterValue2.getFormat());
        Assert.assertEquals("value of config param", testParam2, parameterValue2.getValue());
    }

    /**
     * Get test param 3 int.
     *
     * @return the int
     */
    public int getTestParam3(){
        return testParam3;
    }

    /**
     * Set test param 3.
     *
     * @param testParam3 the test param 3
     */
    public void setTestParam3(int testParam3){
        this.testParam3 = testParam3;
    }

}
