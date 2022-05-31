package uk.ac.ebi.ena.annotation.helper.service.datafetcher;

import uk.ac.ebi.ena.annotation.helper.model.Collection;
import uk.ac.ebi.ena.annotation.helper.repository.SVCollectionRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CollectionDataFetcher implements DataFetcher<Collection>{

    @Autowired
    SVCollectionRepository svCollectionRepository;

    @Override
    public Collection get(DataFetchingEnvironment dataFetchingEnvironment) {

        String collCode = dataFetchingEnvironment.getArgument("collCode");

        return svCollectionRepository.findByCollCode(collCode);
    }
}
