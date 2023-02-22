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

package uk.ac.ebi.ena.sah.biocollections.importer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import uk.ac.ebi.ena.sah.biocollections.importer.service.BioCollectionsImportService;

@SpringBootApplication
@Slf4j
public class BioCollectionsImporterApplication implements CommandLineRunner {

    private final BioCollectionsImportService bioCollectionsImportService;

    public BioCollectionsImporterApplication(BioCollectionsImportService bioCollectionsImportService) {
        this.bioCollectionsImportService = bioCollectionsImportService;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(BioCollectionsImporterApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            bioCollectionsImportService.processBioCollectionsImport();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            exit(1);
        } finally {
            log.info("Execution cycle completed");
            exit(0);
        }
    }

    private void exit(int status) {
        System.exit(status);
    }
}
