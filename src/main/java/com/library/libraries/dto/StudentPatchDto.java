package com.library.libraries.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class StudentPatchDto {

    private Long id;
    private Long studentId;
    private Long bookId;

}
