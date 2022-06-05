package uk.ac.ebi.ena.annotation.helper.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.dto.ResponseDto;
import uk.ac.ebi.ena.annotation.helper.dto.SVResponseDto;
import uk.ac.ebi.ena.annotation.helper.dto.SVSearchResult;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;
import uk.ac.ebi.ena.annotation.helper.mapper.SVResponseMapper;
import uk.ac.ebi.ena.annotation.helper.repository.CollectionRepository;
import uk.ac.ebi.ena.annotation.helper.repository.InstituteRepository;
import uk.ac.ebi.ena.annotation.helper.service.SVService;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
public class SVServiceImpl implements SVService {

    @Value("${ena.annotation.helper.suggestions.limit}")
    private int SUGGESTIONS_LIMIT;

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private SVInstituteServiceHelper svInstituteServiceHelper;

    @Autowired
    private SVCollectionServiceHelper svCollectionServiceHelper;


    @Override
    public SVResponseDto validateSV(String specimenVoucher) {
        String[] tokenizedSV = specimenVoucher.split(":");
        SVResponseMapper svResponseMapper = new SVResponseMapper();

        if (tokenizedSV.length < 2 || tokenizedSV.length > 3) {
            //todo error scenario
        }

        //[<Institution Unique Name>:]<specimen_id>
        SVSearchResult svSearchResult = svInstituteServiceHelper.validateInstitute(tokenizedSV[0]);
        if (svSearchResult.isSuccess()) {
            int responseSize = svSearchResult.getInstitutes().size();
            if (responseSize == 1 && tokenizedSV.length == 2) {
                //since collection code is not available, build the response object and return
                svSearchResult.setSpecimenId(tokenizedSV[1]);
                return svResponseMapper.mapResponseDto(svSearchResult);
            } else if (responseSize > 1 && responseSize <= SUGGESTIONS_LIMIT) {
                //todo set valid options list
            } else if (responseSize > SUGGESTIONS_LIMIT) {
                // todo can't validate and suggest
                //todo set and return too many hits.. please verify / be more accurate -- the institute unique name entered..
            }
        }

        //[<Institution Unique Name>:[<collection-code>:]]<specimen_id>
        if (tokenizedSV.length == 3) {
            //todo for each institute in list validate the provided collection
            svSearchResult.setCollectionAvailable(true);
            //have the institute id and unique name available for rest of the search and construct
//            List<Integer> instIdList = instList.stream().map(x -> x.getInstId())
//                    .collect(Collectors.toMap(x, y));

            Map mapInstIdUniqueName = svSearchResult.getInstitutes().stream()
                    .collect(Collectors.toMap(Institute::getInstId, Institute::getUniqueName));
            svSearchResult.setInstituteIdNameMap(mapInstIdUniqueName);

            svSearchResult = svCollectionServiceHelper
                    .validateMultipleInstIdsAndCollName(svSearchResult, tokenizedSV[1]);
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


    @Override
    public Institute save(Institute institute) {
        return instituteRepository.save(institute);
    }

    @Override
    public List<Institute> findByInstName(String instName) {
        return instituteRepository.findByInstName(instName);
    }
}