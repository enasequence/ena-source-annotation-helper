package uk.ac.ebi.ena.annotation.helper.service;

import uk.ac.ebi.ena.annotation.helper.dto.ResponseDto;
import uk.ac.ebi.ena.annotation.helper.dto.SAHResponseDto;

public interface SVService {

    public ResponseDto findByInstituteStringFuzzy(String name, String qualifierType);

    public ResponseDto findByInstUniqueNameAndCollCode(String instUniqueName, String collCode, String qualifierType);

    public ResponseDto findCollectionsByInstUniqueName(String instUniqueName, String qualifierType);

    public SAHResponseDto validateSV(String specimenVoucher, String qualifierType);

    public SAHResponseDto constructSV(String instCode, String collCode, String specimenId, String qualifierType);
    
}