package com.library.libraries.repository;

import com.library.libraries.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("select a from Author a where a.name like %:name% or a.lastname like %:lastname%")
    List<Author> findByNameOrLastname(String name, String lastname);

    boolean existsByEmail(String email);
}
