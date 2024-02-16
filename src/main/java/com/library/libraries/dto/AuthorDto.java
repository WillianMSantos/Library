package com.library.libraries.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String lastname;

    private String about;

    private String email;

    private List<BookDtoForOneEntity> books;
}
