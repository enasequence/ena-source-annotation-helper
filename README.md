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
- [ ] Expose Search as well as REST APIs for now
- [ ] Contruct and Validate to be exposed as REST APIs
- [ ] Contruct is going to be a REST call
- [ ] Document Institutes / Collections / Contruct based on the current understanding 
- [ ] Start building basic UI / HTML 
- [ ] GraphQL UI exposure -
  - [ ] Customise GraphQL endpoints
  - [ ] UI Customisation to later merge it with ENA Portal
- [ ] GraphQL - logging customisation for more meaningful logs
- [ ] GraphQL - exception handling - Global Exception Handler
- [ ] GraphQL - Unit Tests ??
- [ ] GraphQL Instrumentation / observability for production environment
- 
- 


