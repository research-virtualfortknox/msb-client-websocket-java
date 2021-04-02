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
 
 ## Publish snapshots and releases

Push a `commit` to `master` branch will trigger the github action `maven-publish.yml`, 
which build and deploy a snapshot to our [github packages repo](https://github.com/orgs/research-virtualfortknox/packages).

Create and push a `tag` to git repo will start the github action `maven-central-publish.yml`, 
which build and deploy a release to [sonatype staging repo](https://oss.sonatype.org/#stagingRepositories). 
After a validation the new release at `sonatype` will be published to `MavenCentral`. 
 