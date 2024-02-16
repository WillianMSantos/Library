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
public class BookUpdateDto {


    private Long id;
    @NotNull
    private String title;

    @NotNull
    private Long authorId;

    @NotNull
    private String isbn;

    private Status status;

    private AuthorDtoForOneEntity author;
}
