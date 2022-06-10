package uk.ac.ebi.ena.annotation.helper.exception;

import org.springframework.http.HttpStatus;

public class ServerProcessingError extends SAHBaseException {

    public ServerProcessingError(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, SAHErrorCode.ServerProcessingError);
    }

    public ServerProcessingError(String message, Throwable exp) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, SAHErrorCode.ServerProcessingError, exp);
    }
}
