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

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhg.ipa.vfk.msb.client.api.messages.ConfigurationMessage;
import de.fhg.ipa.vfk.msb.client.api.messages.EventMessage;
import de.fhg.ipa.vfk.msb.client.api.messages.FunctionCallMessage;
import de.fhg.ipa.vfk.msb.client.api.Configuration;
import de.fhg.ipa.vfk.msb.client.api.Event;
import de.fhg.ipa.vfk.msb.client.api.Function;
import de.fhg.ipa.vfk.msb.client.api.Gateway;
import de.fhg.ipa.vfk.msb.client.api.MultipleResponseEvent;
import de.fhg.ipa.vfk.msb.client.api.ParameterValue;
import de.fhg.ipa.vfk.msb.client.api.Service;
import de.fhg.ipa.vfk.msb.client.listener.ConfigurationListener;
import de.fhg.ipa.vfk.msb.client.listener.ConnectionListener;
import de.fhg.ipa.vfk.msb.client.listener.FunctionCallsListener;
import de.fhg.ipa.vfk.msb.client.listener.PublishingError;
import de.fhg.ipa.vfk.msb.client.listener.RegistrationError;
import de.fhg.ipa.vfk.msb.client.parser.ConfigParamParser;
import de.fhg.ipa.vfk.msb.client.parser.EventParser;
import de.fhg.ipa.vfk.msb.client.parser.EventReference;
import de.fhg.ipa.vfk.msb.client.parser.FunctionCallReference;
import de.fhg.ipa.vfk.msb.client.parser.FunctionInvoker;
import de.fhg.ipa.vfk.msb.client.parser.FunctionParser;
import de.fhg.ipa.vfk.msb.client.parser.SelfDescriptionParser;
import de.fhg.ipa.vfk.msb.client.util.DataFormatParser;
import de.fhg.ipa.vfk.msb.client.util.DataObjectValidator;
import de.fhg.ipa.vfk.msb.client.util.TypeMismatchException;
import de.fhg.ipa.vfk.msb.client.util.WrongDataFormatException;
import de.fhg.ipa.vfk.msb.client.api.PrimitiveFormat;
import de.fhg.ipa.vfk.msb.client.api.PrimitiveType;
import de.fhg.ipa.vfk.msb.client.api.messages.EventPriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Class MsbClientWebSocketHandler to interacted with a connected MSB instance.
 *
 * @author des
 */
public class MsbClientWebSocketHandler extends TextWebSocketHandler implements MsbClientHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MsbClientWebSocketHandler.class);

    private static final String REGISTRATION_REQUIRED = "registration is required";

    private static final String PING = "ping";
    private static final String PONG = "pong";
    private static final String REGISTRATION = "R";
    private static final String EVENT = "E";
    private static final String FUNCTION_CALLBACK = "C";
    private static final String CONFIG = "K";
    private static final String IO_CONNECTED = "IO_CONNECTED";
    private static final String IO_REGISTERED = "IO_REGISTERED";
    private static final String IO_PUBLISHED = "IO_PUBLISHED";
    private static final String NIO_ALREADY_CONNECTED = "NIO_ALREADY_CONNECTED";
    private static final String NIO_REGISTRATION_ERROR = "NIO_REGISTRATION_ERROR";
    private static final String NIO_UNEXPECTED_REGISTRATION_ERROR = "NIO_UNEXPECTED_REGISTRATION_ERROR";
    private static final String NIO_UNAUTHORIZED_CONNECTION = "NIO_UNAUTHORIZED_CONNECTION";
    private static final String NIO_EVENT_FORWARDING_ERROR = "NIO_EVENT_FORWARDING_ERROR";
    private static final String NIO_UNEXPECTED_EVENT_FORWARDING_ERROR = "NIO_UNEXPECTED_EVENT_FORWARDING_ERROR";

    private static final String URL_PATH = "/websocket/data";

    private static final int INITIAL = 0;
    private static final int STARTED = 1;
    private static final int REGISTERED = 2;
    private static final int STOPPED = 3;

    private String url = "wss://localhost:8084" + URL_PATH;
    private int websocketTextMessageSize = 1000000;
    private int functionCallExecutorPoolSize =10;
    private boolean invokeFunctionCallEnabled = true;
    private boolean eventCacheEnabled = true;
    private boolean reconnect = true;
    private int reconnectIntervalMillis = 10000;
    private boolean dataFormatValidationEnabled = false;
    private String trustStorePath;
    private String trustStorePwd;

    private int state = INITIAL;
    private static ObjectMapper mapper = DataFormatParser.getObjectMapper();

    private Map<String, FunctionCallReference> functionMap = new LinkedHashMap<>();
    private Map<String, EventReference> eventMap = new LinkedHashMap<>();
    private Map<String, ParameterValue> configParameters = new LinkedHashMap<>();
    private Map<String, FunctionCallReference> addedFunctionMap = new LinkedHashMap<>();
    private Map<String, EventReference> addedEventMap = new LinkedHashMap<>();

    private SockJsClient sockJsClient;
    private ExecutorService functionCallExecutorService;
    private final SynchronizedWebSocketSessionWrapper sessionWrapper = new SynchronizedWebSocketSessionWrapper(null);
    private List<FunctionCallsListener> functionCallsListeners = new ArrayList<>();
    private List<ConnectionListener> connectionListeners = new ArrayList<>();
    private List<ConfigurationListener> configurationListeners = new ArrayList<>();
    private Service selfDescription;
    private LimitedSizeQueue<TextMessage> eventCache = new LimitedSizeQueue<>(1000);

    /**
     * Instantiates a new client web socket handler.
     */
    protected MsbClientWebSocketHandler() {
        functionCallExecutorService = Executors.newFixedThreadPool(functionCallExecutorPoolSize);
    }

    /**
     * Instantiates a new client web socket handler.
     *
     * @param url the url
     */
    protected MsbClientWebSocketHandler(String url) {
        this();
        setUrl(url);
    }

    /**
     * Instantiates a new Msb client web socket handler.
     *
     * @param url            the url
     * @param eventCacheSize the event cache size
     */
    protected MsbClientWebSocketHandler(String url, int eventCacheSize) {
        this(url);
        if (this.eventCache.maximumSize() != eventCacheSize) {
            this.eventCache = new LimitedSizeQueue<>(eventCacheSize);
        }
    }

    /**
     * Instantiates a new Msb client web socket handler.
     *
     * @param url                      the url
     * @param eventCacheSize           the event cache size
     * @param websocketTextMessageSize the websocket text message size
     */
    protected MsbClientWebSocketHandler(String url, int eventCacheSize, int websocketTextMessageSize) {
        this(url, eventCacheSize);
        this.websocketTextMessageSize = websocketTextMessageSize;
    }

    /**
     * Instantiates a new Msb client web socket handler.
     *
     * @param url                          the url
     * @param eventCacheSize               the event cache size
     * @param websocketTextMessageSize     the websocket text message size
     * @param functionCallExecutorPoolSize the function call executor pool size
     */
    protected MsbClientWebSocketHandler(String url, int eventCacheSize, int websocketTextMessageSize, int functionCallExecutorPoolSize) {
        this(url, eventCacheSize);
        this.websocketTextMessageSize = websocketTextMessageSize;
        this.functionCallExecutorPoolSize = functionCallExecutorPoolSize;
    }

    /**
     * After connection established.
     *
     * @param session the session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOG.debug("Connection established: {}",session);
        sessionWrapper.setSession(session);
        for (ConnectionListener connectionListener : connectionListeners) {
            connectionListener.afterConnectionEstablished();
        }
    }

    /**
     * Handle text message.
     *
     * @param session the session
     * @param message the message
     * @throws Exception the exception
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOG.debug("Received: {}", message);
        LOG.debug("Message: {}", message.getPayload());

        String payload = message.getPayload();
        if (payload.startsWith(FUNCTION_CALLBACK)) {
            payload = payload.replaceFirst(FUNCTION_CALLBACK, "");
            FunctionCallMessage outdata = null;
            try {
                outdata = mapper.readValue(payload, FunctionCallMessage.class);
                callFunction(outdata);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        } else if (payload.startsWith(CONFIG)) {
            for (ConfigurationListener configurationListener : configurationListeners) {
                payload = payload.replaceFirst(CONFIG, "");
                ConfigurationMessage configuration = null;
                try {
                    configuration = mapper.readValue(payload, ConfigurationMessage.class);
                    configurationListener.configurationRemoteChanged(configuration);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        } else if (payload.startsWith(PING)) {
            sessionWrapper.sendMessage(new TextMessage(PONG));
        } else if (payload.startsWith(IO_CONNECTED)) {
            // ignore, because duplicate with afterConnectionEstablished
        } else if (payload.startsWith(IO_REGISTERED)) {
            this.state = REGISTERED;
            for (ConnectionListener connectionListener : connectionListeners) {
                connectionListener.afterServiceRegistered();
            }
            // send cached events
            while (!eventCache.isEmpty()) {
                TextMessage cachedMessage = eventCache.poll();
                sessionWrapper.sendMessage(cachedMessage);
            }
        } else if (payload.startsWith(IO_PUBLISHED)) {
            for (ConnectionListener connectionListener : connectionListeners) {
                connectionListener.afterEventPublished();
            }
        } else if (payload.startsWith(NIO_ALREADY_CONNECTED)) {
            for (ConnectionListener connectionListener : connectionListeners) {
                connectionListener.errorAtServiceRegistration(RegistrationError.NIO_ALREADY_CONNECTED);
            }
            if (reconnect) {
                closeSession(CloseStatus.NORMAL.withReason("client restarted"));
            }
        } else if (payload.startsWith(NIO_UNAUTHORIZED_CONNECTION)) {
            if (state == INITIAL && eventCacheEnabled) {
                //TODO: cache event to lose nothing
            }
            for (ConnectionListener connectionListener : connectionListeners) {
                connectionListener.errorAtEventPublishing(PublishingError.NIO_UNAUTHORIZED_CONNECTION);
            }
        } else if (payload.startsWith(NIO_REGISTRATION_ERROR)) {
            for (ConnectionListener connectionListener : connectionListeners) {
                connectionListener.errorAtServiceRegistration(RegistrationError.NIO_REGISTRATION_ERROR);
            }
            if (reconnect) {
                closeSession(CloseStatus.NORMAL.withReason("client restarted"));
            }
        } else if (payload.startsWith(NIO_EVENT_FORWARDING_ERROR)) {
            if (state == INITIAL && eventCacheEnabled) {
                //TODO: cache event to lose nothing
            }
            for (ConnectionListener connectionListener : connectionListeners) {
                connectionListener.errorAtEventPublishing(PublishingError.NIO_EVENT_FORWARDING_ERROR);
            }
        } else if (payload.startsWith(NIO_UNEXPECTED_REGISTRATION_ERROR)) {
            for (ConnectionListener connectionListener : connectionListeners) {
                connectionListener.errorAtServiceRegistration(RegistrationError.NIO_UNEXPECTED_REGISTRATION_ERROR);
            }
        } else if (payload.startsWith(NIO_UNEXPECTED_EVENT_FORWARDING_ERROR)) {
            if (state == INITIAL && eventCacheEnabled) {
                //TODO: cache event to lose nothing
            }
            for (ConnectionListener connectionListener : connectionListeners) {
                connectionListener.errorAtEventPublishing(PublishingError.NIO_UNEXPECTED_EVENT_FORWARDING_ERROR);
            }
        }
    }

    /**
     * After connection closed.
     *
     * @param session the session
     * @param status  the status
     * @throws Exception the exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOG.info("Connection closed: {} - {}",session, status);
        for (ConnectionListener connectionListener : connectionListeners) {
            connectionListener.afterConnectionClosed();
        }
        sessionWrapper.setSession(null);
        super.afterConnectionClosed(session, status);
        if (reconnect && state != STOPPED) {
            reconnect();
        }
    }

    private void setUrl(String url) {
        if (url.endsWith(URL_PATH)) {
            this.url = url;
        } else {
            this.url = url + URL_PATH;
        }
    }

    /**
     * Get url string.
     *
     * @return the string
     */
    protected String getUrl() {
        return this.url;
    }

    /**
     * Gets event cache size.
     *
     * @return the event cache size
     */
    protected int getEventCacheSize() {
        return this.eventCache.maximumSize();
    }

    /**
     * Gets websocket text message size.
     *
     * @return the websocket text message size
     */
    protected int getWebsocketTextMessageSize() {
        return websocketTextMessageSize;
    }

    /**
     * Sets trust store.
     *
     * @param trustStorePath the trust store path
     * @param trustStorePwd  the trust store pwd
     */
    protected void setTrustStore(String trustStorePath, String trustStorePwd) {
        System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        this.trustStorePath = trustStorePath;
        this.trustStorePwd = trustStorePwd;
    }

    /**
     * Is registered boolean.
     *
     * @return the boolean
     */
    protected boolean isRegistered() {
        return state == REGISTERED;
    }

    /**
     * Is event cache enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isEventCacheEnabled() {
        return eventCacheEnabled;
    }

    /**
     * Sets event cache enabled.
     *
     * @param eventCacheEnabled the event cache enabled
     */
    protected void setEventCacheEnabled(boolean eventCacheEnabled) {
        this.eventCacheEnabled = eventCacheEnabled;
    }

    /**
     * Is auto reconnect boolean.
     *
     * @return the boolean
     */
    protected boolean isAutoReconnect() {
        return reconnect;
    }

    /**
     * Sets auto reconnect.
     *
     * @param autoReconnect the auto reconnect
     */
    protected void setAutoReconnect(boolean autoReconnect) {
        this.reconnect = autoReconnect;
    }

    /**
     * Set reconnect interval.
     *
     * @param millis the millis
     */
    protected void setReconnectInterval(int millis) {
        this.reconnectIntervalMillis = millis;
    }

    /**
     * Get reconnect interval int.
     *
     * @return the int
     */
    protected int getReconnectInterval() {
        return reconnectIntervalMillis;
    }

    /**
     * Is invokable function calls boolean.
     *
     * @return the boolean
     */
    protected boolean isInvokableFunctionCalls() {
        return invokeFunctionCallEnabled;
    }

    /**
     * Sets invokable function calls.
     *
     * @param invokable the invokable
     */
    protected void setInvokableFunctionCalls(boolean invokable) {
        this.invokeFunctionCallEnabled = invokable;
    }

    /**
     * Is data format validation boolean.
     *
     * @return the boolean
     */
    protected boolean isDataFormatValidation(){
        return dataFormatValidationEnabled;
    }

    /**
     * Set data format validation.
     *
     * @param dataFormatValidationEnabled the data format validation enabled
     */
    protected void setDataFormatValidation(boolean  dataFormatValidationEnabled){
        this.dataFormatValidationEnabled =  dataFormatValidationEnabled;
    }

    /**
     * Establish connection.
     *
     * @return the client handler
     */
    protected boolean establishConnection() {
        LOG.info("establish connection to: {}", url);
        if(state == INITIAL) {
            sockJsClient = createClient(url, trustStorePath, trustStorePwd);
        }

        if (sockJsClient != null && !sessionWrapper.isOpen()) {
            state = STARTED;
            WebSocketSession session = connect(sockJsClient, this, url, websocketTextMessageSize, reconnectIntervalMillis);
            return session.isOpen();
        }
        return isConnected();
    }

    /**
     * Checks if is connected.
     *
     * @return true, if is connected
     */
    protected boolean isConnected() {
        return sessionWrapper.isOpen();
    }

    /**
     * Reconnect.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected void reconnect() throws IOException {
        for (ConnectionListener connectionListener : connectionListeners) {
            connectionListener.beforeConnectionTryToReconnecting();
        }
        // reconnect
        if (establishConnection() && selfDescription !=null) {
            // re-register
            String s = mapper.writeValueAsString(selfDescription);
            TextMessage message = new TextMessage(REGISTRATION + " " + s);
            sessionWrapper.sendMessage(message);
        }
    }

    /**
     * Close connection.
     *
     * @param interrupt the interrupt
     */
    protected void closeConnection(boolean interrupt) {
        LOG.info("close connection called");
        state = STOPPED;
        closeSession(CloseStatus.NORMAL.withReason("client disconnected"));
        if(sockJsClient!=null) {
            sockJsClient.stop();
        }
    }

    /**
     * Close.
     *
     * @throws Exception the exception
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {
        if (state != STOPPED) {
            LOG.warn("MsbClientHandler close called");
            state = STOPPED;
            closeSession(CloseStatus.NORMAL.withReason("client closed"));
        }
    }

    @Override
    public void addFunction(Service selfDescription, String functionId, String name, String description, String[] responseEventIds, Object functionHandlerInstance, Method method) {
        List<Function> functions = new ArrayList<>();
        FunctionParser.addFunction(selfDescription.getUuid(), addedFunctionMap, functions, functionHandlerInstance, method, functionId, name, description, responseEventIds, addedEventMap);
        selfDescription.addFunctions(functions);
    }

    @Override
    public void addEvent(Service selfDescription, String eventId, String name, String description, Class<?> dataType, EventPriority priority) {
        List<Event> events= new ArrayList<>();
        EventParser.addEvent(selfDescription.getUuid(), addedEventMap, events, eventId, name, description, dataType, priority);
        selfDescription.addEvents(events);
    }

    @Override
    public void addConfigParam(String configParamId, Object value, PrimitiveFormat format) {
        configParameters.put(configParamId, new ParameterValue(value, format));
    }

    @Override
    public void addConfigParam(String configParamId, Object value, PrimitiveType type) {
        configParameters.put(configParamId, new ParameterValue(value, type));
    }

    @Override
    public void addManagedService(Service subService) {
        if (this.selfDescription != null && Gateway.class.equals(this.selfDescription.getClass())) {
            ((Gateway) this.selfDescription).addService(subService);
            //TODO: cache reference of events and functions of subService
            try {
                addEventsToEventReferenceMap(subService.getUuid(),subService.getEvents(), addedEventMap);
            } catch (IOException e) {
                LOG.error("Data format parsing error: ", e);
            }
            try {
                addFunctionsToFunctionReferenceMap(subService.getUuid(),subService.getFunctions(), functionMap);
            } catch (IOException e) {
                LOG.error("Data format parsing error: ", e);
            }
        } else {
            throw new UnsupportedOperationException("Only Gateway supports managed services");
        }
    }

    @Override
    public void register(String packagePath) throws IOException {
        Service service = SelfDescriptionParser.parse(packagePath);
        if(service!=null){
            register(service,packagePath);
        } else {
            LOG.error("Missing declaration of self description, use a other register method or @SelfDescription annotation.");
        }
    }

    @Override
    public void register(String packagePath, Object[] functionHandler) throws IOException {
        Service service = SelfDescriptionParser.parse(packagePath);
        if(service!=null){
            register(service,functionHandler,packagePath);
        } else {
            LOG.error("Missing declaration of self description, use a other register method or @SelfDescription annotation.");
        }
    }

    @Override
    public void register(Service selfDescription, String packagePath) throws IOException {
        prepareRegister();

        addEventsToEventReferenceMap(selfDescription.getUuid(), selfDescription.getEvents(), eventMap);
        addFunctionsToFunctionReferenceMap(selfDescription.getUuid(), selfDescription.getFunctions(), functionMap);
        addManagedServiceReferenceMap(selfDescription,eventMap,functionMap);

        selfDescription.setEvents(EventParser.parseEvents(selfDescription.getUuid(), packagePath, eventMap));
        selfDescription.setFunctions(FunctionParser.parseFunctionHandlers(selfDescription.getUuid(), packagePath, functionMap, eventMap));

        Configuration configuration = ConfigParamParser.parse(packagePath);
        if(configuration!=null) {
            if (selfDescription.getConfiguration() == null) {
                selfDescription.setConfiguration(configuration);
            } else {
                selfDescription.getConfiguration().getParameters().putAll(configuration.getParameters());
            }
        }

        registerService(selfDescription);
    }

    @Override
    public void register(Service selfDescription, Object[] functionHandler) throws IOException {
        prepareRegister();

        addEventsToEventReferenceMap(selfDescription.getUuid(), selfDescription.getEvents(), eventMap);
        addFunctionsToFunctionReferenceMap(selfDescription.getUuid(), selfDescription.getFunctions(), functionMap);
        addManagedServiceReferenceMap(selfDescription,eventMap,functionMap);

        selfDescription.addEvents(EventParser.parseEvents(selfDescription.getUuid(), functionHandler, eventMap));
        selfDescription.addFunctions(FunctionParser.parseFunctionHandlers(selfDescription.getUuid(), functionHandler, functionMap, eventMap));

        registerService(selfDescription);
    }

    @Override
    public void register(Service selfDescription, Object[] functionHandler, String packagePath) throws IOException {
        prepareRegister();

        addEventsToEventReferenceMap(selfDescription.getUuid(), selfDescription.getEvents(), eventMap);
        addFunctionsToFunctionReferenceMap(selfDescription.getUuid(), selfDescription.getFunctions(), functionMap);
        addManagedServiceReferenceMap(selfDescription,eventMap,functionMap);

        selfDescription.addEvents(EventParser.parseEvents(selfDescription.getUuid(), packagePath, eventMap));
        selfDescription.addEvents(EventParser.parseEvents(selfDescription.getUuid(), functionHandler, eventMap));

        List<Function> functionList;
        if(functionHandler!=null){
            functionList = FunctionParser.parseFunctionHandlers(selfDescription.getUuid(), functionHandler, functionMap, eventMap);
        } else {
           functionList = FunctionParser.parseFunctionHandlers(selfDescription.getUuid(), packagePath, functionMap, eventMap);
        }

        selfDescription.addFunctions(functionList);

        Configuration configuration = ConfigParamParser.parse(packagePath);
        if(configuration!=null) {
            if (selfDescription.getConfiguration() == null) {
                selfDescription.setConfiguration(configuration);
            } else {
                selfDescription.getConfiguration().getParameters().putAll(configuration.getParameters());
            }
        }

        registerService(selfDescription);
    }

    @Override
    public void register(Service selfDescription, Object[] functionHandler, Class<?>[] eventDeclarations)
            throws IOException {
        prepareRegister();

        addEventsToEventReferenceMap(selfDescription.getUuid(), selfDescription.getEvents(), eventMap);
        addFunctionsToFunctionReferenceMap(selfDescription.getUuid(), selfDescription.getFunctions(), functionMap);
        addManagedServiceReferenceMap(selfDescription,eventMap,functionMap);

        selfDescription.addEvents(EventParser.parseEvents(selfDescription.getUuid(), eventDeclarations, eventMap));
        selfDescription.addEvents(EventParser.parseEvents(selfDescription.getUuid(), functionHandler, eventMap));
        selfDescription.addFunctions(FunctionParser.parseFunctionHandlers(selfDescription.getUuid(), functionHandler, functionMap, eventMap));

        registerService(selfDescription);
    }

    @Override
    public void register(Service selfDescription) throws IOException {
       prepareRegister();

        // build reference maps
        addEventsToEventReferenceMap(selfDescription.getUuid(), selfDescription.getEvents(), eventMap);
        addFunctionsToFunctionReferenceMap(selfDescription.getUuid(), selfDescription.getFunctions(), functionMap);
        addManagedServiceReferenceMap(selfDescription,eventMap,functionMap);

        registerService(selfDescription);
    }

    private static void addManagedServiceReferenceMap(Service selfDescription, Map<String, EventReference> eventMap, Map<String, FunctionCallReference> functionMap) throws IOException {
        if(selfDescription instanceof Gateway){
            for(Service managedService : ((Gateway) selfDescription).getServices()){
                addEventsToEventReferenceMap(managedService.getUuid(), managedService.getEvents(), eventMap);
                addFunctionsToFunctionReferenceMap(managedService.getUuid(), managedService.getFunctions(), functionMap);
            }
        }
    }

    private void prepareRegister() {
        if (!sessionWrapper.isOpen()) {
            throw new IllegalStateException("Client is currently not connected, start client first or wait until connection is established.");
        }

        functionMap.clear();
        eventMap.clear();

        addEventsToEventReferenceMap(addedEventMap);
        addFunctionsToFunctionReferenceMap(addedFunctionMap);
    }

    private void registerService(Service selfDescription) throws IOException {
        this.selfDescription = selfDescription;

        if (configParameters.size() != 0) {
            if (this.selfDescription.getConfiguration() == null) {
                this.selfDescription.setConfiguration(new Configuration());
            }
            this.selfDescription.getConfiguration().getParameters().putAll(configParameters);
        }

        if (this.selfDescription instanceof Gateway) {
            setInvokableFunctionCalls(false);
        }

        String s = mapper.writeValueAsString(this.selfDescription);
        TextMessage message = new TextMessage(REGISTRATION + " " + s);
        sessionWrapper.sendMessage(message);
    }

    private void addEventsToEventReferenceMap(Map<String, EventReference> addedEventMap) {
        for (Map.Entry<String,EventReference> entry : addedEventMap.entrySet()) {
            if (!eventMap.containsKey(entry.getKey())) {
                eventMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void addEventsToEventReferenceMap(String serviceUuid, List<Event> events, Map<String, EventReference> eventMap) throws IOException {
        for (Event event : events) {
            if(!eventMap.containsKey(serviceUuid+"_"+event.getEventId())) {
                EventReference eventReference = new EventReference();
                eventReference.setDataFormat(mapper.writeValueAsString(event.getDataFormat()));
                eventReference.setName(event.getName());
                eventReference.setEvent(event);
                eventMap.put(serviceUuid+"_"+event.getEventId(), eventReference);
            }
        }
    }

    private void addFunctionsToFunctionReferenceMap(Map<String, FunctionCallReference> addedFunctionMap) {
        for (Map.Entry<String,FunctionCallReference> entry : addedFunctionMap.entrySet()) {
            if (!functionMap.containsKey(entry.getKey())) {
                functionMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void addFunctionsToFunctionReferenceMap(String serviceUuid, List<Function> functions, Map<String, FunctionCallReference> functionMap) throws IOException {
        for (Function function : functions) {
            if(!functionMap.containsKey(serviceUuid+"_"+function.getFunctionId())) {
                FunctionCallReference functionCallReference = new FunctionCallReference();
                functionCallReference.setDataFormat(mapper.writeValueAsString(function.getDataFormat()));
                functionCallReference.setFunction(function);
                functionMap.put(serviceUuid+"_"+function.getFunctionId(), functionCallReference);
            }
        }
    }

    @Override
    public void publish(String eventId) throws IOException {
        if (this.selfDescription == null) {
            throw new IOException(REGISTRATION_REQUIRED);
        }
        publishForService(this.selfDescription.getUuid(), eventId);
    }

    @Override
    public void publish(String eventId, Object obj) throws IOException {
        publish(eventId, obj, null);
    }

    @Override
    public void publish(String eventId, Object obj, EventPriority priority) throws IOException {
        publish(eventId, obj, priority, eventCacheEnabled);
    }

    @Override
    public void publish(String eventId, Object obj, EventPriority priority, boolean cache) throws IOException {
        if (this.selfDescription == null) {
            throw new IOException(REGISTRATION_REQUIRED);
        }
        publishForService(this.selfDescription.getUuid(), eventId, obj, priority, cache);
    }

    @Override
    public void publish(String eventId, Object obj, EventPriority priority, boolean cache, Date postDate) throws IOException {
        if (this.selfDescription == null) {
            throw new IOException(REGISTRATION_REQUIRED);
        }
        publishForService(this.selfDescription.getUuid(), eventId, obj, priority, cache, postDate);
    }

    @Override
    public void publish(String eventId, Object obj, EventPriority priority, boolean cache, Date postDate, String correlationId) throws IOException {
        if (this.selfDescription == null) {
            throw new IOException(REGISTRATION_REQUIRED);
        }
        publishForService(this.selfDescription.getUuid(), eventId, obj, priority, cache, postDate, correlationId);
    }

    @Override
    public void publishForService(String serviceUuid, String eventId) throws IOException {
        publishForService(serviceUuid, eventId, null);
    }

    @Override
    public void publishForService(String serviceUuid, String eventId, Object obj) throws IOException {
        publishForService(serviceUuid, eventId, obj, null);
    }

    @Override
    public void publishForService(String serviceUuid, String eventId, Object obj, EventPriority priority) throws IOException {
        publishForService(serviceUuid, eventId, obj, priority, eventCacheEnabled);
    }

    @Override
    public void publishForService(String serviceUuid, String eventId, Object obj, EventPriority priority, boolean cache) throws IOException{
        publishForService(serviceUuid, eventId, obj, priority, eventCacheEnabled, new Date());
    }

    @Override
    public void publishForService(String serviceUuid, String eventId, Object obj, EventPriority priority, boolean cache, Date postDate)
            throws IOException {
        publishForService(serviceUuid,eventId,obj,priority,cache,postDate,null);
    }

    @Override
    public void publishForService(String serviceUuid, String eventId, Object obj, EventPriority priority, boolean cache, Date postDate, String correlationId)
            throws IOException {
        if (this.selfDescription == null) {
            throw new IOException(REGISTRATION_REQUIRED);
        }
        if (!serviceUuid.equals(this.selfDescription.getUuid()) && !isGateway(this.selfDescription)) {
            throw new IOException("invalid service uuid '" + serviceUuid + "' for publishing an event");
        }

        EventReference eventReference = eventMap.get(serviceUuid + "_" + eventId);
        if (eventReference != null) {
            Map<String, Object> dataObject = new HashMap<>();
            dataObject.put("dataObject", obj);
            if (dataFormatValidationEnabled && !DataObjectValidator.validateDataObject(eventReference.getDataFormat(), dataObject)) {
                throw new WrongDataFormatException("The give data object: " + obj + " don't equals registered data format: " + eventReference.getDataFormat());
            }

            if (priority == null) {
                priority = eventReference.getPriority();
            }

            EventMessage eventMessage = new EventMessage(serviceUuid, eventId, correlationId, postDate, obj, priority);
            String s = mapper.writeValueAsString(eventMessage);
            TextMessage message = new TextMessage(EVENT + " " + s);

            if (sessionWrapper.isOpen() && state == REGISTERED) {
                sessionWrapper.sendMessage(message);

                for (ConnectionListener connectionListener : connectionListeners) {
                    connectionListener.afterEventPublished();
                }
            } else {
                if (eventCacheEnabled && cache) {
                    eventCache.add(message);
                    LOG.warn("no connection or not registered, event is cached");

                    for (ConnectionListener connectionListener : connectionListeners) {
                        connectionListener.afterEventCached();
                    }
                } else {
                    throw new IOException("not registered or no connection or connection closed");
                }
            }
        } else {
            LOG.warn("No event named {} found for service {}", eventId, serviceUuid);
        }
    }

    @Override
    public void addFunctionCallsListener(FunctionCallsListener functionCallsListener) {
        functionCallsListeners.add(functionCallsListener);
    }

    @Override
    public void removeFunctionCallsListener(FunctionCallsListener functionCallsListener) {
        functionCallsListeners.remove(functionCallsListener);
    }

    /**
     * Adds the connection listener.
     *
     * @param connectionListener the connection listener
     * @see MsbClientHandler#addConnectionListener(ConnectionListener)
     */
    @Override
    public void addConnectionListener(ConnectionListener connectionListener) {
        connectionListeners.add(connectionListener);
    }

    /**
     * Removes the connection listener.
     *
     * @param connectionListener the connection listener
     * @see MsbClientHandler#removeConnectionListener(ConnectionListener)
     */
    @Override
    public void removeConnectionListener(ConnectionListener connectionListener) {
        connectionListeners.remove(connectionListener);
    }

    /**
     * Adds the configuration listener.
     *
     * @param configurationListener the configuration listener
     * @see MsbClientHandler#addConfigurationListener(ConfigurationListener)
     */
    @Override
    public void addConfigurationListener(ConfigurationListener configurationListener) {
        configurationListeners.add(configurationListener);
    }

    /**
     * Removes the configuration listener.
     *
     * @param configurationListener the configuration listener
     * @see MsbClientHandler#removeConfigurationListener(ConfigurationListener)
     */
    @Override
    public void removeConfigurationListener(ConfigurationListener configurationListener) {
        configurationListeners.remove(configurationListener);
    }

    private static boolean isGateway(Service service){
        return Gateway.class.equals(service.getClass());
    }

    /**
     * Creates a Runnable and triggers the execution by the ExecutorService.
     *
     * @param outdata
     */
    private void callFunction(final FunctionCallMessage outdata) {
        functionCallExecutorService.execute(() -> {
            //TODO: check if function is registered by service or subService if is a gateway
            for (FunctionCallsListener functionCallsListener : functionCallsListeners) {
                functionCallsListener.onCallback(outdata.getUuid(), outdata.getFunctionId(), outdata.getCorrelationId(), outdata.getFunctionParameters());
            }
            FunctionCallReference functionCallReference = functionMap.get(outdata.getUuid()+"_"+outdata.getFunctionId());
            if(functionCallReference!=null){
                if (invokeFunctionCallEnabled) {
                    try {
                        Object response = FunctionInvoker.callFunctions(outdata, functionCallReference);
                        if (!functionCallReference.getResponseEvents().isEmpty()) {
                            if(response instanceof MultipleResponseEvent){
                                for(MultipleResponseEvent.ResponseEvent responseEvent : (MultipleResponseEvent) response){
                                    if(functionCallReference.getResponseEvents().contains(responseEvent.getEventId())) {
                                        LOG.debug("Response event: {\"uuid\":\"{}\", \"correlationId\":\"{}\", \"functionId\":\"{}\", \"eventId\":\"{}\"}",
                                                selfDescription.getUuid(),outdata.getCorrelationId(),outdata.getFunctionId(),responseEvent.getEventId());
                                        publish(responseEvent.getEventId(), responseEvent.getObj(), responseEvent.getPriority(),
                                                responseEvent.isCache(), new Date(), outdata.getCorrelationId());
                                    } else {
                                        LOG.error("Event is not published, because it is not defined as response event, please use a publish method instead.");
                                    }
                                }
                            } else {
                                String eventId = functionCallReference.getFunction().getResponseEvents()[0].getEventId();
                                LOG.debug("Response event: {\"uuid\":\"{}\", \"correlationId\":\"{}\", \"functionId\":\"{}\", \"eventId\":\"{}\"}",
                                        selfDescription.getUuid(),outdata.getCorrelationId(),outdata.getFunctionId(),eventId);
                                publish(eventId,response,null,eventCacheEnabled, new Date(),outdata.getCorrelationId());
                            }
                        }
                    } catch (IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | IOException | TypeMismatchException e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
            } else {
                LOG.warn("No function named {} found", outdata.getFunctionId());
            }
        });
    }

    /**
     * Close session.
     *
     * @param status the status
     */
    private void closeSession(CloseStatus status) {
        try {
            sessionWrapper.close(status);
            sessionWrapper.setSession(null);
        } catch (IOException e) {
            LOG.warn("IOException during closeConnection: ", e);
        }
    }

    private static SockJsClient createClient(String url, String trustStorePath, String trustStorePwd) {
        StandardWebSocketClient simpleWebSocketClient = new StandardWebSocketClient();
        if ((url.startsWith("wss://") || url.startsWith("https://")) && trustStorePath != null && trustStorePwd != null) {
            Map<String, Object> userProperties = new HashMap<>();
            userProperties.put("org.apache.tomcat.websocket.SSL_TRUSTSTORE", trustStorePath);
            userProperties.put("org.apache.tomcat.websocket.SSL_TRUSTSTORE_PWD", trustStorePwd);

            if (MsbClient.hostnameVerification) {
                // TODO: only for testing with self-signed certificates and ip
                try {
                    SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                    sslContext.init(null, new TrustManager[]{new SkipX509TrustManager()}, new SecureRandom());
                    userProperties.put("org.apache.tomcat.websocket.SSL_CONTEXT", sslContext);
                    SSLContext.setDefault(sslContext);
                } catch (NoSuchAlgorithmException e) {
                   LOG.error("NoSuchAlgorithmException",e);
                } catch (KeyManagementException e) {
                    LOG.error("KeyManagementException",e);
                }
            }
            simpleWebSocketClient.setUserProperties(userProperties);
        }
        List<Transport> transports = new ArrayList<>(2);
        WebSocketTransport w = new WebSocketTransport(simpleWebSocketClient);
        transports.add(w);
       return new SockJsClient(transports);
    }

    /**
     * Connect.
     *
     * @param websocketTextMessageSize the websocket text message size
     * @return the web socket session
     */
    private static WebSocketSession connect(WebSocketClient sockJsClient, WebSocketHandler webSocketHandler,
                                            String url, int websocketTextMessageSize, int reconnectIntervalMillis) {
          WebSocketSession session = null;
        while (session == null || !session.isOpen()) {
            try {
                ListenableFuture<WebSocketSession> f = sockJsClient.doHandshake(webSocketHandler, url);
                session = f.get();
                session.setTextMessageSizeLimit(websocketTextMessageSize);
            } catch (Exception e) {
                LOG.error(e.getMessage(),e);
                if(session!=null) {
                    try {
                        session.close();
                    } catch (IOException e1) {
                        LOG.error(e1.getMessage(),e1);
                    }
                }
                try {
                    Thread.sleep(reconnectIntervalMillis);
                } catch (InterruptedException e1) {
                    LOG.error(e1.getMessage(), e1);
                    // Restore interrupted state...
                    Thread.currentThread().interrupt();
                }
            }
        }
        return session;
    }

    private static class SkipX509TrustManager extends X509ExtendedTrustManager implements X509TrustManager {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
			// Do nothing because it is a fake trust manager to disable hostname verification
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
			// Do nothing because it is a fake trust manager to disable hostname verification
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
			// Do nothing because it is a fake trust manager to disable hostname verification
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
			// Do nothing because it is a fake trust manager to disable hostname verification
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {
        	// Do nothing because it is a fake trust manager to disable hostname verification
		}

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {
        	// Do nothing because it is a fake trust manager to disable hostname verification
		}
    }

}
