package uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.data.SAHDataObject;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.entity.Institution;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.FTPDataReadService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.ncbi.sync.utils.SAHDataSyncConstants.*;

@Service
@Slf4j
public class InstitutionDataReadServiceImpl implements FTPDataReadService {

    // variables to be configured at the time of script setup
    //TODO move to application properties
    @Value("${ftp.ncbi.file.institutions}")
    private String ncbiInstitutionsFilePath;

    public boolean fetchDataFileFromFTP() {

        ApplicationContext appContext =
                new ClassPathXmlApplicationContext();

        Resource resource =
                appContext.getResource(ncbiInstitutionsFilePath);

        try {
            InputStream is = resource.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                StringTokenizer st = new StringTokenizer(line, "|");
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
                SAHDataObject.mapInstitutions.put(instObj.getInstId(), instObj);
            }
            br.close();
            return true;
        } catch (IOException e) {
            log.debug(e.getLocalizedMessage(), e);
            return false;
        }
    }


}
