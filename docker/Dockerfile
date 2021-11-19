FROM localhost/ubuntu-server-base

ENV TZ=Europe/Moscow
RUN cp /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./report-service-0.0.1.jar /home/web-service.jar
WORKDIR /home

ENTRYPOINT ["java", "-Xmx2G", "-Dsun.jnu.encoding=UTF-8", "-jar", "/home/web-service.jar"]

EXPOSE 8080
