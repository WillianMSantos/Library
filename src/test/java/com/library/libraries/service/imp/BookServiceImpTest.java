package com.library.libraries.service.imp;

import com.library.libraries.dto.BookDto;
import com.library.libraries.dto.BookOneDto;
import com.library.libraries.dto.BookUpdateDto;
import com.library.libraries.exception.BookNotFoundException;
import com.library.libraries.model.Author;
import com.library.libraries.model.Book;
import com.library.libraries.model.Status;
import com.library.libraries.repository.AuthorRepository;
import com.library.libraries.repository.BookRepository;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BookServiceImpTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookServiceImp bookService;

    private Book book;
    private BookOneDto bookOneDto;
    private BookUpdateDto bookUpdateDto;

    @BeforeEach
    void setUp() {

        book = new Book();
        book.setId(1L);
        book.setTitle("A Game of Thrones");
        book.setStatus(Status.FREE);

        Author author = new Author();
        author.setId(1L);
        author.setName("George R.R. Martin");

        book.setAuthor(author);

        bookOneDto = new BookOneDto();
        bookOneDto.setId(1L);
        bookOneDto.setTitle("A Game of Thrones");

        bookUpdateDto = new BookUpdateDto();
        bookUpdateDto.setTitle("A Clash of Kings");

        given(modelMapper.map(any(BookOneDto.class), eq(Book.class))).willReturn(book);
        given(modelMapper.map(any(Book.class), eq(BookOneDto.class))).willReturn(bookOneDto);
        given(modelMapper.map(any(BookUpdateDto.class), eq(Book.class))).willReturn(book);
        given(modelMapper.map(any(Book.class), eq(BookUpdateDto.class))).willReturn(bookUpdateDto);
    }

    @Test
    void whenGetOneBookById_thenReturnsBookOneDto() throws NotFoundException {
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        BookOneDto resultDto = bookService.getOne(1L);

        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isEqualTo(1L);
        assertThat(resultDto.getTitle()).isEqualTo("A Game of Thrones");
    }

    @Test
    void whenUpdateBook_thenReturnsUpdatedBookDto() throws NotFoundException {
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        given(bookRepository.save(any(Book.class))).willReturn(book);

        BookUpdateDto resultDto = bookService.update(1L, bookUpdateDto);

        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getTitle()).isEqualTo("A Clash of Kings");
    }

    @Test
    void whenGetOneBookById_thenThrowsBookNotFoundException() {
        given(bookRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getOne(1L));
    }
}