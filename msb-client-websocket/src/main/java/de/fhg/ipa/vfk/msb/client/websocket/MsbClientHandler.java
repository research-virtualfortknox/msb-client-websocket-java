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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

import de.fhg.ipa.vfk.msb.client.listener.ConfigurationListener;
import de.fhg.ipa.vfk.msb.client.listener.ConnectionListener;
import de.fhg.ipa.vfk.msb.client.listener.FunctionCallsListener;
import de.fhg.ipa.vfk.msb.client.api.PrimitiveFormat;
import de.fhg.ipa.vfk.msb.client.api.PrimitiveType;
import de.fhg.ipa.vfk.msb.client.api.messages.EventPriority;
import de.fhg.ipa.vfk.msb.client.api.Service;

/**
 * The Interface MsbClientHandler.
 *
 * @author des
 */
public interface MsbClientHandler extends AutoCloseable {

    /**
     * Adds a function to the given self-description.
     *
     * @param selfDescription         the self description
     * @param functionId              the function id
     * @param name                    the name
     * @param description             the description
     * @param responseEventIds        the response event ids
     * @param functionHandlerInstance the function handler instance
     * @param method                  the method
     */
    void addFunction(Service selfDescription, String functionId, String name, String description, String[] responseEventIds, Object functionHandlerInstance, Method method);

    /**
     * Adds a event to the given self-description.
     *
     * @param selfDescription the self description
     * @param eventId         the event id
     * @param name            the name
     * @param description     the description
     * @param dataType        the data type
     * @param priority        the event priority
     */
    void addEvent(Service selfDescription, String eventId, String name, String description, Class<?> dataType, EventPriority priority);

    /**
     * Add configuration parameter.
     *
     * @param configParamId the configuration parameter id
     * @param value         the value
     * @param format        the format
     */
    void addConfigParam(String configParamId, Object value, PrimitiveFormat format);

    /**
     * Add configuration parameter.
     *
     * @param configParamId the configuration parameter id
     * @param value         the value
     * @param type          the type
     */
    void addConfigParam(String configParamId, Object value, PrimitiveType type);

    /**
     * Add managed service. Only useful for a gateway service.
     *
     * @param subService the sub service
     */
    void addManagedService(Service subService);

    /**
     * Register a service by scanning the package path for annotated events, functions, configuration parameters and self description.
     *
     * @param packagePath the package path
     * @throws IOException the io exception
     */
    void register(String packagePath) throws IOException;

    /**
     * Register a service by scanning the package path for annotated events, functions, configuration parameters and self description.
     * And use the instantiated function handlers to invoke function calls.
     *
     * @param packagePath     the package path
     * @param functionHandler the function handler
     * @throws IOException the io exception
     */
    void register(String packagePath, Object[] functionHandler) throws IOException;

    /**
     * Register a service.
     *
     * @param selfDescription the self description
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void register(Service selfDescription) throws IOException;

    /**
     * Register a service and scan the package path for annotated events, functions and configuration parameters.
     *
     * @param selfDescription the self description
     * @param packagePath     the package path
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void register(Service selfDescription, String packagePath) throws IOException;

    /**
     * Register a service and use the instantiated function handlers to invoke function calls.
     *
     * @param selfDescription the self description
     * @param functionHandler the function handler
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void register(Service selfDescription, Object[] functionHandler) throws IOException;

    /**
     * Register a service, use the instantiated function handlers to invoke function calls and scan the package path for annotated events and configuration parameters..
     * Register a service and scan the package path for annotated events, functions and configuration parameters.
     *
     * @param selfDescription  the self description
     * @param functionHandlers the function handlers
     * @param packagePath      the package path
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void register(Service selfDescription, Object[] functionHandlers, String packagePath) throws IOException;

    /**
     * Register a service, use the instantiated function handlers to invoke function calls and scan event declaration classes for annotated events.
     *
     * @param selfDescription   the self description
     * @param functionHandlers  the function handlers
     * @param eventDeclarations the event declarations
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void register(Service selfDescription, Object[] functionHandlers, Class<?>[] eventDeclarations)
            throws IOException;

    /**
     * Publish a event of the registered service.
     *
     * @param eventId the event id
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void publish(String eventId) throws IOException;

    /**
     * Publish a event of the registered service.
     *
     * @param eventId the event id
     * @param obj     the obj
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void publish(String eventId, Object obj) throws IOException;

    /**
     * Publish a event of the registered service.
     *
     * @param eventId  the event id
     * @param obj      the obj
     * @param priority the priority
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void publish(String eventId, Object obj, EventPriority priority) throws IOException;

    /**
     * Publish a event of the registered service.
     *
     * @param eventId  the event id
     * @param obj      the obj
     * @param priority the priority
     * @param cache    the cache
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void publish(String eventId, Object obj, EventPriority priority, boolean cache) throws IOException;

    /**
     * Publish a event of the registered service.
     *
     * @param eventId  the event id
     * @param obj      the obj
     * @param priority the priority
     * @param cache    the cache
     * @param postDate the post date
     * @throws IOException the io exception
     */
    void publish(String eventId, Object obj, EventPriority priority, boolean cache, Date postDate) throws IOException;

    /**
     * Publish.
     *
     * @param eventId       the event id
     * @param obj           the obj
     * @param priority      the priority
     * @param cache         the cache
     * @param postDate      the post date
     * @param correlationId the correlation id
     * @throws IOException the io exception
     */
    void publish(String eventId, Object obj, EventPriority priority, boolean cache, Date postDate, String correlationId) throws IOException;

    /**
     * Publish a event for the given service uuid.
     *
     * @param serviceUuid the service uuid
     * @param eventId     the event id
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void publishForService(String serviceUuid, String eventId) throws IOException;

    /**
     * Publish a event for the given service uuid.
     *
     * @param serviceUuid the service uuid
     * @param eventId     the event id
     * @param obj         the obj
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void publishForService(String serviceUuid, String eventId, Object obj) throws IOException;

    /**
     * Publish a event for the given service uuid.
     *
     * @param serviceUuid the service uuid
     * @param eventId     the event id
     * @param obj         the obj
     * @param priority    the priority
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void publishForService(String serviceUuid, String eventId, Object obj, EventPriority priority) throws IOException;

    /**
     * Publish a event for the given service uuid.
     *
     * @param serviceUuid the service uuid
     * @param eventId     the event id
     * @param obj         the obj
     * @param priority    the priority
     * @param cache       the cache
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void publishForService(String serviceUuid, String eventId, Object obj, EventPriority priority, boolean cache) throws IOException;

    /**
     * Publish a event for the given service uuid.
     *
     * @param serviceUuid the service uuid
     * @param eventId     the event id
     * @param obj         the obj
     * @param priority    the priority
     * @param cache       the cache
     * @param postDate    the post date
     * @throws IOException the io exception
     */
    void publishForService(String serviceUuid, String eventId, Object obj, EventPriority priority, boolean cache, Date postDate) throws IOException;

    /**
     * Publish for service.
     *
     * @param serviceUuid   the service uuid
     * @param eventId       the event id
     * @param obj           the obj
     * @param priority      the priority
     * @param cache         the cache
     * @param postDate      the post date
     * @param correlationId the correlation id
     * @throws IOException the io exception
     */
    void publishForService(String serviceUuid, String eventId, Object obj, EventPriority priority, boolean cache, Date postDate, String correlationId) throws IOException;

    /**
     * Adds the function calls listener.
     *
     * @param functionCallsListener the function calls listener
     */
    void addFunctionCallsListener(FunctionCallsListener functionCallsListener);

    /**
     * Removes the function calls listener.
     *
     * @param functionCallsListener the function calls listener
     */
    void removeFunctionCallsListener(FunctionCallsListener functionCallsListener);

    /**
     * Adds the connection listener.
     *
     * @param connectionListener the connection listener
     */
    void addConnectionListener(ConnectionListener connectionListener);

    /**
     * Removes the connection listener.
     *
     * @param connectionListener the connection listener
     */
    void removeConnectionListener(ConnectionListener connectionListener);

    /**
     * Adds the configuration listener.
     *
     * @param configurationListener the configuration listener
     */
    void addConfigurationListener(ConfigurationListener configurationListener);

    /**
     * Removes the configuration listener.
     *
     * @param configurationListener the configuration listener
     */
    void removeConfigurationListener(ConfigurationListener configurationListener);

}
