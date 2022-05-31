package uk.ac.ebi.ena.annotation.helper.repository;

import uk.ac.ebi.ena.annotation.helper.model.Institute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface InstVanillaRepository extends ElasticsearchRepository<Institute, String> {

    Institute findByInstCode(String code);

    List<Institute> findByInstName(String name);

    @Query("{\"multi_match\": {\"fields\": [\"inst_code\", \"unique_name\", \"inst_name\" ], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}")
    Page<Institute> findByInstituteNameFuzzyCustomQuery(String name, Pageable pageable);
}
