package uk.ac.ebi.ena.annotation.helper.service.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.dto.SVSearchResult;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.repository.CollectionRepository;
import uk.ac.ebi.ena.annotation.helper.utils.SVConstants;

import java.util.List;
import java.util.stream.Collectors;

import static uk.ac.ebi.ena.annotation.helper.exception.SVErrorCode.MultipleMatchesFoundMessage;

@Service
@Slf4j
public class SVCollectionServiceHelper {

    @Autowired
    private CollectionRepository collectionRepository;


    public SVSearchResult validateMultipleInstIdsAndCollName(SVSearchResult svSearchResult, String collectionString) {
        log.debug("Validating the collection '{}' for given institute(s)", collectionString);
        // reset the earlier message since collection code is also provided
        svSearchResult.setMessage(null);
        
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
                svSearchResult.setMatch(SVConstants.MULTI_NEAR_MATCH);
                svSearchResult.setMessage(MultipleMatchesFoundMessage);
                svSearchResult.setSuccess(true);
                return svSearchResult;
            }
        }
        log.info("No matching collection found for inputs -- {}", svSearchResult.getInputParams());
        svSearchResult.setMatch(SVConstants.NO_MATCH);
        svSearchResult.setSuccess(false);
        return svSearchResult;
    }

}
