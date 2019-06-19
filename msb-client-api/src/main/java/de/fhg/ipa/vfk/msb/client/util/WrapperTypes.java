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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * The Class de.fhg.ipa.vfk.msb.client.util.WrapperTypes.
 *
 * @author des
 */
public final class WrapperTypes {

	private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();
    private static final Set<Class<?>> WRAPPER_TYPES_WITHOUT_CONVERSION = getWrapperTypesWithoutConversion();
    private static final Set<Class<?>> NUMBER_WRAPPER_TYPES = getNumberWrapperTypes();
    private static final Set<Class<?>> BOOLEAN_WRAPPER_TYPES = getBooleanWrapperTypes();
    private static final Set<Class<?>> DATE_WRAPPER_TYPES = getDateWrapperTypes();

    private WrapperTypes(){}

    /**
     * Is number wrapper type boolean.
     *
     * @param clazz the clazz
     * @return the boolean
     */
    public static boolean isNumberWrapperType(Class<?> clazz) {
        return NUMBER_WRAPPER_TYPES.contains(clazz);
    }

    /**
     * Is boolean wrapper type boolean.
     *
     * @param clazz the clazz
     * @return the boolean
     */
    public static boolean isBooleanWrapperType(Class<?> clazz) {
        return BOOLEAN_WRAPPER_TYPES.contains(clazz);
    }

    /**
     * Is date wrapper type boolean.
     *
     * @param clazz the clazz
     * @return the boolean
     */
    public static boolean isDateWrapperType(Class<?> clazz) {
        return DATE_WRAPPER_TYPES.contains(clazz);
    }

    /**
     * Is wrapper type without conversation boolean.
     *
     * @param clazz the clazz
     * @return the boolean
     */
    public static boolean isWrapperTypeWithoutConversation(Class<?> clazz) {
        return WRAPPER_TYPES_WITHOUT_CONVERSION.contains(clazz);
    }

    /**
     * Checks if is wrapper type.
     *
     * @param clazz the clazz
     * @return true, if is wrapper type
     */
    public static boolean isWrapperType(Class<?> clazz) {
		return WRAPPER_TYPES.contains(clazz);
	}

    private static Set<Class<?>> getBooleanWrapperTypes() {
        Set<Class<?>> ret = new HashSet<>();
        ret.add(Boolean.class);
        ret.add(boolean.class);
        return ret;
    }

    private static Set<Class<?>> getNumberWrapperTypes() {
        Set<Class<?>> ret = new HashSet<>();
        ret.add(Integer.class);
        ret.add(int.class);
        ret.add(Long.class);
        ret.add(long.class);
        ret.add(Float.class);
        ret.add(float.class);
        ret.add(Double.class);
        ret.add(double.class);

        return ret;
    }

    private static Set<Class<?>> getDateWrapperTypes() {
        Set<Class<?>> ret = new HashSet<>();
        ret.add(Date.class);
        ret.add(java.sql.Date.class);
        ret.add(java.sql.Time.class);
        ret.add(java.sql.Timestamp.class);
        return ret;
    }

    private static Set<Class<?>> getWrapperTypesWithoutConversion() {
        Set<Class<?>> ret = new HashSet<>();
        ret.add(Character.class);
        ret.add(char.class);
        ret.add(Byte.class);
        ret.add(byte.class);
        ret.add(Short.class);
        ret.add(short.class);
        ret.add(Void.class);
        ret.add(void.class);
        ret.add(String.class);
        ret.add(BigInteger.class);
        ret.add(BigDecimal.class);
        ret.addAll(getBooleanWrapperTypes());
        ret.addAll(getNumberWrapperTypes());
        return ret;
    }

	private static Set<Class<?>> getWrapperTypes() {
		Set<Class<?>> ret = new HashSet<>();
        ret.addAll(getWrapperTypesWithoutConversion());
        ret.addAll(getDateWrapperTypes());
		return ret;
	}

}
