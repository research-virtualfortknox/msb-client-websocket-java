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

import java.util.Date;

public class Connection {

    private Date lastContact;
    private ConnectionState connectionState;
    private ConnectionType connectionType;
    private ConnectionFormat connectionFormat;

    /**
     * Instantiates a new Connection.
     */
    public Connection(){

    }

    /**
     * Instantiates a new Connection.
     *
     * @param connectionState  the connection state
     * @param connectionType   the connection type
     * @param connectionFormat the connection format
     * @param lastContact      the last contact
     */
    public Connection(ConnectionState connectionState, ConnectionType connectionType, ConnectionFormat connectionFormat, Date lastContact){
        this.connectionState = connectionState;
        this.connectionType = connectionType;
        this.connectionFormat = connectionFormat;
        this.lastContact = new Date(lastContact.getTime());
    }

    /**
     * Gets the last contact.
     *
     * @return the last contact
     */
    public Date getLastContact() {
        return new Date(lastContact.getTime());
    }

    /**
     * Sets the last contact.
     *
     * @param lastContact the new last contact
     */
    public void setLastContact(Date lastContact) {
        this.lastContact = new Date(lastContact.getTime());
    }

    /**
     * Gets the connection state.
     *
     * @return the connection state
     */
    public ConnectionState getConnectionState() {
        return connectionState;
    }

    /**
     * Sets the connection state.
     *
     * @param connectionState the new connection state
     */
    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    /**
     * Gets the connection type.
     *
     * @return the connection type
     */
    public ConnectionType getConnectionType() {
        return connectionType;
    }

    /**
     * Sets the connection type.
     *
     * @param connectionType the new connection type
     */
    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    /**
     * Gets the connection format.
     *
     * @return the connection format
     */
    public ConnectionFormat getConnectionFormat() {
        return connectionFormat;
    }

    /**
     * Sets the connection format.
     *
     * @param connectionFormat the new connection format
     */
    public void setConnectionFormat(ConnectionFormat connectionFormat) {
        this.connectionFormat = connectionFormat;
    }

}
