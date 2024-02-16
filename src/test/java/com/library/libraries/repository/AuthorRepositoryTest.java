package com.library.libraries.repository;

import com.library.libraries.model.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {

        Author georgeMartin = new Author();
        georgeMartin.setName("George R. R.");
        georgeMartin.setLastname("Martin");
        georgeMartin.setEmail("grrm@example.com");

        authorRepository.save(georgeMartin);
    }

    @Test
    void whenFindByNameOrLastname_thenReturnList() {

        List<Author> authorsByName = authorRepository.findByNameOrLastname("George R. R.", "George R. R.");
        assertThat(authorsByName).hasSize(1);
        assertThat(authorsByName.get(0).getEmail()).isEqualTo("grrm@example.com");

        List<Author> authorsByLastname = authorRepository.findByNameOrLastname("Martin", "Martin");
        assertThat(authorsByLastname).hasSize(1);
        assertThat(authorsByLastname.get(0).getName()).isEqualTo("George R. R.");
    }

    @Test
    void whenExistsByEmail_thenReturnsTrue() {

        boolean exists = authorRepository.existsByEmail("grrm@example.com");
        assertThat(exists).isTrue();

        boolean notExists = authorRepository.existsByEmail("nobody@example.com");
        assertThat(notExists).isFalse();
    }
}