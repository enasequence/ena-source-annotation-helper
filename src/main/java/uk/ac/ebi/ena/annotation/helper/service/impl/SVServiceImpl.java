package uk.ac.ebi.ena.annotation.helper.service.impl;

import uk.ac.ebi.ena.annotation.helper.dto.ResponseDto;
import uk.ac.ebi.ena.annotation.helper.model.Collection;
import uk.ac.ebi.ena.annotation.helper.model.Institute;
import uk.ac.ebi.ena.annotation.helper.repository.CollectionRepository;
import uk.ac.ebi.ena.annotation.helper.repository.InstituteRepository;
import uk.ac.ebi.ena.annotation.helper.service.SVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SVServiceImpl implements SVService {

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private SVInstituteServiceHelper svInstituteServiceHelper;


    @Override
    public ResponseDto validateSV(String specimenVoucher) {
        String[] tokenizedSV = "specimenVoucher".split(":");
        if(tokenizedSV.length < 2) {
            //todo error scenario
        } else if(tokenizedSV.length == 2) {
            //todo [<Institution Unique Name>:]<specimen_id>
            svInstituteServiceHelper.validateInstituteUniqueName(tokenizedSV[0]);
        } else if(tokenizedSV.length == 3) {
            //todo [<Institution Unique Name>:[<collection-code>:]]<specimen_id>
            svInstituteServiceHelper.validateInstituteUniqueName(tokenizedSV[0]);
            //validateCollectionCode(tokenizedSV[1]);
        } else {
            //todo error no valid scenario
        }
        return null;
    }

    @Override
    public ResponseDto constructSV(String instUniqueName, String collCode, String specimenId) {

        StringJoiner sjSpecimenVoucher = new StringJoiner(":");

//        if(isEmpty(instUniqueName)) {
//            //todo error mandatory
//        } else {
//            //todo validate instCode
//            if(!svInstituteServiceHelper.validateInstituteUniqueName(instUniqueName)) {
//                //todo invalid instCdoe return
//            }
//            sjSpecimenVoucher.add(instUniqueName);
//        }
//
//
//
//        if(isEmpty(specimenId)) {
//            //todo error mandatory
//        } else if(isEmpty(collCode)) {
//            //todo [<Institution Unique Name>:]<specimen_id>
//            //validate the institution code -- specimen id can't be validated for now
//            if(svInstituteServiceHelper.validateInstituteUniqueName(instUniqueName)) {
//                constructString(instUniqueName, specimenId);
//            } else {
//                //todo error institution code not valid
//            }
//        }  else {
//            //todo [<Institution Unique Name>:[<collection-code>:]]<specimen_id>
//            //validate the institution and collection code -- specimen id can't be validated for now
//            if(svInstituteServiceHelper.validateInstituteUniqueName(instUniqueName) && validateCollectionCode(instUniqueName, collCode)) {
//                constructString(instUniqueName, collCode, specimenId);
//            } else {
//                //todo error institution code not valid
//            }
//        }
        return null;
    }



    private boolean validateCollectionCode(String instUniqueName, String collCode) {
//        Optional<Collection> optionalCollection = collectionRepository.findByCollCode(instUniqueName, collCode);
//        if (optionalCollection.isPresent()) {
//            return true;
//        }
        return false;
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