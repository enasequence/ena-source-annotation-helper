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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.sah.biocollections.importer.data.BioCollectionsDataObject;
import uk.ac.ebi.ena.sah.biocollections.importer.entity.Institution;
import uk.ac.ebi.ena.sah.biocollections.importer.exception.BioCollectionsProcessingException;
import uk.ac.ebi.ena.sah.biocollections.importer.service.FTPDataReadService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.sah.biocollections.importer.exception.BioCollectionsErrorCode.InstitutionDataReadError;
import static uk.ac.ebi.ena.sah.biocollections.importer.exception.BioCollectionsErrorCode.InstitutionDataReadErrorMessage;
import static uk.ac.ebi.ena.sah.biocollections.importer.utils.AppConstants.*;

@Service
@Slf4j
public class InstitutionDataReadServiceImpl implements FTPDataReadService, ApplicationContextAware {

    @Value("${ftp.ncbi.file.institutions}")
    private String ncbiInstitutionsFilePath;

    private ApplicationContext appContext;

    public boolean fetchDataFileFromFTP() {

        try ( InputStream is =
                     appContext.getResource(ncbiInstitutionsFilePath).getInputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, NCBI_DELIMITER);
                if (st.countTokens() < 3) {
                    //not a valid record
                    continue;
                }
                Institution instObj = new Institution();
                int tokenCount = 0;
                while (st.hasMoreTokens()) {
                    tokenCount++;
                    String tokenVal = st.nextToken().trim();
                    // Format to be read -
                    // inst_id|inst_code|unique_name|synonyms|inst_name|country|address|phone|fax|home_url|comments
                    switch (tokenCount) {
                        case INST_ID_POS:
                            try {
                                int inst_id = Integer.parseInt(tokenVal);
                                instObj.setInstId(inst_id);
                            } catch (NumberFormatException nfe) {
                                log.debug("Invalid Institution Id - '{}'", tokenVal);
                                continue;
                            }
                            break;
                        case INST_CODE_POS:
                            instObj.setInstCode(tokenVal);
                            break;
                        case UNIQUE_NAME_POS:
                            instObj.setUniqueName(tokenVal);
                            break;
                        case SYNONYMS_POS:
                            instObj.setSynonyms(tokenVal);
                            break;
                        case INST_NAME_POS:
                            instObj.setInstName(tokenVal);
                            break;
                        case COUNTRY_POS:
                            instObj.setCountry(tokenVal);
                            break;
                        case ADDRESS_POS:
                            instObj.setAddress(tokenVal);
                            break;
                        case HOME_URL_POS:
                            instObj.setHomeUrl(tokenVal);
                            break;
                    }
                }
                if (isEmpty(instObj) || instObj.getInstId() == 0) {
                    continue;
                }
                //all tokens read, add to map
                if (!isEmpty(instObj) && instObj.getInstId() != 0 && !isEmpty(instObj.getInstCode())) {
                    BioCollectionsDataObject.mapInstitutions.put(instObj.getInstId(), instObj);
                }
            }
            br.close();
            return true;
        } catch (IOException ex) {
            log.info("Failed to fetch and process institutions data from NCBI FTP - " + ex.getLocalizedMessage(), ex);
            throw new BioCollectionsProcessingException(InstitutionDataReadError, InstitutionDataReadErrorMessage, ex);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = appContext;
    }

}
