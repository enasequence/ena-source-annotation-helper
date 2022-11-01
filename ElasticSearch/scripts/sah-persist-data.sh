#!/bin/sh

# persist institution and collections data in elastic indexes
persist_inst_coll_data() {

  es_base_url="$1"
  local_server_path="$2"
  init_time_stamp="$3"

  #persist institution data
  inst_idx_alias=institution
  inst_data_file=$local_server_path/output/inst.out.$init_time_stamp.json
  persist_processed_data_file $inst_idx_alias $es_base_url $inst_data_file

  #persist collection data
  coll_idx_alias=collection
  coll_data_file=$local_server_path/output/coll.out.$init_time_stamp.json
  persist_processed_data_file $coll_idx_alias $es_base_url $coll_data_file
}

# persist the data in the ElasticSearch index
persist_processed_data_file() {

  idx_alias="$1"
  es_base_url="$2"
  data_file="$3"
  #### Get the index name by alias
  url_get_index_name=$es_base_url/_cat/aliases/$idx_alias
  #response=$(curl --request GET --write-out \\n%{http_code} --silent --output - ${url_get_index_name} 2>/dev/null | cut -d ' ' -f2)
  existing_index_name=$(curl --request GET --silent --output - ${url_get_index_name} 2>/dev/null | cut -d ' ' -f2)

  if [ -z "$existing_index_name" ]; then
    echo "Failed to fetch the index information for index alias: $idx_alias"
    echo "Exiting without making any changes in the index..."
    exit 99
  fi

  echo "Existing index being replaced: $existing_index_name"

  ### construct new index name by incrementing the number
  num=$(($(cut -d "_" -f2 <<<"$existing_index_name") + 1))
  new_index_name=$idx_alias"_"$num
  echo "new index to be created: $new_index_name"

  ### create new index
  url_create_idx=$es_base_url/$new_index_name
  create_idx_status_code=$(curl -XPUT --write-out %{http_code} --header 'Content-Type: application/json' -d @"./mapping/v1/$idx_alias.json" --silent --output /dev/null ${url_create_idx})
  if [[ "$create_idx_status_code" -ne 200 ]]; then
    echo "Failed to create new index: $create_idx_status_code"
    echo "Exiting without proceeding further..."
    exit 99
  else
    echo "Index created successfully - ${new_index_name}"
  fi

  ### Load the data into the new index
  url_load_index=$es_base_url/${new_index_name}/_bulk
  load_status_code=$(curl -XPOST --write-out %{http_code} --header 'Content-Type: application/x-ndjson' --data-binary "@$data_file" --silent --output /dev/null ${url_load_index})
  if [[ "$load_status_code" -ne 200 ]]; then
    echo "Data loading failed for new index ${new_index_name}: $load_status_code"
    echo "Exiting without proceeding further..."
    exit 99
  else
    echo "Data loaded successfully in new the index - ${new_index_name}"
  fi

  #### Delete the alias from the existing index
  delete_alias_url=$es_base_url/$existing_index_name/_alias/$idx_alias
  del_alias_status=$(curl --request DELETE --write-out %{http_code} --silent --output /dev/null ${delete_alias_url})
  if [[ "$del_alias_status" -ne 200 ]]; then
    echo "Invalid response while deleting the alias from the existing index - $existing_index_name: $del_alias_status"
    echo "Exiting without proceeding further..."
    exit 99
  else
    echo "Deleted the alias from existing index: $existing_index_name"
  fi

  #### Create new alias on the new index
  create_alias_url=$es_base_url/$new_index_name/_alias/$idx_alias
  create_alias_status=$(curl --request POST --write-out %{http_code} --silent --output /dev/null ${create_alias_url})
  if [[ "$create_alias_status" -ne 200 ]]; then
    echo "Invalid response while creating the alias on the new index - $new_index_name: $create_alias_status"
    echo "Exiting without proceeding further..."
    exit 99
  else
    echo "Created the alias on the new index: $new_index_name"
  fi

  echo "Data import process completed successfully for: $idx_alias"
}
