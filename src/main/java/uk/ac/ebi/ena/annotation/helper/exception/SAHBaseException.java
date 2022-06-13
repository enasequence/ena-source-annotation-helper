package uk.ac.ebi.ena.annotation.helper.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class SAHBaseException extends RuntimeException {

    private HttpStatus httpStatus;
    private int errorCode;
    private int refId;

    public SAHBaseException(String message, HttpStatus httpStatus, int errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public SAHBaseException(String message, HttpStatus httpStatus, int errorCode, Throwable exp) {
        super(message, exp);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public SAHBaseException(String message, HttpStatus httpStatus, int errorCode, int refId, Throwable exp) {
        super(message, exp);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.refId = refId;
    }

}
