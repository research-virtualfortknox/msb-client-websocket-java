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

package de.fhg.ipa.vfk.msb.client.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * The type Synchronized web socket session wrapper.
 *
 * @author des
 */
public class SynchronizedWebSocketSessionWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(SynchronizedWebSocketSessionWrapper.class);

    private WebSocketSession session;

    /**
     * Instantiates a new Synchronized web socket session wrapper.
     *
     * @param session the session
     */
    protected SynchronizedWebSocketSessionWrapper(WebSocketSession session){
        this.session = session;
    }

    /**
     * Sets session.
     *
     * @param session the session
     */
    protected synchronized void setSession(WebSocketSession session) {
        LOG.trace("setSession: {}", session);
        this.session = session;
    }

    /**
     * Is open boolean.
     *
     * @return the boolean
     */
    public synchronized boolean isOpen() {
        return this.session !=null && this.session.isOpen();
    }

    /**
     * Close.
     *
     * @param closeStatus the close status
     * @throws IOException the io exception
     */
    public synchronized void close(CloseStatus closeStatus) throws IOException{
        if(this.session !=null) {
            this.session.close(closeStatus);
        }
    }

    /**
     * Send message.
     *
     * @param message the message
     * @throws IOException the io exception
     */
    public synchronized void sendMessage(WebSocketMessage<?> message) throws IOException {
        LOG.debug("Send: {}", message);
        LOG.trace("Message: {}", message.getPayload());
        if (this.session != null && this.session.isOpen()) {
            this.session.sendMessage(message);
        } else {
            throw new IOException("no connection or connection closed");
        }
    }
}
