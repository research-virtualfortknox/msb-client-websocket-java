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
package de.fhg.ipa.vfk.msb.client.listener;

/**
 * The listener interface for receiving connection events. The class that is
 * interested in processing a connection event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addConnectionListener</code> method. When the connection
 * event occurs, that object's appropriate method is invoked.
 *
 * @author des
 */
public interface ConnectionListener {

    /**
     * After connection established.
     */
    void afterConnectionEstablished();

    /**
     * After connection closed.
     */
    void afterConnectionClosed();

    /**
     * Before connection try to reconnecting.
     */
    void beforeConnectionTryToReconnecting();

    /**
     * After service registered.
     */
    void afterServiceRegistered();

    /**
     * Error at service registration.
     *
     * @param registrationError the registration error
     */
    void errorAtServiceRegistration(RegistrationError registrationError);

    /**
     * After event published.
     */
    void afterEventPublished();

    /**
     * After event cached.
     */
    void afterEventCached();

    /**
     * Error at event publishing.
     *
     * @param publishingError the publishing error
     */
    void errorAtEventPublishing(PublishingError publishingError);

}
