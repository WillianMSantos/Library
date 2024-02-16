package com.library.libraries.service;

import com.library.libraries.dto.StudentDto;
import com.library.libraries.dto.StudentPatchDto;
import com.library.libraries.service.util.TPage;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;

public interface StudentService {

    public StudentDto save(@Valid StudentDto studentDto);
    public TPage<StudentDto> getAllPageable(Pageable pageable);
    public List<StudentDto> getAll();
    public StudentDto findById(Long id) throws NotFoundException;
    public StudentDto update(Long id, @Valid StudentDto studentDto) throws NotFoundException;
    public StudentDto getBookForStudent(@Valid StudentPatchDto studentPatchDto) throws NotFoundException;
    public Boolean delete(Long id) throws NotFoundException;
    public StudentDto leaveBookForStudent(@Valid StudentPatchDto studentPatchDto) throws NotFoundException;
}