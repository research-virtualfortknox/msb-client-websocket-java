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


import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The Class SmartObject.
 *
 * @author des
 */
@JsonTypeName("SmartObject")
public class SmartObject extends Service {

    protected SmartObject(){
        //Default constructor
    }

    /**
     * Instantiates a new Smart object.
     *
     * @param uuid        the uuid
     * @param name        the name
     * @param description the description
     * @param token       the token
     */
    public SmartObject(String uuid,String name,String description,String token){
        super(uuid,name,description,token);
    }

    /**
     * Instantiates a new Smart object.
     *
     * @param uuid          the uuid
     * @param name          the name
     * @param description   the description
     * @param token         the token
     * @param configuration the configuration
     */
    public SmartObject(String uuid,String name,String description,String token, Configuration configuration){
        super(uuid,name,description,token,configuration);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
