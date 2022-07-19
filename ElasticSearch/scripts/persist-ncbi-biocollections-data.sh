#!/bin/sh

# TODO -- PASS THESE AS SCRIPT ARGUMENTS
INSTITUTION_PROCESSED_FILE=output/Institutions_ElasticSearchData.2022.06.30.json
COLLECTION_PROCESSED_FILE=output/Collections_ElasticSearchData.2022.06.30.json

# Delete the existing data from the ElasticSearch indexes
curl -XPOST '$ELASTIC_DATASTORE_URL/institution/_delete_by_query' --header 'Content-Type: application/json' --data-raw '{"query": {"match_all": {}}}'
curl -XPOST '$ELASTIC_DATASTORE_URL/collection/_delete_by_query' --header 'Content-Type: application/json' --data-raw '{"query": {"match_all": {}}}'

# Load new data in the ElasticSearch indexes
curl -XPOST '$ELASTIC_DATASTORE_URL/institution/_bulk' --header 'Content-Type: application/x-ndjson' --data-binary @$INSTITUTION_PROCESSED_FILE
curl -XPOST '$ELASTIC_DATASTORE_URL/collection/_bulk' --header 'Content-Type: application/x-ndjson' --data-binary @$COLLECTION_PROCESSED_FILE
