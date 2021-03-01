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

import de.fhg.ipa.vfk.msb.client.annotation.EventDeclaration;
import de.fhg.ipa.vfk.msb.client.annotation.Events;
import de.fhg.ipa.vfk.msb.client.annotation.FunctionCall;
import de.fhg.ipa.vfk.msb.client.annotation.FunctionHandler;
import de.fhg.ipa.vfk.msb.client.annotation.FunctionParam;
import de.fhg.ipa.vfk.msb.client.api.MultipleResponseEvent;
import de.fhg.ipa.vfk.msb.client.api.messages.EventPriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
	 * Hello world.
	 */
	@FunctionCall(path="/hello_world", description = "print hello world", responseEvents = {"PULSE"})
	public void helloWorld() {
		LOG.info("Hello World!");
	}

	/**
	 * Print string date.
	 *
	 * @param test the test
	 * @return the date
	 */
	@FunctionCall(path="/printString", description = "print a string",responseEvents={"STOP"})
	public Date printString(@FunctionParam(name = "string") String test) {
		LOG.info("printString: " + test);
		return new Date();
	}

	/**
	 * Print object.
	 *
	 * @param test the test
	 */
	@FunctionCall(path="/printObject", description = "print a object")
	public void printObject(@FunctionParam(name = "obj") Object test) {
		LOG.info("printObject: " + test);
	}

	/**
	 * Print log.
	 *
	 * @param string the string
	 * @param date   the date
	 */
	@FunctionCall(path="/printLog", description = "print log")
    public void printLog(@FunctionParam(name = "string") String string, @FunctionParam(name = "date") Date date) {
        LOG.info("printLOG {}: {}",date,string);
    }

	/**
	 * Print int multiple response event.
	 *
	 * @param test the test
	 * @return the multiple response event
	 */
	@FunctionCall(path="/printInt", description = "test with different response", responseEvents={"START","WAIT","STOP"})
	public MultipleResponseEvent printInt(@FunctionParam(name = "int") int test) {
		LOG.info("/printInt "+test);
        MultipleResponseEvent multipleResponseEvent = new MultipleResponseEvent();
        if(test<10) {
        	multipleResponseEvent.addResponseEvent("WAIT",new Date());
        } else if (test <= 20){
        	multipleResponseEvent.addResponseEvent("START",new Date());
        } else {
        	multipleResponseEvent.addResponseEvent("STOP",new Date());
			multipleResponseEvent.addResponseEvent("WAIT",new Date());
        }
        return multipleResponseEvent;
	}

	/**
	 * Print strings.
	 *
	 * @param strings the strings
	 */
	@FunctionCall(path="/printStringList", description = "test with list of strings")
	public void printStrings(List<String> strings) {
		LOG.info("/printStringList: list size "+strings.size()+" and content "+Arrays.toString(strings.toArray()));
	}

	/**
	 * Print all types.
	 *
	 * @param floatValue  the float value
	 * @param doubleValue the double value
	 * @param intValue    the int value
	 * @param longValue   the long value
	 * @param boolValue   the bool value
	 */
	@FunctionCall(path="/printAllTypes", description = "test for primitiv types")
	public void printAllTypes(@FunctionParam(name = "float") float floatValue, @FunctionParam(name = "double") double doubleValue, @FunctionParam(name = "int") int intValue, @FunctionParam(name = "long") long longValue, @FunctionParam(name = "boolean") boolean boolValue) {
		LOG.info("printAllTypes: " + floatValue+", "+doubleValue+", "+intValue+", "+longValue+", "+boolValue);
	}

	@FunctionCall(path="/helloNull", description = "return null", responseEvents = {"WAIT"})
	public Date helloNull() {
		LOG.info("Hello Null!");
		return null;
	}

}
