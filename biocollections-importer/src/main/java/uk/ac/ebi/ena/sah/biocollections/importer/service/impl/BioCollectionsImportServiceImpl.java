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

package uk.ac.ebi.ena.sah.biocollections.importer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ena.sah.biocollections.importer.repository.BioCollectionsRepository;
import uk.ac.ebi.ena.sah.biocollections.importer.repository.CollectionRepository;
import uk.ac.ebi.ena.sah.biocollections.importer.repository.InstitutionRepository;
import uk.ac.ebi.ena.sah.biocollections.importer.service.BioCollectionsImportService;
import uk.ac.ebi.ena.sah.biocollections.importer.service.FTPDataReadService;

import java.io.IOException;

@Slf4j
@Component
public class BioCollectionsImportServiceImpl implements BioCollectionsImportService {

    final FTPDataReadService institutionDataReadService;
    final FTPDataReadService collectionDataReadService;
    final InstitutionRepository institutionRepository;
    final CollectionRepository collectionRepository;
    final BioCollectionsRepository bioCollectionsRepository;

    public BioCollectionsImportServiceImpl(InstitutionDataReadServiceImpl institutionDataReadService,
                                           CollectionDataReadServiceImpl collectionDataReadService,
                                           InstitutionRepository institutionRepository,
                                           CollectionRepository collectionRepository,
                                           BioCollectionsRepository bioCollectionsRepository) {
        this.institutionDataReadService = institutionDataReadService;
        this.collectionDataReadService = collectionDataReadService;
        this.institutionRepository = institutionRepository;
        this.collectionRepository = collectionRepository;
        this.bioCollectionsRepository = bioCollectionsRepository;
    }

    @Override
    public boolean processBioCollectionsImport() {
        try {
            //fetch bio-collections data
            boolean fetchSuccessful = importBioCollectionsData();
            if (!fetchSuccessful) {
                return false;
            }
            // persist bio-collections data
            return persistBioCollectionsData();
        } catch (Exception ex) {
            log.info("Error occurred while processing the bio-collections data import.");
            return false;
        }
    }

    /**
     * importBioCollectionsData.
     *
     * @return
     */
    public boolean importBioCollectionsData() {
        log.info("Importing Data for Institutions - Started");
        //fetch the data from the FTP server
        boolean instImport = institutionDataReadService.fetchDataFileFromFTP();
        if (!instImport) {
            log.info("Failed to import the Institutions data.");
            return false;
        }
        //fetch the data from the FTP server
        log.info("Importing Data for Collections - Started");
        boolean collImport = collectionDataReadService.fetchDataFileFromFTP();
        if (!collImport) {
            log.info("Failed to import the Collections data.");
            return false;
        }
        return true;
    }

    /**
     * persistBioCollectionsData.
     *
     * @return
     */
    public boolean persistBioCollectionsData() {
        //fetch and persist Collections data
        log.info("Persisting Data for Bio-collections - Started");
        try {
            // processing for the institutions
            if (!institutionRepository.persistInstitutionsData()) {
                return false;
            }
            // processing for the collections
            if (!collectionRepository.persistCollectionsData()) {
                return false;
            }
            log.info("Persisting Data for Bio-collections - finished");
            return true;
        } catch (IOException e) {
            log.info("Failed - Persisting Data for Bio-collections");
            return false;
        }
    }


}
