package com.library.libraries.dto;

import com.library.libraries.model.Status;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@Data
public class BookOneDto {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String isbn;

    private Integer publishYear;

    private Long studentId;

    private Status status;

    @NotNull
    private Long authorId;

    private AuthorDtoForOneEntity author;

    private StudentDtoForOneEntity student;

}
