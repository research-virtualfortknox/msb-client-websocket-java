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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.NullNode;
import de.fhg.ipa.vfk.msb.client.annotation.EventDeclaration;
import de.fhg.ipa.vfk.msb.client.annotation.Events;
import de.fhg.ipa.vfk.msb.client.api.Event;
import de.fhg.ipa.vfk.msb.client.api.messages.EventPriority;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Event parser test.
 *
 * @author des
 */
@Events({
        @EventDeclaration(eventId = "START", name = "Start", description = "started", dataType = Void.class),
        @EventDeclaration(eventId = "WAIT", name = "Wait", description = "start waiting", dataType = String[].class),

})
class EventParserTest {

    private final static Logger LOG = LoggerFactory.getLogger(EventParserTest.class);

    /**
     * Test parse event by package scan.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    void testParseEventByPackageScan() throws JsonProcessingException {
        Map<String, EventReference> eventMap = new HashMap<>();
        List<Event> events = EventParser.parseEvents("uuid","de.fhg.ipa.vfk.msb.client.parser", eventMap);

        LOG.debug("eventReference: {}",eventMap);
        Assertions.assertEquals(3, events.size(), "events size not equals");
        Assertions.assertEquals(3, eventMap.size(), "event references size not equals");

        EventReference eventReference = eventMap.get("uuid_START");
        Assertions.assertNotNull(eventReference, "event reference is null");
        Assertions.assertNull(eventReference.getDataFormat(), "data format of event reference is null");
        Assertions.assertNotNull(eventReference.getDataType(), "data type of event reference is null");
        Assertions.assertNotNull(eventReference.getName(), "name of event reference is null");
        Assertions.assertNotNull(eventReference.getPriority(), "priority of event reference is null");

        Event event = eventReference.getEvent();
        Assertions.assertNotNull(event, "event of event reference is null");
        Assertions.assertTrue(events.contains(event), "events not contains event");
        Assertions.assertEquals( "START", event.getEventId(), "eventId not equals");
        Assertions.assertEquals( "Start", event.getName(),"name not equals");
        Assertions.assertEquals("started", event.getDescription(), "description not equals");
        Assertions.assertNull(event.getDataFormat(), "data format is not null");

        EventReference eventReference2 = eventMap.get("uuid_WAIT");
        Assertions.assertNotNull(eventReference2, "event reference is null");
        Assertions.assertNotNull(eventReference2.getDataFormat(),"data format of event reference is null");
        Assertions.assertNotNull(eventReference2.getDataType(),"data type of event reference is null");
        Assertions.assertNotNull(eventReference2.getName(), "name of event reference is null");
        Assertions.assertNotNull(eventReference2.getPriority(),"priority of event reference is null");

        Event event2 = eventReference2.getEvent();
        Assertions.assertNotNull(event2,"event of event reference is null");
        Assertions.assertTrue(events.contains(event2),"events not contains event");
        Assertions.assertEquals("WAIT", event2.getEventId(),"eventId not equals");
        Assertions.assertEquals("Wait", event2.getName(), "name not equals");
        Assertions.assertEquals("start waiting", event2.getDescription(), "description not equals");
        Assertions.assertNotNull(event2.getDataFormat(), "data format is null");
    }

    /**
     * Test parse event by instance.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    void testParseEventByInstance() throws JsonProcessingException {
        Map<String, EventReference> eventMap = new HashMap<>();
        List<Class<?>> annotatedClasses = new ArrayList<>();
        annotatedClasses.add(EventParserTest.class);
        List<Event> events = EventParser.parseEvents("uuid",annotatedClasses, eventMap);

        LOG.debug("eventReference: {}",eventMap);
        Assertions.assertEquals(2, events.size(), "events size not equals");
        Assertions.assertEquals(2, eventMap.size(), "event references size not equals");

        EventReference eventReference = eventMap.get("uuid_START");
        Assertions.assertNotNull(eventReference, "event reference is null");
        Assertions.assertNull(eventReference.getDataFormat(), "data format of event reference is null");
        Assertions.assertNotNull(eventReference.getDataType(), "data type of event reference is null");
        Assertions.assertNotNull(eventReference.getName(), "name of event reference is null");
        Assertions.assertNotNull(eventReference.getPriority(), "priority of event reference is null");

        Event event = eventReference.getEvent();
        Assertions.assertNotNull(event, "event of event reference is null");
        Assertions.assertTrue(events.contains(event), "events not contains event");
        Assertions.assertEquals( "START", event.getEventId(), "eventId not equals");
        Assertions.assertEquals( "Start", event.getName(),"name not equals");
        Assertions.assertEquals("started", event.getDescription(), "description not equals");
        Assertions.assertNull(event.getDataFormat(), "data format is not null");

        EventReference eventReference2 = eventMap.get("uuid_WAIT");
        Assertions.assertNotNull(eventReference2, "event reference is null");
        Assertions.assertNotNull(eventReference2.getDataFormat(),"data format of event reference is null");
        Assertions.assertNotNull(eventReference2.getDataType(),"data type of event reference is null");
        Assertions.assertNotNull(eventReference2.getName(), "name of event reference is null");
        Assertions.assertNotNull(eventReference2.getPriority(),"priority of event reference is null");

        Event event2 = eventReference2.getEvent();
        Assertions.assertNotNull(event2,"event of event reference is null");
        Assertions.assertTrue(events.contains(event2),"events not contains event");
        Assertions.assertEquals("WAIT", event2.getEventId(),"eventId not equals");
        Assertions.assertEquals("Wait", event2.getName(), "name not equals");
        Assertions.assertEquals("start waiting", event2.getDescription(), "description not equals");
        Assertions.assertNotNull(event2.getDataFormat(), "data format is null");
    }

    /**
     * Test add event.
     */
    @Test
    void testAddEvent() {
        Map<String, EventReference> eventMap = new HashMap<>();
        List<Event> events = new ArrayList<>();
        EventParser.addEvent("uuid",eventMap,events,"TEST","test","", NullNode.class,EventPriority.MEDIUM);

        EventReference eventReference = eventMap.get("uuid_TEST");
        Assertions.assertNotNull(eventReference, "event reference is null");
        Assertions.assertNull(eventReference.getDataFormat(), "data format of event reference is null");
        Assertions.assertNotNull(eventReference.getDataType(), "data type of event reference is null");
        Assertions.assertNotNull(eventReference.getName(), "name of event reference is null");
        Assertions.assertNotNull(eventReference.getPriority(), "priority of event reference is null");

        Event event = eventReference.getEvent();
        Assertions.assertNotNull(event, "event of event reference is null");
        Assertions.assertTrue(events.contains(event), "events not contains event");
        Assertions.assertEquals( "TEST", event.getEventId(), "eventId not equals");
        Assertions.assertEquals( "test", event.getName(),"name not equals");
        Assertions.assertEquals("", event.getDescription(), "description not equals");
        Assertions.assertNull(event.getDataFormat(), "data format is not null");

        LOG.debug("eventReference: {}",eventReference.toString());
    }
}
