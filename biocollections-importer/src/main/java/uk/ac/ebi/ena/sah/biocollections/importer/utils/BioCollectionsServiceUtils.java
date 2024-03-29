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

package uk.ac.ebi.ena.sah.biocollections.importer.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.text.DecimalFormat;

import static java.nio.charset.StandardCharsets.UTF_8;
import static uk.ac.ebi.ena.sah.biocollections.importer.utils.AppConstants.PERCENTAGE_FORMAT;

@Slf4j
public class BioCollectionsServiceUtils {

    static DecimalFormat percentageDF = new DecimalFormat(PERCENTAGE_FORMAT);

    /**
     * getResourceAsString.
     *
     * @param resource
     * @return
     */
    public static String getResourceAsString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * calculatePercentChanged.
     *
     * @param validatedRecords
     * @param existingRecords
     * @return
     */
    public static double calculatePercentChanged(long validatedRecords, long existingRecords) {
        if (existingRecords == 0) {
            return 0;
        } else {
            if (validatedRecords > existingRecords) return 0;
            return (existingRecords - validatedRecords) / (double) existingRecords * 100.0;
        }
    }

    /**
     * logStats.
     *
     * @param oldIndexName
     * @param newIndexName
     * @param oldRecordCount
     * @param newRecordCount
     * @param percentageChanged
     */
    public static void logStats(String oldIndexName, String newIndexName,
                                long oldRecordCount, long newRecordCount,
                                double percentageChanged) {
        log.info("Existing Index {} record count was: {}", oldIndexName, oldRecordCount);
        log.info("New Index {} record count is: {}", newIndexName, newRecordCount);
        StringBuilder buffer = new StringBuilder();
        buffer.append("\n")
                .append("Old Index: ").append(oldIndexName).append("\n")
                .append("New Index: ").append(newIndexName).append("\n")
                .append("Old Index Record Count: ").append(oldRecordCount)
                .append("\n").append("New Index Record Count: ").append(newRecordCount)
                .append("\n").append("Percentage reduced from old: ").append(percentageDF.format(percentageChanged))
                .append("%\n");
        log.info(buffer.toString());
    }

}
