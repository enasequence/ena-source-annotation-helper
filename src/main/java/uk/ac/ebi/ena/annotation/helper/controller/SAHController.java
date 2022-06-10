package uk.ac.ebi.ena.annotation.helper.controller;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.ena.annotation.helper.dto.Data;
import uk.ac.ebi.ena.annotation.helper.dto.QualifierValuesAllowed;
import uk.ac.ebi.ena.annotation.helper.dto.ResponseDto;
import uk.ac.ebi.ena.annotation.helper.dto.SAHResponseDto;
import uk.ac.ebi.ena.annotation.helper.service.GraphQLService;
import uk.ac.ebi.ena.annotation.helper.service.SAHService;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import static java.util.Objects.isNull;
import static uk.ac.ebi.ena.annotation.helper.exception.SAHErrorCode.*;

@RestController
@Slf4j
@Api(tags = "ENA Source Annotations Helper APIs")
@RequestMapping("/ena/sah/")
@Validated
public class SAHController {

    @Autowired
    GraphQLService graphQLService;

    @Autowired
    SAHService SAHService;

    @GetMapping("/institute/{ivalue}")
    @ApiOperation(value = "Fetch similar institute matches by either institute name or institute code.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried the institute(s) information."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> findByInstituteValue(@ApiParam(name = "ivalue", type = "String",
            value = ValidInputSizeMessage) @PathVariable @Size(min = 3, max = 20, message = InstituteNotValidInputMessage) String ivalue,
                                                       @ApiParam(name = "qualifier_type", type = "String[]",
                                                               value = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                                                               example = "specimen_voucher", required = false)
                                                       @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                                       @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        ResponseDto responseDto = SAHService.findByInstituteStringFuzzyWithQTArray(ivalue, qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/institute/{ivalue}/collection")
    @ApiOperation(value = "Get all collections by institute unique name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried the information."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> findByAllCollByInstituteUniqueName(@ApiParam(name = "ivalue", type = "String",
            value = ValidInstituteUniqueNameRequiredMessage) @PathVariable @Size(min = 3, max = 20, message = InstituteNotValidInputMessage) String ivalue,
                                                                     @ApiParam(name = "qualifier_type", type = "String[]",
                                                                             value = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                                                                             example = "specimen_voucher", required = false)
                                                                     @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                                                     @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        ResponseDto responseDto = SAHService.findCollectionsByInstUniqueName(ivalue, qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @GetMapping("/institute/{ivalue}/collection/{cvalue}")
    @ApiOperation(value = "Get collection by institute name and collection code.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried the information."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> findByInstUniqueNameAndCollCode(@ApiParam(name = "ivalue", type = "String",
            value = ValidInstituteUniqueNameRequiredMessage) @PathVariable @Size(min = 3, max = 20, message = InstituteNotValidInputMessage) String ivalue,
                                                                  @ApiParam(name = "cvalue", type = "String", value = ValidInputSizeMessage)
                                                                  @PathVariable @Size(min = 3, max = 20, message = CollectionNotValidInputMessage) String cvalue,
                                                                  @ApiParam(name = "qualifier_type", type = "String[]",
                                                                          value = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                                                                          example = "specimen_voucher", required = false)
                                                                  @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                                                  @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        ResponseDto responseDto = SAHService.findByInstUniqueNameAndCollCode(ivalue, cvalue, qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @GetMapping("/validate")
    @ApiOperation(value = "Validate the provided Qualifier String")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Validated the provided Specimen Voucher."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> validate(@ApiParam(name = "value", type = "String",
            value = ProvideQualifierString) @RequestParam("value") String value,
                                           @ApiParam(name = "qualifier_type", type = "String[]",
                                                   value = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                                                   example = "specimen_voucher", required = false)
                                           @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                           @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        SAHResponseDto responseDto = SAHService.validate(value, qualifierType);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/construct")
    @ApiOperation(value = "Construct the Qualifier String")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Constructed the Specimen Voucher String."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> construct(@ApiParam(name = "institute", type = "String",
            value = ProvideValidInstituteUniqueName) @RequestParam(name = "institute") String institute,
                                            @ApiParam(name = "collection", type = "String",
                                                    value = ProvideValidCollectionCode)
                                            @RequestParam(name = "collection", required = false) String collection,
                                            @ApiParam(name = "id", type = "String",
                                                    value = ProvideValidId)
                                            @RequestParam(name = "id") String id,
                                            @ApiParam(name = "qualifier_type", type = "String[]",
                                                    value = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                                                    example = "specimen_voucher", required = false)
                                            @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                            @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        SAHResponseDto responseDto = SAHService.construct(institute, collection, id, qualifierType);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    //-------------

    //todo -- commented graphql for now; will look at this later for future extensibility
//    @PostMapping("/graphql")
//    @ApiOperation(value = "GraphQL interface to fetch the Institutes and the Collections")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully fetched the required information."),
//            @ApiResponse(code = 400, message = "Invalid request format")
//    })
    @Deprecated
    private ResponseEntity<Object> fetchInstCollMeta(@RequestBody Data query) {
        ExecutionResult execute;
        if (isNull(query.getVariables())) {
            execute = graphQLService.getGraphQL().execute(query.getQuery());
        } else {
            ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                    .query(query.getQuery())
                    .variables(query.getVariables())
                    .build();
            execute = graphQLService.getGraphQL().execute(executionInput);
        }
        return new ResponseEntity<>(execute, HttpStatus.OK);
    }
}
