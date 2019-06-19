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

package de.fhg.ipa.vfk.msb.client.websocket.annotation;

import de.fhg.ipa.vfk.msb.client.annotation.ConfigurationParam;
import de.fhg.ipa.vfk.msb.client.annotation.EventDeclaration;
import de.fhg.ipa.vfk.msb.client.annotation.Events;
import de.fhg.ipa.vfk.msb.client.annotation.SelfDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Annotation test client.
 *
 * @author des
 */
@Events({
        @EventDeclaration(description = "periodical transmitted event", name = "Event pulse", eventId = "PULSE"),
        @EventDeclaration(dataType = float.class, description = "Current temperature", name = "Temperature", eventId = "TEMPERATURE"),
})
@SelfDescription(uuid = "df61a143-6dab-471a-88b4-8feddb4c9e45",name = "AnnotationTestClient",description = "Annotation Test client",token = "token", type = SelfDescription.Type.APPLICATION)
public class AnnotationTestClient {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationTestClient.class);

    /**
     * The Url.
     */
    final String url = "ws://localhost:8085";

    /**
     * The Test param 1.
     */
    @ConfigurationParam(name = "helloParam")
    public String testParam1 = "hello";

    /**
     * The Test param 2.
     */
    @ConfigurationParam
    public int testParam2 = 12345;

    /**
     * The Test param 3.
     */
    @ConfigurationParam
    public Double testParam3 = 1.234D;

}
