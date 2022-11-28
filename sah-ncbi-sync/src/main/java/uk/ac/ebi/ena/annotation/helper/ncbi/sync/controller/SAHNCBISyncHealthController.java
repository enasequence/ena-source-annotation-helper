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

package uk.ac.ebi.ena.annotation.helper.ncbi.sync.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.dto.SAHSyncResponseDto;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.SyncHealthService;

@RestController

@Slf4j
@RequestMapping({"/ena/sah/ncbi/sync/"})
public class SAHNCBISyncHealthController {

    @Autowired
    SyncHealthService syncHealthService;

    @GetMapping("/dbcheck")
    public ResponseEntity<Object> checkElasticConnectivity() {
        SAHSyncResponseDto responseDto = new SAHSyncResponseDto();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
