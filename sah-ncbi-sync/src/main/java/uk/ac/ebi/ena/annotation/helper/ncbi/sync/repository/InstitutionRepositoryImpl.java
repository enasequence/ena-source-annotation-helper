package uk.ac.ebi.ena.annotation.helper.ncbi.sync.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.indices.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.index.AliasAction;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.entity.Institution;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;

import static uk.ac.ebi.ena.annotation.helper.ncbi.sync.utils.SAHDataSyncConstants.*;

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
        InputStream institutionMapping = classLoader.getResourceAsStream("institution.json");
        CreateIndexRequest request =
                CreateIndexRequest.of(builder -> builder.index(NEW_INST_INDEX_NAME).withJson(institutionMapping));
        boolean created = restHighLevelClient.indices().create(request).acknowledged();
        log.info("Institution Index {} Created: ", NEW_INST_INDEX_NAME);
    }

    @Override
    public boolean moveInstitutionIndexAlias() {
        try {
            GetAliasRequest getRequest = new GetAliasRequest.Builder().name(INDEX_INSTITUTION_ALIAS).build();
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
            DeleteAliasRequest delRequest = new DeleteAliasRequest.Builder().index(indexName).name(INDEX_INSTITUTION_ALIAS).build();
            DeleteAliasResponse delAliasResponse = restHighLevelClient.indices().deleteAlias(delRequest);
            log.info("Old Index '{}' alias removed", indexName);
            // add alias to new index
            PutAliasRequest putRequest = new PutAliasRequest.Builder().index(NEW_INST_INDEX_NAME).name(INDEX_INSTITUTION_ALIAS).build();
            PutAliasResponse putAliasResponse = restHighLevelClient.indices().putAlias(putRequest);
            log.info("Alias added to the new Index '{}'", indexName);
        } catch (IOException e) {
            log.error("Alias not moved to the new Index");
        }
        return true;
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
