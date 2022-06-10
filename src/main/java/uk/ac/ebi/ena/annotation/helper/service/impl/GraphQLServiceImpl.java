package uk.ac.ebi.ena.annotation.helper.service.impl;

import uk.ac.ebi.ena.annotation.helper.repository.InstituteRepository;
import uk.ac.ebi.ena.annotation.helper.service.datafetcher.CollectionDataFetcher;
import uk.ac.ebi.ena.annotation.helper.service.datafetcher.CollectionsDataFetcher;
import uk.ac.ebi.ena.annotation.helper.service.datafetcher.InstituteDataFetcher;
import uk.ac.ebi.ena.annotation.helper.service.datafetcher.InstitutesDataFetcher;
import uk.ac.ebi.ena.annotation.helper.service.GraphQLService;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class GraphQLServiceImpl implements GraphQLService {

//    @Autowired
//    InstituteRepository instituteRepository;
//
//    @Autowired
//    private static ElasticsearchOperations esTemplate;
//
//    @Value("${classpath.svSchema}")
//    Resource resource;
//
//    private GraphQL graphQL;
//    @Autowired
//    private InstitutesDataFetcher institutesDataFetcher;
//    @Autowired
//    private InstituteDataFetcher instituteDataFetcher;
//    @Autowired
//    private CollectionsDataFetcher collectionsDataFetcher;
//    @Autowired
//    private CollectionDataFetcher collectionDataFetcher;
//
//    // load schema at application start up
//    @PostConstruct
//    private void loadSchema() throws IOException {
//
//        // get the schema
//        File schemaFile = resource.getFile();
//        // parse schema
//        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
//        RuntimeWiring wiring = buildRuntimeWiring();
//        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
//        graphQL = GraphQL.newGraphQL(schema).build();
//    }
//
//    private RuntimeWiring buildRuntimeWiring() {
//        return RuntimeWiring.newRuntimeWiring()
//                .type("Query", typeWiring -> typeWiring
//                        .dataFetcher("institutes", institutesDataFetcher)
//                        .dataFetcher("institute", instituteDataFetcher)
//                        .dataFetcher("collections", collectionsDataFetcher)
//                        .dataFetcher("svCollection", collectionDataFetcher))
//                .build();
//    }
//
//    public GraphQL getGraphQL() {
//        return graphQL;
//    }
//
//    public String getGraphQLSchema () {
//        return new SchemaPrinter(
//                // Tweak the options accordingly
//                SchemaPrinter.Options.defaultOptions()
//                        .includeSchemaDefinition(true)
//                        .includeDirectives(false)
//                        .includeScalarTypes(false)
//                        .includeIntrospectionTypes(false)
//        ).print(graphQL.getGraphQLSchema());
//    }

}
