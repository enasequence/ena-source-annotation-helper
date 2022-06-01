package uk.ac.ebi.ena.annotation.helper.service.datafetcher;

import uk.ac.ebi.ena.annotation.helper.model.Institute;
import uk.ac.ebi.ena.annotation.helper.repository.InstituteRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class InstitutesDataFetcher implements DataFetcher<List<Institute>>{

    @Autowired
    InstituteRepository instituteRepository;

    @Override
    public List<Institute> get(DataFetchingEnvironment dataFetchingEnvironment) {

        String query = dataFetchingEnvironment.getArgument("query");

        if(isEmpty(query)) {
            return instituteRepository.findAll();
        } else {
            return instituteRepository.findByInstituteStringFuzzy(query);
        }
    }
}
