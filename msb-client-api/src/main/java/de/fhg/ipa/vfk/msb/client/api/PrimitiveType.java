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
 * The Enum PrimitiveTyps.
 *
 * @author des
 */
public enum PrimitiveType {

    /**
     * The integer.
     */
    INTEGER("integer"),

    /**
     * The number.
     */
    NUMBER("number"),

    /**
     * The string.
     */
    STRING("string"),

    /**
     * The boolean.
     */
    BOOLEAN("boolean"),

    /**
     * The array.
     */
    ARRAY("array"),

    /**
     * The object.
     */
    OBJECT("object");

    /**
     * The name.
     */
    private String name;

    /**
     * Instantiates a new primitive typs.
     *
     * @param value the value
     */
    PrimitiveType(String value) {
        this.name = value;
    }

    /**
     * Parses the primitive type.
     *
     * @param value the value
     * @return the primitive typs
     */
    public static PrimitiveType parse(String value) {
        if (value != null) {
            value = value.toLowerCase(Locale.ENGLISH);
            switch (value) {
                case "integer":
                    return INTEGER;
                case "number":
                    return NUMBER;
                case "string":
                    return STRING;
                case "boolean":
                    return BOOLEAN;
                case "array":
                    return ARRAY;
                case "object":
                    return OBJECT;
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

}
