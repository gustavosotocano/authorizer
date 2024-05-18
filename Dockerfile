FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 2222
ARG JAR_FILE=target/authorizer-0.1.0.jar
ADD ${JAR_FILE} authorizer.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/authorizer.jar"]
