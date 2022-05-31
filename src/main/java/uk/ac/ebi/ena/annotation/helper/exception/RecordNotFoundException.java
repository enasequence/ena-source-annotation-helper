package uk.ac.ebi.ena.annotation.helper.exception;

import org.springframework.http.HttpStatus;

public class RecordNotFoundException extends SVBaseException {

    public RecordNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST, SVErrorCode.RecordNotFoundError);
    }

    public RecordNotFoundException(String message, Throwable exp) {
        super(message, HttpStatus.BAD_REQUEST, SVErrorCode.RecordNotFoundError, exp);
    }
}
