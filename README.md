# Source Annotation Helper Application

## Application Requirements / Context
For application requirements, please refer to the document - 
[Project Requirements](https://docs.google.com/document/d/1LyIMCls1Zf6r0TQRXuFy6obXwfacQQ6gqfcUpEUc7os/edit?pli=1#heading=h.dqhch82jqy5z)

## Pre-Requisites
Following applications must be installed, configured and accessible for the build and deployment to work -
- Gradle
- JDK 8
- ElasticSearch

## Swagger Documentation
Swagger API documentation can be launched by pointing the browser to [Swagger-UI](http://localhost:8080/swagger-ui/)

## Actuator Endpoints
- **Health**
  - Displays application health information
  - URL to access - `http://localhost:8080/actuator/health`
- **Info**
  - Displays application information (to be extended to desired attributes)
  - URL to access - `http://localhost:8080/actuator/`


## Document References
- Docker integration [Document](./docs/DockerIntegration.md)


## Future Roadmap items
- [ ] Performance / Elastic Queries Optimisation
- [ ] Top 10 limit implementation
- [ ] Review Messages to be displayed in responses
- [ ] Add unit tests



