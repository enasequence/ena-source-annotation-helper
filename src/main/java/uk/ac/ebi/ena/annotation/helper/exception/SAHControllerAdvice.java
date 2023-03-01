/*
 * ******************************************************************************
 *  * Copyright 2021 EMBL-EBI, Hinxton outstation
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package uk.ac.ebi.ena.annotation.helper.exception;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.ac.ebi.ena.annotation.helper.dto.ResponseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.exception.SAHErrorCode.FieldValidationError;
import static uk.ac.ebi.ena.annotation.helper.exception.SAHErrorCode.QualifierTypeInvalidError;


@RestControllerAdvice
@Slf4j
public class SAHControllerAdvice {

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
     * Method handler for ElasticsearchException
     *
     * @param ex
     * @return ResponseEntity<ResponseDto>
     */
    @ExceptionHandler(ElasticsearchException.class)
    public ResponseEntity<ResponseDto> handleElasticsearchException(ElasticsearchException ex) {
        String customErrorMessage = SAHErrorCode.GenericInputInvalidMessage;
        // custom handling of the exception for non-parseable query
        if (ex instanceof ElasticsearchException && !isEmpty(ex.response())) {
            //fetch the response string
            String errorResponse = ex.response().toString();
            if (errorResponse.contains("Failed to parse query")) {
                String err0 = errorResponse.substring(errorResponse.indexOf("Failed to parse query"), errorResponse.length() - 1);
                // log the error
                log.debug(err0);
                //substring to grep exact issue
                String errM = err0.substring(0, err0.indexOf("\","));
                customErrorMessage = customErrorMessage + ": " + errM;
            }
        }
        ErrorResponse errorItem = ErrorResponse.builder().message(customErrorMessage)
                .code(SAHErrorCode.GenericInvalidInputError).build();
        ResponseDto responseDto = new ResponseDto();
        responseDto.setErrors(Collections.singletonList(errorItem));
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
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Method handler for BadRequestException
     *
     * @param ex
     * @return ResponseEntity<ResponseDto>
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDto> handleBadRequestException(BadRequestException ex) {
        ErrorResponse errorItem = ErrorResponse.builder().message(ex.getMessage()).code(ex.getErrorCode()).build();
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
