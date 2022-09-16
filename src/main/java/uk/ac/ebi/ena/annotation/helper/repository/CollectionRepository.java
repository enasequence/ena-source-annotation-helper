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

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends ElasticsearchRepository<Collection, String> {

    List<Collection> findAll();

    Optional<Collection> findByInstIdAndCollCode(int instId, String collCode);

    List<Collection> findByInstId(int instId);

    @Query("{\"multi_match\": {\"fields\": [\"coll_code\", \"coll_name\"], \"query\": \"?0\", \"fuzziness\": \"AUTO\" }}")
    List<Collection> findByCollNameFuzzy(String name);

    @Query("{\"bool\": {\"must\": [{ \"match\": { \"inst_id\": \"?0\" } },{\"terms\": {\"qualifier_type\": ?1 }}]}}")
    List<Collection> findByInstIdAndQualifierTypeArray(int instId, List<String> qualifierTypeArrray);

    @Query("{ \"bool\": { \"must\": [ { \"match\": { \"inst_id\": ?0 } }, { \"multi_match\": " +
            "{ \"fields\": [ \"coll_code\", \"coll_name\" ], \"query\": \"?1\", \"fuzziness\": \"AUTO\" } } ] } }")
    List<Collection> findByInstIdAndCollNameFuzzy(int instId, String name);

    @Query("{ \"bool\": { \"must\": [ { \"match\": { \"inst_id\": ?0 } }, { \"terms\": { \"qualifier_type\": ?2 } }, { \"multi_match\": " +
            "{ \"fields\": [ \"coll_code\", \"coll_name\" ], \"query\": \"?1\", \"fuzziness\": \"AUTO\" } } ] } }")
    List<Collection> findByInstIdAndCollNameFuzzyWithQualifierType(int instId, String name, List<String> qualifierType);

    // validate and construct scenarios - start

    @Query("{ \"bool\": { \"must\": [ { \"terms\": { \"inst_id\": [?0] } }, { \"multi_match\": " +
            "{ \"fields\": [ \"coll_code\"], \"query\": \"?1\" } } ] } }")
    List<Collection> findByMultipleInstIdsAndCollNameExact(int[] instIdList, String collCode);

    @Query("{ \"bool\": { \"must\": [ { \"terms\": { \"inst_id\": [?0] } }, { \"terms\": { \"qualifier_type\": ?2 } }, { \"multi_match\": " +
            "{ \"fields\": [ \"coll_code\" ], \"query\": \"?1\" } } ] } }")
    List<Collection> findByMultipleInstIdsAndCollNameExactAndQualifierType(int[] instIdList, String collCode, List<String> qualifierTypeArrray);

    @Query("{\"bool\": {\"must\": [{\"terms\": {\"inst_id\": [?0]}}," +
            "{\"query_string\": {\"query\": \"?1*\",\"fields\": [\"coll_code\"]}}]}}")
    List<Collection> findByMultipleInstIdsAndStartsWithCollName(int[] instIdList, String collCode);

    @Query("{\"bool\": {\"must\": [{\"terms\": {\"inst_id\": [?0]}},{\"terms\": {\"qualifier_type\": ?2 }}," +
            "{\"query_string\": {\"query\": \"?1*\",\"fields\": [\"coll_code\"]}}]}}")
    List<Collection> findByMultipleInstIdsAndStartsWithCollNameAndQualifierType(int[] instIdList, String collCode, List<String> qualifierTypeArrray);

    @Query("{ \"bool\": { \"must\": [ { \"terms\": { \"inst_id\": [?0] } }, { \"multi_match\": " +
            "{ \"fields\": [ \"coll_code\", \"coll_name\" ], \"query\": \"?1\", \"fuzziness\": \"AUTO\" } } ] } }")
    List<Collection> findByMultipleInstIdsAndCollNameFuzzy(int[] instIdList, String name);

    @Query("{ \"bool\": { \"must\": [ { \"terms\": { \"inst_id\": [?0] } }, { \"terms\": { \"qualifier_type\": ?2 } }, { \"multi_match\": " +
            "{ \"fields\": [ \"coll_code\", \"coll_name\" ], \"query\": \"?1\", \"fuzziness\": \"AUTO\" } } ] } }")
    List<Collection> findByMultipleInstIdsAndCollNameFuzzyAndQualifierType(int[] listInstituteIds, String collectionString, List<String> qualifierType);

    // validate and construct scenarios - end
}
