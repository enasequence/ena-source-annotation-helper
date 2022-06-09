package uk.ac.ebi.ena.annotation.helper.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends ElasticsearchRepository<Collection, String> {
    List<Collection> findAll();

    List<Collection> findByCollCode(String collCode);

    Optional<Collection> findByInstIdAndCollCode(int instId, String collCode);

    List<Collection> findByInstId(int instId);

    @Query("{\"bool\": {\"must\": [{ \"match\": { \"inst_id\": \"?0\" } },{\"terms\": {\"qualifier_type\": ?1 }}]}}")
    List<Collection> findByInstIdAndQualifierTypeArray(int instId, List<String> qualifierTypeArrray);

    @Query("{ \"bool\": { \"must\": [ { \"match\": { \"inst_id\": ?0 } }, { \"multi_match\": " +
            "{ \"fields\": [ \"coll_code\", \"coll_name\" ], \"query\": \"?1\", \"fuzziness\": \"AUTO\" } } ] } }")
    List<Collection> findByInstIdAndCollNameFuzzy(int instId, String name);

    @Query("{ \"bool\": { \"must\": [ { \"match\": { \"inst_id\": ?0 } }, { \"terms\": { \"qualifier_type\": ?2 } }, { \"multi_match\": " +
            "{ \"fields\": [ \"coll_code\", \"coll_name\" ], \"query\": \"?1\", \"fuzziness\": \"AUTO\" } } ] } }")
    List<Collection> findByInstIdAndCollNameFuzzyWithQualifierType(int instId, String name, List<String> qualifierType);


    @Query("{ \"bool\": { \"must\": [ { \"terms\": { \"inst_id\": [?0] } }, { \"multi_match\": " +
            "{ \"fields\": [ \"coll_code\", \"coll_name\" ], \"query\": \"?1\", \"fuzziness\": \"AUTO\" } } ] } }")
    List<Collection> findByMultipleInstIdsAndCollNameFuzzy(int[] instIdList, String name);

    @Query("{\"multi_match\": {\"fields\": [\"coll_code\", \"coll_name\"], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}")
    List<Collection> findByCollNameFuzzy(String name);

    @Query("{ \"bool\": { \"must\": [ { \"terms\": { \"inst_id\": [?0] } }, { \"match\": { \"qualifier_type\": ?2 } }, { \"multi_match\": " +
            "{ \"fields\": [ \"coll_code\", \"coll_name\" ], \"query\": \"?1\", \"fuzziness\": \"AUTO\" } } ] } }")
    List<Collection> findByMultipleInstIdsAndCollNameFuzzyAndQualifierType(int[] listInstituteIds, String collectionString, String qualifierType);
}
