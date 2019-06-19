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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import de.fhg.ipa.vfk.msb.client.annotation.EventDeclaration;
import de.fhg.ipa.vfk.msb.client.annotation.Events;
import de.fhg.ipa.vfk.msb.client.api.Event;
import de.fhg.ipa.vfk.msb.client.util.DataFormatParser;
import de.fhg.ipa.vfk.msb.client.api.messages.EventPriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Class EventParser.
 *
 * @author des
 */
public final class EventParser {

	private static final Logger LOG = LoggerFactory.getLogger(EventParser.class);

	private EventParser() {}

	/**
	 * Parses the events.
	 *
	 * @param serviceUuid the service uuid
	 * @param packagePath the package path
	 * @param eventMap    the event map
	 * @return the list
	 */
	public static List<Event> parseEvents(String serviceUuid, String packagePath, Map<String, EventReference> eventMap) {
		try {
			List<Class<?>> eventDeclarations = PackageScanner.findMyTypes(packagePath, new Class[] {EventDeclaration.class, Events.class });
			if(!eventDeclarations.isEmpty()) {
				return EventParser.parseEvents(serviceUuid, eventDeclarations, eventMap);
			}
		} catch (ClassNotFoundException | IOException e) {
			LOG.warn(e.getMessage(), e);
		}
		return new ArrayList<>();
	}

	/**
	 * Parse events list.
	 *
	 * @param serviceUuid      the service uuid
	 * @param annotatedClasses the annotated classes
	 * @param eventMap         the event map
	 * @return the list
	 */
	public static List<Event> parseEvents(String serviceUuid, List<Class<?>> annotatedClasses, Map<String, EventReference> eventMap) {
		List<Event> list = new ArrayList<>();
		if (annotatedClasses!=null) {
			for (Class<?> annotatedClasse : annotatedClasses) {
				parseEvents(serviceUuid, annotatedClasse, list, eventMap);
			}
		}
		return list;
	}

	/**
	 * Parses the events.
	 *
	 * @param serviceUuid      the service uuid
	 * @param annotatedClasses the annotated classes
	 * @param eventMap         the event map
	 * @return the list
	 */
	public static List<Event> parseEvents(String serviceUuid, Object[] annotatedClasses, Map<String, EventReference> eventMap) {
		List<Event> list = new ArrayList<>();
		if (annotatedClasses!=null) {
			for (Object annotatedClasse : annotatedClasses) {
				if (annotatedClasse instanceof Class) {
					parseEvents(serviceUuid, (Class<?>) annotatedClasse, list, eventMap);
				} else {
					parseEvents(serviceUuid, annotatedClasse.getClass(), list, eventMap);
				}
			}
		}
		return list;
	}

	private static List<Event> parseEvents(String serviceUuid, Class<?> annotatedClasse, List<Event> list, Map<String, EventReference> eventMap) {
		if (annotatedClasse.isAnnotationPresent(EventDeclaration.class)) {
			EventDeclaration eventDeclaration = annotatedClasse.getAnnotation(EventDeclaration.class);
			String eventId = eventDeclaration.eventId();
			if(!eventMap.containsKey(serviceUuid+"_"+eventId)) {
				list.add(parseEvent(serviceUuid, eventDeclaration, eventMap));
			} else {
				LOG.warn("duplicate event parsing: {}",eventId);
			}
		}
		if (annotatedClasse.isAnnotationPresent(Events.class)) {
			Events eventDeclaration = annotatedClasse.getAnnotation(Events.class);
			list.addAll(parseEvents(serviceUuid, eventDeclaration, eventMap));
		}
		return list;
	}

	/**
	 * Parses the events.
	 *
	 * @param eventDeclarations the event declarations
	 * @param eventMap the event map
	 * @return the list
	 */
	private static List<Event> parseEvents(String serviceUuid, Events eventDeclarations, Map<String, EventReference> eventMap){
		List<Event> list = new ArrayList<>();
		for (EventDeclaration eventDeclaration : eventDeclarations.value()) {
			String eventId = eventDeclaration.eventId();
			if(!eventMap.containsKey(serviceUuid+"_"+eventId)) {
				list.add(parseEvent(serviceUuid, eventDeclaration, eventMap));
			} else {
				LOG.warn("duplicate event parsing: {}",eventId);
			}
		}
		return list;
	}

	/**
	 * Parses the event.
	 *
	 * @param eventDeclaration the event declaration
	 * @param eventMap the event map
	 * @return the event
	 */
	private static Event parseEvent(String serviceUuid, EventDeclaration eventDeclaration, Map<String, EventReference> eventMap) {
		String description = eventDeclaration.description();
		String name = eventDeclaration.name();
		String eventId = eventDeclaration.eventId();
		Class<?> dataType = eventDeclaration.dataType();
		EventPriority priority = eventDeclaration.priority();
		EventReference eventReference = getEventReference(eventId, name, description, dataType, priority);
		eventMap.put(serviceUuid+"_"+eventId, eventReference);
		return eventReference.getEvent();
	}

	/**
	 * Adds the event.
	 *
	 * @param serviceUuid the service uuid
	 * @param eventMap    the event map
	 * @param eventList   the event list
	 * @param eventId     the event id
	 * @param name        the name
	 * @param description the description
	 * @param dataType    the data type
	 * @param priority    the priority
	 */
	public static void addEvent(String serviceUuid, Map<String, EventReference> eventMap, List<Event> eventList, String eventId, String name, String description, Type dataType, EventPriority priority){
		EventReference eventReference = getEventReference(eventId, name, description, dataType, priority);
		eventMap.put(serviceUuid+"_"+eventId, eventReference);
		eventList.add(eventReference.getEvent());
	}

	/**
	 * Gets the event reference.
	 *
	 * @param eventId     the event id
	 * @param name        the name
	 * @param description the description
	 * @param dataType    the data type
	 * @param priority    the priority
	 * @return the event reference
	 */
	public static EventReference getEventReference(String eventId, String name, String description, Type dataType, EventPriority priority){
        LOG.trace("parse event: {}", eventId);

	    Event event = new Event(eventId,name,description);

		EventReference eventReference = new EventReference();
		eventReference.setDataType(dataType);
		eventReference.setName(name);
		eventReference.setEvent(event);
		eventReference.setPriority(priority);

		// check if event have data
		if(dataType != NullNode.class && dataType != Void.class){
			Map<String, Object>  dataFormat = DataFormatParser.parse(dataType);
			event.setDataFormat(dataFormat);

			try {
				ObjectMapper mapper = DataFormatParser.getObjectMapper();
				eventReference.setDataFormat(mapper.writeValueAsString(dataFormat));
			} catch (JsonProcessingException e) {
				LOG.error("JsonProcessingException at data format parsing",e);
			}
		}
        return eventReference;
	}

}
