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

package uk.ac.ebi.ena.annotation.helper.service.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.dto.ValidationSearchResult;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.repository.CollectionRepository;
import uk.ac.ebi.ena.annotation.helper.utils.SAHConstants;

import java.util.Arrays;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.exception.SAHErrorCode.MultipleMatchesFoundMessage;

@Service
@Slf4j
public class SAHCollectionServiceHelper {

    @Autowired
    private CollectionRepository collectionRepository;


    public ValidationSearchResult validateMultipleInstIdsAndCollName(ValidationSearchResult validationSearchResult,
                                                                     String collectionString, String[] qualifierType) {
        log.debug("Validating the collection '{}' for given institute(s)", collectionString);
        // reset the earlier message since collection code is also provided
        validationSearchResult.setMessage(null);

        //step-1 - Exact search on Collection Code
        int[] listInstituteIds = validationSearchResult.getInstitutions().stream()
                .map(x -> x.getInstId()).mapToInt(i -> i).toArray();

        List<Collection> listCollection;
        if (isEmpty(qualifierType)) {
            listCollection = collectionRepository.findByMultipleInstIdsAndCollNameFuzzy(
                    listInstituteIds, collectionString);
        } else {
            List<String> listQT = Arrays.asList(qualifierType);
            listCollection = collectionRepository.findByMultipleInstIdsAndCollNameFuzzyAndQualifierType(
                    listInstituteIds, collectionString, listQT);
        }

        if (!listCollection.isEmpty()) {
            log.debug("found matching {} collections", listCollection.size());
            if (listCollection.size() == 1) {
                log.debug("found collection exact match");
                validationSearchResult.setCollections(listCollection);
                validationSearchResult.setMatch(SAHConstants.EXACT_MATCH);
                validationSearchResult.setSuccess(true);
                return validationSearchResult;
            } else {
                validationSearchResult.setCollections(listCollection);
                validationSearchResult.setMatch(SAHConstants.MULTI_NEAR_MATCH);
                validationSearchResult.setMessage(MultipleMatchesFoundMessage);
                validationSearchResult.setSuccess(true);
                return validationSearchResult;
            }
        }
        log.info("No matching collection found for inputs -- {}", validationSearchResult.getInputParams());
        validationSearchResult.setMatch(SAHConstants.NO_MATCH);
        validationSearchResult.setSuccess(false);
        return validationSearchResult;
    }

}