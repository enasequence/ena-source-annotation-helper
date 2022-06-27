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

package uk.ac.ebi.ena.annotation.helper.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static uk.ac.ebi.ena.annotation.helper.utils.SAHConstants.ES_INITIAL_DELAY;
import static uk.ac.ebi.ena.annotation.helper.utils.SAHConstants.ES_KEEP_ALIVE_FREQUENCY;

@Component
@Slf4j
public class ElasticSearchConnectionKeepAlive {

    @Autowired
    private CollectionRepository collectionRepository;

    // ElasticSearch KeepAlive Prob Configuration -- default is 5 minutes
    @Scheduled(fixedRate = ES_KEEP_ALIVE_FREQUENCY, initialDelay = ES_INITIAL_DELAY)
    public void keepConnectionAlive() {
        log.debug("Trying to ping Elasticsearch Datastore...");
        try {
            final long noOfCollections = collectionRepository.count();
            log.info("Ping succeeded for CollectionRepository, it contains {} entities", noOfCollections);
        } catch (Exception e) {
            log.info("Ping failed for CollectionRepository");
        }
    }
}