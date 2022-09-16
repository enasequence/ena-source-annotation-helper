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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.dto.ValidationSearchResult;
import uk.ac.ebi.ena.annotation.helper.entity.Institution;
import uk.ac.ebi.ena.annotation.helper.repository.InstitutionRepository;
import uk.ac.ebi.ena.annotation.helper.utils.SAHConstants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.exception.SAHErrorCode.MultipleMatchesFoundMessage;

@Service
@Slf4j
public class SAHInstituteServiceHelper {

    @Autowired
    private InstitutionRepository institutionRepository;

    @Value("${query.results.limit}")
    private int QUERY_RESULTS_LIMIT;

    //@Value("${ena.annotation.helper.suggestions.limit}")
    //private int SUGGESTIONS_LIMIT;

    public ValidationSearchResult validateInstitute(String instituteString, String[] qualifierType) {
        log.debug("Validating the institute -- " + instituteString);
        //step-1 - Exact Search on InstUniqueName
        ValidationSearchResult ValidationSearchResult = isValidInstituteUniqueName(instituteString, qualifierType);
        if (ValidationSearchResult.isSuccess()) {
            return ValidationSearchResult;
        }
        //step-2 - check for Similar Search on provided string
        ValidationSearchResult = searchSimilarInstitutesByUniqueName(instituteString, qualifierType);
        if (ValidationSearchResult.isSuccess()) {
            return ValidationSearchResult;
        }
        //step-3 - Similar Search with more fuzziness on InstName
        return searchSimilarInstitutesByName(instituteString, qualifierType);

    }

    /**
     * isValidInstituteUniqueName - exact match.
     *
     * @param instCode
     * @param qualifierType
     * @return
     */
    private ValidationSearchResult isValidInstituteUniqueName(String instCode, String[] qualifierType) {
        //Exact Search on InstUniqueName
        Optional<Institution> optionalInstitute;
        if (isEmpty(qualifierType)) {
            optionalInstitute = institutionRepository.findByUniqueName(instCode);
        } else {
            List<String> listQT = Arrays.asList(qualifierType);
            optionalInstitute = institutionRepository.findByUniqueNameAndQualifierTypeArray(instCode, listQT);
        }
        if (optionalInstitute.isPresent()) {
            log.debug("found exact match -- " + instCode);
            return ValidationSearchResult.builder()
                    .institutions(Collections.singletonList(optionalInstitute.get()))
                    .match(SAHConstants.EXACT_MATCH)
                    .success(true)
                    .build();
        }
        return ValidationSearchResult.builder()
                .match(SAHConstants.NO_MATCH)
                .success(false)
                .build();
    }

    private ValidationSearchResult searchSimilarInstitutesByUniqueName(String instUniqueName, String[] qualifierType) {
        //Similar Search on InstUniqueName
        List<Institution> listInstitution;
        if (isEmpty(qualifierType)) {
            listInstitution = institutionRepository.findByInstituteUniqueNameFuzzy(instUniqueName, PageRequest.of(0, QUERY_RESULTS_LIMIT,
                    Sort.by("_score").descending()));
        } else {
            List<String> listQT = Arrays.asList(qualifierType);
            listInstitution = institutionRepository.findByInstituteUniqueNameFuzzyAndQualifierType(instUniqueName, listQT, PageRequest.of(0, QUERY_RESULTS_LIMIT,
                    Sort.by("_score").descending()));
        }
        return getQVSearchResult(listInstitution);
    }

    private ValidationSearchResult searchSimilarInstitutesByName(String instName, String[] qualifierType) {
        //Similar Search with more fuzziness on InstName
        List<Institution> listInstitution;
        if (isEmpty(qualifierType)) {
            listInstitution = institutionRepository.findByInstituteNameFuzzy(instName, PageRequest.of(0, QUERY_RESULTS_LIMIT,
                    Sort.by("_score").descending()));
        } else {
            List<String> listQT = Arrays.asList(qualifierType);
            listInstitution = institutionRepository.findByInstituteNameFuzzyAndQualifierType(instName, listQT, PageRequest.of(0, QUERY_RESULTS_LIMIT,
                    Sort.by("_score").descending()));
        }
        return getQVSearchResult(listInstitution);
    }

    private ValidationSearchResult getQVSearchResult(List<Institution> listInstitution) {
        if (!listInstitution.isEmpty() && listInstitution.size() >= 1) {
            //todo verify -- later to restrict if query fails because of search data load
//            if(listInstitute.size() > SUGGESTIONS_LIMIT){
//                log.debug("found similar {} institutes, beyond configured limit", listInstitute.size());
//                return ValidationSearchResult.builder()
//                        .institutes(listInstitute)
//                        .match(TOO_MANY_MATCH)
//                        .success(true)
//                        .build();
//            }
            log.debug("found similar {} institutes", listInstitution.size());
            return ValidationSearchResult.builder()
                    .institutions(listInstitution)
                    .match(SAHConstants.MULTI_NEAR_MATCH)
                    .message(listInstitution.size() > 1 ? MultipleMatchesFoundMessage : null)
                    .success(true)
                    .build();

        }
        log.debug("No match found for the given inputs");
        return ValidationSearchResult.builder()
                .match(SAHConstants.NO_MATCH)
                .success(false)
                .build();
    }

}
