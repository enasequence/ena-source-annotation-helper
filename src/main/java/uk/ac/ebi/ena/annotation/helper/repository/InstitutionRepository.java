/*
 * ******************************************************************************
 *  * Copyright 2021 EMBL-EBI, Hinxton outstation
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package uk.ac.ebi.ena.annotation.helper.repository;

import org.springframework.data.domain.PageRequest;
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

    //TODO make changes here
    @Query("{\"multi_match\": {\"fields\": [\"inst_code\", \"unique_name\", \"inst_name\" ], \"query\": \"?0\"}}")
    List<Institution> findByInstituteFuzzy(String name, PageRequest pageable);

    //TODO make changes here
    @Query("{\"bool\": {\"must\": [{\"terms\": {\"qualifier_type\": ?1 }}, " +
            "{\"multi_match\": {\"fields\": [\"inst_code\", \"unique_name\", \"inst_name\" ], \"query\": \"?0\"}}]}}")
    List<Institution> findByInstituteFuzzyAndQualifierTypeArray(String name, List<String> qualifierTypeArrray, PageRequest pageable);

    @Query("{\"bool\": {\"must\": [{\"terms\": {\"qualifier_type\": ?1}}, " +
            "{\"multi_match\": {\"fields\": [\"inst_name\" ], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}]}}")
    List<Institution> findByInstituteNameFuzzyAndQualifierType(String instName, List<String> qualifierType);
}
