package com.library.libraries.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class StudentDto {

    private Long id;

    private String registration;

    @NotNull
    private String fullname;

    private String university;

    private String department;

    @NotNull
    private String email;

    @NotNull
    private String phone;

    private String address;

    private List<BookOneDto> books;
}
