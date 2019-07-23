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
     * Int 32 primitive format, signed 32 bits.
     */
    INT32("int32"),

    /**
     * Int 64 primitive format, signed 64 bits
     */
    INT64("int64"),

    /**
     * Float primitive format.
     */
    FLOAT("float"),

    /**
     * Double primitive format.
     */
    DOUBLE("double"),

    /**
     * Byte primitive format, base64 encoded characters
     */
    BYTE("byte"),
    /**
     * Binary primitive format, any sequence of octets.
     */
    BINARY("binary"),

    /**
     * Date primitive format, as defined by full-date - RFC3339
     */
    DATE("date"),

    /**
     * Date time primitive format, as defined by date-time - RFC3339
     */
    DATE_TIME("date-time"),

    /**
     * Password primitive format, used to hint UIs the input needs to be obscured.
     */
    PASSWORD("password");

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
                case "binary":
                    return BINARY;
                case "date":
                    return DATE;
                case "date-time":
                    return DATE_TIME;
                case "password":
                    return PASSWORD;
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

    /**
     * Gets corresponding type.
     *
     * @return the corresponding type
     */
    public PrimitiveType getCorrespondingType() {
        switch (this) {
            case BYTE:
            case BINARY:
            case DATE:
            case DATE_TIME:
            case PASSWORD:
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
