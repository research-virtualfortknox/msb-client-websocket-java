[![Apache 2.0 License](https://img.shields.io/:license-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.research-virtualfortknox/msb-client-websocket/badge.svg?color=blue)](https://maven-badges.herokuapp.com/maven-central/com.github.research-virtualfortknox/msb-client-websocket)
[![Javadocs](https://www.javadoc.io/badge/com.github.research-virtualfortknox/msb-client-websocket.svg?color=blue)](https://www.javadoc.io/doc/com.github.research-virtualfortknox/msb-client-websocket)
[![SourceSpy Dashboard](https://sourcespy.com/shield.svg)](https://sourcespy.com/github/researchvirtualfortknoxmsbclientwebsocketjava/)

[![Build Status](https://app.travis-ci.com/research-virtualfortknox/msb-client-websocket-java.svg?branch=master)](https://app.travis-ci.com/research-virtualfortknox/msb-client-websocket-java)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=research-virtualfortknox_msb-client-websocket-java&metric=alert_status)](https://sonarcloud.io/dashboard?id=research-virtualfortknox_msb-client-websocket-java)
[![Known Vulnerabilities](https://snyk.io/test/github/research-virtualfortknox/msb-client-websocket-java/badge.svg?targetFile=msb-client-websocket/pom.xml)](https://snyk.io/test/github/research-virtualfortknox/msb-client-websocket-java)
[![Coverage Status](https://coveralls.io/repos/github/research-virtualfortknox/msb-client-websocket-java/badge.svg?branch=master)](https://coveralls.io/github/research-virtualfortknox/msb-client-websocket-java?branch=master)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fresearch-virtualfortknox%2Fmsb-client-websocket-java.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fresearch-virtualfortknox%2Fmsb-client-websocket-java?ref=badge_shield)

<p align="center">
  <a href="https://research.virtualfortknox.de" target="_blank" rel="noopener noreferrer">
    <img src="https://research.virtualfortknox.de/static/cms/img/vfk_research_logo.png" alt="VFK Research Logo" height="70" >
  </a>
</p>

# MSB websocket client library for Java

**Compatibility Matrix**

Client version compatibility to MSB versions:

| | **1.5.x-RELEASE** | **1.6.x-RELEASE** |
|---|:---:|:---:|
| 1.0.x       | x | x |

## Welcome

If you want to contribute, please read the [Contribution Guidelines](.github/CONTRIBUTING.md).

If you want to test this client by using the example project.

If you want to know how to use this client in your own project, read below.

## What is VFK MSB

TODO: Link to general documentation about VFK MSB

You can use this client to connect a java app to VFK MSB.

## Prerequisites

Import to your application via maven

```xml
<properties>
    ...
    <!-- Use the latest version whenever possible. -->
    <msb-client-websocket.version>1.0.6-RELEASE</msb-client-websocket.version>
    ...
</properties>

<dependencies>
    ...
    <dependency>
      <groupId>com.github.research-virtualfortknox</groupId>
      <artifactId>msb-client-websocket</artifactId>
      <version>${msb-client-websocket.version}</version>
    </dependency>
    ...
</dependencies>
```

## Create self-description

The figure below shows a minimal required `self-description model` of a smart object / application.
Every smart object / application requires (must have) a uuid and a token.
The uuid is competent for identification
and the token is used to verify the smart object / application for its owner on the MSB side.

![Self Description](doc/images/self-description.png)

The data model of an application and smart object is identical, both are services.
A smart object represents a physical object which is connected to MSB. 
An application is a pure software solution which is connected.

Each service is capable of triggering events and receiving function calls.
The declaration of these elements and the service can be done with the help of annotations or by the constructor.

An **annotation** is a form of syntactic metadata or a specific marker that can be added to Java source code but has no effect on a program construct at run time [see 1]. 
The annotated instances can be classes, methods, variables, parameters, and packages. 
Annotations are embedded in class files and may be retained by the Java VM to become retrievable at run time [see 2, S. 67].

TODO: Here you can find more information about the `self-description structure` and `supported data formats`.

#### Alternative 1 - By annotations:

Declaration of self description:

  - Annotation `@SelfDescription` describes the service

```java
@SelfDescription(
    uuid = "df61a143-6dab-471a-88b4-8feddb4c9e71",
    name = "Drill Machine",
    description = "A small drill-machine, which reminding you of your last visit to the dentist.",
    token = "57e721c9bcdf",
    type = SelfDescription.Type.SMART_OBJECT
)
public class DrillMachine {
    // …
}
```

#### Alternative 2 - By constructor:

If you do not wan to use annotations, use the constructor to define the basic self-description.

```java
public class DrillMachine {
    // …
    SmartObject smartObject = new SmartObject(uuid, name, description, token);
    // …
}
```

### Add Events

Add `events` to your smart object or application which can be send to MSB.

#### Alternative 1 - By annotation:

Declaration of thrown events:

  - Annotation `@Events` excepts a list of event declarations
  - Annotation `@EventDeclaration` describes the event

```java
@Events({
    @EventDeclaration(dataType = Date.class,description = "Timestamp of start machine",name = "Start", eventId = "START"),
    @EventDeclaration(dataType = Date.class,description = "Timestamp of stop machine",name = "Stop", eventId = "STOP"),
    @EventDeclaration(description = "periodical transmitted event", name = "Event pulse", eventId = "PULSE")
})
// …
public class DrillMachine {
    // …
}
```

#### Alternative 2 - By constructor:

The event declaration can be added directly to the created service instance.

```java
public class DrillMachine {
    // …
    public void constructSelfDescription() {
        SmartObject smartObject = new SmartObject(uuid, name, description, token);
        smartObject.addEvent(new Event("TEMPERATURE", "Temperature", "Current temperature", DataFormatParser.parse(float.class)));
        // …
    }
}
```

Or it can be added via the `MsbClientHandler`.

```java
public class DrillMachine {
    // …
    public void constructSelfDescription() {
        // …
        MsbClientHandler handler = msbClient.getClientHandler();
        handler.addEvent(smartObject, "TEMPERATURE", "Temperature", "Current temperature", float.class, EventPriority.MEDIUM);
        // …
    }
}
```

### Add Functions

Add `functions` and their implementations your smart object or application is able to handle.

#### Alternative 1 - By annotation:

Declaration of callable functions:

  - Annotation `@FunctionHandler` signs to a class for package scanning which contains the function calls
  - Annotation `@FunctionCall` signs to a method of this class to make the method be callable over the MSB. 
    The combination of the `FunctionHandler` and `FunctionCall` path must be a unique identifier for this method. 
    An optional parameter are the responseEvents, which contains a list of before decelerated events. 
    This events are the possible return values of this method, which are pushed to the MSB.
  - Annotation `@FunctionParam` sings to a parameter in question

```java
@FunctionHandler(path="/controller")
public class DrillMachineController {

    @FunctionCall(path="/start", description = "start processing", responseEvents = {"START","STOP"})
    public MultipleResponseEvent startProcessing(@FunctionParam(name = "drilling_depth") int drillingDepth) {
      MultipleResponseEvent multipleResponseEvent = new MultipleResponseEvent();
      // …
      if(started) {
        multipleResponseEvent.addResponseEvent("START",new Date());
      } else {
        multipleResponseEvent.addResponseEvent("STOP",new Date());
      }
      return multipleResponseEvent;
    }

    @FunctionCall(path="/stop", description = "stop processing", responseEvents = {"STOP"})
    public Date stopProcessing() {
      // …
      return new Date();
    }
    
    public void printString(String msg){
        
    }

}
```

The return value of the method call is used for publishing a response event. 
This must therefore correspond to the defined `dataType` of the event referenced in the parameter `responseEvent` of the `FunctionCall` annotation.
For several possible response events there is a special wrapper object `MultipleResponseEvent`. 
When publishing the response events, the `correlationId` of the function call is automatically added to the event.

#### Alternative 2 - By constructor:

The function declaration can be added to the constructed service instance directly.
Method invocation not possible in this way, use `FunctionCallsListener` to observe function calls.

```java
public class DrillMachine {
    // …
    public void constructSelfDescription() {
        SmartObject smartObject = new SmartObject(uuid, name, description, token);
        smartObject.addFunction(new Function("printString", "printString", "print a string", DataFormatParser.parse("msg",String.class)));
        // …
        MsbClientHandler handler = msbClient.getClientHandler();
        handler.addFunctionCallsListener(functionCallsListener);
    }
    
    FunctionCallsListener functionCallsListener = new FunctionCallsListener() {
        @Override
        public void onCallback(String serviceUuid, String functionId, String correlationId, Map<String,Object> functionParameters) {
            // …
        }
    };
}
```

To utilize the method invocation without annotations, the `MsbClientHandler` can be used as follows.

```java
public class DrillMachine {
    // …
    public void constructSelfDescription() {
        SmartObject smartObject = new SmartObject(uuid, name, description, token);
        // …
        MsbClientHandler handler = msbClient.getClientHandler();
        // …
        try {
            Method stringMethod = TestClientFunctionHandler.class.getMethod("printString", String.class);
            handler.addFunction(smartObject, "printString", "printString", "print a string", new String[]{"PULSE"}, new DrillMachineController(), stringMethod);
        } catch (NoSuchMethodException e) {
            LOG.error("NoSuchMethodException",e);
        }

       
    }
}
```

### Configuration parameters

Configuration parameters are a simple list of key value pairs for the smart object or application.
It is displayed and can be customized in the MSB UI to change your apps behaviour during runtime.

#### Alternative 1 - By annotation:

You can add a configuration parameter declarative with the annotation `@ConfigParam` for a class attribute. 
Note that the class need be also annotated with `@SelfDescription`, `@EventDeclaration` or `@FunctionHandler`.

```java
@SelfDescription("...")
public class DrillMachineController {

    @ConfigurationParam(name = "drilling_speed")
    public int drillingSpeed = 100;
    // …
}
```

#### Alternative 2 - By constructor:

The configurations parameters can be added to the constructed service instance directly or via the `MsbClientHandler`.

```java
public class DrillMachine {
    // …
    public void constructSelfDescription() {
        Map<String, ParameterValue> parameters = new HashMap<>();
        parameters.put("drillingSpeed", new ParameterValue(100, PrimitiveType.INTEGER, PrimitiveFormat.INT32));
        parameters.put("scheduled", new ParameterValue(true, PrimitiveType.BOOLEAN));
        Configuration configuration = new Configuration(parameters);
        
        SmartObject smartObject = new SmartObject(uuid, name, description, token);
        smartObject.setConfiguration(configuration);
        // …
        MsbClientHandler handler = msbClient.getClientHandler();
        handler.addConfigParam("drillingSpeed", "100", PrimitiveFormat.INT32);
        // …
    }
}
```

#### React on configuration change:

To observe configuration parameter updates (after changed in MSB UI) to change your app behaviour, a `ConfigurationListener` implementation must be provided.

```java
public class DrillMachine {
    // …
    public void constructSelfDescription() {
        MsbClientHandler handler = msbClient.getClientHandler();
        // …
        handler.addConfigurationListener(configurationListener);
    }
    
    ConfigurationListener configurationListener = new ConfigurationListener() {
        @Override
        public void configurationRemoteChanged(ConfigurationMessage configuration) {
            // ...
        }
    };
}
```

## Connect and Register Client

The `MsbClientHandler` provides register methods for passing a package path. 
The package path specifies the root package from which the annotations are searched. 
So it is a good idea to use this variant of registration if annotation is used to create the self-description of the service.

```java
public class DrillMachine { 
    // …
    public void startClient() {
        final String url = "ws://127.0.0.1:8085";
        MsbClient msbClient = new MsbClient.Builder().url(url).build();
        Future<MsbClientHandler> future = msbClient.connect();
        try {
            MsbClientHandler handler = future.get();
            handler.register("de.fhg.ipa.vfk.msb.client.websocket.test");
        } catch (InterruptedException | ExecutionException | IOException e){
            // …
      }
      // …
  }
}
```

If the self-description is created using constructor, the `MsbClientHandler` also provides register methods for it, as in the following example.

```java
public class DrillMachine { 
    // …
    public void startClient(SmartObject smartObject) {
        // …
        try {
            handler.register(smartObject);
        } catch (InterruptedException | ExecutionException | IOException e){
            // …
      }
      // …
  }
}
```

To use certain instances of the classes with `@FunctionHandler`annotation for the method invocation, these can also be passed during registration.

```java
public class DrillMachine { 
    // …
    public void startClient(SmartObject smartObject) {
        // …
        try {
            handler.register("de.fhg.ipa.vfk.msb.client.websocket.test", new Object[]{new DrillMachineController()});
        } catch (InterruptedException | ExecutionException | IOException e){
            // …
      }
      // …
  }
}
```

These were only a few examples there are still more variants of the register method.

You will get an `IO_CONNECTED` and `IO_REGISTERED` event from MSB, if successful. 
This can be observed by adding a `ConnectionListener` to the `MsbClientHandler` instance.

## Event publishing

For publishing an event to a MSB websocket interface,
only the `eventId` and `data` are required of the already specified event (see above).

```java
public class DrillMachine { 
    // …
    public void publishStartEvent() {
        MsbClientHandler handler = msbClient.getClientHandler();
        handler.publish("START", new Date()); 
    }
    // …
}
```

The MSB responds with an `IO_PUBLISHED` event, if successful. 
This can be observed by adding a `ConnectionListener` to the `MsbClientHandler` instance.

By default events are published with a low priority.

It is also possible to `set the priority` of an event.

There are three possible priorities for events like it is shown at the following table.

| `Constant` | `Value` |
|:---:|:---:|
| LOW | 0 |
| MEDIUM| 1 |
| HIGH| 2 |

```java
public class DrillMachine { 
    // …
    public void publishHighStartEvent() {
        MsbClientHandler handler = msbClient.getClientHandler();
        handler.publish("START", new Date(), EventPriority.HIGH);
    }
    // …
}
```

Another option is to publish an event as a cached event by setting the cache parameter to true or false, 
which deviates the behavior from the global cache setting.

If it is set to true, this means that the event is not deleted if the connection is broken.

```java
public class DrillMachine { 
    // …
    public void publishUncachedStartEvent() {
        MsbClientHandler handler = msbClient.getClientHandler();
        handler.publish("START", new Date(), EventPriority.LOW, true);
    }
    // …
}
```

## Function call handling

As shown above the methods of a `@FunctionHandler` annotated class, which are annotated with `@FunctionCall`, are invokable by the client library. 
This is done via reflection. It is also possible to pass a specific instance of a function handler to the client during the registration. 
Otherwise the client will create an instance of the function handler by itself. 
To allow this the default constructor is required at function handler class.

As shown above, the annotated methods `@FunctionCall` of an annotated class `@FunctionHandler` are called from the client library. 
This is done by reflection. It is also possible to pass a particular instance of a function handler to the client during registration. 
Otherwise, the client itself creates an instance of the function handler. 
To enable this, the default constructor is required in the function handler class.

The function invocation can be disabled and function calls can be handled by your own listener implementation.

```java
public class DrillMachine { 
    // …
    public void startClient() {
        final String url = "wss://127.0.0.1:8084";
        MsbClient msbClient = new MsbClient.Builder().url(url).disableFunctionCallsInvocation().build();
       // …
   }
 }
 ```

## SSL/TLS connection configuration

To enable `SSL/TLS`, you need to specify wss:// or https:// in the URL instead of ws:// or http://.

Furthermore, it is necessary to specify a trust store in the client,
which contains the public certificate of the MSB interface, so that it is considered trustworthy.

```java
public class DrillMachine { 
    // …
    public void startClient() {
        final String url = "wss://127.0.0.1:8084";
        final String trustStore = "./truststore.trs";
        final String password = "password";
        MsbClient msbClient = new MsbClient.Builder().url(url).trustStore(trustStore, password).build();
      // …
  }
}
```

Open websocket interface in the browser and export the certificate. 
Choose the `X.509 Certificate (DER)` type, so the exported file has a der extension.
Assuming the file is called msb.der, pick the alias 'msb' for this certificate.

Or you use the `openssl s_client` to export the certificate (https://www.openssl.org/docs/man1.1.1/man1/s_client.html)

```sh
$ openssl s_client -host 127.0.0.1 -port 8084 -prexit -showcerts
```

Now you can create a new truststore and use it as shown above, e.g. with the following command.

```sh
$ keytool -import -file msb.der -alias msb -keystore CERTS.trs
```

Or you can import the certificate into the existing truststore of your local JVM installation that is used to run the client.
You will be asked for a password, the default is 'changeit'.

```sh
$ keytool -import -alias msb -keystore  <path-to-jre>/lib/security/cacerts -file msb.der
```

If you use an IP instead of a public url during development,
it will be necessary to disable the hostname verification to connect via web socket secure.

```java
public class DrillMachine { 
    // …
    public void startClient() {
        final String url = "wss://127.0.0.1:8084";
        final String trustStore = "./truststore.trs";
        final String password = "password";
        MsbClient msbClient = new MsbClient.Builder().url(url).trustStore(trustStore, password).disableHostnameVerification().build();
      // …
  }
}
```

## Connection recovery

If connection to the common websocket interface is broken the client performs a reconnect.

After a reconnect the registration at the MSB will be redone automatically by the client.

You can also change this interval by setting an integer value in `ms` for the reconnect interval.

```java
public class DrillMachine { 
    // …
    public void startClient() {
        final String url = "ws://127.0.0.1:8085";
        MsbClient msbClient = new MsbClient.Builder().url(url).reconnectInterval(10000).build();
      // …
  }
}
```

Or you can disable the automatic reconnect.

```java
public class DrillMachine { 
    // …
    public void startClient() {
        final String url = "ws://127.0.0.1:8085";
        MsbClient msbClient = new MsbClient.Builder().url(url).disableAutoReconnect().build();
      // …
  }
}
```

## Event caching

If the client loses the connection, the published events are cached in a queue.

After a successful reconnection, the queued events are published to MSB (FIFO principle).
The default size of the queue is 1000 entries. The size can be changed:

```java
public class DrillMachine { 
    // …
    public void startClient() {
        final String url = "ws://127.0.0.1:8085";
        MsbClient msbClient = new MsbClient.Builder().url(url).eventCacheSize(9999).build();
      // …
  }
}
```

If no event caching is needed, you can disable it.

```java
public class DrillMachine { 
    // …
    public void startClient() {
        final String url = "ws://127.0.0.1:8085";
        MsbClient msbClient = new MsbClient.Builder().url(url).disableEventCache().build();
      // …
  }
}
```

## Debug mode

It might be also helpful to enable data format validation, to check if an event value is valid

```java
public class DrillMachine { 
    // …
    public void startClient() {
        final String url = "ws://127.0.0.1:8085";
        MsbClient msbClient = new MsbClient.Builder().url(url).enabledDataFormatValidation().build();

      // …
  }
}
```

## Logging

Logging is implemented with [SLF4J (Simple Logging Facade for Java)](http://www.slf4j.org), which gives access to many logging frameworks 
such as Log4j, Logback or java.util.logging. This allows the end user to plug-in the desired logging framework. 
This section shows how to use Logback as logging functionality without making any change in application code.

If you want to use [Logback](http://logback.qos.ch) for logging, you only need to add the logback-classic jar to the project dependency.

```xml
<dependency>
  <groupId>ch.qos.logback</groupId>
  <artifactId>logback-classic</artifactId>
  <version>${logback.version}</version>
</dependency>
```

To configure the log output, the configuration file logback.xml must be available at the root resource 
folder src\main\resources. 
The following examples illustrate how to set the root logger to INFO and a specific logger to DEBUG, 
which causes info, warning and error messages from all loggers in the application to be logged and also DEBUG for the specific logger:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- set log level in the logback.xml file -->

<configuration>
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <layout class="ch.qos.logback.classic.PatternLayout">
      <Pattern>
        %d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
      </Pattern>
    </layout>
  </appender>

  <root level="INFO">
    <appender-ref ref="STDOUT"/>
  </root>

  <logger name="de.fhg.ipa.vfk" level="DEBUG" />

</configuration>
```


## License
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fresearch-virtualfortknox%2Fmsb-client-websocket-java.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2Fresearch-virtualfortknox%2Fmsb-client-websocket-java?ref=badge_large)
