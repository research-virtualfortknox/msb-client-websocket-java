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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * The Class NarrowingNumberConversionTest.
 *
 * @author des
 */
class NarrowingNumberConversionTest {

	/**
	 * Test parse object.
	 */
	@Test
	void testNumberConversion() {
		Assertions.assertEquals(1, NarrowingNumberConversion.convert(Integer.class, new Integer(1)).intValue());
		Assertions.assertEquals(1.2D, NarrowingNumberConversion.convert(Double.class, 1.2D));
		Assertions.assertEquals(BigDecimal.valueOf(1.2), NarrowingNumberConversion.convert(BigDecimal.class, BigDecimal.valueOf(1.2)));
		Assertions.assertEquals(BigInteger.valueOf(1), NarrowingNumberConversion.convert(BigInteger.class, BigInteger.valueOf(1)));
		Assertions.assertEquals(Short.MAX_VALUE, NarrowingNumberConversion.convert(Short.class, Short.MAX_VALUE));
		Assertions.assertEquals(Byte.MAX_VALUE, NarrowingNumberConversion.convert(Byte.class, Byte.MAX_VALUE));
		Assertions.assertNull(NarrowingNumberConversion.convert(BigInteger.class, null));
	}
	
	/**
	 * Test number conversion type change.
	 */
	@Test
	void testNumberConversionTypeChange() {
		Assertions.assertEquals(1, NarrowingNumberConversion.convert(Integer.class, 1.2D));
		Assertions.assertEquals(1L, NarrowingNumberConversion.convert(Long.class, 1.2D));
		Assertions.assertEquals(1.2F, NarrowingNumberConversion.convert(Float.class, 1.2D));
		Assertions.assertEquals(BigDecimal.valueOf(1.2), NarrowingNumberConversion.convert(BigDecimal.class, 1.2));
		Assertions.assertEquals(BigInteger.valueOf(1), NarrowingNumberConversion.convert(BigInteger.class, 1));
		Assertions.assertEquals(Short.valueOf("1"), NarrowingNumberConversion.convert(Short.class, 1.2));
		Assertions.assertEquals(Byte.valueOf("1"), NarrowingNumberConversion.convert(Byte.class, 1.2));
	}

	/**
	 * Test number conversion exception.
	 */
	@Test
	void testNumberConversionException() {
		Assertions.assertThrows(TypeMismatchException.class,()->NarrowingNumberConversion.convert(String.class, new BigInteger("112")));
	}
	
}
