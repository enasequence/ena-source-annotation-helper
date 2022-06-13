package uk.ac.ebi.ena.annotation.helper.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends SAHBaseException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST, SAHErrorCode.BadRequestData);
    }

    public BadRequestException(String message, Throwable exp) {
        super(message, HttpStatus.BAD_REQUEST, SAHErrorCode.BadRequestData, exp);
    }
}
