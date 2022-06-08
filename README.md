# GraphQL Reference 

### GraphQL UI URL -
```shell
http://localhost:8080/graphiql
```

### GraphQL API Endpoint -
```shell
http://localhost:8080/graphql
```

### Sample Request

#### Query Institutes - 
```shell
query institutes ($queryStr: String) {
   institutes (query: $queryStr) {
     instId
     instCode
     uniqueName
     instName
   }
 }
```
#### Query Variables -
```shell
{
  "queryStr": "ANS"
}
```

#### Query Collections -
```shell
query collections($queryStr: String) {
    collections (query: $queryStr) {
     collId
     instId
     collCode
     collName
   }
 }
```
#### Query Variables -
```shell
{
  "queryStr": "MQtes"
}
```

##Tasks to complete
- [X] Springboot GraphQL ElasticSearch Fuzzy Search Flow
- [X] GraphQL -- GraphQL format Handling + Handling of Variables
- [X] GraphQL UI exposure -
  - [X] Integrate Querying Playground / UI
  - [X] Publish Schema Docs on UI
- [X] Bring all in convention
- [X] Bulk Load Elastic Indexes for Institutes and Collections 
- [X] Expose Institutes and Collections Search as REST APIs for now
- [X] Construct and Validate to be exposed as REST APIs
- [X] Instrumentation / observability for production environment
- [X] Improve logging
- [ ] Review Messages to be displayed in responses
- [ ] Add unit tests
- [ ] Deployment gitlab-ci to be constructed
- [ ] Deploy in development environment
- [ ] Document Institutes / Collections / Construct based on the current understanding
- [ ] 
- 



