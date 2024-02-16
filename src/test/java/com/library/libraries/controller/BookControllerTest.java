package com.library.libraries.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.libraries.dto.BookDto;
import com.library.libraries.dto.BookOneDto;
import com.library.libraries.dto.BookUpdateDto;
import com.library.libraries.service.BookService;
import com.library.libraries.service.util.TPage;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @BeforeEach
    void setUp() {
        List<BookDto> bookDtoList = new ArrayList<>();
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test Book");
        bookDto.setIsbn("123456");

        bookDtoList.add(bookDto);

        given(bookService.getAll()).willReturn(bookDtoList);
    }

    @Test
    void whenGetAllBooks_thenReturnJsonArray() throws Exception {
        mockMvc.perform(get("/api/book")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Book"));
    }

    @Test
    void whenGetAllBooksByPagination_thenReturnPaginatedData() throws Exception {

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test Book");

        List<BookDto> bookDtoList = Collections.singletonList(bookDto);
        PageImpl<BookDto> bookPage = new PageImpl<>(bookDtoList);

        TPage<BookDto> tPage = new TPage<>();
        tPage.setStat(bookPage, bookDtoList);

        given(bookService.getAllPageable(any(Pageable.class))).willReturn(tPage);

        mockMvc.perform(get("/api/book/pagination")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value(bookDto.getTitle()));
    }

    @Test
    void whenGetOneAuthor_thenReturnBookDto() throws Exception {

        Long bookId = 1L;
        BookOneDto bookOneDto = new BookOneDto();
        bookOneDto.setId(bookId);
        bookOneDto.setTitle("Sample Book");

        given(bookService.getOne(bookId)).willReturn(bookOneDto);


        mockMvc.perform(get("/api/book/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookOneDto.getId()))
                .andExpect(jsonPath("$.title").value(bookOneDto.getTitle()));
    }

    @Test
    void whenGetOneAuthor_thenThrowNotFoundException() throws Exception {
        Long bookId = 1L;
        given(bookService.getOne(bookId)).willThrow(new NotFoundException("Book not found"));

        mockMvc.perform(get("/api/book/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenUpdateBook_thenReturnUpdatedBookDto() throws Exception {
        Long bookId = 1L;
        BookUpdateDto bookUpdateDto = new BookUpdateDto();
        bookUpdateDto.setTitle("Updated Title");

        given(bookService.update(eq(bookId), any(BookUpdateDto.class))).willReturn(bookUpdateDto);

        mockMvc.perform(put("/api/book/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }


    @Test
    void whenUpdateBook_thenThrowNotFoundException() throws Exception {
        Long bookId = 1L;
        BookUpdateDto bookUpdateDto = new BookUpdateDto();

        given(bookService.update(eq(bookId), any(BookUpdateDto.class))).willThrow(new NotFoundException("Book not found"));

        mockMvc.perform(put("/api/book/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookUpdateDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenUpdateBook_thenThrowIllegalStateException() throws Exception {
        Long bookId = 1L;
        BookUpdateDto bookUpdateDto = new BookUpdateDto();

        given(bookService.update(eq(bookId), any(BookUpdateDto.class))).willThrow(new IllegalStateException("Invalid request"));

        mockMvc.perform(put("/api/book/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookUpdateDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void whenDeleteBookAndBookExists_thenRespondWith200() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(delete("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Book successfully deleted"));

        verify(bookService, times(1)).delete(bookId);
    }

    @Test
    void whenDeleteBookAndBookDoesNotExist_thenRespondWith404() throws Exception {

        doThrow(new javassist.NotFoundException("Book not found")).when(bookService).delete(anyLong());

        mockMvc.perform(delete("/books/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}