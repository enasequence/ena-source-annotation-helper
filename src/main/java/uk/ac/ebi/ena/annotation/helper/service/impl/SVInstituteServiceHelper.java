package uk.ac.ebi.ena.annotation.helper.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.dto.SVSearchResult;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;
import uk.ac.ebi.ena.annotation.helper.repository.InstituteRepository;
import uk.ac.ebi.ena.annotation.helper.utils.SVConstants;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static uk.ac.ebi.ena.annotation.helper.utils.SVConstants.TOO_MANY_MATCH;

@Service
@Slf4j
public class SVInstituteServiceHelper {

    @Autowired
    private InstituteRepository instituteRepository;

    @Value("${ena.annotation.helper.suggestions.limit}")
    private int SUGGESTIONS_LIMIT;

    public SVSearchResult validateInstitute(String instituteString) {
        log.debug("Validating the institute -- " + instituteString);
        //step-1 - Exact Search on InstUniqueName
        SVSearchResult SVSearchResult = isValidInstituteUniqueName(instituteString);
        if (SVSearchResult.isSuccess()) {
            return SVSearchResult;
        }
        //step-2 - check for Similar Search on provided string
        SVSearchResult = searchSimilarInstitutesByUniqueName(instituteString);
        if (SVSearchResult.isSuccess()) {
            return SVSearchResult;
        }
        //step-3 - Similar Search with more fuzziness on InstName
        return searchSimilarInstitutesByName(instituteString);

    }

    /**
     * isValidInstituteUniqueName - exact match.
     *
     * @param instCode
     * @return
     */
    private SVSearchResult isValidInstituteUniqueName(String instCode) {
        //todo >> Exact Search on InstUniqueName
        Optional<Institute> optionalInstitute = instituteRepository.findByUniqueName(instCode);
        if (optionalInstitute.isPresent()) {
            log.debug("found exact match -- " + instCode);
            return SVSearchResult.builder()
                    .institutes(Collections.singletonList(optionalInstitute.get()))
                    .match(SVConstants.EXACT_MATCH)
                    .success(true)
                    .build();
        }
        return SVSearchResult.builder()
                .match(SVConstants.NO_MATCH)
                .success(false)
                .build();
    }

    private SVSearchResult searchSimilarInstitutesByUniqueName(String instUniqueName) {
        //Similar Search on InstUniqueName
        List<Institute> listInstitute = instituteRepository.findByInstituteUniqueNameFuzzy(instUniqueName);
        return getSvSearchResult(listInstitute);
    }

    private SVSearchResult searchSimilarInstitutesByName(String instName) {
        //Similar Search with more fuzziness on InstName
        List<Institute> listInstitute = instituteRepository.findByInstituteNameFuzzy(instName);
        return getSvSearchResult(listInstitute);
    }

    private SVSearchResult getSvSearchResult(List<Institute> listInstitute) {
        if (!listInstitute.isEmpty() && listInstitute.size() >= 1) {
            if (listInstitute.size() <= SUGGESTIONS_LIMIT) {
                log.debug("found similar {} institutes", listInstitute.size());
                return SVSearchResult.builder()
                        .institutes(listInstitute)
                        .match(listInstitute.size() == 1 ? SVConstants.EXACT_MATCH : SVConstants.MULTI_NEAR_MATCH)
                        .success(true)
                        .build();
            } else {
                log.debug("found similar {} institutes, beyond configured limit", listInstitute.size());
                return SVSearchResult.builder()
                        .institutes(listInstitute)
                        .match(TOO_MANY_MATCH)
                        .success(true)
                        .build();
            }
        }
        return SVSearchResult.builder()
                .match(SVConstants.NO_MATCH)
                .success(false)
                .build();
    }

}
