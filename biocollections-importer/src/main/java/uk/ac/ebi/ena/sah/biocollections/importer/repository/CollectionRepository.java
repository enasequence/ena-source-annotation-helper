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

import java.io.IOException;
import java.util.Collection;

import static uk.ac.ebi.ena.sah.biocollections.importer.utils.AppConstants.*;

@Repository
@Slf4j
public class CollectionRepository {

    private final BioCollectionsRepository bioCollectionsRepository;
    private final ElasticsearchClient restHighLevelClient;

    public CollectionRepository(ElasticsearchClient restHighLevelClient, BioCollectionsRepository bioCollectionsRepository) {
        this.restHighLevelClient = restHighLevelClient;
        this.bioCollectionsRepository = bioCollectionsRepository;
    }

    public boolean createCollectionIndex() throws IOException {
        boolean success = bioCollectionsRepository.createIndex(COLL_MAPPING_JSON, NEW_COLL_INDEX_NAME);
        if (success) {
            log.info("Collection Index {} Created: ", NEW_COLL_INDEX_NAME);
            return true;
        }
        return false;
    }

    public boolean moveCollectionIndexAlias() {
        boolean success = bioCollectionsRepository.moveInstitutionIndexAlias(INDEX_COLLECTION_ALIAS, NEW_COLL_INDEX_NAME, COLL_INDEX_PREFIX);
        if (success) {
            log.info("Alias moved to new Index {} ", NEW_INST_INDEX_NAME);
            return true;
        }
        return false;
    }

    public BulkResponse saveAll(Collection<uk.ac.ebi.ena.sah.biocollections.importer.entity.Collection> collections) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (uk.ac.ebi.ena.sah.biocollections.importer.entity.Collection coll : collections) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(NEW_COLL_INDEX_NAME)
                            .document(coll)
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
