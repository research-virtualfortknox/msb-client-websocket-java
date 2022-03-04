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

import de.fhg.ipa.vfk.msb.client.annotation.Events;
import de.fhg.ipa.vfk.msb.client.annotation.FunctionCall;
import de.fhg.ipa.vfk.msb.client.annotation.FunctionHandler;
import de.fhg.ipa.vfk.msb.client.annotation.SelfDescription;
import de.fhg.ipa.vfk.msb.client.parser.entities.TestApplication;
import de.fhg.ipa.vfk.msb.client.parser.entities.TestGateway;
import de.fhg.ipa.vfk.msb.client.parser.entities.TestSmartObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

/**
 * The type Package scanner test.
 *
 * @author des
 */
class PackageScannerTest {

    /**
     * Test package scan.
     *
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    @Test
    void testPackageScan() throws IOException, ClassNotFoundException {
        List<Class<?>> found = PackageScanner.findMyType("de.fhg.ipa.vfk.msb.client.parser.entities", SelfDescription.class);
        Assertions.assertNotNull(found);
        Assertions.assertEquals(3,found.size());
        Assertions.assertTrue(TestApplication.class.equals(found.get(0)) || TestSmartObject.class.equals(found.get(0)) ||
                TestGateway.class.equals(found.get(0)));
    }

    /**
     * Test package scan multiple.
     *
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    @Test
    void testPackageScanMultiple() throws IOException, ClassNotFoundException {
        List<Class<?>> found = PackageScanner.findMyTypes("de.fhg.ipa.vfk.msb.client.parser", new Class[] { FunctionHandler.class, FunctionCall.class });
        Assertions.assertNotNull(found);
        Assertions.assertEquals(2,found.size());
        Assertions.assertTrue(found.contains(FunctionParserTest.class));
        Assertions.assertTrue(found.contains(FunctionParserTest.FailingClazz.class));
    }

    /**
     * Test package scan nothing.
     *
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    @Test
    void testPackageScanNothing() throws IOException, ClassNotFoundException {
        List<Class<?>> found = PackageScanner.findMyTypes("dead.package.path", new Class[] { Events.class });
        Assertions.assertNotNull(found,"list is null");
        Assertions.assertTrue(found.isEmpty(), "found classes");
    }

}
