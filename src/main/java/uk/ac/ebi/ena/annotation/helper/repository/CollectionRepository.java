package uk.ac.ebi.ena.annotation.helper.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import uk.ac.ebi.ena.annotation.helper.model.Collection;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends ElasticsearchRepository<Collection, String> {
    List<Collection> findAll();

    Optional<Collection> findByCollCode(String collCode);

    @Query("{\"multi_match\": {\"fields\": [\"coll_code\", \"coll_name\"], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}")
    List<Collection> findByCollNameFuzzy(String name);

}
