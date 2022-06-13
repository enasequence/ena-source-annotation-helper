FROM java:8-jre

ADD build/libs/clearinghouse-1.0.0.jar clearinghouse-1.0.0.jar

EXPOSE 8080

ARG JAR_FILE=./build/libs/source-annotation-helper-1.0.0.jar
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
