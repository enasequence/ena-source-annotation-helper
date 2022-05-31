package uk.ac.ebi.ena.annotation.helper.service;

import uk.ac.ebi.ena.annotation.helper.model.Institute;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
//        esTemplate.indexOps(IndexCoordinates.of("institutes")).delete();
//        esTemplate.indexOps(IndexCoordinates.of("institutes")).create();
//        esTemplate.indexOps(IndexCoordinates.of("institutes")).putMapping(Institute.class);
//        esTemplate.indexOps(IndexCoordinates.of("institutes")).refresh();
    }

    @Test
    public void testSave() {

        Institute institute = new Institute("1", 1, "AEI", "American Entomological Institute", "USA",
                "3005 SW 56th Avenue, Gainesville, Florida 32608-5047", "museum",
                "specimen_voucher", "AEI", "homeURL", "urlRule");
        Institute testInstitute = SVService.save(institute);

        assertNotNull(testInstitute.getId());
        assertEquals(testInstitute.getInstId(), institute.getInstId());
        assertEquals(testInstitute.getInstName(), institute.getInstName());
        assertEquals(testInstitute.getUniqueName(), institute.getUniqueName());

    }

//    @Test
//    public void testFindOne() {
//
//        Institute institute = new Institute("1", 1, "AEI", "American Entomological Institute", "USA",
//                "3005 SW 56th Avenue, Gainesville, Florida 32608-5047", "museum",
//                "specimen_voucher", "AEI", "homeURL", "urlRule");
//        instituteService.save(institute);
//
//        Institute testInstitute = instituteService.findOne(institute.getId());
//
//        assertNotNull(testInstitute.getId());
//        assertEquals(testInstitute.getInstId(), institute.getInstId());
//        assertEquals(testInstitute.getInstName(), institute.getInstName());
//        assertEquals(testInstitute.getUniqueName(), institute.getUniqueName());
//
//    }

    @Test
    public void testFindByTitle() {

        Institute institute = new Institute("1", 1, "AEI", "American Entomological Institute", "USA",
                "3005 SW 56th Avenue, Gainesville, Florida 32608-5047", "museum",
                "specimen_voucher", "AEI", "homeURL", "urlRule");
        SVService.save(institute);

        List<Institute> byTitle = SVService.findByInstName(institute.getInstName());
        assertThat(byTitle.size(), is(1));
    }

    @Test
    public void testFindByAuthor() {

        List<Institute> instituteList = new ArrayList<>();

        instituteList.add(new Institute("1", 1, "AEI", "American Entomological Institute", "USA",
                "3005 SW 56th Avenue, Gainesville, Florida 32608-5047", "museum",
                "specimen_voucher", "AEI", "homeURL", "urlRule"));
        instituteList.add(new Institute("2", 2, "AMG", "Albany Museum", "South Africa",
                "Albany Museum, Somerset Street, Grahamstown 6139 ", "museum", "" +
                "specimen_voucher", "AMG", "homeURL", "urlRule"));
        instituteList.add(new Institute("3", 6, "ANSDU", "Academy of Natural Sciences of Drexel University",
                "USA", "1900 Benjamin Franklin Pkwy., Philadelphia, PA 19103-1195",
                "museum", "specimen_voucher", "ANSP", "homeURL", "urlRule"));
        instituteList.add(new Institute("4", 10, "CNC", "Canadian National Collection of Insects, Arachnids, and Nematodes",
                "Canada", "960 Carling Avenue, K.W. Neatby Building, Ottawa, Ontario K1A 0C6 ",
                "museum", "specimen_voucher", "CNC", "homeURL", "urlRule"));


        for (Institute institute : instituteList) {
            SVService.save(institute);
        }

//        Page<Institute> byAuthor = instituteService.findByInstCode("Rambabu Posa", PageRequest.of(0, 10));
//        assertThat(byAuthor.getTotalElements(), is(4L));
//
//        Page<Institute> byAuthor2 = instituteService.findByInstCode("Mkyong", PageRequest.of(0, 10));
//        assertThat(byAuthor2.getTotalElements(), is(1L));

    }


}