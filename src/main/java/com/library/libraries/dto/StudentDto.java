package com.library.libraries.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Data
public class StudentDto {

    private Long id;

    @NotNull
    private String registration;

    @NotNull
    private String fullname;

    private String university;

    private String department;

    @NotNull
    private String email;

    @NotNull
    private String phone;

    private String course;

    private String address;

    private List<BookOneDto> books;
}
