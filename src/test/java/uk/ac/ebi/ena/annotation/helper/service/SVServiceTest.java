package uk.ac.ebi.ena.annotation.helper.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SVServiceTest {

    @Autowired
    private SVService SVService;

    @Autowired
    private static ElasticsearchOperations esTemplate;

    @BeforeAll
    public static void before() {

    }


    @AfterAll
    public static void tearDown() {

    }

    @Test
    public void firstTest() throws Exception {
    }

}