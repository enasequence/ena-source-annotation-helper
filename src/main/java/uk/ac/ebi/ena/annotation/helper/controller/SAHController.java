package uk.ac.ebi.ena.annotation.helper.controller;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.ena.annotation.helper.dto.Data;
import uk.ac.ebi.ena.annotation.helper.dto.ResponseDto;
import uk.ac.ebi.ena.annotation.helper.dto.SAHResponseDto;
import uk.ac.ebi.ena.annotation.helper.service.GraphQLService;
import uk.ac.ebi.ena.annotation.helper.service.SVService;

import static java.util.Objects.isNull;

@RestController
@Slf4j
@Api(tags = "ENA Source Annotation Helper APIs")
@RequestMapping("/ena/sah/")
public class SAHController {

    @Autowired
    GraphQLService graphQLService;

    @Autowired
    SVService SVService;

    @GetMapping("/institute/{value}")
    @ApiOperation(value = "Fetch similar institute matches by either institute name or institute code.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried the institute(s) information."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> findByInstituteStringFuzzy(@PathVariable String value,
                                                             @RequestParam(name="qualifier_type", required=false) String qualifierType) {
        ResponseDto responseDto = SVService.findByInstituteStringFuzzy(value, qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/institute/{ivalue}/collection")
    @ApiOperation(value = "Get all collections by institute name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried the information."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> findByInstIdAndCollCode(@PathVariable String ivalue,
                                                          @RequestParam(name="qualifier_type", required=false) String qualifierType) {
        ResponseDto responseDto = SVService.findCollectionsByInstUniqueName(ivalue, qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
        //return new ResponseEntity<>("Not Implemented yet", HttpStatus.NOT_FOUND);
    }


    @GetMapping("/institute/{ivalue}/collection/{cvalue}")
    @ApiOperation(value = "Get collection by institute name and collection code.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried the information."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> findByInstIdAndCollCode(@PathVariable String ivalue,
                                                          @PathVariable String cvalue,
                                                          @RequestParam(name="qualifier_type", required=false) String qualifierType) {
        ResponseDto responseDto = SVService.findByInstUniqueNameAndCollCode(ivalue, cvalue, qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @GetMapping("/validate")
    @ApiOperation(value = "Validate the provided Specimen Voucher.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Validated the provided Specimen Voucher."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> validate(@RequestParam("value") String value,
                                           @RequestParam(name="qualifier_type", required=false) String qualifierType) {
        SAHResponseDto responseDto = SVService.validateSV(value, qualifierType);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/construct")
    @ApiOperation(value = "Construct the Specimen Voucher String.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Constructed the Specimen Voucher String."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> construct(@RequestParam(name="institute") String institute,
                                            @RequestParam(name="collection", required=false) String collection,
                                            @RequestParam(name="id") String id,
                                            @RequestParam(name="qualifier_type", required=false) String qualifierType) {
        SAHResponseDto responseDto = SVService.constructSV(institute, collection, id, qualifierType);

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
