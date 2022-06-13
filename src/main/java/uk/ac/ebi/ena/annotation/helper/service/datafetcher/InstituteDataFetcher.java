package uk.ac.ebi.ena.annotation.helper.service.datafetcher;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;
import uk.ac.ebi.ena.annotation.helper.repository.InstituteRepository;

import java.util.Optional;

@Component
public class InstituteDataFetcher implements DataFetcher<Institute> {

    @Autowired
    InstituteRepository instituteRepository;

    @Override
    public Institute get(DataFetchingEnvironment dataFetchingEnvironment) {
        String instCode = dataFetchingEnvironment.getArgument("instUniqueName");
        Optional<Institute> optionalInstitute = instituteRepository.findByUniqueName(instCode);
        if (optionalInstitute.isPresent()) {
            return optionalInstitute.get();
        } else {
            return null;
        }
    }

}
