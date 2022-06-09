package uk.ac.ebi.ena.annotation.helper.repository;

import org.springframework.data.repository.query.Param;
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

    Optional<Institute> findByUniqueNameAndQualifierType(String instCode, String qualifierType);

    @Query("{\"multi_match\": {\"fields\": [\"inst_code\", \"unique_name\"], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}")
    List<Institute> findByInstituteUniqueNameFuzzy(String uniqueName);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"qualifier_type\": \"?1\"}}, " +
            "{\"multi_match\": {\"fields\": [\"inst_code\", \"unique_name\" ], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}]}}")
    List<Institute> findByInstituteUniqueNameFuzzyAndQualifierType(String instUniqueName, String qualifierType);

    @Query("{\"multi_match\": {\"fields\": [\"inst_name\"], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}")
    List<Institute> findByInstituteNameFuzzy(String instName);

    @Query("{\"multi_match\": {\"fields\": [\"inst_code\", \"unique_name\", \"inst_name\" ], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}")
    List<Institute> findByInstituteFuzzy(String name);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"qualifier_type\": \"?1\"}}, " +
            "{\"multi_match\": {\"fields\": [\"inst_code\", \"unique_name\", \"inst_name\" ], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}]}}")
    List<Institute> findByInstituteFuzzyAndQualifierType(String name, String qualifierType, int limit);

    @Query("{\"bool\": {\"must\": [{\"terms\": {\"qualifier_type\": ?1 }}, " +
            "{\"multi_match\": {\"fields\": [\"inst_code\", \"unique_name\", \"inst_name\" ], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}]}}")
    List<Institute> findByInstituteFuzzyAndQualifierTypeArray(String name, List<String> qualifierTypeArrray, int limit);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"qualifier_type\": \"?1\"}}, " +
            "{\"multi_match\": {\"fields\": [\"inst_name\" ], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}]}}")
    List<Institute> findByInstituteNameFuzzyAndQualifierType(String instName, String qualifierType);
}
