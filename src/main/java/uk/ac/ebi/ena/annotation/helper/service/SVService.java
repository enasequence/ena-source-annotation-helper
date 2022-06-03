package uk.ac.ebi.ena.annotation.helper.service;

import uk.ac.ebi.ena.annotation.helper.dto.ResponseDto;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;

import java.util.List;

public interface SVService {

    public ResponseDto validateSV(String specimenVoucher);

    public ResponseDto constructSV(String instCode, String collCode, String specimenId);

    Institute save(Institute institute);

    List<Institute> findByInstName(String instName);
}