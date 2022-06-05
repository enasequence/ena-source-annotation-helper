package uk.ac.ebi.ena.annotation.helper.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.dto.SVSearchResult;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;
import uk.ac.ebi.ena.annotation.helper.repository.CollectionRepository;
import uk.ac.ebi.ena.annotation.helper.utils.SVConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SVCollectionServiceHelper {

    //todo >> >> Exact search on Collection Code
    //todo >> Similar Search on Collection Name

    @Autowired
    private CollectionRepository collectionRepository;


    public SVSearchResult validateMultipleInstIdsAndCollName(SVSearchResult svSearchResult, String collectionString) {
        //step-1 - Exact search on Collection Code
        List<Collection> listCollection = collectionRepository
                .findByMultipleInstIdsAndCollNameFuzzy(
                        (List<Integer>) svSearchResult.getInstitutes().stream().map(x -> x.getInstId()).collect(Collectors.toList())
                        , collectionString);
        if (listCollection.isEmpty() && listCollection.size() >= 1) {
            svSearchResult.setCollections(listCollection);
            svSearchResult.setMatch(listCollection.size() == 1 ? SVConstants.MULTI_NEAR_MATCH : SVConstants.MULTI_NEAR_MATCH);
            svSearchResult.setSuccess(true);
            return svSearchResult;
        }
        return SVSearchResult.builder()
                .match(SVConstants.NO_MATCH)
                .success(false)
                .build();
    }

    public SVSearchResult validateCollection(int instId, String collectionString) {
        //step-1 - Exact search on Collection Code
        SVSearchResult svSearchResult = isValidCollectionCode(instId, collectionString);
        if (svSearchResult.isSuccess()) {
            return svSearchResult;
        }
        //step-2 - Similar Search on Collection Name
        return searchPossibleCollectionsByName(instId, collectionString);
    }

    private SVSearchResult isValidCollectionCode(int instId, String collectionString) {
        //todo >> Exact search on Collection Code
        Optional<Collection> optionalCollection = collectionRepository.findByInstIdAndCollCode(instId, collectionString);
        if (optionalCollection.isPresent()) {
            Collection inst = optionalCollection.get();
            List collList = new ArrayList<Institute>();
            collList.add(inst);
            return SVSearchResult.builder()
                    .collections(collList)
                    .match(SVConstants.EXACT_MATCH)
                    .success(true)
                    .build();
        }
        return SVSearchResult.builder()
                .match(SVConstants.NO_MATCH)
                .success(false)
                .build();
    }

    private SVSearchResult searchPossibleCollectionsByName(int instId, String collectionString) {
        //todo >> Similar Search on Collection Name
        List<Collection> listCollection = collectionRepository.findByInstIdAndCollNameFuzzy(instId, collectionString);
        if (listCollection.isEmpty() && listCollection.size() >= 1) {
            return SVSearchResult.builder()
                    .collections(listCollection)
                    .match(listCollection.size() == 1 ? SVConstants.MULTI_NEAR_MATCH : SVConstants.MULTI_NEAR_MATCH)
                    .success(true)
                    .build();
        }
        return SVSearchResult.builder()
                .match(SVConstants.NO_MATCH)
                .success(false)
                .build();
    }

}
