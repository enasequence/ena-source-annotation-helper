package uk.ac.ebi.ena.annotation.helper.ncbi.sync.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collection;

import static uk.ac.ebi.ena.annotation.helper.ncbi.sync.utils.SAHDataSyncConstants.NEW_COLL_INDEX_NAME;

@Repository
@Slf4j
public class CollectionRepositoryImpl implements CollectionRepository {

    private final ElasticsearchClient restHighLevelClient;

    public CollectionRepositoryImpl(ElasticsearchClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
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
            log.error("Bulk had errors");
            for (BulkResponseItem item : result.items()) {
                if (item.error() != null) {
                    log.error(item.error().reason());
                }
            }
        }
        return result;
    }

}
