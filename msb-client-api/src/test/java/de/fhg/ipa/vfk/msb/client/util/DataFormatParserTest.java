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
package de.fhg.ipa.vfk.msb.client.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.fhg.ipa.vfk.msb.client.api.DataFormat;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The Class DataFormatParserTest.
 *
 * @author des
 */
public class DataFormatParserTest {

	/**
	 * Test parse object.
	 *
	 * @throws JsonProcessingException the json processing exception
	 */
	@Test
	void testParseObjectToString() throws JsonProcessingException, JSONException {
		String s = DataFormatParser.parseToString(InnerTestObject.class);
		JSONAssert.assertEquals("{\"dataObject\":{\"$ref\":\"#/definitions/InnerTestObject\"},\"InnerTestObject\":{\"type\":\"object\",\"properties\":{\"integer\":{\"type\":\"integer\",\"format\":\"int32\"},\"list\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}},\"map\":{\"type\":\"object\",\"additionalProperties\":{\"type\":\"string\"}}}}}", s,true);
	}

	/**
	 * Test parse short.
	 */
	@Test
	void testParseShort() {
		DataFormat dataFormat = new DataFormat(DataFormatParser.parse("test", Short.class));
		Assertions.assertEquals("{\"test\":{\"type\":\"integer\",\"format\":\"int32\",\"minimum\":-32768,\"maximum\":32767}}", dataFormat.toString());
	}

	/**
	 * Test read string.
	 *
	 * @throws IOException the io exception
	 */
	@Test
	void testReadString() throws IOException {
		Map<String, Object> dataFormat = DataFormatParser.readFromString("{\"dataObject\":{\"$ref\":\"#/definitions/InnerTestObject\"},\"InnerTestObject\":{\"type\":\"object\",\"properties\":{\"integer\":{\"type\":\"integer\",\"format\":\"int32\"},\"list\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}},\"map\":{\"type\":\"object\",\"additionalProperties\":{\"type\":\"string\"}}}}}");
		Assertions.assertEquals(2,dataFormat.keySet().size());
		Assertions.assertTrue(dataFormat.containsKey("dataObject"));
		Assertions.assertTrue(dataFormat.containsKey("InnerTestObject"));
		Assertions.assertEquals(1,((Map<String, Object>)dataFormat.get("dataObject")).keySet().size());
		Assertions.assertTrue(((Map<String, Object>)dataFormat.get("dataObject")).containsKey("$ref"));
	}

	/**
	 * Test parse nested objects.
	 *
	 * @throws JsonProcessingException the json processing exception
	 */
	@Test
	void testParseNestedObjects() throws JsonProcessingException, JSONException {
		String s = DataFormatParser.parseToString(TestObject.class);
		JSONAssert.assertEquals("{\"dataObject\":{\"$ref\":\"#/definitions/TestObject\"},"
				+ "\"InnerTestObject\":{\"type\":\"object\","
				+ "\"properties\":{\"integer\":{\"type\":\"integer\",\"format\":\"int32\"},"
				+ "\"list\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}},\"map\":{\"type\":\"object\",\"additionalProperties\":{\"type\":\"string\"}}}},"
				+ "\"TestObject\":{\"type\":\"object\","
				+ "\"properties\":{\"integer\":{\"type\":\"integer\",\"format\":\"int32\"},"
				+ "\"floatValue\":{\"type\":\"number\",\"format\":\"float\"},"
				+ "\"list\":{\"type\":\"array\",\"items\":{\"$ref\":\"#/definitions/InnerTestObject\"}},"
				+ "\"map\":{\"type\":\"object\",\"additionalProperties\":{\"$ref\":\"#/definitions/InnerTestObject\"}}}}}", s, true);
	}

	/**
	 * The Class TestObject.
	 *
	 * @author des
	 */
	public class TestObject{
		
		/** The integer. */
		private int integer;
		
		/** The float value. */
		private float floatValue;
		
		/** The list. */
		private List<InnerTestObject> list;
		
		/** The map. */
		private Map<String,InnerTestObject> map;

		/**
		 * Gets the integer.
		 *
		 * @return the integer
		 */
		public int getInteger() {
			return integer;
		}

		/**
		 * Sets the integer.
		 *
		 * @param integer the new integer
		 */
		public void setInteger(int integer) {
			this.integer = integer;
		}

		/**
		 * Gets the float value.
		 *
		 * @return the float value
		 */
		public float getFloatValue() {
			return floatValue;
		}

		/**
		 * Sets the float value.
		 *
		 * @param floatValue the new float value
		 */
		public void setFloatValue(float floatValue) {
			this.floatValue = floatValue;
		}

		/**
		 * Gets the list.
		 *
		 * @return the list
		 */
		public List<InnerTestObject> getList() {
			return list;
		}

		/**
		 * Sets the list.
		 *
		 * @param list the new list
		 */
		public void setList(List<InnerTestObject> list) {
			this.list = list;
		}

		/**
		 * Gets the map.
		 *
		 * @return the map
		 */
		public Map<String,InnerTestObject> getMap() {
			return map;
		}

		/**
		 * Sets the map.
		 *
		 * @param map the map
		 */
		public void setMap(Map<String,InnerTestObject> map) {
			this.map = map;
		}

	}

	/**
	 * The Class InnerTestObject.
	 *
	 * @author des
	 */
	public class InnerTestObject{

		/** The integer. */
		private int integer;

		/** The list. */
		private List<String> list;

		/** The map. */
		private Map<Integer, String> map;

		/**
		 * Gets the integer.
		 *
		 * @return the integer
		 */
		public int getInteger() {
			return integer;
		}

		/**
		 * Sets the integer.
		 *
		 * @param integer the new integer
		 */
		public void setInteger(int integer) {
			this.integer = integer;
		}

		/**
		 * Gets the list.
		 *
		 * @return the list
		 */
		public List<String> getList() {
			return list;
		}

		/**
		 * Sets the list.
		 *
		 * @param list the new list
		 */
		public void setList(List<String> list) {
			this.list = list;
		}

		/**
		 * Gets the map.
		 *
		 * @return the map
		 */
		public Map<Integer, String> getMap() {
			return map;
		}

		/**
		 * Sets the map.
		 *
		 * @param map the map
		 */
		public void setMap(Map<Integer, String> map) {
			this.map = map;
		}
	}

}
