package uk.ac.ebi.ena.annotation.helper.ncbi.sync.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.entity.Institution;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import static uk.ac.ebi.ena.annotation.helper.ncbi.sync.utils.SAHDataSyncConstants.NEW_COLL_INDEX_NAME;
import static uk.ac.ebi.ena.annotation.helper.ncbi.sync.utils.SAHDataSyncConstants.NEW_INST_INDEX_NAME;

@Repository
@Slf4j
public class InstitutionRepositoryImpl implements InstitutionRepository {

    private final ElasticsearchClient restHighLevelClient;

    public InstitutionRepositoryImpl(ElasticsearchClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public void createInstitutionIndex() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream instutionMapping = classLoader.getResourceAsStream("institution.json");
        CreateIndexRequest request =
                CreateIndexRequest.of(builder -> builder.index(NEW_INST_INDEX_NAME).withJson(instutionMapping));
        boolean created = restHighLevelClient.indices().create(request).acknowledged();
        log.debug("Institution Index {} Created: ", NEW_INST_INDEX_NAME);
    }

    @Override
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
