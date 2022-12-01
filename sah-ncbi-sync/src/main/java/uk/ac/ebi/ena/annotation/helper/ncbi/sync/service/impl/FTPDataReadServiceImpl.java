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
                while (st.hasMoreTokens()) {
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
                while (st.hasMoreTokens()) {
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
