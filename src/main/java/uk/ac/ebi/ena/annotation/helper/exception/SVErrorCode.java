package uk.ac.ebi.ena.annotation.helper.exception;

public interface SVErrorCode {

    int ServerProcessingError = 2000;
    int BadRequestData = 2001;
    int BusinessObjectNotFound = 2002;
    int FieldValidationError = 2003;
    int RecordNotFoundError = 2004;
    int NoDataIdentifierError = 2005;
    int ValidationFailedError = 2006;
    int TooManyMatchesError = 2007;

    String ServerProcessingErrorMessage = "Unable to process your request, please contact system administrator";
    String FieldValidationFailedMessage = "One or more field(s) failed validation";
    String RecordNotFoundMessage = "Requested record not found";
    String NoDataIdentifierMessage = "No data identifier specified";
    String ValidationFailedMessage = "Failed to validate institution id / collection id. Please verify and try again.";
    String TooManyMatchesMessage = "Too many similar matches found. Please verify and try again.";

}
