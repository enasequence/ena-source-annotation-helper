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
import uk.ac.ebi.ena.annotation.helper.dto.ResponseDto;
import uk.ac.ebi.ena.annotation.helper.model.Data;
import uk.ac.ebi.ena.annotation.helper.service.GraphQLService;
import uk.ac.ebi.ena.annotation.helper.service.SVService;

import static java.util.Objects.isNull;

@RestController
@Slf4j
@Api(tags = "Specimen Voucher APIs")
public class SVController {

    @Autowired
    GraphQLService graphQLService;

    @Autowired
    SVService SVService;

    @PostMapping("/graphql")
    @ApiOperation(value = "GraphQL interface to fetch the Institutes and the Collections")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched the required information."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> fetchInstCollMeta(@RequestBody Data query) {
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

    @GetMapping("/ena/sv/validate")
    @ApiOperation(value = "Validate the provided Specimen Voucher.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Validated the provided Specimen Voucher."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> validateSV(@PathVariable String specimenVoucher) {
        ResponseDto responseDto = SVService.validate(specimenVoucher);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/ena/sv/construct")
    @ApiOperation(value = "Construct the Specimen Voucher String.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Constructed the Specimen Voucher String."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> constructSV(@PathVariable String instCode, @PathVariable String collCode, @PathVariable String specimenId) {
        ResponseDto responseDto = SVService.construct(instCode, collCode, specimenId);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
