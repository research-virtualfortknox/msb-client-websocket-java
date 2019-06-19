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

import de.fhg.ipa.vfk.msb.client.annotation.SelfDescription;
import de.fhg.ipa.vfk.msb.client.api.Application;
import de.fhg.ipa.vfk.msb.client.api.Gateway;
import de.fhg.ipa.vfk.msb.client.api.Service;
import de.fhg.ipa.vfk.msb.client.api.SmartObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * The type Self description parser.
 *
 * @author des
 */
public final class SelfDescriptionParser {

    private static final Logger LOG = LoggerFactory.getLogger(SelfDescriptionParser.class);

    private SelfDescriptionParser() {}

    /**
     * Parse service.
     *
     * @param packagePath the package path
     * @return the service
     */
    public static Service parse(String packagePath) {
        LOG.debug("find and parse service at package path: {}", packagePath);
        try {
            List<Class<?>> classes = PackageScanner.findMyType(packagePath, SelfDescription.class);
            if (!classes.isEmpty()) {
                return parseService(classes);
            }
        } catch (ClassNotFoundException | IOException e) {
            LOG.error("Can not find or parse @SelfDescription annotation",e);
        }
        return null;
    }

    private static Service parseService(List<Class<?>> annotatedClasses) {
        LOG.debug("parse service for found classes: {}", annotatedClasses);
        Service object = null;
        for (Class<?> annotatedClasse : annotatedClasses) {
            if (annotatedClasse.isAnnotationPresent(SelfDescription.class)) {
                SelfDescription selfDescription = annotatedClasse.getAnnotation(SelfDescription.class);
                object = parseService(selfDescription);
            }
        }
        return object;

    }

    private static Service parseService(SelfDescription selfDescription) {
        LOG.debug("parse service: {}", selfDescription);
        String description = selfDescription.description();
        String name = selfDescription.name();
        String token = selfDescription.token();
        String uuid = selfDescription.uuid();
        switch (selfDescription.type()) {
            case SMART_OBJECT:
                return new SmartObject(uuid, name, description, token);
            case GATEWAY:
                return new Gateway(uuid,name,description,token);
            default:
                return new Application(uuid, name, description, token);
        }
    }

}
