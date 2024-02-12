package com.library.libraries.dto;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
public class AuthorOneDto {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String lastname;

    private String about;

    private String email;

    private List<BookOneDto> books;

}
