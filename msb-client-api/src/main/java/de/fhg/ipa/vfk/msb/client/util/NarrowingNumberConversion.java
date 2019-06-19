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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * The Class NarrowingNumberConversion.
 * (superclass)                                Number
 *                                      _________/\___________________________________________________
 *                                    /      |        |        |        |         |        |          \
 * (concrete subclasses)            Byte   Short   Integer   Long  BigInteger   Float    Double   BigDecimal
 *
 * @author des
 */
public final class NarrowingNumberConversion {

	private static final Logger LOG = LoggerFactory.getLogger(NarrowingNumberConversion.class);

	private NarrowingNumberConversion() {}

	/**
	 * Convert.
	 *
	 * @param outputType
	 *            the output type
	 * @param value
	 *            the value
	 * @return the number
	 */
	public static Number convert(Class outputType, Number value) {
		if (value == null) {
			return null;
		} else if (Byte.class.equals(outputType) || byte.class.equals(outputType)) {
			return value.byteValue();
		} else if (Short.class.equals(outputType) || short.class.equals(outputType)) {
			return value.shortValue();
		} else if (Integer.class.equals(outputType) || int.class.equals(outputType)) {
			return value.intValue();
		} else if (Long.class.equals(outputType) || long.class.equals(outputType)) {
			return value.longValue();
		} else if (Float.class.equals(outputType) || float.class.equals(outputType)) {
			return value.floatValue();
		} else if (Double.class.equals(outputType) || double.class.equals(outputType)) {
			return value.doubleValue();
		} else if (BigInteger.class.equals(outputType)) {
			try {
				return (BigInteger) value;
			} catch (ClassCastException e) {
				LOG.warn("ClassCastException to BigInteger",e);
				return BigInteger.valueOf(value.longValue());
			}
		} else if (BigDecimal.class.equals(outputType)) {
			try {
				return (BigDecimal) value;
			} catch (ClassCastException e) {
				LOG.warn("ClassCastException to BigDecimal",e);
				return BigDecimal.valueOf(value.doubleValue());
			}
		}
		throw new TypeMismatchException(value, outputType);
	}
}
