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
public class StudentOneDto {

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

    private Long bookId;

    private List<BookDtoForOneEntity> books;
}
