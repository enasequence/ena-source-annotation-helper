package uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.FTPDataReadService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import static uk.ac.ebi.ena.annotation.helper.ncbi.sync.utils.SAHDataSyncConstants.*;

@Service
@Slf4j
public class FTPDataReadServiceImpl implements FTPDataReadService {

    // variables to be configured at the time of script setup
    //TODO move to application properties
    @Value("${ftp.ncbi.file.institutions}")
   private String ncbiInstitutionsFilePath;

    @Value("${ftp.ncbi.file.collections}")
    private String ncbiCollectionsFilePath;

    public boolean fetchInstitutionsFileFromFTP() {

        ApplicationContext appContext =
                new ClassPathXmlApplicationContext();

        Resource resource =
                appContext.getResource(ncbiInstitutionsFilePath);

        try {
            InputStream is = resource.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                StringTokenizer st = new StringTokenizer(line, "|");
                int tokenCount = 0;
                while (st.hasMoreTokens()) {
                    tokenCount++;
                    //TODO Format to be read -
                    // inst_id|inst_code|unique_name|synonyms|inst_name|country|address|phone|fax|home_url|comments
                    switch (tokenCount) {
                        case INST_ID_POS:
                        case INST_CODE_POS:
                        case UNIQUE_NAME_POS:
                        case SYNONYMS_POS:
                        case INST_NAME_POS:
                        case COUNTRY_POS:
                        case ADDRESS_POS:
                        case PHONE_POS:
                        case FAX_POS:
                        case HOME_URL_POS:
                        case INS_COMMENTS_POS:
                    }
                    System.out.print(st.nextToken().trim());
                }
                System.out.println();
                if (i >= 20) break;
                i++;
            }
            br.close();
            return true;
        } catch (IOException e) {
            log.debug(e.getLocalizedMessage(), e);
            return false;
        }
    }

    public boolean fetchCollectionsFileFromFTP() {

        ApplicationContext appContext =
                new ClassPathXmlApplicationContext();

        Resource resource =
                appContext.getResource(ncbiCollectionsFilePath);

        try {
            InputStream is = resource.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                StringTokenizer st = new StringTokenizer(line, "|");
                int tokenCount = 0;
                while (st.hasMoreTokens()) {
                    tokenCount++;
                    //TODO Format to be read -
                    // coll_id|inst_id|coll_name|coll_code|coll_size|collection_type|coll_url_rule|coll_url|qualifier_type|comments
                    switch (tokenCount) {
                        case COLL_ID_POS:
                        case COLL_INST_ID_POS:
                        case COLL_NAME_POS:
                        case COLL_CODE_POS:
                        case COLL_SIZE_POS:
                        case COLLECTION_TYPE_POS:
                        case COLL_URL_RULE_POS:
                        case COLL_URL_POS:
                        case QUALIFIER_TYPE_POS:
                        case COLL_COMMENTS_POS:
                    }
                    System.out.print(st.nextToken().trim());
                }
                System.out.println();
                if (i >= 20) break;
                i++;
            }
            br.close();
            return true;
        } catch (IOException e) {
            log.debug(e.getLocalizedMessage(), e);
            return false;
        }
    }

}
