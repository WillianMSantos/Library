package com.library.libraries.dto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Data
public class UserDto {

    private String username;
    private String firstname;
    private String lastname;
    @NotNull
    private String email;
}