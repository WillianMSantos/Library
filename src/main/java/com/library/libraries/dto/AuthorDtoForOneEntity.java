package com.library.libraries.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@Data
@NoArgsConstructor
public class AuthorDtoForOneEntity {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String lastname;

    private String email;

    private String about;
}
