FROM openjdk:11-jre
COPY ./target/eureka-server.jar  eureka-server.jar
EXPOSE 8761
ENTRYPOINT ["java","-jar","eureka-server.jar"]
CMD  java -jar  eureka-server.jar
