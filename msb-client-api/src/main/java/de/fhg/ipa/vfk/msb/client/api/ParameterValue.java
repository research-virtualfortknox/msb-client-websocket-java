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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ParameterValue.
 *
 * @author des
 */
public class ParameterValue {

    private static final Logger LOG = LoggerFactory.getLogger(ParameterValue.class);

    /**
     * The value.
     */
    private Object value;

    /**
     * The type.
     */
    @JsonIgnore
    private PrimitiveType type = PrimitiveType.STRING;

    /**
     * The format.
     */
    @JsonIgnore
    private PrimitiveFormat format;

    /**
     * Instantiates a new parameter value.
     */
    public ParameterValue() {
        // empty constructor (p.e. for reflection)
    }

    /**
     * Instantiates a new parameter value.
     *
     * @param value the value
     */
    public ParameterValue(Object value) {
        this.value = value;
    }

    /**
     * Instantiates a new parameter value.
     *
     * @param value the value
     * @param type  the type
     */
    public ParameterValue(Object value, PrimitiveType type) {
        this.value = value;
        this.type = type;
        switch (type) {
            case NUMBER:
                this.format = PrimitiveFormat.DOUBLE;
                LOG.warn("missing format for type {}, set format {}", type, format);
                break;
            case INTEGER:
                this.format = PrimitiveFormat.INT64;
                LOG.warn("missing format for type {}, set format {}", type, format);
                break;
            case OBJECT:
            case ARRAY:
                LOG.error("item/properties definition currently not supporter for type {}", type);
                break;
            default: break;
        }
    }

    public ParameterValue(Object value, PrimitiveFormat format) {
        this.value = value;
        this.format = format;

        if (format != null) {
            this.type = format.getCorrespondingType();
        }
    }

    /**
     * Instantiates a new parameter value.
     *
     * @param value  the value
     * @param type   the type
     * @param format the format
     */
    public ParameterValue(Object value, PrimitiveType type, PrimitiveFormat format) {
        this.value = value;
        this.type = type;
        this.format = format;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public PrimitiveType getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public void setType(PrimitiveType type) {
        this.type = type;
    }

    /**
     * Gets type string.
     *
     * @return the type string
     */
    @JsonGetter("type")
    private String getTypeString() {
        if (type != null) {
            return type.toString();
        }
        return null;
    }

    /**
     * Sets type string.
     *
     * @param type the type
     */
    @JsonSetter("type")
    private void setTypeString(String type) {
        this.type = PrimitiveType.parse(type);
    }

    /**
     * Gets the format.
     *
     * @return the format
     */
    public PrimitiveFormat getFormat() {
        return format;
    }

    /**
     * Sets the format.
     *
     * @param format the new format
     */
    public void setFormat(PrimitiveFormat format) {
        this.format = format;
    }

    /**
     * Gets format string.
     *
     * @return the format string
     */
    @JsonGetter("format")
    private String getFormatString() {
        if (format != null) {
            return format.toString();
        }
        return null;
    }

    /**
     * Sets format string.
     *
     * @param format the format
     */
    @JsonSetter("format")
    private void setFormatString(String format) {
        this.format = PrimitiveFormat.parse(format);
    }


}

