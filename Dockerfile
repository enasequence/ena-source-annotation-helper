FROM openjdk:17.0.2

EXPOSE 8080

ARG JAR_FILE=./build/libs/source-annotation-helper.jar
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
