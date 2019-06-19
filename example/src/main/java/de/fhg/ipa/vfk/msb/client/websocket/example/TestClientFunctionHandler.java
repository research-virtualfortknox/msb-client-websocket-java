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

package de.fhg.ipa.vfk.msb.client.websocket.example;

import de.fhg.ipa.vfk.msb.client.annotation.EventDeclaration;
import de.fhg.ipa.vfk.msb.client.annotation.Events;
import de.fhg.ipa.vfk.msb.client.annotation.FunctionCall;
import de.fhg.ipa.vfk.msb.client.annotation.FunctionHandler;
import de.fhg.ipa.vfk.msb.client.annotation.FunctionParam;
import de.fhg.ipa.vfk.msb.client.api.MultipleResponseEvent;
import de.fhg.ipa.vfk.msb.client.api.messages.EventPriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * The type Test client function handler.
 *
 * @author des
 */
@Events({
        @EventDeclaration(dataType = Date.class, description = "Timestamp of start", name = "Start", eventId = "START", priority = EventPriority.HIGH),
        @EventDeclaration(dataType = Date.class, description = "Timestamp of stop", name = "Stop", eventId = "STOP"),
        @EventDeclaration(dataType = Date.class, description = "Timestamp of start waiting", name = "Wait", eventId = "WAIT"),
})
@FunctionHandler(path="/functionhandler")
public class TestClientFunctionHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(TestClientFunctionHandler.class);

	/**
	 * Instantiates a new Test client function handler.
	 */
	public TestClientFunctionHandler(){
        // Default Constructor
    }

	/**
	 * Print string date.
	 *
	 * @param test the test
	 * @return the date
	 */
	@FunctionCall(path="/printString", description = "print a string",responseEvents={"STOP"})
	public Date printString(@FunctionParam(name = "string") String test) {
		LOG.info("/printString: {}", test);
		return new Date();
	}

	/**
	 * Print int multiple response event.
	 *
	 * @param test the test
	 * @return the multiple response event
	 */
	@FunctionCall(path="/printInt", description = "test with different response", responseEvents={"START","WAIT","STOP"})
	public MultipleResponseEvent printInt(@FunctionParam(name = "int") int test) {
		LOG.info("/printInt: {}", test);
        MultipleResponseEvent multipleResponseEvent = new MultipleResponseEvent();
        if(test<10) {
        	multipleResponseEvent.addResponseEvent("WAIT",new Date());
        } else if (test <= 20){
        	multipleResponseEvent.addResponseEvent("START",new Date());
        } else {
        	multipleResponseEvent.addResponseEvent("STOP",new Date());
        }
        return multipleResponseEvent;
	}

	/**
	 * Print entity.
	 *
	 * @param testEntity the test entity
	 */
	@FunctionCall(path="/printEntity", description = "test with multiple references to one class")
	public void printEntity(@FunctionParam(name = "entity") TestEntity testEntity) {
		LOG.info("/printEntity: {}", testEntity);
	}


	/**
	 * The type Test entity.
	 */
	public class TestEntity {

		private String name;
		private Object value;

		/**
		 * Instantiates a new Test sub entity.
		 */
		public TestEntity() {
			//Default Constructor
		}

		/**
		 * Sets name.
		 *
		 * @param name the name
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * Gets name.
		 *
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Sets value.
		 *
		 * @param value the value
		 */
		public void setValue(Object value) {
			this.value = value;
		}

		/**
		 * Gets value.
		 *
		 * @return the value
		 */
		public Object getValue() {
			return value;
		}

		@Override
		public String toString() {
			return name +" - "+ value;
		}
	}

}
