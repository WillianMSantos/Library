package com.library.libraries.service;

import com.library.libraries.dto.BookDto;
import com.library.libraries.dto.BookOneDto;
import com.library.libraries.exception.BookDeletionException;
import com.library.libraries.exception.BookNotFoundException;
import com.library.libraries.service.util.TPage;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    public BookOneDto save(BookOneDto bookOneDto) throws Exception;
    public List<BookDto> getAll() throws NotFoundException;
    public TPage<BookDto> getAllPageable(Pageable pageable) throws NotFoundException;
    //public BookUpdateDto update(Long id, BookUpdateDto bookUpdateDto) throws NotFoundException;
    public BookOneDto getOne(Long id) throws NotFoundException ;
    public void delete(Long id) throws BookNotFoundException, BookDeletionException;
    public List<BookDto> searchBooksByTitle(String title) throws NotFoundException;
}
