# Testing Guide

This is an overview on how to test the project.

## Prerequisites

- Java JDK 1.8 https://openjdk.java.net and Maven https://maven.apache.org/index.html installed

### Install/Build

To get all required dependencies, run:

```sh
$ mvn install
```

## Basic Test

Unit test are written in `JUnit`.

Run `basic tests` (will execute lint check and unit tests)

```sh
$ mvn test
```

## Code Style Test

We use `sonarlint` and base on the `default ruleset`.

Install the `sonarlint` plugin and run check for violations out of our IDE, 
more information you can find here: https://www.sonarlint.org

Possible rule violations are listed here: https://eslint.org/docs/rules/

## Unit Test

Run `unit tests`

```sh
$ mvn test
```

## Integration Test

Run `integration tests` against local or remote MSB instance.

Set IP of MSB server and use standard ports:

```sh
$ mvn verify -DTESTENV_CUSTOMIP=192.168.0.10
```

Or define urls for websocket interface, smart object management and flow management:

```sh
$ mvn verify -DTESTENV_BROKER_URL=https://ws.15xr.msb.oss.cell.vfk.fraunhofer.de/ \
             -DTESTENV_SO_URL=https://so.15xr.msb.oss.cell.vfk.fraunhofer.de/ \
             -DTESTENV_FLOW_URL=https://flow.15xr.msb.oss.cell.vfk.fraunhofer.de/ \
```

## Test Coverage

The coverage framework `JaCoCo` is used.

Generate the `unit test coverage` report (outputs to target/site/)
```sh
$ mvn test
$ mvn site
```


