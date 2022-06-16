/*
 * ******************************************************************************
 *  * Copyright 2021 EMBL-EBI, Hinxton outstation
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

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