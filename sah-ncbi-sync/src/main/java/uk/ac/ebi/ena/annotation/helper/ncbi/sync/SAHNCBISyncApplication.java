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

package uk.ac.ebi.ena.annotation.helper.ncbi.sync;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.NCBISyncService;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "uk.ac.ebi.ena.annotation.helper.ncbi.sync.repository")
@Slf4j
public class SAHNCBISyncApplication implements CommandLineRunner {

    private final NCBISyncService ncbiSyncService;

    public SAHNCBISyncApplication(NCBISyncService ncbiSyncService) {
        this.ncbiSyncService = ncbiSyncService;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SAHNCBISyncApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            ncbiSyncService.processNCBIDataRead();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            log.info("Execution cycle completed");
            exit(0);
        }
    }

    private void exit(int status) {
        System.exit(status);
    }
}
