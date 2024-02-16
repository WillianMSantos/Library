package com.library.libraries.service;

import com.library.libraries.dto.BookDto;
import com.library.libraries.dto.BookOneDto;
import com.library.libraries.dto.BookUpdateDto;
import com.library.libraries.exception.AuthorNotFoundException;
import com.library.libraries.exception.BookAlreadyExistsException;
import com.library.libraries.exception.BookDeletionException;
import com.library.libraries.exception.BookNotFoundException;
import com.library.libraries.service.util.TPage;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    BookOneDto save(BookOneDto bookOneDto) throws BookAlreadyExistsException, AuthorNotFoundException;
    List<BookDto> getAll();
    TPage<BookDto> getAllPageable(Pageable pageable) throws NotFoundException;
    BookUpdateDto update(Long id, BookUpdateDto bookUpdateDto) throws NotFoundException, IllegalStateException;
    BookOneDto getOne(Long id) throws NotFoundException;
    void delete(Long id) throws BookNotFoundException, BookDeletionException;
    List<BookDto> searchBooksByTitle(String title);
    List<BookDto> searchBooks(BookDto bookDto);
}
