package uk.ac.ebi.ena.annotation.helper.service.datafetcher;

import uk.ac.ebi.ena.annotation.helper.model.Institute;
import uk.ac.ebi.ena.annotation.helper.repository.InstituteRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InstituteDataFetcher implements DataFetcher<Institute>{

    @Autowired
    InstituteRepository instituteRepository;

    @Override
    public Institute get(DataFetchingEnvironment dataFetchingEnvironment) {

        String instCode = dataFetchingEnvironment.getArgument("instCode");

        return instituteRepository.findByInstCode(instCode);
    }
}
