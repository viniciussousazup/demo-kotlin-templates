FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.9_11_openj9-0.23.0-alpine-slim
ENV LANG C.UTF-8
WORKDIR /home/app
COPY build/libs/app.jar /home/app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/home/app/app.jar"]