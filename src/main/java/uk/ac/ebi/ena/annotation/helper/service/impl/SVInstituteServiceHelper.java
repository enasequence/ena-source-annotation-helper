package uk.ac.ebi.ena.annotation.helper.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.dto.SVSearchResult;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;
import uk.ac.ebi.ena.annotation.helper.repository.InstituteRepository;
import uk.ac.ebi.ena.annotation.helper.utils.SVConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SVInstituteServiceHelper {

    @Autowired
    private InstituteRepository instituteRepository;

    public SVSearchResult validateInstitute(String instituteString) {
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
            Institute inst = optionalInstitute.get();
            List instList = new ArrayList<Institute>();
            instList.add(inst);
            return SVSearchResult.builder()
                    .institutes(instList)
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
        //todo >> Similar Search on InstUniqueName
        List<Institute> listInstitute = instituteRepository.findByInstituteUniqueNameFuzzy(instUniqueName);
        if (!listInstitute.isEmpty() && listInstitute.size() >= 1) {
            return SVSearchResult.builder()
                    .institutes(listInstitute)
                    .match(listInstitute.size() == 1 ? SVConstants.MULTI_NEAR_MATCH : SVConstants.MULTI_NEAR_MATCH)
                    .success(true)
                    .build();
        }
        return SVSearchResult.builder()
                .match(SVConstants.NO_MATCH)
                .success(false)
                .build();
    }

    private SVSearchResult searchSimilarInstitutesByName(String instName) {
        //todo >> Similar Search with more fuzziness on InstName
        List<Institute> listInstitute = instituteRepository.findByInstituteNameFuzzy(instName);
        if (!listInstitute.isEmpty() && listInstitute.size() >= 1) {
            return SVSearchResult.builder()
                    .institutes(listInstitute)
                    .match(listInstitute.size() == 1 ? SVConstants.EXACT_MATCH : SVConstants.MULTI_NEAR_MATCH)
                    .success(true)
                    .build();
        }
        return SVSearchResult.builder()
                .match(SVConstants.NO_MATCH)
                .success(false)
                .build();
    }

}
