package uk.ac.ebi.ena.annotation.helper.service.impl;

import uk.ac.ebi.ena.annotation.helper.dto.ResponseDto;
import uk.ac.ebi.ena.annotation.helper.model.Institute;
import uk.ac.ebi.ena.annotation.helper.repository.InstVanillaRepository;
import uk.ac.ebi.ena.annotation.helper.service.SVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SVServiceImpl implements SVService {

    @Autowired
    private InstVanillaRepository instituteRepository;


    @Override
    public ResponseDto validate(String specimenVoucher) {
        return null;
    }

    @Override
    public ResponseDto construct(String instCode, String collCode, String specimenId) {
        return null;
    }

    @Override
    public Institute save(Institute institute) {
        return instituteRepository.save(institute);
    }

    @Override
    public List<Institute> findByInstName(String instName) {
        return instituteRepository.findByInstName(instName);
    }
}