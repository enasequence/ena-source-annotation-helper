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
import uk.ac.ebi.ena.sah.biocollections.importer.entity.Collection;
import uk.ac.ebi.ena.sah.biocollections.importer.entity.Institution;
import uk.ac.ebi.ena.sah.biocollections.importer.exception.BioCollectionsProcessingException;
import uk.ac.ebi.ena.sah.biocollections.importer.service.FTPDataReadService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.sah.biocollections.importer.exception.BioCollectionsErrorCode.CollectionDataReadError;
import static uk.ac.ebi.ena.sah.biocollections.importer.exception.BioCollectionsErrorCode.CollectionDataReadErrorMessage;
import static uk.ac.ebi.ena.sah.biocollections.importer.utils.AppConstants.*;

@Service
@Slf4j
public class CollectionDataReadServiceImpl implements FTPDataReadService, ApplicationContextAware {

    @Value("${ftp.ncbi.file.collections}")
    private String ncbiCollectionsFilePath;

    private ApplicationContext appContext;

    public boolean fetchDataFileFromFTP() {

        try ( InputStream is =
                      appContext.getResource(ncbiCollectionsFilePath).getInputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                StringTokenizer st = new StringTokenizer(line, NCBI_DELIMITER);
                if (st.countTokens() < 4) {
                    //not a valid record
                    continue;
                }
                Collection collObj = new Collection();
                boolean inst = false;
                int inst_id = 0;
                Institution instObj = null;
                int tokenCount = 0;
                while (st.hasMoreTokens()) {
                    tokenCount++;
                    String tokenVal = st.nextToken().trim();
                    // Format to be read -
                    // coll_id|inst_id|coll_name|coll_code|coll_size|collection_type|coll_url_rule|coll_url|qualifier_type|comments
                    switch (tokenCount) {
                        case COLL_ID_POS:
                            try {
                                int coll_id = Integer.parseInt(tokenVal);
                                collObj.setCollId(coll_id);
                            } catch (NumberFormatException nfe) {
                                log.debug("Invalid Collection Id - '{}'", tokenVal);
                                continue;
                            }
                            break;
                        case COLL_INST_ID_POS:
                            try {
                                inst_id = Integer.parseInt(tokenVal);
                                collObj.setInstId(inst_id);
                            } catch (NumberFormatException nfe) {
                                log.debug("Invalid Institution Id - '{}' in collection file", tokenVal);
                                continue;
                            }
                            break;
                        case COLL_NAME_POS:
                            collObj.setCollName(tokenVal);
                            break;
                        case COLL_CODE_POS:
                            if (tokenVal.contains(":")) {
                                // it is a collection row
                                collObj.setCollCode(tokenVal.substring(tokenVal.indexOf(":")+1));
                            } else if (inst_id != 0) {
                                //it is a valid institution row
                                instObj = BioCollectionsDataObject.mapInstitutions.get(inst_id);
                                inst = true;
                            }
                            break;
                        case COLLECTION_TYPE_POS:
                            if (inst) {
                                instObj.setCollType(tokenVal);
                            } else {
                                collObj.setCollType(tokenVal);
                            }
                            break;
                        case COLL_URL_RULE_POS:
                            collObj.setCollUrlRule(tokenVal);
                            break;
                        case COLL_URL_POS:
                            collObj.setCollUrl(tokenVal);
                            break;
                        case QUALIFIER_TYPE_POS:
                            if (inst) {
                                instObj.setQualifierType(tokenVal);
                            } else {
                                collObj.setQualifierType(tokenVal);
                            }
                            break;
                    }
                }
                //all tokens read, add to map
                if (inst) {
                    BioCollectionsDataObject.mapInstitutions.put(instObj.getInstId(), instObj);
                } else if (!isEmpty(collObj) && collObj.getCollId() != 0) {
                    BioCollectionsDataObject.mapCollections.put(collObj.getCollId(), collObj);
                }
            }
            br.close();
            return true;
        } catch (IOException ex) {
            log.debug(ex.getLocalizedMessage(), ex);
            throw new BioCollectionsProcessingException(CollectionDataReadError, CollectionDataReadErrorMessage, ex);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = appContext;
    }

}
