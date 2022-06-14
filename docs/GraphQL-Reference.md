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