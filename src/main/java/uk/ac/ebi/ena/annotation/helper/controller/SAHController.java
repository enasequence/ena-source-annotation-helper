/*
 * ******************************************************************************
 *  * Copyright 2021 EMBL-EBI, Hinxton outstation
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

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

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.exception.SAHErrorCode.*;

@RestController
@CrossOrigin
@Slf4j
@Api(tags = "ENA Source Attribute Helper APIs")
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
    })
    public ResponseEntity<Object> findByInstitutionValue(@ApiParam(name = "ivalue", type = "String",
            value = ValidInputSizeMessage) @PathVariable @Size(min = 1, max = 100, message = InstituteNotValidInputMessage) String ivalue,
                                                         @ApiParam(name = "qualifier_type", type = "String[]",
                                                                 value = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                                                                 example = "specimen_voucher", required = false)
                                                         @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                                         @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        ResponseDto responseDto = SAHService.findByInstituteStringFuzzyWithQTArray(ivalue.trim(), qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/institution/{institutionUniqueName}/collection")
    @ApiOperation(value = "Get all collections by institution unique name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried the information."),
            @ApiResponse(code = 400, message = "Invalid request format"),
    })
    public ResponseEntity<Object> findByAllCollByInstituteUniqueName(@ApiParam(name = "institutionUniqueName", type = "String",
            value = ValidInstituteUniqueNameRequiredMessage) @PathVariable @Size(min = 1, max = 100, message = InstituteNotValidInputMessage) String institutionUniqueName,
                                                                     @ApiParam(name = "qualifier_type", type = "String[]",
                                                                             value = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                                                                             example = "specimen_voucher", required = false)
                                                                     @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                                                     @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        ResponseDto responseDto = SAHService.findCollectionsByInstUniqueName(institutionUniqueName.trim(), qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @GetMapping("/institution/{institutionUniqueName}/collection/{cvalue}")
    @ApiOperation(value = "Get collection by institution name and collection code.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried the information."),
            @ApiResponse(code = 400, message = "Invalid request format"),
    })
    public ResponseEntity<Object> findByInstUniqueNameAndCollCode(@ApiParam(name = "institutionUniqueName", type = "String",
            value = ValidInstituteUniqueNameRequiredMessage) @PathVariable @Size(min = 1, max = 100, message = InstituteNotValidInputMessage) String institutionUniqueName,
                                                                  @ApiParam(name = "cvalue", type = "String", value = ValidInputSizeMessage)
                                                                  @PathVariable @Size(min = 1, max = 100, message = CollectionNotValidInputMessage) String cvalue,
                                                                  @ApiParam(name = "qualifier_type", type = "String[]",
                                                                          value = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                                                                          example = "specimen_voucher", required = false)
                                                                  @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                                                  @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        ResponseDto responseDto = SAHService.findByInstUniqueNameAndCollCode(institutionUniqueName.trim(), cvalue.trim(), qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @GetMapping("/validate")
    @ApiOperation(value = "Validate the provided Attribute String")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully processed the provided Attribute String."),
            @ApiResponse(code = 400, message = "Invalid request format"),
    })
    public ResponseEntity<Object> validate(@ApiParam(name = "value", type = "String",
            value = ProvideQualifierString) @RequestParam("value") String value,
                                           @ApiParam(name = "qualifier_type", type = "String[]",
                                                   value = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                                                   example = "specimen_voucher", required = false)
                                           @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                           @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        SAHResponseDto responseDto = SAHService.validate(value.trim(), qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/construct")
    @ApiOperation(value = "Construct the Attribute String")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully processed the inputs for the Attribute String."),
            @ApiResponse(code = 400, message = "Invalid request format"),
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
        SAHResponseDto responseDto = SAHService.construct(institution.trim(), isEmpty(collection) ? collection : collection.trim(), id.trim(), qualifierType);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/error-codes")
    @ApiOperation(value = "Get application error codes metadata for reference.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched the error codes."),
            @ApiResponse(code = 400, message = "Invalid request format"),
    })
    public ResponseEntity<Object> getErrorCodes() {
        ResponseDto responseDto = SAHService.getErrorCodes();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
