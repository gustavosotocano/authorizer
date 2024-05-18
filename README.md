## Authorizer in Java

This application contains an implementation for a Spring application .
 

## Getting Started

### Requirement

This application requires Java 8 or later.

### Building and running the application

To build the application run this command in the project directory:
```
mvn package
```
To start the application run this command:
```
mvn spring-boot:run
```
To Access the application
```
The application open an Socket in port 2222
```
stdin
```
{"account": {"active-card": true, "available-limit": 100}}
```
out 
```
{"account": {"active-card": true, "available-limit": 100}, "violations": []}
```
To run the application from docker
```
docker build -t containerName
docker run  -p 2222:2222 containerName
For find the ip address
docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' containerName 