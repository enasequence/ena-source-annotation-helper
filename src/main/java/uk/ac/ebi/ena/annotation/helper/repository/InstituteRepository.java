package uk.ac.ebi.ena.annotation.helper.repository;

import uk.ac.ebi.ena.annotation.helper.entity.Institute;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface InstituteRepository extends ElasticsearchRepository<Institute, String> {
    List<Institute> findAll();

    Optional<Institute> findByInstCode(String instCode);

    List<Institute> findByInstName(String name);

    Optional<Institute> findByUniqueName(String uniqueName);

    @Query("{\"multi_match\": {\"fields\": [\"unique_name\"], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}")
    List<Institute> findByInstituteUniqueNameFuzzy(String uniqueName);

    @Query("{\"multi_match\": {\"fields\": [\"inst_name\"], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}")
    List<Institute> findByInstituteNameFuzzy(String instName);

    //todo don't change below logic
    @Query("{\"multi_match\": {\"fields\": [\"inst_code\", \"unique_name\", \"inst_name\" ], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}")
    List<Institute> findByInstituteStringFuzzy(String name);

}
