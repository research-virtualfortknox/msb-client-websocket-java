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

import de.fhg.ipa.vfk.msb.client.api.messages.EventPriority;

import java.util.ArrayList;

/**
 * The type Multiple response event.
 *
 * @author des
 */
public class MultipleResponseEvent extends ArrayList<MultipleResponseEvent.ResponseEvent> {

    private static final long serialVersionUID = 8674893397753975532L;

    /**
     * Instantiates a new Multiple response event.
     */
    public MultipleResponseEvent(){
        //Default constructor
    }

    /**
     * Add response event.
     *
     * @param eventId  the event id
     * @param obj      the obj
     * @param priority the priority
     * @param cache    the cache
     */
    public void addResponseEvent(String eventId, Object obj, EventPriority priority, boolean cache) {
        super.add(new ResponseEvent(eventId,obj,priority,cache));
    }

    /**
     * Add response event.
     *
     * @param eventId  the event id
     * @param obj      the obj
     * @param priority the priority
     */
    public void addResponseEvent(String eventId, Object obj, EventPriority priority){
        addResponseEvent(eventId,obj,priority,true);
    }

    /**
     * Add response event.
     *
     * @param eventId the event id
     * @param obj     the obj
     */
    public void addResponseEvent(String eventId, Object obj) {
        addResponseEvent(eventId,obj,null);
    }

    /**
     * The type Response event.
     */
    public class ResponseEvent {

        private String eventId;
        private Object obj;
        private EventPriority priority;
        private boolean cache;

        /**
         * Instantiates a new Response event.
         *
         * @param eventId  the event id
         * @param obj      the obj
         * @param priority the priority
         * @param cache    the cache
         */
        protected ResponseEvent(String eventId, Object obj, EventPriority priority, boolean cache){
            this.eventId = eventId;
            this.obj = obj;
            this.priority = priority;
            this.cache = cache;
        }

        public String getEventId() {
            return eventId;
        }

        public EventPriority getPriority() {
            return priority;
        }

        public Object getObj() {
            return obj;
        }

        public boolean isCache() {
            return cache;
        }
    }
}
