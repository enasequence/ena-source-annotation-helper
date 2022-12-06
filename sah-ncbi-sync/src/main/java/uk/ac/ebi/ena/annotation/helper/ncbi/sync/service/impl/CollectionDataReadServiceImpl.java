package uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.data.SAHDataObject;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.entity.Institution;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.FTPDataReadService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.ncbi.sync.utils.SAHDataSyncConstants.*;

@Service
@Slf4j
public class CollectionDataReadServiceImpl implements FTPDataReadService {

    @Value("${ftp.ncbi.file.collections}")
    private String ncbiCollectionsFilePath;

    public boolean fetchDataFileFromFTP() {

        ApplicationContext appContext =
                new ClassPathXmlApplicationContext();

        Resource resource =
                appContext.getResource(ncbiCollectionsFilePath);

        try {
            InputStream is = resource.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                StringTokenizer st = new StringTokenizer(line, "|");
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
                                collObj.setColl_id(coll_id);
                            } catch (NumberFormatException nfe) {
                                log.debug("Invalid Collection Id - '{}'", tokenVal);
                            }
                            break;
                        case COLL_INST_ID_POS:
                            try {
                                inst_id = Integer.parseInt(tokenVal);
                                collObj.setInstId(inst_id);
                            } catch (NumberFormatException nfe) {
                                log.debug("Invalid Institution Id - '{}' in collection file", tokenVal);
                            }
                            break;
                        case COLL_NAME_POS:
                            collObj.setCollName(tokenVal);
                            break;
                        case COLL_CODE_POS:
                            if (tokenVal.contains(":")) {
                                // it is a collection row
                                collObj.setCollCode(tokenVal.substring(tokenVal.indexOf(":")));
                            } else if (inst_id != 0) {
                                //it is a valid institution row
                                instObj = SAHDataObject.mapInstitutions.get(inst_id);
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
                    SAHDataObject.mapInstitutions.put(instObj.getInstId(), instObj);
                } else if (!isEmpty(collObj) || collObj.getColl_id() != 0) {
                    SAHDataObject.mapCollections.put(collObj.getColl_id(), collObj);
                }
            }
            br.close();
            return true;
        } catch (IOException e) {
            log.debug(e.getLocalizedMessage(), e);
            return false;
        }
    }

}
