package uk.ac.ebi.ena.annotation.helper.exception;


import org.springframework.http.HttpStatus;

public class BadRequestException extends SVBaseException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST, SVErrorCode.BadRequestData);
    }

    public BadRequestException(String message, Throwable exp) {
        super(message, HttpStatus.BAD_REQUEST, SVErrorCode.BadRequestData, exp);
    }
}
