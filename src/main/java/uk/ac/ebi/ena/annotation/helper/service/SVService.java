package uk.ac.ebi.ena.annotation.helper.service;

import uk.ac.ebi.ena.annotation.helper.dto.ResponseDto;
import uk.ac.ebi.ena.annotation.helper.dto.SVResponseDto;

public interface SVService {

    public SVResponseDto validateSV(String specimenVoucher);

    public SVResponseDto constructSV(String instCode, String collCode, String specimenId);

    public ResponseDto findByInstName(String instName);

    public ResponseDto findByInstituteStringFuzzy(String name);

    public ResponseDto findByInstCode(String instCode);

    public ResponseDto findByUniqueName(String uniqueName);

    public ResponseDto findByInstIdAndCollCode(int instId, String collCode);

    public ResponseDto findByCollCode(String collCode);

    public ResponseDto findByCollNameFuzzy(String name);

}