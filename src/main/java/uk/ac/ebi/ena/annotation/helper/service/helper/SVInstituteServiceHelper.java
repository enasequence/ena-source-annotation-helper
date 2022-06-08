package uk.ac.ebi.ena.annotation.helper.service.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.dto.ValidationSearchResult;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;
import uk.ac.ebi.ena.annotation.helper.repository.InstituteRepository;
import uk.ac.ebi.ena.annotation.helper.utils.SVConstants;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.exception.SVErrorCode.MultipleMatchesFoundMessage;
import static uk.ac.ebi.ena.annotation.helper.utils.SVConstants.TOO_MANY_MATCH;

@Service
@Slf4j
public class SVInstituteServiceHelper {

    @Autowired
    private InstituteRepository instituteRepository;

    @Value("${ena.annotation.helper.suggestions.limit}")
    private int SUGGESTIONS_LIMIT;

    public ValidationSearchResult validateInstitute(String instituteString, String qualifierType) {
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
    private ValidationSearchResult isValidInstituteUniqueName(String instCode, String qualifierType) {
        //Exact Search on InstUniqueName
        Optional<Institute> optionalInstitute;
        if (isEmpty(qualifierType)) {
            optionalInstitute = instituteRepository.findByUniqueName(instCode);
        } else {
            optionalInstitute = instituteRepository.findByUniqueNameAndQualifierType(instCode, qualifierType);
        }
        if (optionalInstitute.isPresent()) {
            log.debug("found exact match -- " + instCode);
            return ValidationSearchResult.builder()
                    .institutes(Collections.singletonList(optionalInstitute.get()))
                    .match(SVConstants.EXACT_MATCH)
                    .success(true)
                    .build();
        }
        return ValidationSearchResult.builder()
                .match(SVConstants.NO_MATCH)
                .success(false)
                .build();
    }

    private ValidationSearchResult searchSimilarInstitutesByUniqueName(String instUniqueName, String qualifierType) {
        //Similar Search on InstUniqueName
        List<Institute> listInstitute;
        if (isEmpty(qualifierType)) {
            listInstitute = instituteRepository.findByInstituteUniqueNameFuzzy(instUniqueName);
        } else {
            listInstitute = instituteRepository.findByInstituteUniqueNameFuzzyAndQualifierType(instUniqueName, qualifierType);
        }
        return getSvSearchResult(listInstitute);
    }

    private ValidationSearchResult searchSimilarInstitutesByName(String instName, String qualifierType) {
        //Similar Search with more fuzziness on InstName
        List<Institute> listInstitute;
        if (isEmpty(qualifierType)) {
            listInstitute = instituteRepository.findByInstituteNameFuzzy(instName);
        } else {
            listInstitute = instituteRepository.findByInstituteNameFuzzyAndQualifierType(instName, qualifierType);
        }
        return getSvSearchResult(listInstitute);
    }

    private ValidationSearchResult getSvSearchResult(List<Institute> listInstitute) {
        if (!listInstitute.isEmpty() && listInstitute.size() >= 1) {
            if (listInstitute.size() <= SUGGESTIONS_LIMIT) {
                log.debug("found similar {} institutes", listInstitute.size());
                return ValidationSearchResult.builder()
                        .institutes(listInstitute)
                        .match(listInstitute.size() == 1 ? SVConstants.EXACT_MATCH : SVConstants.MULTI_NEAR_MATCH)
                        .message(listInstitute.size() > 1 ? MultipleMatchesFoundMessage : null)
                        .success(true)
                        .build();
            } else {
                log.debug("found similar {} institutes, beyond configured limit", listInstitute.size());
                return ValidationSearchResult.builder()
                        .institutes(listInstitute)
                        .match(TOO_MANY_MATCH)
                        .success(true)
                        .build();
            }
        }
        log.debug("No match found for the given inputs");
        return ValidationSearchResult.builder()
                .match(SVConstants.NO_MATCH)
                .success(false)
                .build();
    }

}
