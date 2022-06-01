package uk.ac.ebi.ena.annotation.helper.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.model.Institute;
import uk.ac.ebi.ena.annotation.helper.repository.InstituteRepository;

import java.util.Optional;

@Service
public class SVInstituteServiceHelper {

    @Autowired
    private InstituteRepository instituteRepository;

    public boolean validateInstituteUniqueName(String instUniqueName) {
        Optional<Institute> optionalInstitute = instituteRepository.findByUniqueName(instUniqueName);
        if (optionalInstitute.isPresent()) {
            return true;
        }
        return false;
    }
}
