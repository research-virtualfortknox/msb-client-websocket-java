package de.fhg.ipa.vfk.msb.client.api.messages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EventPriorityTest {

    @Test
    void testIsEventPriority(){
        Assertions.assertFalse(EventPriority.isEventPriority(-1));
        Assertions.assertTrue(EventPriority.isEventPriority(0));
        Assertions.assertTrue(EventPriority.isEventPriority(1));
        Assertions.assertTrue(EventPriority.isEventPriority(2));
        Assertions.assertFalse(EventPriority.isEventPriority(3));
    }

    @Test
    void testParse(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> EventPriority.parse(-1));
        Assertions.assertEquals(EventPriority.LOW, EventPriority.parse(0));
        Assertions.assertEquals(EventPriority.MEDIUM, EventPriority.parse(1));
        Assertions.assertEquals(EventPriority.HIGH, EventPriority.parse(2));
        Assertions.assertThrows(IllegalArgumentException.class, ()-> EventPriority.parse(3));
    }

    @Test
    void testGetValue(){
        Assertions.assertEquals(0, EventPriority.LOW.getValue());
        Assertions.assertEquals(1, EventPriority.MEDIUM.getValue());
        Assertions.assertEquals(2, EventPriority.HIGH.getValue());
    }

    @Test
    void testToString(){
        Assertions.assertEquals("0", EventPriority.LOW.toString());
        Assertions.assertEquals("1", EventPriority.MEDIUM.toString());
        Assertions.assertEquals("2", EventPriority.HIGH.toString());
    }

}
