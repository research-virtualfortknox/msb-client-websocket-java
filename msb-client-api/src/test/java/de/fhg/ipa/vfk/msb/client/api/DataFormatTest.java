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

package de.fhg.ipa.vfk.msb.client.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class DataFormatTest {

    @Test
    void testDataFormat(){
        DataFormat dataFormat = new DataFormat();
        dataFormat.put("type","string");
        Assertions.assertEquals("{\"type\":\"string\"}",dataFormat.toString());
    }

    @Test
    void testDataFormatStringConstructor(){
        Assertions.assertEquals("{\"type\":\"string\"}",new DataFormat("{\"type\":\"string\"}").toString());
    }

    @Test
    void testDataFormatWrongInput(){
        Assertions.assertEquals("{}",new DataFormat("{wrong}").toString());
    }

    @Test
    void testDataFormatMapConstructor(){
        Map<String, Object> map = new HashMap<>();
        map.put("type","string");
        Assertions.assertEquals("{\"type\":\"string\"}",new DataFormat(map).toString());
    }

    @Test
    void testDataFormatClassConstructor(){
        Assertions.assertEquals("{\"dataObject\":{\"type\":\"integer\",\"format\":\"int32\"}}",new DataFormat(Integer.class).toString());
    }

}
