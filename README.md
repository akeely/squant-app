# squant.io

This repository contains a Spring Boot app that provides the backend for [squant.io](https://www.squant.io).

The back-end is responsible for authenticating with Google, serving the static front-end `public` directory, and
exposing a REST API on port 8081 that the front-end uses to access data.

The front-end is provided by [squant-js](https://www.github.com/akeely/squant-js).

## Creating a new EC2 instance

To create a new EC2 instance:
1. Launch an instance that is attached to the ALB on ports 8081 (app) and 9081 (health check)
1. Install [sdkman](https://sdkman.io/install)
1. Install Java 13 `sdk install java 13.0.2-zulu` (update to latest version)
1. On the instance, create the directory `/opt/squant` and `sudo chown ec2-user:ec2-user /opt/squant`
1. Copy the production configuration to `/opt/squant/config/application.yml`
1. Build the production jar: `mvn clean install -Pprod`
1. Copy the jar file to `/opt/squant/squant-<version>.jar`
1. Create the service directories: `sudo mkdir /var/{run,log}/squant; sudo chown ec2-user:ec2-user /var/{run,log}/squant`
1. Create a file `/opt/squant/squant-<version>.conf` and add the following contents: 
```
JAVA_OPTS='--enable-preview'
JAVA_HOME='/home/ec2-user/.sdkman/candidates/java/current'
PID_FOLDER='/var/run/squant'
LOG_FOLDER='/var/log/squant'
```
1. Create a service simlink: `sudo ln -s /opt/squant/squant-app-0.0.1-SNAPSHOT.jar /etc/init.d/squant`
1. Start the service `service squant start`
