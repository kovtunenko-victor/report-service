FROM ubuntu:20.04

RUN apt-get -y update && apt-get install -y tzdata
RUN apt-get install -y openjdk-8-jdk
COPY ./target/report-service-0.0.1.jar /home/web-service.jar

ENTRYPOINT ["java", "-jar", "/home/web-service.jar"]

EXPOSE 8080