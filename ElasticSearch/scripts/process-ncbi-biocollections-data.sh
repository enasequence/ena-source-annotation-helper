#!/bin/sh

# set the current time format
current_date=$(date "+%Y.%m.%d")
echo "Processing started at: $current_date"

NCBI_FTP_SERVER="https://ftp.ncbi.nih.gov"
NCBI_INSTITUTION_FILE_PATH="pub/taxonomy/biocollections/Institution_codes.txt"
NCBI_COLLECTION_FILE_PATH="pub/taxonomy/biocollections/Collection_codes.txt"
ftp_inst_file=./ftp/Institution_codes.ftp.$current_date.txt
ftp_coll_file=./ftp/Collection_codes.ftp.$current_date.txt
inst_input_file=input/Institution_codes.input.$current_date.txt
coll_input_file=input/Collection_codes.input.$current_date.txt
inst_output_file=output/Institutions_ElasticSearchData.$current_date.json
coll_output_file=output/Collections_ElasticSearchData.$current_date.json

# download the files from the NCBI FTP server
download_files() {
    mkdir -p ftp
    # download the institutions data file
    wget $NCBI_FTP_SERVER/$NCBI_INSTITUTION_FILE_PATH -P ./ftp -O $ftp_inst_file

    # download the collections data file
    wget $NCBI_FTP_SERVER/$NCBI_COLLECTION_FILE_PATH -P ./ftp -O $ftp_coll_file
}

# process the inputs files, including making a copy to process the files
process_input_data_files() {
    # once process established, this step won't be requried and can be merged
    # Make a copy of ftp files to process and generate the ElasticSearch loader files
    mkdir -p input
    cp $ftp_inst_file $inst_input_file
    cp $ftp_coll_file $coll_input_file

    # Clean the provided files before transformation
    EXP_TO_SEARCH="[[:space:]]*\|[[:space:]]*"
    REPLACE_WITH="|"
    sed -i -e "s/${EXP_TO_SEARCH}/${REPLACE_WITH}/g" $inst_input_file
    sed -i -e "s/${EXP_TO_SEARCH}/${REPLACE_WITH}/g" $coll_input_file

    # Clean up the double quotes in the texts
    sed -i -e "s/\"//g" $inst_input_file
    sed -i -e "s/\"//g" $coll_input_file

    # Clean up the tab chars
    sed -i -e "s/\t/ /g" $inst_input_file
    sed -i -e "s/\t/ /g" $coll_input_file

    # Clean up the backslash chars and replace them with forward slashes
    sed -i -e "s/\\/\//g" $inst_input_file
    sed -i -e "s/\\/\//g" $coll_input_file
}

# Generate the ElasticSearch bulk upload json file for institutions
transform_inst_data_file() {
    IFS='|'
    [ ! -f $inst_input_file ] && {
        echo "$inst_input_file file not found"
        exit 99
    }
    while read inst_id inst_code aka inst_name country address phone fax record_source home_url url_rule comments coll_type qualifier_type unique_name synonyms; do
        if [[ $inst_id =~ ^[0-9]+$ ]]; then
            echo '{"index":{}}' >>$inst_output_file
            JSON_STRING='{"inst_id": '$inst_id', "inst_code": "'$inst_code'", "unique_name": "'$unique_name'", "synonyms": "'$synonyms'", "inst_name": "'$inst_name'", "country": "'$country'", "address": "'$address'", "coll_type": "'$coll_type'", "qualifier_type": "'$qualifier_type'", "home_url": "'$home_url'", "url_rule": "'$url_rule'"}'
            echo "$JSON_STRING" >>$inst_output_file
        else
            echo "inst_id is not a valid integer."
        fi
    done <$inst_input_file
}

# Generate the ElasticSearch bulk upload json file for collections
transform_collection_data_file() {
    IFS='|'
    [ ! -f $coll_input_file ] && {
        echo "$coll_input_file file not found"
        exit 99
    }
    while read coll_id inst_id coll_name coll_code coll_size coll_type coll_status coll_url coll_url_rule comments qualifier_type; do
        if [[ $coll_id =~ ^[0-9]+$ ]]; then
            echo '{"index":{}}' >>$coll_output_file
            JSON_STRING='{"coll_id": "'$coll_id'", "inst_id": "'$inst_id'", "coll_code": "'$coll_code'", "coll_name": "'$coll_name'", "coll_type": "'$coll_type'", "qualifier_type": "'$qualifier_type'", "coll_url": "'$coll_url'", "coll_url_rule": "'$coll_url_rule'"}'
            echo "$JSON_STRING" >>$coll_output_file
        else
            echo "coll_id is not a valid integer."
        fi
    done <$coll_input_file
}

# Transform the data file to ElasticSearch bulk upload json files
transform_data_files() {
    # set the output directory for generated ElasticSearch bulk upload file
    mkdir -p output
    touch $inst_output_file
    touch $coll_output_file

    transform_inst_data_file
    transform_collection_data_file

}

# download the files from the NCBI FTP server
download_files

# process the inputs files, including making a copy to process the files
process_input_data_files

# Transform the data file to ElasticSearch bulk upload json files
transform_data_files
