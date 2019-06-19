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

package de.fhg.ipa.vfk.msb.client.api;

import java.util.Locale;

/**
 * The Enum PrimitiveFormats.
 *
 * @author des
 */
public enum PrimitiveFormat {

    /**
     * The int32.
     */
    INT32("int32"),
    /**
     * The int64.
     */
    INT64("int64"),
    /**
     * The float.
     */
    FLOAT("float"),
    /**
     * The double.
     */
    DOUBLE("double"),
    /**
     * The byte.
     */
    BYTE("byte"),
    /**
     * The date.
     */
    DATE("date"),
    /**
     * The date time.
     */
    DATE_TIME("date-time");

    /**
     * The name.
     */
    private String name;

    /**
     * Instantiates a new primitive formats.
     *
     * @param value the value
     */
    PrimitiveFormat(String value) {
        this.name = value;
    }

    /**
     * Parses the.
     *
     * @param value the value
     * @return the primitive formats
     */
    public static PrimitiveFormat parse(String value) {
        if (value != null) {
            value = value.toLowerCase(Locale.ENGLISH);
            switch (value) {
                case "int32":
                    return INT32;
                case "int64":
                    return INT64;
                case "float":
                    return FLOAT;
                case "double":
                    return DOUBLE;
                case "byte":
                    return BYTE;
                case "date":
                    return DATE;
                case "date-time":
                    return DATE_TIME;
                default:
                    return null;
            }
        }
        return null;
    }

    /**
     * To string.
     *
     * @return the string
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return name;
    }

    public PrimitiveType getCorrespondingType() {
        switch (this) {
            case BYTE:
            case DATE:
            case DATE_TIME:
                return PrimitiveType.STRING;
            case DOUBLE:
            case FLOAT:
                return PrimitiveType.NUMBER;
            case INT32:
            case INT64:
                return PrimitiveType.INTEGER;
        }
        return null;
    }
}
