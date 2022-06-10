package uk.ac.ebi.ena.annotation.helper.exception;

import org.springframework.http.HttpStatus;

public class RecordNotFoundException extends SAHBaseException {

    public RecordNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST, SAHErrorCode.RecordNotFoundError);
    }

    public RecordNotFoundException(String message, Throwable exp) {
        super(message, HttpStatus.BAD_REQUEST, SAHErrorCode.RecordNotFoundError, exp);
    }
}
