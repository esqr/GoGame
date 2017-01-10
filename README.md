# GoGame
[![Build Status](https://travis-ci.org/esqr/GoGame.svg?branch=master)](https://travis-ci.org/esqr/GoGame)

## Building & running
#### Requirements
* JDK with JavaFX >= `8u40` (recommended oracle)
* Apache Maven 3

#### Building:
To build: `mvn package`  
maven will resolve dependencies and create jar artifacts

#### Running:
To run server: `java -jar server/target/server-1.0-SNAPSHOT.jar`  
To run client: `java -jar client/target/client-1.0-SNAPSHOT.jar`

## Importing project to IntelliJ Idea
Just import as maven project.  
Note that the imported project will not have run/debug configurations. You have to set up them manually or just run class which contain `main()` method (right click on class and select `Run 'ClassName.main()'`.

Below are classes containing `main()`

| Module | Class Name          | Location in project root directory                          |
| ------ | ------------------- | ----------------------------------------------------------- |
| Server | `Server`            | `server/src/main/java/gogame/server/Server.java`            |
| Client | `ClientApplication` | `client/src/main/java/gogame/client/ClientApplication.java` |
