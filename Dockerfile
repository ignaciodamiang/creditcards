FROM openjdk:17-jdk-alpine
EXPOSE 8080
ADD target/creditcards.jar creditcards.jar
ENTRYPOINT ["java","-jar","/creditcards.jar"]