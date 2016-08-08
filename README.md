# Sber tech test task
Sber tech test task is called `Stt`. `Stt` is accounting application. It allows to list registered accounts, operations on an account and to make transfers between accounts.

## Install
The `Stt` requires Java 8 to run.
Since Java 8 is installed, everything is ready to play with accounts.

## Build
`Stt` contains three parts. They are: `stt-model`, `stt-client`, `stt-server`. `stt-model` contains model classes and most abstract interfaces. It is shared between `stt-client` and `stt-server`.
`stt-server` is Spring MVC application and `stt-client` is JavaFX GUI client for `stt-server`.
To build the `Stt` you need Maven. Cd into a folder of the project and type `mvn clean package` on the command line. Note, tests requre `H2` database already run. `H2` runtime dependency is included in maven dependencies and it will be installed in Maven repository by the build process. But tests will not work unless `H2` database server is run. So, you can run `H2` database from maven repository folder (java -jar  /<user-home-folder>/.m2/repository/com/h2database/h2/1.4.192/h2-1.4.192.jar ) or you can download `H2` database jar manually and also simply run it as java -jar h2-1.4.192.jar.

To view code coverage reports after `maven clean package` has been executed, go to `target/site/jacoco` subfolder of every project and view `index.html` file in this subfolder with your browser.

To generate project reports (FindBugs, CMD, PMD and CheckStyle), cd into a folder of a particular project and type `mvn site` on the command line.

To view these reports, go to `target/site` subfolder of a particuler project and view `project-reports.html` file in this subfolder with your browser.

## Run
To run `stt-server` you need running `H2` database server. Also you need Servlet container to deploy `stt-server-1.0-SNAPSHOT.war`. It is recemmended to use Apache tomcat for playing purposes. Note, that by default Aapche tomcat has no jdbc drivers on its classpath, so plaese add `h2-1.4.192.jar` to `tomcat/lib` folder before deploying and running the application.
The deplyment procedure is generic. So run an `H2` instance and deploy `stt-server-1.0-SNAPSHOT.war`. After that, everything is ready to play with accounts. 
Cd into a `target` folder of the project `stt-client` and type on the command line `java -jar stt-client-1.0-SNAPSHOT.jar`.

Alternatively, you may use some IDE for assistance in build, test, deployment and running processes. 

