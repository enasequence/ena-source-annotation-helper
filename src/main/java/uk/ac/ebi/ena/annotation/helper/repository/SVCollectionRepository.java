package uk.ac.ebi.ena.annotation.helper.repository;

import uk.ac.ebi.ena.annotation.helper.model.Collection;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SVCollectionRepository extends ElasticsearchRepository<Collection, String> {
    List<Collection> findAll();

    Collection findByCollCode(String collCode);

    @Query("{\"multi_match\": {\"fields\": [\"coll_code\", \"coll_name\"], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}")
    List<Collection> findByCollNameFuzzy(String name);

}
