package uk.ac.ebi.ena.annotation.helper.service.datafetcher;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ena.annotation.helper.model.Collection;
import uk.ac.ebi.ena.annotation.helper.repository.CollectionRepository;

import java.util.Optional;

@Component
public class CollectionDataFetcher implements DataFetcher<Collection> {

    @Autowired
    CollectionRepository collectionRepository;

    @Override
    public Collection get(DataFetchingEnvironment dataFetchingEnvironment) {
        String collCode = dataFetchingEnvironment.getArgument("collCode");
        Optional<Collection> optionalCollection = collectionRepository.findByCollCode(collCode);
        if (optionalCollection.isPresent()) {
            return optionalCollection.get();
        } else {
            return null;
        }
    }

}
