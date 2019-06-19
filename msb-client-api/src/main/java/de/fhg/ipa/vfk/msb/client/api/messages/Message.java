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

package de.fhg.ipa.vfk.msb.client.api.messages;

import java.util.UUID;

/**
 * The type Message.
 *
 * @author des
 */
public class Message {

    private String uuid;
    private String correlationId = UUID.randomUUID().toString();

    /**
     * Instantiates a new Message.
     */
    protected Message(){
        //Default constructor for json deserializer
    }

    /**
     * Instantiates a new Message.
     *
     * @param uuid          the uuid
     * @param correlationId the correlation id
     */
    public Message(String uuid, String correlationId){
        this.uuid = uuid;
        if (correlationId != null) {
            this.correlationId = correlationId;
        }
    }

    /**
     * Gets the UU id.
     *
     * @return the UU id
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the UU id.
     *
     * @param uuid the new UU id
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Gets correlation id.
     *
     * @return the correlation id
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * Sets correlation id.
     *
     * @param correlationId the correlation id
     */
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

}
