package com.library.libraries.dto;

import javax.validation.constraints.NotNull;

public class StudentDtoForOneEntity {

    private Long id;

    private String registration;

    @NotNull
    private String fullname;

    private String university;

    private String course;

    @NotNull
    private String email;

    @NotNull
    private String phone;

    private String address;
}
