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
Swagger API documentation can be launched by pointing the browser to [Swagger-UI](https://wwwdev.ebi.ac.uk/ena/sah/api/)

## Actuator Endpoints
- **Health**
  - Displays application health information
  - URL to access - `http://localhost:8080/actuator/health`
- **Info**
  - Displays application information (to be extended to desired attributes)
  - URL to access - `http://localhost:8080/actuator/`

## Migrate Data from NCBI Biocollections FTP Server (Manual Process)
### Step - 1 execute `./ElasticSearch/scripts/process-ncbi-biocollections-data.sh`
- this will process the input files for institutions and collections, and will generate the output files in  `./ElasticSearch/scripts/output` directory

### Step - 2 replace filenames and Elastic URL before executing `./ElasticSearch/scripts/persist-ncbi-biocollections-data.sh`
- replace the generated output file names from the files generated in `./ElasticSearch/scripts/output` directory
- replace the variable occurrences `$ELASTIC_DATASTORE_URL` with the actual ElasticSearch URL
- execute the script `./ElasticSearch/scripts/persist-ncbi-biocollections-data.sh`



## Document References
- Docker integration [Document](./docs/DockerIntegration.md)


## Future Roadmap items
- [ ] Performance / Elastic Queries Optimisation
- [ ] Top 10 limit implementation
- [ ] Review Messages to be displayed in responses
- [ ] Add unit tests



