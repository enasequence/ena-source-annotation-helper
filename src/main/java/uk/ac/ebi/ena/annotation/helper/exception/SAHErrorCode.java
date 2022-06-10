package uk.ac.ebi.ena.annotation.helper.exception;

public interface SAHErrorCode {

    int ServerProcessingError = 3000;
    int BadRequestData = 3001;
    int FieldValidationError = 3003;
    int RecordNotFoundError = 3004;
    int NoMatchingInstituteError = 3005;
    int NoMatchingCollectionError = 3006;
    int InvalidFormatProvidedError = 3008;
    int TooManyMatchesError = 3009;
    int InstituteMissingError = 3010;
    int IdentifierMissingError = 3011;
    int NoMatchError = 3012;
    int QualifierTypeInvalidError = 3015;

    String ServerProcessingErrorMessage = "Unable to process your request, please contact system administrator";
    String QualifierTypeInvalidMessage = "QualifierType values should be from list of ";
    String RecordNotFoundMessage = "Requested record not found";
    String NoMatchingInstituteMessage = "No matching institute found";
    String NoMatchingCollectionMessage = "No matching collection found";
    String InvalidFormatProvidedMessage = "Invalid Qualifier Value Format Provided. Please provide input " +
            "in the format - [<Institution Unique Name>:[<collection-code>:]]<identifier>";
    String TooManyMatchesMessage = "Too many similar matches found. Please verify and try again";
    String MultipleMatchesFoundMessage = "Multiple similar matches found. Please select the appropriate one or retry the search";
    String InstituteMissingMessage = "Institute Unique Code is mandatory";
    String IdentifierMissingMessage = "Identifier is mandatory";
    String NoMatchMessage = "No Matching Institute/Collection found";
    String ValidInputSizeMessage = "for search, accepts minimum 3 chars and maximum 100 chars";
    String ValidInstituteUniqueNameRequiredMessage = "a valid institute unique name is required to search";
    String ProvideValidInstituteUniqueName = "a valid institute unique name is required";
    String ProvideValidCollectionCode = "a valid collection code is required";
    String ProvideValidId = "an identifier is required";
    String ProvideQualifierString = "qualifier string is required";
    String InstituteNotValidInputMessage = "For institute name/code, please provide at least 3 chars to search and max 100 chars";
    String CollectionNotValidInputMessage = "For collection name/code, please provide at least 3 chars to search and max 100 chars";

}
