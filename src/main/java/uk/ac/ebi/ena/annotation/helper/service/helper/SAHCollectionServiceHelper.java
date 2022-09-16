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
import uk.ac.ebi.ena.annotation.helper.entity.Institution;
import uk.ac.ebi.ena.annotation.helper.repository.CollectionRepository;
import uk.ac.ebi.ena.annotation.helper.utils.SAHConstants;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

        int[] listInstituteIds = validationSearchResult.getInstitutions().stream()
                .map(x -> x.getInstId()).mapToInt(i -> i).toArray();

        List<Collection> listCollection;

        //step-1 - Exact Search on Collection Code
        ValidationSearchResult ValidationSearchResult = isValidCollectionCode(validationSearchResult, listInstituteIds, collectionString, qualifierType);
        if (ValidationSearchResult.isSuccess()) {
            return ValidationSearchResult;
        }

        //step-2 - check for starts with string
        ValidationSearchResult = searchStartsWithCollectionsByCode(validationSearchResult, listInstituteIds, collectionString, qualifierType);
        if (ValidationSearchResult.isSuccess()) {
            return ValidationSearchResult;
        }

        //step-2 - check for Similar Search on provided string
        ValidationSearchResult = searchSimilarCollectionsByCode(validationSearchResult, listInstituteIds, collectionString, qualifierType);
        if (ValidationSearchResult.isSuccess()) {
            return ValidationSearchResult;
        }

        log.info("No matching collection found for inputs -- {}", validationSearchResult.getInputParams());
        validationSearchResult.setMatch(SAHConstants.NO_MATCH);
        validationSearchResult.setSuccess(false);
        return validationSearchResult;
    }

    /**
     * isValidCollectionCode - exact match.
     *
     * @param listInstituteIds
     * @param collectionCode
     * @param qualifierType
     * @return
     */
    private ValidationSearchResult isValidCollectionCode(ValidationSearchResult validationSearchResult,
                                                         int[] listInstituteIds, String collectionCode, String[] qualifierType) {
        List<Collection> listCollection;
        //Exact Search on Collection Code
        Optional<Institution> optionalInstitute;
        if (isEmpty(qualifierType)) {
            listCollection = collectionRepository.findByMultipleInstIdsAndCollNameExact(listInstituteIds, collectionCode);
        } else {
            List<String> listQT = Arrays.asList(qualifierType);
            listCollection = collectionRepository.findByMultipleInstIdsAndCollNameExactAndQualifierType(listInstituteIds, collectionCode, listQT);
        }

        if (!listCollection.isEmpty()) {
            log.debug("found matching {} collections", listCollection.size());
            if (listCollection.size() == 1) {
                log.debug("found collection exact match");
                validationSearchResult.setCollections(listCollection);
                // no need to set the match level at this level for a collection code exact match
                //validationSearchResult.setMatch(SAHConstants.EXACT_MATCH);
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
        // no match found yet
        validationSearchResult.setSuccess(false);
        return validationSearchResult;
    }

    /**
     * searchStartsWithCollectionsByCode - search for similar collections which starts with .
     *
     * @param validationSearchResult
     * @param listInstituteIds
     * @param collectionCode
     * @param qualifierType
     * @return
     */
    private ValidationSearchResult searchStartsWithCollectionsByCode(ValidationSearchResult validationSearchResult,
                                                                     int[] listInstituteIds, String collectionCode, String[] qualifierType) {
        //Similar Search on InstUniqueName
        List<Collection> listCollection;
        if (isEmpty(qualifierType)) {
            listCollection = collectionRepository.findByMultipleInstIdsAndStartsWithCollName(
                    listInstituteIds, collectionCode);
        } else {
            List<String> listQT = Arrays.asList(qualifierType);
            listCollection = collectionRepository.findByMultipleInstIdsAndStartsWithCollNameAndQualifierType(
                    listInstituteIds, collectionCode, listQT);
        }

        if (!listCollection.isEmpty()) {
            log.debug("found similar {} collections", listCollection.size());
            if (listCollection.size() == 1) {
                log.debug("found a collection that starts with collection string");
                validationSearchResult.setCollections(listCollection);
                validationSearchResult.setMatch(SAHConstants.MULTI_NEAR_MATCH);
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
        // no match found yet
        validationSearchResult.setSuccess(false);
        return validationSearchResult;
    }

    /**
     * searchSimilarCollectionsByCode - search for similar collections.
     *
     * @param validationSearchResult
     * @param listInstituteIds
     * @param collectionCode
     * @param qualifierType
     * @return
     */
    private ValidationSearchResult searchSimilarCollectionsByCode(ValidationSearchResult validationSearchResult,
                                                                  int[] listInstituteIds, String collectionCode, String[] qualifierType) {
        //Similar Search on InstUniqueName
        List<Collection> listCollection;
        if (isEmpty(qualifierType)) {
            listCollection = collectionRepository.findByMultipleInstIdsAndCollNameFuzzy(
                    listInstituteIds, collectionCode);
        } else {
            List<String> listQT = Arrays.asList(qualifierType);
            listCollection = collectionRepository.findByMultipleInstIdsAndCollNameFuzzyAndQualifierType(
                    listInstituteIds, collectionCode, listQT);
        }

        if (!listCollection.isEmpty()) {
            log.debug("found similar {} collections", listCollection.size());
            if (listCollection.size() == 1) {
                log.debug("found a similar collection");
                validationSearchResult.setCollections(listCollection);
                validationSearchResult.setMatch(SAHConstants.MULTI_NEAR_MATCH);
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
        // no match found
        validationSearchResult.setSuccess(false);
        return validationSearchResult;
    }

}
