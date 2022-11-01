#!/bin/sh
### -----------------------------
# variables to be configured at the time of script setup
ncbi_ftp_server="https://ftp.ncbi.nih.gov"
ncbi_file_path="pub/taxonomy/biocollections"
ftp_inst_file_name="Institution_codes.txt"
ftp_coll_file_name="Collection_codes.txt"
# files storage path on local server (used for processing and maintaining the history)
local_server_path=./sah
# ElasticSearch base url
es_base_url=http://localhost:9200
### -----------------------------

current_date=$(date "+%Y.%m.%d-%H.%M")
echo "SAH NCBI data import started at: $current_date"

### Download the data
echo "Starting data download from NCBI FTP server"
source ./sah-fetch-process-data.sh
download_ftp_files $ncbi_ftp_server $ncbi_file_path $local_server_path $current_date $ftp_inst_file_name $ftp_coll_file_name
echo "Completed data download from NCBI FTP server"

### Process the data in required format
echo "Data processing started"
# clean input data files
clean_input_data_files $local_server_path $current_date
# process and transform the downloaded files
process_data_files $local_server_path $current_date
echo "Data processing completed"

### persist the data in ENA database
source ./sah-persist-data.sh
persist_inst_coll_data $es_base_url $local_server_path $current_date

echo "SAH NCBI data import completed at: $(date "+%Y.%m.%d-%H.%M")"
