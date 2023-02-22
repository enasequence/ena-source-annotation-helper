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

package uk.ac.ebi.ena.sah.biocollections.importer.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppConstants {

    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuuMMdd_HHmm");
    static final String CURRENT_DATE = dtf.format(LocalDateTime.now());
    public static final String INST_MAPPING_JSON = "institution.json";
    public static final String COLL_MAPPING_JSON = "collection.json";
    public static final String INDEX_COLLECTION_ALIAS = "collection";
    public static final String INDEX_INSTITUTION_ALIAS= "institution";
    public static final String INST_INDEX_PREFIX = "institution_";
    public static final String COLL_INDEX_PREFIX = "collection_";
    public static final String NEW_INST_INDEX_NAME = INST_INDEX_PREFIX + CURRENT_DATE;
    public static final String NEW_COLL_INDEX_NAME = COLL_INDEX_PREFIX + CURRENT_DATE;

    public static final String PERCENTAGE_FORMAT = "#0.00";

    public static final int INST_ID_POS = 1;
    public static final int INST_CODE_POS = 2;
    public static final int UNIQUE_NAME_POS = 3;
    public static final int SYNONYMS_POS = 4;
    public static final int INST_NAME_POS = 5;
    public static final int COUNTRY_POS = 6;
    public static final int ADDRESS_POS = 7;
    public static final int PHONE_POS = 8;
    public static final int FAX_POS = 9;
    public static final int HOME_URL_POS = 10;
    public static final int INS_COMMENTS_POS = 11;

    public static final int COLL_ID_POS = 1;
    public static final int COLL_INST_ID_POS = 2;
    public static final int COLL_NAME_POS = 3;
    public static final int COLL_CODE_POS = 4;
    public static final int COLL_SIZE_POS = 5;
    public static final int COLLECTION_TYPE_POS =6;
    public static final int COLL_URL_RULE_POS = 7;
    public static final int COLL_URL_POS = 8;
    public static final int QUALIFIER_TYPE_POS = 9;
    public static final int COLL_COMMENTS_POS = 10;

}
