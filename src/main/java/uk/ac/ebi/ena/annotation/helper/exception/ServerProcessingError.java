package uk.ac.ebi.ena.annotation.helper.exception;

import org.springframework.http.HttpStatus;

public class ServerProcessingError extends SVBaseException {

    public ServerProcessingError(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, SVErrorCode.ServerProcessingError);
    }

    public ServerProcessingError(String message, Throwable exp) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, SVErrorCode.ServerProcessingError, exp);
    }
}
