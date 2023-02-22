/*
 * ******************************************************************************
 *  * Copyright 2021 EMBL-EBI, Hinxton outstation
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package uk.ac.ebi.ena.sah.biocollections.importer.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ena.sah.biocollections.importer.data.BioCollectionsDataObject;
import uk.ac.ebi.ena.sah.biocollections.importer.entity.Institution;

import java.io.IOException;
import java.util.Collection;

import static uk.ac.ebi.ena.sah.biocollections.importer.utils.AppConstants.*;

@Repository
@Slf4j
public class InstitutionRepository {

    private final BioCollectionsRepository bioCollectionsRepository;
    private final ElasticsearchClient restHighLevelClient;

    public InstitutionRepository(ElasticsearchClient restHighLevelClient, BioCollectionsRepository bioCollectionsRepository) {
        this.restHighLevelClient = restHighLevelClient;
        this.bioCollectionsRepository = bioCollectionsRepository;
    }

    public boolean persistInstitutionsData() throws IOException {
        // create a new index
        boolean createInstIdxSuccess =  bioCollectionsRepository.createIndex(INST_MAPPING_JSON, NEW_INST_INDEX_NAME);
        if (!createInstIdxSuccess) {
            log.error("Failed to create new Institution Index. exiting now...");
            return false;
        } else {
            log.info("Institution Index {} Created: ", NEW_INST_INDEX_NAME);
        }
        // populate the index with data fetched from FTP server
        BulkResponse resultInst = saveAll(BioCollectionsDataObject.mapInstitutions.values());
        log.info("Persisting data for Institutions - Finished");
        // refresh the index post loading
        bioCollectionsRepository.refreshIndex(NEW_INST_INDEX_NAME);
        // move the alias to new index
        boolean moveSuccessInst = bioCollectionsRepository.moveBioCollectionIndexAlias(INDEX_INSTITUTION_ALIAS, NEW_INST_INDEX_NAME, INST_INDEX_PREFIX, resultInst.items().size());
        if (!moveSuccessInst) {
            log.info("Couldn't move the alias to new index {} ", NEW_INST_INDEX_NAME);
            return false;
        } else {
            log.info("Alias moved to new Institution Index {} Successfully.", NEW_INST_INDEX_NAME);
        }
        return true;
    }


    public BulkResponse saveAll(Collection<Institution> institutions) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (Institution inst : institutions) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(NEW_INST_INDEX_NAME)
                            .document(inst)
                    )
            );
        }
        BulkResponse result = restHighLevelClient.bulk(br.build());
        // Log errors, if any
        if (result.errors()) {
            log.error("Encountered errors while loading Institutions.");
            for (BulkResponseItem item : result.items()) {
                if (item.error() != null) {
                    log.error(item.error().reason());
                }
            }
        }
        return result;
    }


}
