#Run first 'mvn package spring-boot:repackage' to generate the jar file and copy file in Dockerfile folder

FROM openjdk:11
COPY springboot-auth-1.0-SNAPSHOT.jar auth.jar
ENTRYPOINT ["java","-jar","auth.jar"]
EXPOSE 8081
