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

import de.fhg.ipa.vfk.msb.client.api.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * The type Self description parser test.
 *
 * @author des
 */
public class SelfDescriptionParserTest {

    /**
     * The constant UUID.
     */
    public static final String UUID = "df61a143-6dab-471a-88b4-8feddb4c9e71";
    /**
     * The constant NAME.
     */
    public static final String NAME = "TestClient1";
    /**
     * The constant DESCRIPTION.
     */
    public static final String DESCRIPTION = "Test client";
    /**
     * The constant TOKEN.
     */
    public static final String TOKEN = "57e721c9bcdf";

    /**
     * Test parse self description.
     */
    @Test
    void testParseSelfDescription() throws IOException {
        Service service = SelfDescriptionParser.parse("de.fhg.ipa.vfk.msb.client.parser");
        Assertions.assertEquals(UUID, service.getUuid(), "uuid not equals");
        Assertions.assertEquals(NAME, service.getName(), "name not equals");
        Assertions.assertEquals(DESCRIPTION, service.getDescription(),"description not equals");
        Assertions.assertEquals(TOKEN, service.getToken(), "token not equals");
        Assertions.assertTrue("Application".equals(service.getType()) || "SmartObject".equals(service.getType()) || "Gateway".equals(service.getType()), "type not equals");
    }

    @Test
    void testNoSelfDescription() throws IOException {
        Service service = SelfDescriptionParser.parse("dead.package.path");
        Assertions.assertNull(service, "found a service");
    }

}