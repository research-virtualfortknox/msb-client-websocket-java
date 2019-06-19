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

import com.fasterxml.jackson.databind.node.NullNode;
import de.fhg.ipa.vfk.msb.client.api.messages.EventPriority;

/**
 * The Interface EventDeclaration.
 *
 * @author des
 */
@Documented
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventDeclaration {

    /**
     * Event id.
     *
     * @return the string
     */
    String eventId() default "";

    /**
     * Name.
     *
     * @return the string
     */
    String name() default "";

    /**
     * Description.
     *
     * @return the string
     */
    String description() default "";

    /**
     * Data type.
     *
     * @return the class
     */
    Class<?> dataType() default NullNode.class;

    /**
     * Priority event priority.
     *
     * @return the event priority
     */
    EventPriority priority() default EventPriority.LOW;

}
