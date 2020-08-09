#Run first 'mvn package' to generate the jar file

FROM openjdk:11-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} auth.jar
ENTRYPOINT ["java","-jar","/auth.jar"]
EXPOSE 8081
