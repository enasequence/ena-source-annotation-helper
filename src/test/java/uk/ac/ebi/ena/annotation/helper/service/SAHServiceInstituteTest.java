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
public class SAHServiceInstituteTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String INSTITUTE_SEARCH_BASE_ENDPOINT = "/ena/sah/institute/";

    @BeforeAll
    public static void before() {

    }

    @AfterAll
    public static void tearDown() {

    }

    @Test
    public void getInstFuzzySearchStr() throws Exception {
        String params = "ANS";
        MvcResult result = this.mockMvc.perform(get(INSTITUTE_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }

    @Test
    public void getInstByUniqueName() throws Exception {
        //todo improvement -- handle this scenario
        String params = "CIS<USA-CA>";
        MvcResult result = this.mockMvc.perform(get(INSTITUTE_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                //.andExpect(jsonPath("$.institutes.*", hasSize(1)))
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }

    @Test
    public void getInstFuzzySearchStrDoesNotExist() throws Exception {
        String params = "QQQ";
        MvcResult result = this.mockMvc.perform(get(INSTITUTE_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(equalTo(false)))
                .andExpect(jsonPath("$.errors[0].code").value(equalTo(3004)))
                .andReturn();
    }

    @Test
    public void getInstFuzzySearchStrWithQT() throws Exception {
        String params = "ANS?qualifier_type=bio_material";
        MvcResult result = this.mockMvc.perform(get(INSTITUTE_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();

    }

    @Test
    public void getInstFuzzySearchStrWithQTArray() throws Exception {
        String params = "ANS?qualifier_type=specimen_voucher,bio_material";
        MvcResult result = this.mockMvc.perform(get(INSTITUTE_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();

    }

    @Test
    public void getInstFuzzySearchStrWithQTArrayEmpty() throws Exception {
        String params = "ANS?qualifier_type=";
        MvcResult result = this.mockMvc.perform(get(INSTITUTE_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();

    }

    @Test
    public void getInstFuzzySearchStrWithBadQT() throws Exception {
        String params = "ANS?qualifier_type=abc";
        MvcResult result = this.mockMvc.perform(get(INSTITUTE_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(equalTo(false)))
                .andExpect(jsonPath("$.errors[0].code").value(equalTo(3015)))
                .andReturn();

    }


}