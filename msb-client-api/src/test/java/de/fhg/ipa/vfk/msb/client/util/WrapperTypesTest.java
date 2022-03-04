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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


/**
 * The type Wrapper types test.
 *
 * @author des
 */
class WrapperTypesTest {

    /**
     * Test boolean wrapper types.
     */
    @Test
    void testBooleanWrapperTypes(){
        Assertions.assertTrue(WrapperTypes.isBooleanWrapperType(boolean.class));
        Assertions.assertTrue(WrapperTypes.isBooleanWrapperType(Boolean.class));

        Assertions.assertFalse(WrapperTypes.isBooleanWrapperType(int.class));
    }

    /**
     * Test number wrapper types.
     */
    @Test
    void testNumberWrapperTypes(){
        Assertions.assertTrue(WrapperTypes.isNumberWrapperType(float.class));
        Assertions.assertTrue(WrapperTypes.isNumberWrapperType(double.class));
        Assertions.assertTrue(WrapperTypes.isNumberWrapperType(Float.class));
        Assertions.assertTrue(WrapperTypes.isNumberWrapperType(Double.class));
        Assertions.assertTrue(WrapperTypes.isNumberWrapperType(int.class));
        Assertions.assertTrue(WrapperTypes.isNumberWrapperType(long.class));
        Assertions.assertTrue(WrapperTypes.isNumberWrapperType(Integer.class));
        Assertions.assertTrue(WrapperTypes.isNumberWrapperType(Long.class));

        Assertions.assertFalse(WrapperTypes.isNumberWrapperType(String.class));
    }

    /**
     * Test date wrapper types.
     */
    @Test
    void testDateWrapperTypes(){
        Assertions.assertTrue(WrapperTypes.isDateWrapperType(Date.class));
        Assertions.assertTrue(WrapperTypes.isDateWrapperType(java.sql.Time.class));
        Assertions.assertTrue(WrapperTypes.isDateWrapperType(java.sql.Date.class));
        Assertions.assertTrue(WrapperTypes.isDateWrapperType(java.sql.Timestamp.class));

        Assertions.assertFalse(WrapperTypes.isDateWrapperType(Object.class));
    }

    /**
     * Test wrapper types without conversation.
     */
    @Test
    void testWrapperTypesWithoutConversation(){
        Assertions.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(char.class));
        Assertions.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(byte.class));
        Assertions.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(short.class));
        Assertions.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(void.class));
        Assertions.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(Character.class));
        Assertions.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(Byte.class));
        Assertions.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(Short.class));
        Assertions.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(Void.class));
        Assertions.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(String.class));
        Assertions.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(BigInteger.class));
        Assertions.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(BigDecimal.class));
        Assertions.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(int.class));
        Assertions.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(Long.class));
        Assertions.assertTrue(WrapperTypes.isWrapperTypeWithoutConversation(Boolean.class));

        Assertions.assertFalse(WrapperTypes.isWrapperTypeWithoutConversation(Date.class));
        Assertions.assertFalse(WrapperTypes.isWrapperTypeWithoutConversation(Object.class));
    }

    /**
     * Test wrapper types.
     */
    @Test
    void testWrapperTypes(){
        Assertions.assertTrue(WrapperTypes.isWrapperType(char.class));
        Assertions.assertTrue(WrapperTypes.isWrapperType(byte.class));
        Assertions.assertTrue(WrapperTypes.isWrapperType(short.class));
        Assertions.assertTrue(WrapperTypes.isWrapperType(void.class));
        Assertions.assertTrue(WrapperTypes.isWrapperType(Character.class));
        Assertions.assertTrue(WrapperTypes.isWrapperType(Byte.class));
        Assertions.assertTrue(WrapperTypes.isWrapperType(Short.class));
        Assertions.assertTrue(WrapperTypes.isWrapperType(Void.class));
        Assertions.assertTrue(WrapperTypes.isWrapperType(String.class));
        Assertions.assertTrue(WrapperTypes.isWrapperType(BigDecimal.class));
        Assertions.assertTrue(WrapperTypes.isWrapperType(int.class));
        Assertions.assertTrue(WrapperTypes.isWrapperType(long.class));
        Assertions.assertTrue(WrapperTypes.isWrapperType(boolean.class));
        Assertions.assertTrue(WrapperTypes.isWrapperType(java.sql.Timestamp.class));

        Assertions.assertFalse(WrapperTypes.isWrapperType(Object.class));
    }

}
