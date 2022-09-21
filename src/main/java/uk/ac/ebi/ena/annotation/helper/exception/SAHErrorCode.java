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

package uk.ac.ebi.ena.annotation.helper.exception;

import java.util.HashMap;
import java.util.Map;

public class SAHErrorCode {

    public static final int ServerProcessingError = 3000;
    public static final int BadRequestData = 3001;
    public static final int FieldValidationError = 3003;
    public static final int RecordNotFoundError = 3004;
    public static final int NoMatchingInstituteError = 3005;
    public static final int NoMatchingCollectionError = 3006;
    public static final int InvalidFormatProvidedError = 3008;
    public static final int TooManyMatchesError = 3009;
    public static final int InstituteMissingError = 3010;
    public static final int IdentifierMissingError = 3011;
    public static final int NoMatchError = 3012;
    public static final int QualifierTypeInvalidError = 3015;
    public static final int GenericInvalidInputError = 3016;

    public static final String ServerProcessingMessage = "Server Processing Error";
    public static final String BadRequestDataMessage = "Bad Request Data";
    public static final String FieldValidationMessage = "Field Validation Error";
    public static final String RecordNotFoundMessage = "Requested record not found";
    public static final String NoMatchingInstituteMessage = "No matching institution found";
    public static final String NoMatchingCollectionMessage = "No matching collection found";
    public static final String InvalidFormatProvidedMessage = "Invalid Attribute Value Format Provided. Please provide input " +
            "in the format - [<Institution Unique Name>:[<collection-code>:]]<identifier>";
    public static final String TooManyMatchesMessage = "Too many similar matches found. Please verify and try again";
    public static final String MultipleMatchesFoundMessage = "Multiple similar matches found. Please select the appropriate one or retry the search";
    public static final String InstituteMissingMessage = "Institution Unique Code is mandatory";
    public static final String IdentifierMissingMessage = "Identifier is mandatory";
    public static final String NoMatchMessage = "No Matching Institution/Collection found";
    public static final String ValidInputSizeMessage = "for search, accepts minimum 1 char and maximum 100 chars";
    public static final String ValidInstituteUniqueNameRequiredMessage = "a valid institution unique name is required to search";
    public static final String ProvideValidInstituteUniqueName = "a valid institution unique name is required";
    public static final String ProvideValidCollectionCode = "a valid collection code is required";
    public static final String ProvideValidId = "an identifier is required";
    public static final String ProvideQualifierString = "qualifier string is required";
    public static final String InstituteNotValidInputMessage = "For institution name/code, please provide at least 1 char to search and max 100 chars";
    public static final String CollectionNotValidInputMessage = "For collection name/code, please provide at least 1 char to search and max 100 chars";
    public static final String QualifierTypeInvalidMessage = "QualifierType values should be from list of ";
    public static final String QualifierTypeInvalidCompleteMessage = "QualifierType values should be from list of {specimen_voucher, bio_material, culture_collection}";
    public static final String GenericInputInvalidMessage = "Invalid Input";

    public static Map<Integer, String> errorCodesMap = new HashMap<>();

    static {
        errorCodesMap.put(ServerProcessingError, ServerProcessingMessage);
        errorCodesMap.put(BadRequestData, BadRequestDataMessage);
        errorCodesMap.put(FieldValidationError, FieldValidationMessage);
        errorCodesMap.put(RecordNotFoundError, RecordNotFoundMessage);
        errorCodesMap.put(InvalidFormatProvidedError, InvalidFormatProvidedMessage);
        errorCodesMap.put(InstituteMissingError, InstituteMissingMessage);
        errorCodesMap.put(IdentifierMissingError, IdentifierMissingMessage);
        errorCodesMap.put(QualifierTypeInvalidError, QualifierTypeInvalidCompleteMessage);
        // [NOT IN USE FOR NOW]
        // errorCodesMap.put(NoMatchingInstituteError, NoMatchingInstituteMessage);
        // errorCodesMap.put(NoMatchingCollectionError, NoMatchingCollectionMessage);
        // errorCodesMap.put(TooManyMatchesError, TooManyMatchesMessage);
        // errorCodesMap.put(NoMatchError, NoMatchMessage);
    }
}