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
 * The type Connection adapter is a empty implementation of Connection listener interface.
 *
 * @author des
 */
public class ConnectionAdapter implements ConnectionListener {

    @Override
    public void afterConnectionEstablished() {
        // Do nothing as default implementation
    }

    @Override
    public void afterConnectionClosed() {
        // Do nothing as default implementation
    }

    @Override
    public void beforeConnectionTryToReconnecting() {
        // Do nothing as default implementation
    }

    @Override
    public void afterServiceRegistered() {
        // Do nothing as default implementation
    }

    @Override
    public void errorAtServiceRegistration(RegistrationError registrationError) {
        // Do nothing as default implementation
    }

    @Override
    public void afterEventPublished() {
        // Do nothing as default implementation
    }

    @Override
    public void afterEventCached() {
        // Do nothing as default implementation
    }

    @Override
    public void errorAtEventPublishing(PublishingError publishingError) {
        // Do nothing as default implementation
    }
}
