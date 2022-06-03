package uk.ac.ebi.ena.annotation.helper.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.dto.InstituteResponse;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;
import uk.ac.ebi.ena.annotation.helper.repository.InstituteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SVInstituteServiceHelper {

    @Autowired
    private InstituteRepository instituteRepository;

    public InstituteResponse validateInstitute(String instituteString) {
        //todo - step-1
        isValidInstituteCode(instituteString);
        //todo - step-2
        searchPossibleInstitutesByUniqueName(instituteString);
        //todo - step-3
        searchPossibleInstitutesByName(instituteString);

        return null;
    }


    /**
     * isValidInstituteName - exact match.
     *
     * @param instCode
     * @return
     */
    private boolean isValidInstituteCode(String instCode) {
        //todo >> Exact Search on InstCode
        Optional<Institute> optionalInstitute = instituteRepository.findByInstCode(instCode);
        if (optionalInstitute.isPresent()) {
            return true;
        }
        return false;
    }

    private boolean searchPossibleInstitutesByUniqueName(String instUniqueName) {
        //todo >> Similar Search on InstUniqueName
        List<Institute> listInstitute = instituteRepository.findByInstituteUniqueNameFuzzy(instUniqueName);
        if (listInstitute.isEmpty()) {
            //todo create object to set
        }
        return false;
    }

    private boolean searchPossibleInstitutesByName(String instName) {
        //todo >> Similar Search with more fuzziness on InstName
        List<Institute> listInstitute = instituteRepository.findByInstituteNameFuzzy(instName);
        if (listInstitute.isEmpty()) {
            //todo create object to set
        }
        return false;
    }

}
