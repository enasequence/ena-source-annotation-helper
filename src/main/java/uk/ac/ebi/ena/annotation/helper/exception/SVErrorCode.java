package uk.ac.ebi.ena.annotation.helper.exception;

public interface SVErrorCode {

    int ServerProcessingError = 3000;
    int BadRequestData = 3001;
    int BusinessObjectNotFound = 3002;
    int FieldValidationError = 3003;
    int RecordNotFoundError = 3004;
    int NoMatchingInstituteError = 3005;
    int NoMatchingCollectionError = 3006;
    int ValidationFailedError = 3007;
    int InvalidFormatProvidedError = 3008;
    int TooManyMatchesError = 3009;
    int InstituteMissingError = 3010;
    int SpecimenIdMissingError = 3011;
    int NoMatchError = 3012;
    int QualifierTypeInvalidError = 3015;

    String ServerProcessingErrorMessage = "Unable to process your request, please contact system administrator";
    String QualifierTypeInvalidMessage = "QualifierType values should be from list of ";
    String FieldValidationFailedMessage = "One or more field(s) failed validation";
    String RecordNotFoundMessage = "Requested record not found";
    String NoMatchingInstituteMessage = "No matching institute found";
    String NoMatchingCollectionMessage = "No matching collection found";
    String ValidationFailedMessage = "Failed to validate institution id / collection id. Please verify and try again";
    String InvalidFormatProvidedMessage = "Invalid Specimen Voucher Format Provided. Please provide input " +
            "in the format - [<Institution Unique Name>:[<collection-code>:]]<specimen_id>";
    String TooManyMatchesMessage = "Too many similar matches found. Please verify and try again";
    String MultipleMatchesFoundMessage = "Multiple similar matches found. Please select the appropriate one or retry the search";
    String InstituteMissingMessage = "Institute Unique Code is mandatory";
    String SpecimenIdMissingMessage = "Specimen Id is mandatory";
    String NoMatchMessage = "No Matching Institute/Collection found";
    String ValidInputSizeMessage = "for search, accepts minimum 3 chars and maximum 20 chars";
    String ValidInstituteUniqueNameRequiredMessage = "a valid institute unique name is required to search";
    String InstituteNotValidInputMessage = "For institute name/code,please provide at least 3 chars to search and max 20 chars";
    String CollectionNotValidInputMessage = "For collection name/code,please provide at least 3 chars to search and max 20 chars";

}
