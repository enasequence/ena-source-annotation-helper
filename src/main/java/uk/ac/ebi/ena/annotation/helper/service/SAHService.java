package uk.ac.ebi.ena.annotation.helper.service;

import uk.ac.ebi.ena.annotation.helper.dto.ResponseDto;
import uk.ac.ebi.ena.annotation.helper.dto.SAHResponseDto;

public interface SAHService {

    public ResponseDto findByInstituteStringFuzzyWithQTArray(String name, String[] qualifierType);

    public ResponseDto findCollectionsByInstUniqueName(String instUniqueName, String[] qualifierType);

    public ResponseDto findByInstUniqueNameAndCollCode(String instUniqueName, String collCode, String[] qualifierType);

    public SAHResponseDto validate(String qualifierValue, String[] qualifierType);

    public SAHResponseDto construct(String instCode, String collCode, String identifier, String[] qualifierType);

    public ResponseDto getErrorCodes();

}