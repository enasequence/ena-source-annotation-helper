## Basic Docker Integration

### Create a Docker image packaging as existing jar

```bash
$ ./gradlew clean build
$ docker build . -t source-annotation-helper-app -f Dockerfile
```

### To run the docker image

```bash
$ docker run -p 8080:8080 source-annotation-helper-app
```