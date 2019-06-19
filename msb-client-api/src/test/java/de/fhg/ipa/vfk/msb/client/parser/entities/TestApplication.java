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

package de.fhg.ipa.vfk.msb.client.parser.entities;

import de.fhg.ipa.vfk.msb.client.annotation.EventDeclaration;
import de.fhg.ipa.vfk.msb.client.annotation.SelfDescription;
import de.fhg.ipa.vfk.msb.client.parser.SelfDescriptionParserTest;

/**
 * The type Test application.
 *
 * @author des
 */
@EventDeclaration(eventId = "STOP", name = "Stop", description = "stop")
@SelfDescription(uuid = SelfDescriptionParserTest.UUID, name = SelfDescriptionParserTest.NAME, description = SelfDescriptionParserTest.DESCRIPTION, token = SelfDescriptionParserTest.TOKEN)
public class TestApplication {
}
