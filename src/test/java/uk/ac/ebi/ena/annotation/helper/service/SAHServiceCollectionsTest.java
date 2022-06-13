package uk.ac.ebi.ena.annotation.helper.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SAHServiceCollectionsTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void before() {

    }

    @AfterAll
    public static void tearDown() {

    }

    @Test
    public void getCollectionByInstCode() throws Exception {
        String params = "CMN/collection";

    }


    @Test
    public void getCollectionByInstCodeWithQT() throws Exception {
        String params = "CMN/collection?qualifier_type=specimen_voucher";


    }

    @Test
    public void getCollectionByInstCodeWithQTNoMatch() throws Exception {
        String params = "CMN/collection?qualifier_type=bio_material";


    }

    @Test
    public void getCollectionInstituteDoesNotExist() throws Exception {
        String params = "QQQ/collection";

    }

    @Test
    public void getCollectionInstituteExistNoColl() throws Exception {
        String params = "QQQ/collection";

    }

    @Test
    public void getCollectionByInstCodeAndCollCode() throws Exception {
        String params = "MSNT<ITA-Torino>/collection/FAZC";

    }
}