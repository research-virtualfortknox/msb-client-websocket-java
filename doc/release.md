# Release Guide

This is an overview of the release management of the project.

##Version scheme

| `Description` | `Version Tag` |
|:---:|:---:|
| During development | x.x.x-SNAPSHOT
| For release | x.x.x-RELEASE

##Requirements for multi-module projects

Define important properties in the root pom. 

Add properties for all dependencies versions.

Always use this properties in the child module poms when adding client dependencies

##Set new version

If a new version for a multi-module project (e.g. vfk.platform) has to be set, use the follwoing command:

```sh
mvn versions:set -DnewVersion=0.0.4
```

for the root project. All child modules will be updated automatically.

A temporary backup of all changed pom files are created automatically.

Check if the versions were update correctly e.g. by running mvn clean install and checking the local maven repository .m2.

Revert version update
```sh
mvn versions:revert 
 ```
or commit version update
```sh
 mvn versions:commit
 ```
 
 ## Publish release
 
 Create and push a `tag` to git repo. 
 The CI tool `Travis` will start a build and deploy the results to 
 
 https://bintray.com/research-virtualfortknox/research-virtualfortknox/msb-client-websocket-java.
 
 The new release at `JFrog Bintray` will be published to `JCenter` and `MavenCentral`. 
 