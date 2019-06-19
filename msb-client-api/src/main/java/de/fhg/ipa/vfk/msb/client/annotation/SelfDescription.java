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
package de.fhg.ipa.vfk.msb.client.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;

/**
 * The Interface SelfDescription.
 *
 * @author des
 */
@Documented
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SelfDescription {

    /**
     * Uuid.
     *
     * @return the string
     */
    String uuid();

    /**
     * Name.
     *
     * @return the string
     */
    String name();

    /**
     * Description string.
     *
     * @return the string
     */
    String description();

    /**
     * Token string.
     *
     * @return the string
     */
    String token();

    /**
     * Type type.
     *
     * @return the type
     */
    Type type() default Type.APPLICATION;

    /**
     * The enum Type.
     */
    enum Type{

        /**
         * Smart object type.
         */
        SMART_OBJECT("SmartObject"),

        /**
         * Application type.
         */
        APPLICATION("Application"),

        /**
         * Gateway type.
         */
        GATEWAY("Gateway");


        private String name;

        Type(String name) {
            this.name = name;
        }

        /**
         * Parses the type.
         *
         * @param value the value
         * @return the typs
         */
        public static Type parse(String value) {
            if (value != null) {
                value = value.toLowerCase(Locale.ENGLISH);
                switch (value) {
                    case "smartobject":
                        return SMART_OBJECT;
                    case "application":
                        return APPLICATION;
                    case "gateway":
                        return GATEWAY;
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

}
