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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
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

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.exception.SAHErrorCode.*;

@RestController
@CrossOrigin
@Slf4j
//@Api(tags = "ENA Source Attribute Helper APIs")
@Tag(name = "SAH", description = "Source Attribute Helper APIs")
@RequestMapping({"/ena/sah/api/"})
@Validated
public class SAHController {

    @Autowired
    SAHService SAHService;

    @GetMapping("/institution/{ivalue}")
    @Operation(summary = "Get institution. In case no exact match is found, fetches similar institution matches by either institution name or institution code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully queried the institution(s) information."),
            @ApiResponse(responseCode = "400", description = "Invalid request format"),
    })
    public ResponseEntity<Object> findByInstitutionValue(
            @Parameter(name = "ivalue",
                    description = ValidInputSizeMessage) @PathVariable @Size(min = 1, max = 100, message = InstituteNotValidInputMessage) String ivalue,
            @Parameter(name = "qualifier_type",
                    description = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                    example = "specimen_voucher", required = false)
            @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
            @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        ResponseDto responseDto = SAHService.findByInstituteStringFuzzyWithQTArray(ivalue.trim(), qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/institution/{institutionUniqueName}/collection")
    @Operation(summary = "Get all collections by institution unique name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully queried the information."),
            @ApiResponse(responseCode = "400", description = "Invalid request format"),
    })
    public ResponseEntity<Object> findByAllCollByInstituteUniqueName(@Parameter(name = "institutionUniqueName",
            description = ValidInstituteUniqueNameRequiredMessage) @PathVariable @Size(min = 1, max = 100, message = InstituteNotValidInputMessage) String institutionUniqueName,
                                                                     @Parameter(name = "qualifier_type",
                                                                             description = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                                                                             example = "specimen_voucher", required = false)
                                                                     @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                                                     @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        ResponseDto responseDto = SAHService.findCollectionsByInstUniqueName(institutionUniqueName.trim(), qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @GetMapping("/institution/{institutionUniqueName}/collection/{cvalue}")
    @Operation(summary = "Get collection by institution name and collection code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully queried the information."),
            @ApiResponse(responseCode = "400", description = "Invalid request format"),
    })
    public ResponseEntity<Object> findByInstUniqueNameAndCollCode(
            @Parameter(name = "institutionUniqueName",
                    description = ValidInstituteUniqueNameRequiredMessage)
            @PathVariable @Size(min = 1, max = 100, message = InstituteNotValidInputMessage) String institutionUniqueName,
            @Parameter(name = "cvalue", description = ValidInputSizeMessage)
            @PathVariable @Size(min = 1, max = 100, message = CollectionNotValidInputMessage) String cvalue,
            @Parameter(name = "qualifier_type",
                    description = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                    example = "specimen_voucher", required = false)
            @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
            @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        ResponseDto responseDto = SAHService.findByInstUniqueNameAndCollCode(institutionUniqueName.trim(), cvalue.trim(), qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @GetMapping("/validate")
    @Operation(summary = "Validate the provided Attribute String")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully processed the provided Attribute String."),
            @ApiResponse(responseCode = "400", description = "Invalid request format"),
    })
    public ResponseEntity<Object> validate(@Parameter(name = "value",
            description = ProvideQualifierString) @RequestParam("value") String value,
                                           @Parameter(name = "qualifier_type",
                                                   description = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                                                   example = "specimen_voucher", required = false)
                                           @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                           @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        SAHResponseDto responseDto = SAHService.validate(value.trim(), qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/construct")
    @Operation(summary = "Construct the Attribute String")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully processed the inputs for the Attribute String."),
            @ApiResponse(responseCode = "400", description = "Invalid request format"),
    })
    public ResponseEntity<Object> construct(
            @Parameter(name = "institution",
                    description = ProvideValidInstituteUniqueName) @RequestParam(name = "institution") String institution,
            @Parameter(name = "collection",
                    description = ProvideValidCollectionCode)
            @RequestParam(name = "collection", required = false) String collection,
            @Parameter(name = "id",
                    description = ProvideValidId)
            @RequestParam(name = "id") String id,
            @Parameter(name = "qualifier_type",
                    description = "Acceptable values are {specimen_voucher, bio_material, culture_collection}",
                    example = "specimen_voucher", required = false)
            @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
            @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        SAHResponseDto responseDto = SAHService.construct(institution.trim(), isEmpty(collection) ? collection : collection.trim(), id.trim(), qualifierType);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/error-codes")
    @Operation(summary = "Get application error codes metadata for reference.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the error codes."),
            @ApiResponse(responseCode = "400", description = "Invalid request format"),
    })
    public ResponseEntity<Object> getErrorCodes() {
        ResponseDto responseDto = SAHService.getErrorCodes();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
