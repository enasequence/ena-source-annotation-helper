package uk.ac.ebi.ena.annotation.helper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.exception.SVErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionResponseDto extends ResponseDto {

    List<Collection> collections;

    public CollectionResponseDto(List<Collection> collections) {
        super();
        this.collections = collections;
    }

    public CollectionResponseDto(String message, boolean success, LocalDateTime timestamp,
                                 List<SVErrorResponse> errors, List<Collection> collections) {
        super(message, success, timestamp, errors);
        this.collections = collections;
    }
}
