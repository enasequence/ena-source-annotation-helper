package uk.ac.ebi.ena.annotation.helper.exception;

public interface SVErrorCode {

    int ServerProcessingError = 2000;
    int BadRequestData = 2001;
    int BusinessObjectNotFound = 2002;
    int FieldValidationError = 2003;
    int RecordNotFoundError = 2004;
    int NoDataIdentifierError = 2005;

    String ServerProcessingErrorMessage = "Unable to process your request, please contact system administrator";
    String FieldValidationFailedMessage = "One or more field(s) failed validation";
    String RecordNotFoundMessage = "Requested record not found";
    String NoDataIdentifierMessage = "No data identifier specified";

}
