package com.library.libraries.dto;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class UserPasswordDto {

    private String username;
    private String password;
    private String newpassword;
}