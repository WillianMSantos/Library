package com.library.libraries.repository;

import com.library.libraries.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        Book book1 = new Book();
        book1.setTitle("A Game of Thrones");
        book1.setIsbn("ISBN001");

        Book book2 = new Book();
        book2.setTitle("A Clash of Kings");
        book2.setIsbn("ISBN002");

        bookRepository.save(book1);
        bookRepository.save(book2);
    }

    @Test
    void whenFindByTitle_thenReturnList() {
        List<Book> books = bookRepository.findByTitle("A Game of Thrones");
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("A Game of Thrones");
    }

    @Test
    void whenExistsByTitle_thenReturnsTrue() {
        boolean exists = bookRepository.existsByTitle("A Clash of Kings");
        assertThat(exists).isTrue();
    }

    @Test
    void whenSearchBooksByTitle_thenReturnList() {
        List<Book> books = bookRepository.searchBooksByTitle("Game");
        assertThat(books).isNotEmpty();
        assertThat(books.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void whenSaveDuplicateTitle_thenThrowsException() {
        Book book = new Book();
        book.setTitle("A Game of Thrones");
        book.setIsbn("ISBN003");

        assertThrows(DataIntegrityViolationException.class, () -> bookRepository.save(book));
    }
}