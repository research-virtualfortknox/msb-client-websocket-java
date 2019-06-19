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

import de.fhg.ipa.vfk.msb.client.util.WrapperTypes;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


/**
 * The type Wrapper types test.
 *
 * @author des
 */
public class WrapperTypesTest {

    /**
     * Test boolean wrapper types.
     */
    @Test
    public void testBooleanWrapperTypes(){
        Assert.assertTrue(WrapperTypes.isBooleanWrapperType(boolean.class));
        Assert.assertTrue(WrapperTypes.isBooleanWrapperType(Boolean.class));

        Assert.assertFalse(WrapperTypes.isBooleanWrapperType(int.class));
    }

    /**
     * Test number wrapper types.
     */
    @Test
    public void testNumberWrapperTypes(){
        Assert.assertTrue(WrapperTypes.isNumberWrapperType(float.class));
        Assert.assertTrue(WrapperTypes.isNumberWrapperType(double.class));
        Assert.assertTrue(WrapperTypes.isNumberWrapperType(Float.class));
        Assert.assertTrue(WrapperTypes.isNumberWrapperType(Double.class));
        Assert.assertTrue(WrapperTypes.isNumberWrapperType(int.class));
        Assert.assertTrue(WrapperTypes.isNumberWrapperType(long.class));
        Assert.assertTrue(WrapperTypes.isNumberWrapperType(Integer.class));
        Assert.assertTrue(WrapperTypes.isNumberWrapperType(Long.class));

        Assert.assertFalse(WrapperTypes.isNumberWrapperType(String.class));
    }

    /**
     * Test date wrapper types.
     */
    @Test
    public void testDateWrapperTypes(){
        Assert.assertTrue(WrapperTypes.isDateWrapperType(Date.class));
        Assert.assertTrue(WrapperTypes.isDateWrapperType(java.sql.Time.class));
        Assert.assertTrue(WrapperTypes.isDateWrapperType(java.sql.Date.class));
        Assert.assertTrue(WrapperTypes.isDateWrapperType(java.sql.Timestamp.class));

        Assert.assertFalse(WrapperTypes.isDateWrapperType(Object.class));
    }

    /**
     * Test wrapper types without conversation.
     */
    @Test
    public void testWrapperTypesWithoutConversation(){
        Assert.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(char.class));
        Assert.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(byte.class));
        Assert.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(short.class));
        Assert.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(void.class));
        Assert.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(Character.class));
        Assert.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(Byte.class));
        Assert.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(Short.class));
        Assert.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(Void.class));
        Assert.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(String.class));
        Assert.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(BigInteger.class));
        Assert.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(BigDecimal.class));
        Assert.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(int.class));
        Assert.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(Long.class));
        Assert.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(Boolean.class));

        Assert.assertFalse(WrapperTypes.isWrapperTypeWithoutConversation(Date.class));
        Assert.assertFalse(WrapperTypes.isWrapperTypeWithoutConversation(Object.class));
    }

    /**
     * Test wrapper types.
     */
    @Test
    public void testWrapperTypes(){
        Assert.assertTrue(WrapperTypes.isWrapperType(char.class));
        Assert.assertTrue(WrapperTypes.isWrapperType(byte.class));
        Assert.assertTrue(WrapperTypes.isWrapperType(short.class));
        Assert.assertTrue(WrapperTypes.isWrapperType(void.class));
        Assert.assertTrue(WrapperTypes.isWrapperType(Character.class));
        Assert.assertTrue(WrapperTypes.isWrapperType(Byte.class));
        Assert.assertTrue(WrapperTypes.isWrapperType(Short.class));
        Assert.assertTrue(WrapperTypes.isWrapperType(Void.class));
        Assert.assertTrue(WrapperTypes.isWrapperType(String.class));
        Assert.assertTrue(WrapperTypes.isWrapperType(BigDecimal.class));
        Assert.assertTrue(WrapperTypes.isWrapperType(int.class));
        Assert.assertTrue(WrapperTypes.isWrapperType(long.class));
        Assert.assertTrue(WrapperTypes.isWrapperType(boolean.class));
        Assert.assertTrue(WrapperTypes.isWrapperType(java.sql.Timestamp.class));

        Assert.assertFalse(WrapperTypes.isWrapperType(Object.class));
    }

}
