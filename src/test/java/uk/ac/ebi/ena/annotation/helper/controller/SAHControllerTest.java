package uk.ac.ebi.ena.annotation.helper.controller;

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
class SAHControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String META_SEARCH_BASE_ENDPOINT = "/ena/source-annotation-helper/institute/";
    private static final String META_SEARCH_BASE_ENDPOINT2 = "/ena/sah/institute/";
    private static final String VALIDATE_BASE_ENDPOINT = "/ena/source-annotation-helper/validate";
    private static final String CONSTRUCT_BASE_ENDPOINT = "/ena/source-annotation-helper/construct";

    @BeforeAll
    public static void before() {

    }

    @AfterAll
    public static void tearDown() {

    }

    @Test
    void findByInstituteValue() throws Exception {
        String params = "ANS";
        MvcResult result = this.mockMvc.perform(get(META_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }

    @Test
    void findByInstituteValueMappingSAH() throws Exception {
        String params = "ANS";
        MvcResult result = this.mockMvc.perform(get(META_SEARCH_BASE_ENDPOINT2 + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }

    @Test
    void findByAllCollByInstituteUniqueName() throws Exception {
        //todo improvement -- handle this scenario
        String params = "CIS<USA-CA>";
        MvcResult result = this.mockMvc.perform(get(META_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                //.andExpect(jsonPath("$.institutes.*", hasSize(1)))
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }

    @Test
    void findByInstUniqueNameAndCollCode() throws Exception {
        String params = "MSNT<ITA-Torino>/collection/FAZC";
        MvcResult result = this.mockMvc.perform(get(META_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                .andExpect(jsonPath("$.institutes[0].collections").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }

    @Test
    void validate() throws Exception {
        String params = "?value=MSNT<ITA-Torino>:FAZC:123456";
        MvcResult result = this.mockMvc.perform(get(VALIDATE_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }

    @Test
    void construct() throws Exception {
        String params = "?institute=MSNT<ITA-Torino>&collection=FAZC&id=123456";
        MvcResult result = this.mockMvc.perform(get(CONSTRUCT_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }

    @Test
    public void getInstFuzzySearchStr() throws Exception {
        String params = "ANS";
        MvcResult result = this.mockMvc.perform(get(META_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }


    @Test
    public void getInstFuzzySearchStrDoesNotExist() throws Exception {
        String params = "QQQ";
        MvcResult result = this.mockMvc.perform(get(META_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(equalTo(false)))
                .andExpect(jsonPath("$.errors[0].code").value(equalTo(3004)))
                .andReturn();
    }

    @Test
    public void getInstFuzzySearchStrWithQT() throws Exception {
        String params = "ANS?qualifier_type=bio_material";
        MvcResult result = this.mockMvc.perform(get(META_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();

    }

    @Test
    public void getInstFuzzySearchStrWithQTArray() throws Exception {
        String params = "ANS?qualifier_type=specimen_voucher,bio_material";
        MvcResult result = this.mockMvc.perform(get(META_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();

    }

    @Test
    public void getInstFuzzySearchStrWithQTArrayEmpty() throws Exception {
        String params = "ANS?qualifier_type=";
        MvcResult result = this.mockMvc.perform(get(META_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();

    }

    @Test
    public void getInstFuzzySearchStrWithBadQT() throws Exception {
        String params = "ANS?qualifier_type=abc";
        MvcResult result = this.mockMvc.perform(get(META_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(equalTo(false)))
                .andExpect(jsonPath("$.errors[0].code").value(equalTo(3015)))
                .andReturn();

    }

    @Test
    public void getCollectionByInstCode() throws Exception {
        String params = "CMN/collection";
        MvcResult result = this.mockMvc.perform(get(META_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                .andExpect(jsonPath("$.institutes[0].collections").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }


    @Test
    public void getCollectionByInstCodeWithQT() throws Exception {
        String params = "CMN/collection?qualifier_type=specimen_voucher";
        MvcResult result = this.mockMvc.perform(get(META_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutes").exists())
                .andExpect(jsonPath("$.institutes[0].collections").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();

    }

    @Test
    public void getCollectionByInstCodeWithQTNoMatch() throws Exception {
        String params = "CMN/collection?qualifier_type=bio_material";
        MvcResult result = this.mockMvc.perform(get(META_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(equalTo(false)))
                .andExpect(jsonPath("$.errors[0].code").value(equalTo(3006)))
                .andReturn();

    }

    @Test
    public void getCollectionInstituteDoesNotExist() throws Exception {
        String params = "QQQ/collection";
        MvcResult result = this.mockMvc.perform(get(META_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(equalTo(false)))
                .andExpect(jsonPath("$.errors[0].code").value(equalTo(3005)))
                .andReturn();
    }

    @Test
    public void getCollectionInstituteExistNoColl() throws Exception {
        String params = "QQQ/collection";
        MvcResult result = this.mockMvc.perform(get(META_SEARCH_BASE_ENDPOINT + params))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(equalTo(false)))
                .andExpect(jsonPath("$.errors[0].code").value(equalTo(3005)))
                .andReturn();
    }

    @Test
    public void getCollectionByInstCodeAndCollCode() throws Exception {

    }

    @Test
    void validateExactMatchInstOnly() throws Exception {
        String params = "?value=MSNT<ITA-Torino>:123456";
        MvcResult result = this.mockMvc.perform(get(VALIDATE_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }

    @Test
    void validateExactMatchInstWithQT() throws Exception {
        String params = "?value=MSNT:123456&qualifier_type=specimen_voucher";
        MvcResult result = this.mockMvc.perform(get(VALIDATE_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }

    @Test
    void validateSimilarMatchInstOnly() throws Exception {
        String params = "?value=NYX:123456";
        MvcResult result = this.mockMvc.perform(get(VALIDATE_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }

    @Test
    void validateSimilarMatchInstWithCollWithQT() throws Exception {
        String params = "?value=HSUV:Bird:123456&qualifier_type=specimen_voucher";
        MvcResult result = this.mockMvc.perform(get(VALIDATE_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }

    @Test
    void constructExactMatchInstOnly() throws Exception {
        String params = "?institute=MSNT<ITA-Torino>&id=123456";
        MvcResult result = this.mockMvc.perform(get(CONSTRUCT_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }

    @Test
    void constructExactMatchInstWithQT() throws Exception {
        String params = "?institute=MSNT<ITA-Torino>&id=123456&qualifier_type=specimen_voucher";
        MvcResult result = this.mockMvc.perform(get(CONSTRUCT_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }

    @Test
    void constructSimilarMatchInstOnly() throws Exception {
        String params = "?institute=NYX&id=123456&qualifier_type=specimen_voucher";
        MvcResult result = this.mockMvc.perform(get(CONSTRUCT_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }

    @Test
    void constructSimilarMatchInstWithCollWithQT() throws Exception {
        String params = "?institute=HSUV&collection=Bird&id=123456&qualifier_type=specimen_voucher,bio_material";
        MvcResult result = this.mockMvc.perform(get(CONSTRUCT_BASE_ENDPOINT + params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches").exists())
                .andExpect(jsonPath("$.success").value(equalTo(true)))
                .andReturn();
    }

    @Test
    void constructSimilarMatchInstWithCollWithQTNoMatch() throws Exception {
        String params = "?institute=HSUV&collection=Bird&id=123456&qualifier_type=bio_material";
        MvcResult result = this.mockMvc.perform(get(CONSTRUCT_BASE_ENDPOINT + params))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(equalTo(false)))
                .andExpect(jsonPath("$.error.code").value(equalTo(3012)))
                .andReturn();
    }

}