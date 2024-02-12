package com.library.libraries.repository;

import com.library.libraries.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitle(String title);

    boolean existsByTitle(String Title);

    @Query("select b from Book b where b.title like %:title%")
    List<Book> SearchBooksByTitle(String title);

}
