package com.library.libraries.service;

import com.library.libraries.dto.AuthorDto;
import com.library.libraries.dto.AuthorOneDto;
import com.library.libraries.service.util.TPage;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorService {

    public AuthorDto save(AuthorDto authorDto);
    public List<AuthorDto> getAll() throws NotFoundException;
    public TPage<AuthorDto> getAllPageable(Pageable pageable) throws NotFoundException;
    public List<AuthorDto> findAllByName(String name) throws NotFoundException ;
  //  public AuthorUpdateDto update(Long id, @Valid AuthorUpdateDto authorUpdateDto) throws NotFoundException;
    public AuthorOneDto getOne(Long id) throws NotFoundException;
    public void delete(Long id) throws NotFoundException ;
}
