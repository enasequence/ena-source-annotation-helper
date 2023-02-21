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
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.indices.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ena.sah.biocollections.importer.entity.Institution;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import static uk.ac.ebi.ena.sah.biocollections.importer.utils.AppConstants.*;
import static uk.ac.ebi.ena.sah.biocollections.importer.utils.BioCollectionsServiceUtils.calculatePercentChanged;

@Repository
@Slf4j
public class BioCollectionsRepository {

    private final ElasticsearchClient restHighLevelClient;

    public BioCollectionsRepository(ElasticsearchClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    public boolean createIndex(String mappingJSONFile, String newIndexName) throws IOException {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream idxMapping = classLoader.getResourceAsStream(mappingJSONFile);
            CreateIndexRequest request =
                    CreateIndexRequest.of(builder -> builder.index(newIndexName).withJson(idxMapping));
            boolean created = restHighLevelClient.indices().create(request).acknowledged();
            log.info("New Index {} Created: ", newIndexName);
            return true;
        } catch (IOException e) {
            log.error("Failed to create new index {}", newIndexName);
        }
        return false;
    }

    public boolean moveInstitutionIndexAlias(String indexAlias, String newIndexName, String indexPrefix) {
        try {
            GetAliasRequest getRequest = new GetAliasRequest.Builder().name(indexAlias).build();
            GetAliasResponse aliasResponse = null;
            aliasResponse = restHighLevelClient.indices().getAlias(getRequest);
            Optional<String> optionalIdxName = aliasResponse.result().keySet().stream().findFirst();
            String indexName;
            if (optionalIdxName.isPresent()) {
                indexName = optionalIdxName.get();
            } else {
                return false;
            }
            // TODO check for records count variation here
            final var countOldIdxRequest = new CountRequest.Builder().index(indexName).build();
            final var oldCount = restHighLevelClient.count(countOldIdxRequest).count();
            log.info("Existing Index {} record count was: {}", indexName, oldCount);
            final var countNewIdxRequest = new CountRequest.Builder().index(newIndexName).build();
            final var newCount = restHighLevelClient.count(countNewIdxRequest).count();
            log.info("New Index {} record count is: {}", newIndexName, newCount);
            var percentageChanged = calculatePercentChanged(newCount,oldCount );
            if(percentageChanged != 0 && percentageChanged < 95) {
                log.error("New Index {} records count is reduced by more than {}%", newIndexName, percentageChanged);
                log.error("Alias not moved to the new Index {}", newIndexName);
                return false;
            }
            // remove alias from old index
            DeleteAliasRequest delRequest = new DeleteAliasRequest.Builder().index(indexName).name(indexAlias).build();
            DeleteAliasResponse delAliasResponse = restHighLevelClient.indices().deleteAlias(delRequest);
            log.info("Old Index '{}' alias removed", indexName);
            // add alias to new index
            PutAliasRequest putRequest = new PutAliasRequest.Builder().index(newIndexName).name(indexAlias).build();
            PutAliasResponse putAliasResponse = restHighLevelClient.indices().putAlias(putRequest);
            log.info("Alias added to the new Index '{}'", indexName);
            //clean up older indexes
            boolean removalCompleted = cleanupOlderIndexes(indexAlias, indexPrefix);
            if (removalCompleted) {
                log.info("Cleanup completed for indexes with prefix '{}'", indexPrefix);
            }
            return true;
        } catch (IOException e) {
            log.error("Alias not moved to the new Index {}", newIndexName);
        }
        return false;
    }

    public boolean cleanupOlderIndexes(String indexAlias, String indexPrefix) {
        try {
            GetIndexRequest request = GetIndexRequest.of(gr -> gr.index(indexPrefix + "*"));
            GetIndexResponse response = restHighLevelClient.indices().get(request);
            final Map<String, IndexState> result = response.result();
            Map<String, IndexState> keySortedMap = new TreeMap<String, IndexState>(result);
            String[] array = keySortedMap.keySet().toArray(new String[0]);
            for (int i = 0; i < array.length - 2; i++) {
                String removeIdx = array[i];
                // below condition should never be reached, or revise the logic
                if (result.get(removeIdx).aliases().containsKey(indexAlias)) {
                    log.info("Verify code flow. Tried to clear active index -->  " + removeIdx);
                    break;
                }
                DeleteIndexRequest dRequest = DeleteIndexRequest.of(dr -> dr.index(removeIdx));
                final DeleteIndexResponse delete = restHighLevelClient.indices().delete(dRequest);
                if (delete.acknowledged()) {
                    log.info("Deleted index -->  " + removeIdx);
                }
            }
            return true;
        } catch (IOException e) {
            log.warn("Failed to cleanup older indexes for {} Indexes ", indexPrefix);
        }
        return false;
    }


}
