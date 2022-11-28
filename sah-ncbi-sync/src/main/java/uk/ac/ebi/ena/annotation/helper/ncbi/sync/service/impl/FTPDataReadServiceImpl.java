package uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.impl;

import lombok.extern.slf4j.Slf4j;
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
    public static final String NCBI_FTP_SERVER = "https://ftp.ncbi.nih.gov/";
    public static final String NCBI_FILE_PATH = "pub/taxonomy/biocollections/";
    public static final String INST_FILE_TXT = "Institution_codes.txt";
    public static final String COLL_FILE_TXT = "Collection_codes.txt";

    public void fetchInstitutionsFileFromFTP() {

        ApplicationContext appContext =
                new ClassPathXmlApplicationContext();

        Resource resource =
                appContext.getResource(NCBI_FTP_SERVER + NCBI_FILE_PATH + INST_FILE_TXT);

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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fetchCollectionsFileFromFTP() {

        ApplicationContext appContext =
                new ClassPathXmlApplicationContext();

        Resource resource =
                appContext.getResource(NCBI_FTP_SERVER + NCBI_FILE_PATH + COLL_FILE_TXT);

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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
