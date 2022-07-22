## Basic Docker Integration

### Create a Docker image packaging as existing jar

#### Build the application
```bash
$ ./gradlew clean build
```

#### Login into DockerHub
```bash
$ docker login dockerhub.ebi.ac.uk
```


#### Build docker image the application
```bash
$ docker build -t dockerhub.ebi.ac.uk/ena-dcap/ena-source-annotation-helper .
```

#### Push the image to the Dockerhub
```bash
$ docker push dockerhub.ebi.ac.uk/ena-dcap/ena-source-annotation-helper
```

#### To run the docker image

```bash
$ docker run -p 8080:8080 dockerhub.ebi.ac.uk/ena-dcap/ena-source-annotation-helper
```

