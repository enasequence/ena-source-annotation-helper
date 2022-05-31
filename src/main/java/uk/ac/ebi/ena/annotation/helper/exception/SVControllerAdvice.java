package uk.ac.ebi.ena.annotation.helper.exception;

import uk.ac.ebi.ena.annotation.helper.dto.ResponseDto;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Collections;


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
        SVErrorResponse errorItem = SVErrorResponse.builder().message(ex.getMessage()).build();
        ResponseDto responseDto = ResponseDto.builder()
                .errors(Collections.singletonList(errorItem))
                .success(false)
                .timestamp(LocalDateTime.now()).build();

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
        SVErrorResponse errorItem = SVErrorResponse.builder().message(ex.getMessage()).build();
        ResponseDto responseDto = ResponseDto.builder()
                .errors(Collections.singletonList(errorItem))
                .success(false)
                .timestamp(LocalDateTime.now()).build();
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
        SVErrorResponse errorItem = SVErrorResponse.builder().field(ex.getPath().get(0).getFieldName())
                .fieldValue(ex.getValue().toString())
                .message(ex.getTargetType().getSimpleName()).build();
        ResponseDto responseDto = ResponseDto.builder()
                .errors(Collections.singletonList(errorItem))
                .success(false)
                .timestamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }


}
