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
import uk.ac.ebi.ena.annotation.helper.dto.SVResponseDto;
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

    @GetMapping("/ena/sv/institute/code")
    @ApiOperation(value = "Get institute by institute code.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SSuccessfully queried the information."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> findByInstCode(@RequestParam("instCode") String instCode) {
        ResponseDto responseDto = SVService.findByInstCode(instCode);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/ena/sv/institute/uniqueName")
    @ApiOperation(value = "Get institute by institute unique name.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried the information."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> findByUniqueName(@RequestParam("uniqueName") String uniqueName) {
        ResponseDto responseDto = SVService.findByUniqueName(uniqueName);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/ena/sv/institute")
    @ApiOperation(value = "Get institute by institute name or unique name or code.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried the information."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> findByInstituteStringFuzzy(@RequestParam("instStr") String instStr) {
        ResponseDto responseDto = SVService.findByInstituteStringFuzzy(instStr);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/ena/sv/collection/code")
    @ApiOperation(value = "Get collection by collection code.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried the information."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> findByCollCode(@RequestParam("collCode") String collCode) {
        ResponseDto responseDto = SVService.findByCollCode(collCode);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/ena/sv/collection")
    @ApiOperation(value = "Get collection by collection name or code.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried the information."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> findByCollNameFuzzy(@RequestParam("collString") String collString) {
        ResponseDto responseDto = SVService.findByCollNameFuzzy(collString);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/ena/sv/collection/instcode")
    @ApiOperation(value = "Get collection by institute name and collection code.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried the information."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> findByInstIdAndCollCode(@RequestParam("instCode") String instCode,
                                                          @RequestParam("collCode") String collCode) {
        ResponseDto responseDto = SVService.findByInstUniqueNameAndCollCode(instCode, collCode);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/ena/sv/collection/instid")
//    @ApiOperation(value = "Get collection by institute name and collection code.")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully queried the information."),
//            @ApiResponse(code = 400, message = "Invalid request format")
//    })
    //todo verify -- keeping this internal
    public ResponseEntity<Object> findByInstIdAndCollCode(@RequestParam("instId") int instId,
                                                          @RequestParam("collCode") String collCode) {
        ResponseDto responseDto = SVService.findByInstIdAndCollCode(instId, collCode);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @GetMapping("/ena/sv/validate")
    @ApiOperation(value = "Validate the provided Specimen Voucher.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Validated the provided Specimen Voucher."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> validateSV(@RequestParam("specimenVoucher") String specimenVoucher) {
        SVResponseDto responseDto = SVService.validateSV(specimenVoucher);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/ena/sv/construct")
    @ApiOperation(value = "Construct the Specimen Voucher String.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Constructed the Specimen Voucher String."),
            @ApiResponse(code = 400, message = "Invalid request format")
    })
    public ResponseEntity<Object> constructSV(@RequestParam("instCode") String instCode,
                                              @RequestParam("collCode") String collCode,
                                              @RequestParam("specimenId") String specimenId) {
        SVResponseDto responseDto = SVService.constructSV(instCode, collCode, specimenId);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
