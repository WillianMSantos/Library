package com.library.libraries.service.imp;

import com.library.libraries.dto.AuthorDto;
import com.library.libraries.dto.AuthorOneDto;
import com.library.libraries.dto.AuthorUpdateDto;
import com.library.libraries.exception.AuthorAlreadyExistsException;
import com.library.libraries.exception.AuthorNotFoundException;
import com.library.libraries.model.Author;
import com.library.libraries.repository.AuthorRepository;
import com.library.libraries.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {AuthorServiceImp.class})
class AuthorServiceImpTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private ModelMapper modelMapper;

    private Author author;
    private AuthorDto authorDto;

    @BeforeEach
    void setUp() {

        author = new Author();
        author.setId(1L);
        author.setName("George R. R. Martin");
        author.setEmail("george@example.com");

        authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setName("George R. R. Martin");
        authorDto.setEmail("george@example.com");

        when(modelMapper.map(any(), eq(Author.class))).thenReturn(author);
        when(modelMapper.map(any(Author.class), eq(AuthorDto.class))).thenReturn(authorDto);
    }

    @Test
    void saveAuthor_whenAuthorDoesNotExist_thenAuthorSaved() {
        when(authorRepository.existsByEmail(anyString())).thenReturn(false);
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        AuthorDto savedAuthor = authorService.save(authorDto);

        assertThat(savedAuthor.getEmail()).isEqualTo(authorDto.getEmail());
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void saveAuthor_whenAuthorExists_thenThrowException() {
        when(authorRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(AuthorAlreadyExistsException.class, () -> {
            authorService.save(authorDto);
        });
    }

    @Test
    void findAllByName_WhenAuthorsFound_ThenReturnAuthorDtoList() {

        String name = "George";
        Author author = new Author();
        author.setId(1L);
        author.setName(name);
        author.setLastname("R.R. Martin");
        author.setEmail("george@example.com");

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setName(author.getName());
        authorDto.setLastname(author.getLastname());
        authorDto.setEmail(author.getEmail());

        given(authorRepository.findByNameOrLastname(name, name)).willReturn(Collections.singletonList(author));
        given(modelMapper.map(author, AuthorDto.class)).willReturn(authorDto);

        List<AuthorDto> result = authorService.findAllByName(name);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo(name);
        assertThat(result.get(0).getEmail()).isEqualTo("george@example.com");
    }

    @Test
    void whenGetOneAuthorById_ThenReturnAuthorOneDto() {

        Long authorId = 1L;
        Author author = new Author();
        author.setId(authorId);
        author.setName("George R.R. Martin");

        AuthorOneDto authorOneDto = new AuthorOneDto();
        authorOneDto.setId(authorId);
        authorOneDto.setName("George R.R. Martin");

        given(authorRepository.findById(authorId)).willReturn(Optional.of(author));
        given(modelMapper.map(author, AuthorOneDto.class)).willReturn(authorOneDto);

        AuthorOneDto foundAuthor = authorService.getOne(authorId);

        assertThat(foundAuthor).isNotNull();
        assertThat(foundAuthor.getId()).isEqualTo(authorId);
        assertThat(foundAuthor.getName()).isEqualTo("George R.R. Martin");
    }

    @Test
    void whenUpdateAuthor_ThenReturnUpdatedAuthorDto() {

        Long authorId = 1L;
        Author originalAuthor = new Author();
        originalAuthor.setId(authorId);
        originalAuthor.setName("George");

        AuthorUpdateDto updateDto = new AuthorUpdateDto();
        updateDto.setName("George R.R. Martin");

        Author updatedAuthor = new Author();
        updatedAuthor.setId(authorId);
        updatedAuthor.setName(updateDto.getName());

        given(authorRepository.findById(authorId)).willReturn(Optional.of(originalAuthor));
        given(modelMapper.map(updateDto, Author.class)).willReturn(updatedAuthor);
        given(authorRepository.save(updatedAuthor)).willReturn(updatedAuthor);
        given(modelMapper.map(updatedAuthor, AuthorUpdateDto.class)).willReturn(updateDto);

        AuthorUpdateDto resultDto = authorService.update(authorId, updateDto);

        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getName()).isEqualTo("George R.R. Martin");
    }

}