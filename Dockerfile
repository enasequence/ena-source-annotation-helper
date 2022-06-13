FROM java:8-jre

EXPOSE 8080

ARG JAR_FILE=./build/libs/source-annotation-helper*.jar
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
