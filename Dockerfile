FROM openjdk:17
ADD target/infomail.jar infomail.jar
EXPOSE 8190
ENTRYPOINT ["java","-jar","infomail.jar"]