#!/bin/sh

# download the files from the NCBI FTP server
download_ftp_files() {
  ncbi_ftp_server="$1"
  ncbi_file_path="$2"
  local_server_path="$3"
  init_time_stamp="$4"
  ftp_inst_file_name="$5"
  ftp_coll_file_name="$6"

  mkdir -p $local_server_path/ftp
  # download the institutions data file
  wget $ncbi_ftp_server/$ncbi_file_path/$ftp_inst_file_name -P ./ftp -O $local_server_path/ftp/institution_codes.ftp.$init_time_stamp.txt
  if [ $? -ne 0 ]; then
    echo "Failed to download file '$ftp_inst_file_name' from server path $ncbi_ftp_server/$ncbi_file_path"
    echo "Terminating the process now..."
    exit 99
  fi
  # download the collections data file
  wget $ncbi_ftp_server/$ncbi_file_path/$ftp_coll_file_name -P ./ftp -O $local_server_path/ftp/collection_codes.ftp.$init_time_stamp.txt
  if [ $? -ne 0 ]; then
    echo "Failed to download file '$ftp_coll_file_name' from server path $ncbi_ftp_server/$ncbi_file_path"
    echo "Terminating the process now..."
    exit 99
  fi
}

# clean the inputs files, including making a copy to process the files
clean_input_data_files() {
  # once process established, this step won't be required and can be merged
  local_server_path="$1"
  init_time_stamp="$2"
  echo "Cleaning the files downloaded from the ftp server"
  # Make a copy of ftp files to process and generate the ElasticSearch loader files
  mkdir -p $local_server_path/input
  cp $local_server_path/ftp/institution_codes.ftp.$init_time_stamp.txt $local_server_path/input/inst.in.$init_time_stamp.txt
  cp $local_server_path/ftp/collection_codes.ftp.$init_time_stamp.txt $local_server_path/input/coll.in.$init_time_stamp.txt

  # Clean the provided files before transformation
  exp_to_search="[[:space:]]*\|[[:space:]]*"
  replace_with="|"
  sed -i -e "s/${exp_to_search}/${replace_with}/g" $local_server_path/input/inst.in.$init_time_stamp.txt
  sed -i -e "s/${exp_to_search}/${replace_with}/g" $local_server_path/input/coll.in.$init_time_stamp.txt
}

# Process and transform the data file to ElasticSearch bulk upload json files
process_data_files() {
  local_server_path="$1"
  init_time_stamp="$2"
  # set the output directory for generated ElasticSearch bulk upload file
  mkdir -p $local_server_path/output
  touch $local_server_path/output/inst.out.$init_time_stamp.json
  touch $local_server_path/output/coll.out.$init_time_stamp.json

  transform_inst_data_file $local_server_path $current_date
  transform_collection_data_file $local_server_path $current_date
}

# Generate the ElasticSearch bulk upload json file for institutions
transform_inst_data_file() {
  local_server_path="$1"
  init_time_stamp="$2"
  echo "transforming the institutions data file"
  IFS='|'
  [ ! -f $local_server_path/input/inst.in.$init_time_stamp.txt ] && {
    echo "$local_server_path/input/inst.in.$init_time_stamp.txt file not found"
    exit 99
  }
  while read inst_id inst_code aka inst_name country address phone fax record_source home_url url_rule comments coll_type qualifier_type unique_name synonyms; do
    if [[ $inst_id =~ ^[0-9]+$ ]]; then
      echo '{"index":{}}' >>$local_server_path/output/inst.out.$init_time_stamp.json
      JSON_STRING='{"inst_id": '$inst_id', "inst_code": "'$inst_code'", "unique_name": "'$unique_name'", "synonyms": "'$synonyms'", "inst_name": "'$inst_name'", "country": "'$country'", "address": "'$address'", "coll_type": "'$coll_type'", "qualifier_type": "'$qualifier_type'", "home_url": "'$home_url'", "url_rule": "'$url_rule'"}'
      echo "$JSON_STRING" >>$local_server_path/output/inst.out.$init_time_stamp.json
    else
      echo "inst_id is not a valid integer."
    fi
  done <$local_server_path/input/inst.in.$init_time_stamp.txt
}

# Generate the ElasticSearch bulk upload json file for collections
transform_collection_data_file() {
  local_server_path="$1"
  init_time_stamp="$2"
  echo "transforming the collections data file"
  IFS='|'
  [ ! -f $local_server_path/input/coll.in.$init_time_stamp.txt ] && {
    echo "$local_server_path/input/coll.in.$init_time_stamp.txt file not found"
    exit 99
  }
  while read coll_id inst_id coll_name coll_code coll_size coll_type coll_status coll_url coll_url_rule comments qualifier_type; do
    if [[ $coll_id =~ ^[0-9]+$ ]]; then
      echo '{"index":{}}' >>$local_server_path/output/coll.out.$init_time_stamp.json
      JSON_STRING='{"coll_id": "'$coll_id'", "inst_id": "'$inst_id'", "coll_code": "'$coll_code'", "coll_name": "'$coll_name'", "coll_type": "'$coll_type'", "qualifier_type": "'$qualifier_type'", "coll_url": "'$coll_url'", "coll_url_rule": "'$coll_url_rule'"}'
      echo "$JSON_STRING" >>$local_server_path/output/coll.out.$init_time_stamp.json
    else
      echo "coll_id is not a valid integer."
    fi
  done <$local_server_path/input/coll.in.$init_time_stamp.txt
}
