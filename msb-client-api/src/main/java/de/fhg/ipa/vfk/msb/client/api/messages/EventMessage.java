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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.fhg.ipa.vfk.msb.client.util.DateDeserializer;
import de.fhg.ipa.vfk.msb.client.util.DateSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * The Class EventMessage.
 *
 * @author des
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventMessage extends Message {

	private String eventId;
	@ApiModelProperty(required = true)
	@JsonFormat(shape= JsonFormat.Shape.NUMBER_INT)
	private EventPriority priority = EventPriority.DEFAULT;
	@JsonDeserialize(using = DateDeserializer.class, as = Date.class)
	@JsonSerialize(using = DateSerializer.class, as = Date.class)
	@ApiModelProperty(required = true, notes = "2015-07-27T11:14:39.428+02:00")
	private Date postDate;
	private Object dataObject;

    /**
     * Instantiates a new Incoming data.
     */
    protected EventMessage(){
		//Default constructor for json deserializer
	}

    /**
     * Instantiates a new Incoming data.
     *
     * @param uuid          the uuid
     * @param eventId       the event id
     * @param correlationId the correlation id
     * @param postDate      the post date
     */
    public EventMessage(String uuid, String eventId, String correlationId, Date postDate) {
        super(uuid,correlationId);
        this.eventId = eventId;
        if(postDate!=null) {
            this.postDate = new Date(postDate.getTime());
        }
    }

    /**
     * Instantiates a new incoming data.
     *
     * @param uuid          the uuid
     * @param eventId       the event id
     * @param correlationId the correlation id
     * @param postDate      the post date
     * @param dataObject    the data object
     */
    public EventMessage(String uuid, String eventId, String correlationId, Date postDate, Object dataObject) {
        this(uuid,eventId,correlationId,postDate);
        this.dataObject = dataObject;
    }

    /**
     * Instantiates a new incoming data.
     *
     * @param uuid          the uuid
     * @param eventId       the event id
     * @param correlationId the correlation id
     * @param postDate      the post date
     * @param dataObject    the data object
     * @param priority      the priority
     */
    public EventMessage(String uuid, String eventId, String correlationId, Date postDate, Object dataObject, EventPriority priority) {
		this(uuid,eventId,correlationId,postDate,dataObject);
		this.priority = priority;
	}


    /**
     * Gets the event id.
     *
     * @return the event id
     */
    public String getEventId() {
		return eventId;
	}

    /**
     * Sets the event id.
     *
     * @param eventId the new event id
     */
    public void setEventId(String eventId) {
		this.eventId = eventId;
	}


    /**
     * Gets the priority.
     *
     * @return the priority
     */
    public EventPriority getPriority() {
		return priority;
	}

    /**
     * Sets the priority.
     *
     * @param priority the new priority
     */
    public void setPriority(EventPriority priority) {
		this.priority = priority;
	}

    /**
     * Gets the postDate.
     *
     * @return the postDate
     */
    public Date getPostDate() {
        if(postDate!=null) {
            return new Date(postDate.getTime());
        } else {
            return null;
        }
	}

    /**
     * Sets the postDate.
     *
     * @param postDate the new post date
     */
    public void setPostDate(Date postDate) {
        if(postDate!=null) {
            this.postDate = new Date(postDate.getTime());
        } else {
            this.postDate = null;
        }
	}

    /**
     * Gets the data object.
     *
     * @return the data object
     */
    public Object getDataObject() {
		return dataObject;
	}

    /**
     * Sets the data object.
     *
     * @param dataObject the new data object
     */
    public void setDataObject(Object dataObject) {
		this.dataObject = dataObject;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{\"uuid\":\"" + getUuid() + "\",\"eventId\":\"" + eventId + "\",\"correlationId\":\"" + getCorrelationId() + "\",\"priority\":" + priority + ",\"postDate\":\""
				+ postDate + "\",\"dataObject\":" + dataObject.toString() + "}";
	}

}
