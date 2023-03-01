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

package uk.ac.ebi.ena.sah.biocollections.importer.exception;

public class BioCollectionsErrorCode {

    public static final int ServerProcessingError = 2000;
    public static final int PersistenceError = 2001;
    public static final int InstitutionDataReadError = 2002;
    public static final int CollectionDataReadError = 2003;
    public static final int IndexAliasMoveError = 2004;
    public static final int IndexCleanupError = 2005;


    public static final String ServerProcessingMessage = "Server Processing Error.";
    public static final String PersistenceErrorMessage = "Error occurred while Persisting Data for Bio-collections.";
    public static final String InstitutionDataReadErrorMessage = "Error while reading the Institutions Data from the FTP.";
    public static final String CollectionDataReadErrorMessage = "Error while reading the Collections Data from the FTP.";
    public static final String IndexAliasMoveErrorMessage = "Failed to move index alias.";
    public static final String IndexCleanupErrorMessage = "Error occurred while cleaning up old indexes.";

}
