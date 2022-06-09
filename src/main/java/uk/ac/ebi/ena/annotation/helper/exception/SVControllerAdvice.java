package uk.ac.ebi.ena.annotation.helper.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.ac.ebi.ena.annotation.helper.dto.ResponseDto;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static uk.ac.ebi.ena.annotation.helper.exception.SVErrorCode.FieldValidationError;
import static uk.ac.ebi.ena.annotation.helper.exception.SVErrorCode.QualifierTypeInvalidError;


@RestControllerAdvice
public class SVControllerAdvice {

    /**
     * Method handler for MethodArgumentNotValidException
     *
     * @param ex
     * @return ResponseEntity<ResponseDto>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ErrorResponse errorItem = ErrorResponse.builder().message(ex.getMessage()).build();
        ResponseDto responseDto = new ResponseDto();
        responseDto.setErrors(Collections.singletonList(errorItem));
        responseDto.setSuccess(false);
        responseDto.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDto> handleConstraintViolationException(ConstraintViolationException ex) {

        final List<ErrorResponse> errors = new ArrayList();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(new ErrorResponse(QualifierTypeInvalidError, violation.getMessage()));
        }
        ResponseDto responseDto = new ResponseDto();
        responseDto.setErrors(errors);
        responseDto.setSuccess(false);
        responseDto.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }


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
        responseDto.setErrors(Collections.singletonList(errorItem));
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
        responseDto.setErrors(Collections.singletonList(errorItem));
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
        responseDto.setErrors(Collections.singletonList(errorItem));
        responseDto.setSuccess(false);
        responseDto.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }


}
