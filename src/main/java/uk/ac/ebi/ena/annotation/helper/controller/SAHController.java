package uk.ac.ebi.ena.annotation.helper.controller;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.ena.annotation.helper.dto.QualifierValuesAllowed;
import uk.ac.ebi.ena.annotation.helper.dto.ResponseDto;
import uk.ac.ebi.ena.annotation.helper.dto.SAHResponseDto;
import uk.ac.ebi.ena.annotation.helper.service.SAHService;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import static uk.ac.ebi.ena.annotation.helper.exception.SAHErrorCode.*;

@RestController
@Slf4j
@Api(tags = "ENA Source Annotations Helper APIs")
@RequestMapping({"/ena/sah/api/"})
@Validated
public class SAHController {

    @Autowired
    SAHService SAHService;

    @GetMapping("/institution/{ivalue}")
    @ApiOperation(value = "Get institution. In case no exact match is found, fetches similar institution matches by either institution name or institution code.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried the institution(s) information."),
            @ApiResponse(code = 400, message = "Invalid request format"),
            @ApiResponse(code = 404, message = "Record Not Found")
    })
    public ResponseEntity<Object> findByInstitutionValue(@ApiParam(name = "ivalue", type = "String",
            value = ValidInputSizeMessage) @PathVariable @Size(min = 3, max = 100, message = InstituteNotValidInputMessage) String ivalue,
                                                         @ApiParam(name = "qualifier_type", type = "String[]",
                                                                 value = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                                                                 example = "specimen_voucher", required = false)
                                                         @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                                         @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        ResponseDto responseDto = SAHService.findByInstituteStringFuzzyWithQTArray(ivalue, qualifierType);
        return new ResponseEntity<>(responseDto, responseDto.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/institution/{ivalue}/collection")
    @ApiOperation(value = "Get all collections by institution unique name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried the information."),
            @ApiResponse(code = 400, message = "Invalid request format"),
            @ApiResponse(code = 404, message = "Record Not Found")
    })
    public ResponseEntity<Object> findByAllCollByInstituteUniqueName(@ApiParam(name = "ivalue", type = "String",
            value = ValidInstituteUniqueNameRequiredMessage) @PathVariable @Size(min = 3, max = 100, message = InstituteNotValidInputMessage) String ivalue,
                                                                     @ApiParam(name = "qualifier_type", type = "String[]",
                                                                             value = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                                                                             example = "specimen_voucher", required = false)
                                                                     @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                                                     @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        ResponseDto responseDto = SAHService.findCollectionsByInstUniqueName(ivalue, qualifierType);
        return new ResponseEntity<>(responseDto, responseDto.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }


    @GetMapping("/institution/{ivalue}/collection/{cvalue}")
    @ApiOperation(value = "Get collection by institution name and collection code.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried the information."),
            @ApiResponse(code = 400, message = "Invalid request format"),
            @ApiResponse(code = 404, message = "Record Not Found")
    })
    public ResponseEntity<Object> findByInstUniqueNameAndCollCode(@ApiParam(name = "ivalue", type = "String",
            value = ValidInstituteUniqueNameRequiredMessage) @PathVariable @Size(min = 3, max = 100, message = InstituteNotValidInputMessage) String ivalue,
                                                                  @ApiParam(name = "cvalue", type = "String", value = ValidInputSizeMessage)
                                                                  @PathVariable @Size(min = 3, max = 100, message = CollectionNotValidInputMessage) String cvalue,
                                                                  @ApiParam(name = "qualifier_type", type = "String[]",
                                                                          value = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                                                                          example = "specimen_voucher", required = false)
                                                                  @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                                                  @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        ResponseDto responseDto = SAHService.findByInstUniqueNameAndCollCode(ivalue, cvalue, qualifierType);
        return new ResponseEntity<>(responseDto, responseDto.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }


    @GetMapping("/validate")
    @ApiOperation(value = "Validate the provided Qualifier String")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Validated the provided Specimen Voucher."),
            @ApiResponse(code = 400, message = "Invalid request format"),
            @ApiResponse(code = 404, message = "Record Not Found")
    })
    public ResponseEntity<Object> validate(@ApiParam(name = "value", type = "String",
            value = ProvideQualifierString) @RequestParam("value") String value,
                                           @ApiParam(name = "qualifier_type", type = "String[]",
                                                   value = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                                                   example = "specimen_voucher", required = false)
                                           @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                           @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        SAHResponseDto responseDto = SAHService.validate(value, qualifierType);

        return new ResponseEntity<>(responseDto, responseDto.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/construct")
    @ApiOperation(value = "Construct the Qualifier String")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Constructed the Specimen Voucher String."),
            @ApiResponse(code = 400, message = "Invalid request format"),
            @ApiResponse(code = 404, message = "Record Not Found")
    })
    public ResponseEntity<Object> construct(@ApiParam(name = "institution", type = "String",
            value = ProvideValidInstituteUniqueName) @RequestParam(name = "institution") String institution,
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
        SAHResponseDto responseDto = SAHService.construct(institution, collection, id, qualifierType);

        return new ResponseEntity<>(responseDto, responseDto.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/error-codes")
    @ApiOperation(value = "Get application error codes metadata for reference.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched the error codes."),
            @ApiResponse(code = 400, message = "Invalid request format"),
            @ApiResponse(code = 404, message = "Record Not Found")
    })
    public ResponseEntity<Object> getErrorCodes() {
        ResponseDto responseDto = SAHService.getErrorCodes();
        return new ResponseEntity<>(responseDto, responseDto.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

}
