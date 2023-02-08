package uk.ac.ebi.ena.annotation.helper.ncbi.sync.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.indices.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;

import static uk.ac.ebi.ena.annotation.helper.ncbi.sync.utils.SAHDataSyncConstants.INDEX_COLLECTION_ALIAS;
import static uk.ac.ebi.ena.annotation.helper.ncbi.sync.utils.SAHDataSyncConstants.NEW_COLL_INDEX_NAME;

@Repository
@Slf4j
public class CollectionRepositoryImpl implements CollectionRepository {

    private final ElasticsearchClient restHighLevelClient;

    public CollectionRepositoryImpl(ElasticsearchClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public void createCollectionIndex() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream collectionMapping = classLoader.getResourceAsStream("collection.json");
        CreateIndexRequest request =
                CreateIndexRequest.of(builder -> builder.index(NEW_COLL_INDEX_NAME).withJson(collectionMapping));
        boolean created = restHighLevelClient.indices().create(request).acknowledged();
        log.debug("Collection Index {} Created: ", NEW_COLL_INDEX_NAME);
    }

    @Override
    public boolean moveCollectionIndexAlias() {
        try {
            GetAliasRequest getRequest = new GetAliasRequest.Builder().name(INDEX_COLLECTION_ALIAS).build();
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
            // remove alias from old index
            DeleteAliasRequest delRequest = new DeleteAliasRequest.Builder().index(indexName).name(INDEX_COLLECTION_ALIAS).build();
            DeleteAliasResponse delAliasResponse = restHighLevelClient.indices().deleteAlias(delRequest);
            log.info("Old Index '{}' alias removed", indexName);
            // add alias to new index
            PutAliasRequest putRequest = new PutAliasRequest.Builder().index(NEW_COLL_INDEX_NAME).name(INDEX_COLLECTION_ALIAS).build();
            PutAliasResponse putAliasResponse = restHighLevelClient.indices().putAlias(putRequest);
            log.info("Alias added to the new Index '{}'", indexName);
        } catch (IOException e) {
            log.error("Alias not moved to the new Index");
        }
        return true;
    }

    @Override
    public BulkResponse saveAll(Collection<uk.ac.ebi.ena.annotation.helper.ncbi.sync.entity.Collection> collections) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (uk.ac.ebi.ena.annotation.helper.ncbi.sync.entity.Collection coll : collections) {
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
