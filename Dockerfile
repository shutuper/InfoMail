FROM openjdk:17
ADD target/infomail.jar infomail.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","infomail.jar"]