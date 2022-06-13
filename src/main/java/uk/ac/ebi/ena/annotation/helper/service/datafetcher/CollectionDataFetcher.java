package uk.ac.ebi.ena.annotation.helper.service.datafetcher;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;
import uk.ac.ebi.ena.annotation.helper.repository.CollectionRepository;
import uk.ac.ebi.ena.annotation.helper.repository.InstituteRepository;

import java.util.Optional;

@Component
public class CollectionDataFetcher implements DataFetcher<Collection> {

    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    InstituteRepository instituteRepository;

    @Override
    public Collection get(DataFetchingEnvironment dataFetchingEnvironment) {
        String collCode = dataFetchingEnvironment.getArgument("collCode");
        String instUniqueName = dataFetchingEnvironment.getArgument("instUniqueName");
        //find institute first
        Optional<Institute> optionalInstitute = instituteRepository.findByUniqueName(instUniqueName);
        if (optionalInstitute.isPresent()) {
            Optional<Collection> optionalCollection =
                    collectionRepository.findByInstIdAndCollCode(optionalInstitute.get().getInstId(), collCode);
            if (optionalCollection.isPresent()) {
                return optionalCollection.get();
            }
        }
        return null;

    }

}
