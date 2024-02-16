package com.library.libraries.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegistrationRequestDto {

    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
}
