package com.library.libraries.service;

import com.library.libraries.dto.AuthorDto;
import com.library.libraries.dto.AuthorOneDto;
import com.library.libraries.dto.AuthorUpdateDto;
import com.library.libraries.service.util.TPage;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;

public interface AuthorService {

    public AuthorDto save(AuthorDto authorDto);
    public List<AuthorDto> getAll();
    public TPage<AuthorDto> getAllPageable(Pageable pageable);
    public List<AuthorDto> findAllByName(String name);
    public AuthorUpdateDto update(Long id, @Valid AuthorUpdateDto authorUpdateDto);
    public AuthorOneDto getOne(Long id);
    public void delete(Long id) ;
}
