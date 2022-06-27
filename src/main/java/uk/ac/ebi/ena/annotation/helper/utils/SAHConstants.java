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

package uk.ac.ebi.ena.annotation.helper.utils;

public interface SAHConstants {

    int EXACT_MATCH = 0;
    int MULTI_NEAR_MATCH = 1;
    int TOO_MANY_MATCH = 2;
    int NO_MATCH = -1;

    String MATCH_LEVEL_EXACT = "Exact";
    String MATCH_LEVEL_PARTIAL = "Partial";
    String MATCH_LEVEL_TOO_MANY = "Too Many Partial Matches";
    String MATCH_LEVEL_NONE = "None";

    // ElasticSearch KeepAlive Configuration -- default is 5 minutes
    int ES_KEEP_ALIVE_FREQUENCY = 300000; //300000 millis
    int ES_INITIAL_DELAY = 300000; //300000 millis

}
