package uk.ac.ebi.ena.annotation.helper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.exception.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionResponseDto extends ResponseDto {

    List<Collection> collections;

    public CollectionResponseDto(List<Collection> collections) {
        super();
        this.collections = collections;
    }

    public CollectionResponseDto(List<Collection> collections, boolean success, LocalDateTime timestamp) {
        super(success, timestamp);
        this.collections = collections;
    }

    public CollectionResponseDto(List<Collection> collections, boolean success, LocalDateTime timestamp,
                                 ErrorResponse error) {
        super(success, timestamp, error);
        this.collections = collections;
    }
}
