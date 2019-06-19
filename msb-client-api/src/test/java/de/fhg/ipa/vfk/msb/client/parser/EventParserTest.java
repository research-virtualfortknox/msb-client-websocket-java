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
import org.junit.Assert;
import org.junit.Test;
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
public class EventParserTest {

    private final static Logger LOG = LoggerFactory.getLogger(EventParserTest.class);

    /**
     * Test parse event by package scan.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void testParseEventByPackageScan() throws JsonProcessingException {
        Map<String, EventReference> eventMap = new HashMap<>();
        List<Event> events = EventParser.parseEvents("uuid","de.fhg.ipa.vfk.msb.client.parser", eventMap);

        LOG.debug("eventReference: {}",eventMap);
        Assert.assertEquals("events size not equals", 3, events.size());
        Assert.assertEquals("event references size not equals", 3, eventMap.size());

        EventReference eventReference = eventMap.get("uuid_START");
        Assert.assertNotNull("event reference is null", eventReference);
        Assert.assertNull("data format of event reference is null", eventReference.getDataFormat());
        Assert.assertNotNull("data type of event reference is null", eventReference.getDataType());
        Assert.assertNotNull("name of event reference is null", eventReference.getName());
        Assert.assertNotNull("priority of event reference is null", eventReference.getPriority());

        Event event = eventReference.getEvent();
        Assert.assertNotNull("event of event reference is null", event);
        Assert.assertTrue("events not contains event",events.contains(event));
        Assert.assertEquals("eventId not equals", "START", event.getEventId());
        Assert.assertEquals("name not equals", "Start", event.getName());
        Assert.assertEquals("description not equals", "started", event.getDescription());
        Assert.assertNull("data format is not null", event.getDataFormat());

        EventReference eventReference2 = eventMap.get("uuid_WAIT");
        Assert.assertNotNull("event reference is null", eventReference2);
        Assert.assertNotNull("data format of event reference is null", eventReference2.getDataFormat());
        Assert.assertNotNull("data type of event reference is null", eventReference2.getDataType());

        Event event2 = eventReference2.getEvent();
        Assert.assertNotNull("event of event reference is null", event2);
        Assert.assertTrue("events not contains event",events.contains(event2));
        Assert.assertEquals("eventId not equals", "WAIT", event2.getEventId());
        Assert.assertEquals("name not equals", "Wait", event2.getName());
        Assert.assertEquals("description not equals", "start waiting", event2.getDescription());
        Assert.assertNotNull("data format is null", event2.getDataFormat());
    }

    /**
     * Test parse event by instance.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void testParseEventByInstance() throws JsonProcessingException {
        Map<String, EventReference> eventMap = new HashMap<>();
        List<Class<?>> annotatedClasses = new ArrayList<>();
        annotatedClasses.add(EventParserTest.class);
        List<Event> events = EventParser.parseEvents("uuid",annotatedClasses, eventMap);

        LOG.debug("eventReference: {}",eventMap);
        Assert.assertEquals("events size not equals", 2, events.size());
        Assert.assertEquals("event references size not equals", 2, eventMap.size());

        EventReference eventReference = eventMap.get("uuid_START");
        Assert.assertNotNull("event reference is null", eventReference);
        Assert.assertNull("data format of event reference is null", eventReference.getDataFormat());
        Assert.assertNotNull("data type of event reference is null", eventReference.getDataType());
        Assert.assertNotNull("name of event reference is null", eventReference.getName());
        Assert.assertNotNull("priority of event reference is null", eventReference.getPriority());

        Event event = eventReference.getEvent();
        Assert.assertNotNull("event of event reference is null", event);
        Assert.assertTrue("events not contains event",events.contains(event));
        Assert.assertEquals("eventId not equals", "START", event.getEventId());
        Assert.assertEquals("name not equals", "Start", event.getName());
        Assert.assertEquals("description not equals", "started", event.getDescription());
        Assert.assertNull("data format is not null", event.getDataFormat());

        EventReference eventReference2 = eventMap.get("uuid_WAIT");
        Assert.assertNotNull("event reference is null", eventReference2);
        Assert.assertNotNull("data format of event reference is null", eventReference2.getDataFormat());
        Assert.assertNotNull("data type of event reference is null", eventReference2.getDataType());
        Assert.assertNotNull("name of event reference is null", eventReference2.getName());
        Assert.assertNotNull("priority of event reference is null", eventReference2.getPriority());

        Event event2 = eventReference2.getEvent();
        Assert.assertNotNull("event of event reference is null", event2);
        Assert.assertTrue("events not contains event",events.contains(event2));
        Assert.assertEquals("eventId not equals", "WAIT", event2.getEventId());
        Assert.assertEquals("name not equals", "Wait", event2.getName());
        Assert.assertEquals("description not equals", "start waiting", event2.getDescription());
        Assert.assertNotNull("data format is null", event2.getDataFormat());
    }

    /**
     * Test add event.
     */
    @Test
    public void testAddEvent() {
        Map<String, EventReference> eventMap = new HashMap<>();
        List<Event> events = new ArrayList<>();
        EventParser.addEvent("uuid",eventMap,events,"TEST","test","", NullNode.class,EventPriority.MEDIUM);

        EventReference eventReference = eventMap.get("uuid_TEST");
        Assert.assertNotNull("event reference is null", eventReference);
        Assert.assertNull("data format of event reference is null", eventReference.getDataFormat());
        Assert.assertNotNull("data type of event reference is null", eventReference.getDataType());
        Assert.assertNotNull("name of event reference is null", eventReference.getName());
        Assert.assertNotNull("priority of event reference is null", eventReference.getPriority());

        Event event = eventReference.getEvent();
        Assert.assertNotNull("event of event reference is null", event);
        Assert.assertTrue("events not contains event",events.contains(event));
        Assert.assertEquals("eventId not equals", "TEST", event.getEventId());
        Assert.assertEquals("name not equals", "test", event.getName());
        Assert.assertEquals("description not equals", "", event.getDescription());
        Assert.assertNull("data format is not null", event.getDataFormat());

        LOG.debug("eventReference: {}",eventReference.toString());
    }
}
