package uk.ac.ebi.ena.annotation.helper.service.datafetcher;

import uk.ac.ebi.ena.annotation.helper.model.Collection;
import uk.ac.ebi.ena.annotation.helper.repository.SVCollectionRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class CollectionsDataFetcher implements DataFetcher<List<Collection>> {

    @Autowired
    SVCollectionRepository svCollectionRepository;

    @Override
    public List<Collection> get(DataFetchingEnvironment dataFetchingEnvironment) {

        String query = dataFetchingEnvironment.getArgument("query");

        if (isEmpty(query)) {
            return svCollectionRepository.findAll();
        } else {
            return svCollectionRepository.findByCollNameFuzzy(query);
        }
    }
}
