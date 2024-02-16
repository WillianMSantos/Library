package com.library.libraries.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.libraries.dto.AuthorDto;
import com.library.libraries.exception.AuthorNotFoundException;
import com.library.libraries.service.AuthorService;
import com.library.libraries.service.util.TPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    private AuthorDto createAuthorDto(Long id, String name, String email) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(id);
        authorDto.setName(name);
        authorDto.setEmail(email);
        return authorDto;
    }

    @Test
    void whenGetAll_thenReturns200AndAuthorList() throws Exception {
        List<AuthorDto> authorDtos = Collections.singletonList(createAuthorDto(1L, "George R.R. Martin", "george@example.com"));
        when(authorService.getAll()).thenReturn(authorDtos);

        mockMvc.perform(get("/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{'id':1,'name':'George R.R. Martin','email':'george@example.com'}]"));
    }

    @Test
    void whenGetAllByPagination_thenReturns200AndPaginatedData() throws Exception {
        List<AuthorDto> authorDtos = Collections.singletonList(createAuthorDto(1L, "George R.R. Martin", "george@example.com"));
        TPage<AuthorDto> page = new TPage<>();
        page.setStat(new PageImpl<>(authorDtos, PageRequest.of(0, 10), authorDtos.size()), authorDtos);
        when(authorService.getAllPageable(PageRequest.of(0, 10))).thenReturn(page);

        mockMvc.perform(get("/authors/pagination?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void whenFindAllByName_thenReturns200AndAuthorList() throws Exception {
        List<AuthorDto> authorDtos = Collections.singletonList(createAuthorDto(1L, "George R.R. Martin", "george@example.com"));
        when(authorService.findAllByName("George")).thenReturn(authorDtos);

        mockMvc.perform(get("/authors/find?name=George")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{'id':1,'name':'George R.R. Martin','email':'george@example.com'}]"));
    }

    @Test
    void whenDeleteAuthor_thenReturns204NoContent() throws Exception {
        Long authorId = 1L;

        doNothing().when(authorService).delete(authorId);

        mockMvc.perform(delete("/authors/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteAuthorNotFound_thenReturns404NotFound() throws Exception {
        Long authorId = 1L;

        doThrow(new AuthorNotFoundException(authorId)).when(authorService).delete(authorId);

        mockMvc.perform(delete("/authors/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCreateAuthor_thenReturns201Created() throws Exception {

        AuthorDto authorDtoToCreate = new AuthorDto();
        authorDtoToCreate.setName("George R.R. Martin");
        authorDtoToCreate.setEmail("george@example.com");

        AuthorDto savedAuthorDto = new AuthorDto();
        savedAuthorDto.setId(1L);
        savedAuthorDto.setName("George R.R. Martin");
        savedAuthorDto.setEmail("george@example.com");

        when(authorService.save(any(AuthorDto.class))).thenReturn(savedAuthorDto);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authorDtoToCreate)))
                .andExpect(status().isCreated())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(savedAuthorDto)));
    }
}