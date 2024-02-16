package com.library.libraries.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@Data
public class AuthorUpdateDto {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String lastname;

    private String about;

    private String email;

}