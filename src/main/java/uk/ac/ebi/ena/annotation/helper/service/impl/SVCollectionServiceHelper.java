package uk.ac.ebi.ena.annotation.helper.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.dto.SVSearchResult;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.repository.CollectionRepository;
import uk.ac.ebi.ena.annotation.helper.utils.SVConstants;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SVCollectionServiceHelper {

    @Autowired
    private CollectionRepository collectionRepository;


    public SVSearchResult validateMultipleInstIdsAndCollName(SVSearchResult svSearchResult, String collectionString) {
        log.debug("Validating the collection '{}' for given institute(s)", collectionString);
        //step-1 - Exact search on Collection Code
        int[] listInstituteIds = svSearchResult.getInstitutes().stream().map(x -> x.getInstId()).mapToInt(i->i).toArray();
        List<Collection> listCollection = collectionRepository
                .findByMultipleInstIdsAndCollNameFuzzy(
                        listInstituteIds
                        , collectionString);
        if (!listCollection.isEmpty()) {
            log.debug("found matching {} collections", listCollection.size());
            if(listCollection.size() == 1) {
                log.debug("found collection exact match");
                svSearchResult.setCollections(listCollection);
                svSearchResult.setMatch(SVConstants.EXACT_MATCH);
                svSearchResult.setSuccess(true);
                return svSearchResult;
            } else {
                svSearchResult.setCollections(listCollection);
                svSearchResult.setMatch(listCollection.size() == 1 ? SVConstants.MULTI_NEAR_MATCH : SVConstants.MULTI_NEAR_MATCH);
                svSearchResult.setSuccess(true);
                return svSearchResult;
            }
        }
        log.debug("no matching collection found");
        return SVSearchResult.builder()
                .match(SVConstants.NO_MATCH)
                .success(false)
                .build();
    }

//    public SVSearchResult validateCollection(int instId, String collectionString) {
//        //step-1 - Exact search on Collection Code
//        SVSearchResult svSearchResult = isValidCollectionCode(instId, collectionString);
//        if (svSearchResult.isSuccess()) {
//            return svSearchResult;
//        }
//        //step-2 - Similar Search on Collection Name
//        return searchPossibleCollectionsByName(instId, collectionString);
//    }
//
//    private SVSearchResult isValidCollectionCode(int instId, String collectionString) {
//        //todo >> Exact search on Collection Code
//        Optional<Collection> optionalCollection = collectionRepository.findByInstIdAndCollCode(instId, collectionString);
//        if (optionalCollection.isPresent()) {
//            Collection inst = optionalCollection.get();
//            List collList = new ArrayList<Institute>();
//            collList.add(inst);
//            return SVSearchResult.builder()
//                    .collections(collList)
//                    .match(SVConstants.EXACT_MATCH)
//                    .success(true)
//                    .build();
//        }
//        return SVSearchResult.builder()
//                .match(SVConstants.NO_MATCH)
//                .success(false)
//                .build();
//    }
//
//    private SVSearchResult searchPossibleCollectionsByName(int instId, String collectionString) {
//        //todo >> Similar Search on Collection Name
//        List<Collection> listCollection = collectionRepository.findByInstIdAndCollNameFuzzy(instId, collectionString);
//        if (listCollection.isEmpty() && listCollection.size() >= 1) {
//            return SVSearchResult.builder()
//                    .collections(listCollection)
//                    .match(listCollection.size() == 1 ? SVConstants.MULTI_NEAR_MATCH : SVConstants.MULTI_NEAR_MATCH)
//                    .success(true)
//                    .build();
//        }
//        return SVSearchResult.builder()
//                .match(SVConstants.NO_MATCH)
//                .success(false)
//                .build();
//    }

}
