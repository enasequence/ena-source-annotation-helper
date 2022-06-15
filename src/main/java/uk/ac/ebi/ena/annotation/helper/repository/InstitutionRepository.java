package uk.ac.ebi.ena.annotation.helper.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import uk.ac.ebi.ena.annotation.helper.entity.Institution;

import java.util.List;
import java.util.Optional;

public interface InstitutionRepository extends ElasticsearchRepository<Institution, String> {

    List<Institution> findAll();

    Optional<Institution> findByUniqueName(String uniqueName);

    @Query("{\"bool\": {\"must\": [{ \"match\": { \"unique_name\": \"?0\" } },{\"terms\": {\"qualifier_type\": ?1 }}]}}")
    Optional<Institution> findByUniqueNameAndQualifierTypeArray(String uniqueName, List<String> qualifierType);

    @Query("{\"multi_match\": {\"fields\": [\"inst_code\", \"unique_name\"], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}")
    List<Institution> findByInstituteUniqueNameFuzzy(String uniqueName);

    @Query("{\"bool\": {\"must\": [{\"terms\": {\"qualifier_type\": ?1}}, " +
            "{\"multi_match\": {\"fields\": [\"inst_code\", \"unique_name\" ], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}]}}")
    List<Institution> findByInstituteUniqueNameFuzzyAndQualifierType(String instUniqueName, List<String> qualifierType);

    @Query("{\"multi_match\": {\"fields\": [\"inst_name\"], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}")
    List<Institution> findByInstituteNameFuzzy(String instName);

    @Query("{\"multi_match\": {\"fields\": [\"inst_code\", \"unique_name\", \"inst_name\" ], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}")
    List<Institution> findByInstituteFuzzy(String name);

    @Query("{\"bool\": {\"must\": [{\"terms\": {\"qualifier_type\": ?1 }}, " +
            "{\"multi_match\": {\"fields\": [\"inst_code\", \"unique_name\", \"inst_name\" ], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}]}}")
    List<Institution> findByInstituteFuzzyAndQualifierTypeArray(String name, List<String> qualifierTypeArrray, int limit);

    @Query("{\"bool\": {\"must\": [{\"terms\": {\"qualifier_type\": ?1}}, " +
            "{\"multi_match\": {\"fields\": [\"inst_name\" ], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}]}}")
    List<Institution> findByInstituteNameFuzzyAndQualifierType(String instName, List<String> qualifierType);
}
