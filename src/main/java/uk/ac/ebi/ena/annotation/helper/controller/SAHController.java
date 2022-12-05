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
@RequestMapping({"/ena/sah/api/"})
@Validated
public class SAHController {

    @Autowired
    SAHService SAHService;

    @GetMapping("/institution/{ivalue}")

    public ResponseEntity<Object> findByInstitutionValue(@PathVariable @Size(min = 1, max = 100, message = InstituteNotValidInputMessage) String ivalue,

                                                         @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                                         @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        ResponseDto responseDto = SAHService.findByInstituteStringFuzzyWithQTArray(ivalue.trim(), qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/institution/{institutionUniqueName}/collection")
    public ResponseEntity<Object> findByAllCollByInstituteUniqueName(@PathVariable @Size(min = 1, max = 100, message = InstituteNotValidInputMessage) String institutionUniqueName,

                                                                     @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                                                     @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        ResponseDto responseDto = SAHService.findCollectionsByInstUniqueName(institutionUniqueName.trim(), qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @GetMapping("/institution/{institutionUniqueName}/collection/{cvalue}")
    public ResponseEntity<Object> findByInstUniqueNameAndCollCode(@PathVariable @Size(min = 1, max = 100, message = InstituteNotValidInputMessage) String institutionUniqueName,

                                                                  @PathVariable @Size(min = 1, max = 100, message = CollectionNotValidInputMessage) String cvalue,

                                                                  @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                                                  @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        ResponseDto responseDto = SAHService.findByInstUniqueNameAndCollCode(institutionUniqueName.trim(), cvalue.trim(), qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @GetMapping("/validate")
    public ResponseEntity<Object> validate(@RequestParam("value") String value,

                                           @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                           @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        SAHResponseDto responseDto = SAHService.validate(value.trim(), qualifierType);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/construct")
    public ResponseEntity<Object> construct(@RequestParam(name = "institution") String institution,

                                            @RequestParam(name = "collection", required = false) String collection,

                                            @RequestParam(name = "id") String id,

                                            @QualifierValuesAllowed(propName = "qualifier_type", values = {"specimen_voucher", "bio_material", "culture_collection"})
                                            @Valid @RequestParam(name = "qualifier_type", required = false) String[] qualifierType) {
        SAHResponseDto responseDto = SAHService.construct(institution.trim(), isEmpty(collection) ? collection : collection.trim(), id.trim(), qualifierType);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/error-codes")
    public ResponseEntity<Object> getErrorCodes() {
        ResponseDto responseDto = SAHService.getErrorCodes();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
