package uk.ac.ebi.ena.annotation.helper.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends ElasticsearchRepository<Collection, String> {
    List<Collection> findAll();

    Optional<Collection> findByCollCode(String collCode);

    Optional<Collection> findByInstIdAndCollCode(int instId, String collCode);

    @Query("{ \"bool\": { \"must\": [ { \"match\": { \"inst_id\": ?0 } }, { \"multi_match\": " +
            "{ \"fields\": [ \"coll_code\", \"coll_name\" ], \"query\": \"?1\", \"fuzziness\": \"AUTO\" } } ] } }")
    List<Collection> findByInstIdAndCollNameFuzzy(int instId, String name);

    @Query("{ \"bool\": { \"must\": [ { \"terms\": { \"inst_id\": [?0] } }, { \"multi_match\": " +
            "{ \"fields\": [ \"coll_code\", \"coll_name\" ], \"query\": \"?1\", \"fuzziness\": \"AUTO\" } } ] } }")
    List<Collection> findByMultipleInstIdsAndCollNameFuzzy(List<Integer> instIdList, String name);

    @Query("{\"multi_match\": {\"fields\": [\"coll_code\", \"coll_name\"], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}")
    List<Collection> findByCollNameFuzzy(String name);

}
