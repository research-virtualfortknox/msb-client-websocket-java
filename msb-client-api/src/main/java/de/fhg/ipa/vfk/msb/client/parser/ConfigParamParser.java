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

package de.fhg.ipa.vfk.msb.client.parser;

import de.fhg.ipa.vfk.msb.client.annotation.ConfigurationParam;
import de.fhg.ipa.vfk.msb.client.annotation.EventDeclaration;
import de.fhg.ipa.vfk.msb.client.annotation.Events;
import de.fhg.ipa.vfk.msb.client.annotation.FunctionHandler;
import de.fhg.ipa.vfk.msb.client.annotation.SelfDescription;
import de.fhg.ipa.vfk.msb.client.api.Configuration;
import de.fhg.ipa.vfk.msb.client.api.ParameterValue;
import de.fhg.ipa.vfk.msb.client.util.DataFormatParser;
import de.fhg.ipa.vfk.msb.client.util.WrapperTypes;
import de.fhg.ipa.vfk.msb.client.api.PrimitiveFormat;
import de.fhg.ipa.vfk.msb.client.api.PrimitiveType;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.ByteArrayProperty;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.DateTimeProperty;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.FloatProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Config param parser.
 *
 * @author des
 */
public final class ConfigParamParser {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigParamParser.class);

    private ConfigParamParser() {}

    /**
     * Parse configuration.
     *
     * @param packagePath the package path
     * @return the configuration
     */
    public static Configuration parse(String packagePath) {
        LOG.debug("find and parse configuration parameteres at package path: {}", packagePath);
        try {
            List<Class<?>> classes = PackageScanner.findMyTypes(packagePath,
                    new Class[]{SelfDescription.class,EventDeclaration.class, Events.class, FunctionHandler.class});
            if (!classes.isEmpty()) {
                return parseConfig(classes);
            }
        } catch (ClassNotFoundException | IOException e) {
            LOG.warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Parse config configuration.
     *
     * @param annotatedClasses the annotated classes
     * @return the configuration
     */
    public static Configuration parseConfig(List<Class<?>> annotatedClasses) {
        LOG.debug("parse configuration parameters for found classes: {}", annotatedClasses);
        Map<String,ParameterValue> parameters = new HashMap<>();
        for (Class<?> annotatedClass : annotatedClasses) {
            if (annotatedClass.isAnnotationPresent(SelfDescription.class)||
                    annotatedClass.isAnnotationPresent(EventDeclaration.class)||
                    annotatedClass.isAnnotationPresent(Events.class)||
                    annotatedClass.isAnnotationPresent(FunctionHandler.class)) {
                // TODO: allow access of field over getter/setter if is private
                try {
                    parseParameterValue(annotatedClass,parameters);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    LOG.error("Instantiation error, check if a default constructor is defined at " + annotatedClass.getName(), e);
                }
            }
        }
        return new Configuration(parameters);
    }

    private static void parseParameterValue(Class<?> annotatedClass, Map<String, ParameterValue> parameters)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Object instance = annotatedClass.getDeclaredConstructor().newInstance();
        for (Field field : annotatedClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigurationParam.class)) {
                ConfigurationParam configurationParam = field.getAnnotation(ConfigurationParam.class);
                String name = !"".equalsIgnoreCase(configurationParam.name()) ? configurationParam.name() : field.getName();
                try {
                    ParameterValue parameterValue = parseConfigParam(field, instance);
                    parameters.put(name, parameterValue);
                } catch (IllegalAccessException e) {
                    LOG.error("IllegalAccessException at parameter " + name + ": ", e);
                }
            }
        }
    }

    private static ParameterValue parseConfigParam(Field field, Object instance) throws IllegalAccessException {
        LOG.debug("parse configuration parameter: {}",field);
        Class<?> fieldType = field.getType();
        Object value = field.get(instance);
        LOG.debug("field type: {}, value: {}", fieldType, value);
        if(WrapperTypes.isWrapperType(fieldType)) {
            LOG.debug("isWrapperType");
            Map<String, Object> dataFormat = DataFormatParser.parse(fieldType);
            Object dataObject = dataFormat.get("dataObject");

            if (dataObject instanceof RefProperty|| dataObject instanceof MapProperty) {
                return new ParameterValue(value, PrimitiveType.OBJECT);
            } else
            if ( dataObject instanceof ArrayProperty) {
                return new ParameterValue(value, PrimitiveType.ARRAY);
            } else
            if (dataObject instanceof IntegerProperty) {
                return new ParameterValue(value, PrimitiveFormat.INT32);
            } else
            if (dataObject instanceof LongProperty) {
                return new ParameterValue(value, PrimitiveFormat.INT64);
            } else
            if (dataObject instanceof FloatProperty) {
                return new ParameterValue(value, PrimitiveFormat.FLOAT);
            } else
            if (dataObject instanceof DoubleProperty) {
                return new ParameterValue(value, PrimitiveFormat.DOUBLE);
            } else
            if (dataObject instanceof BooleanProperty) {
                return new ParameterValue(value, PrimitiveType.BOOLEAN);
            } else
            if (dataObject instanceof DateProperty){
                return new ParameterValue(value, PrimitiveFormat.DATE);
            } else
            if (dataObject instanceof DateTimeProperty) {
                return new ParameterValue(value, PrimitiveFormat.DATE_TIME);
            } else
            if (dataObject instanceof ByteArrayProperty) {
                return new ParameterValue(value, PrimitiveFormat.BYTE);
            } else
            if (dataObject instanceof StringProperty) {
                return new ParameterValue(value, PrimitiveType.STRING);
            }
        }
        return null;
    }

}
