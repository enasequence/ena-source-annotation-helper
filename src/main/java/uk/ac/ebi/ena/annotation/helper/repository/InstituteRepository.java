package uk.ac.ebi.ena.annotation.helper.repository;

import uk.ac.ebi.ena.annotation.helper.model.Institute;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface InstituteRepository extends ElasticsearchRepository<Institute, String> {
    List<Institute> findAll();

    Institute findByInstCode(String instCode);

    @Query("{\"multi_match\": {\"fields\": [\"inst_code\", \"unique_name\", \"inst_name\" ], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}")
    List<Institute> findByInstituteNameFuzzy(String name);
}
