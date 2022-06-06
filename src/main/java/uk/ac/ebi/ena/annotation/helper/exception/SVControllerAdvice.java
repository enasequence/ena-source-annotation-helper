package uk.ac.ebi.ena.annotation.helper.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.ac.ebi.ena.annotation.helper.dto.ResponseDto;

import java.time.LocalDateTime;

import static uk.ac.ebi.ena.annotation.helper.exception.SVErrorCode.FieldValidationError;


@RestControllerAdvice
public class SVControllerAdvice {

    /**
     * Method handler for RecordNotFoundException
     *
     * @param ex
     * @return ResponseEntity<ResponseDto>
     */
    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ResponseDto> handleRecordNotFoundException(RecordNotFoundException ex) {
        ErrorResponse errorItem = ErrorResponse.builder().message(ex.getMessage()).build();
        ResponseDto responseDto = new ResponseDto();
        responseDto.setError(errorItem);
        responseDto.setSuccess(false);
        responseDto.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }

    /**
     * Method handler for BadRequestException
     *
     * @param ex
     * @return ResponseEntity<ResponseDto>
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDto> handleBadRequestException(BadRequestException ex) {
        ErrorResponse errorItem = ErrorResponse.builder().message(ex.getMessage()).build();
        ResponseDto responseDto = new ResponseDto();
        responseDto.setError(errorItem);
        responseDto.setSuccess(false);
        responseDto.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Method handler for InvalidFormatException
     *
     * @param ex
     * @return ResponseEntity<ResponseDto>
     */
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ResponseDto> handleInvalidFormatException(InvalidFormatException ex) {
        ErrorResponse errorItem = ErrorResponse.builder().code(FieldValidationError)
                .message(ex.getTargetType().getSimpleName()).build();
        ResponseDto responseDto = new ResponseDto();
        responseDto.setError(errorItem);
        responseDto.setSuccess(false);
        responseDto.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }


}
