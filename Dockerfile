FROM openjdk:17
ADD target/infomail.jar infomail.jar
EXPOSE 8028
ENTRYPOINT ["java","-jar","infomail.jar"]