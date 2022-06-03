package uk.ac.ebi.ena.annotation.helper.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.dto.CollectionResponse;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.repository.CollectionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SVCollectionServiceHelper {

    //todo >> >> Exact search on Collection Code
    //todo >> Similar Search on Collection Name

    @Autowired
    private CollectionRepository collectionRepository;

    private CollectionResponse validateCollection(int instId, String collectionString) {
        //todo - step-1
        isValidCollectionCode(instId, collectionString);
        //todo - step-2
        searchPossibleCollectionsByName(instId, collectionString);

        return null;
    }

    private boolean isValidCollectionCode(int instId, String collectionString) {
        //todo >> Exact Search on InstCode
        Optional<Collection> optionalCollection = collectionRepository.findByInstIdAndCollCode(instId, collectionString);
        if (optionalCollection.isPresent()) {
            return true;
        }
        return false;
    }

    private boolean searchPossibleCollectionsByName(int instId, String collectionString) {
        //todo >> Similar Search with more fuzziness on InstName
        List<Collection> listCollection = collectionRepository.findByInstIdAndCollNameFuzzy(instId, collectionString);
        if (listCollection.isEmpty()) {
            //todo create object to set
        }
        return false;
    }


}
