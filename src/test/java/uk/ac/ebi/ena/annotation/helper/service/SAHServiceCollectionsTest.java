package uk.ac.ebi.ena.annotation.helper.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SAHServiceCollectionsTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String SEARCH_BASE_ENDPOINT = "/ena/sah/institute/";

    @BeforeAll
    public static void before() {

    }

    @AfterAll
    public static void tearDown() {

    }

    @Test
    public void getCollectionByInstCode() throws Exception {
        String params = "CMN/collection";
        MvcResult result = this.mockMvc.perform(get(SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                .andExpect(jsonPath("$.institutes[0].collections").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }


    @Test
    public void getCollectionByInstCodeWithQT() throws Exception {
        String params = "CMN/collection?qualifier_type=specimen_voucher";
        MvcResult result = this.mockMvc.perform(get(SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                .andExpect(jsonPath("$.institutes[0].collections").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();

    }

    @Test
    public void getCollectionByInstCodeWithQTNoMatch() throws Exception {
        String params = "CMN/collection?qualifier_type=bio_material";
        MvcResult result = this.mockMvc.perform(get(SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(equalTo(false)))
                .andExpect(jsonPath("$.errors[0].code").value(equalTo(3006)))
                .andReturn();

    }

    @Test
    public void getCollectionInstituteDoesNotExist() throws Exception {
        String params = "QQQ/collection";
        MvcResult result = this.mockMvc.perform(get(SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(equalTo(false)))
                .andExpect(jsonPath("$.errors[0].code").value(equalTo(3005)))
                .andReturn();
    }

    @Test
    public void getCollectionInstituteExistNoColl() throws Exception {
        String params = "QQQ/collection";
        MvcResult result = this.mockMvc.perform(get(SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(equalTo(false)))
                .andExpect(jsonPath("$.errors[0].code").value(equalTo(3005)))
                .andReturn();
    }

    @Test
    public void getCollectionByInstCodeAndCollCode() throws Exception {
        String params = "MSNT<ITA-Torino>/collection/FAZC";
        MvcResult result = this.mockMvc.perform(get(SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                .andExpect(jsonPath("$.institutes[0].collections").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }
}