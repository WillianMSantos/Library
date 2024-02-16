package com.library.libraries.repository;

import com.library.libraries.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByEmail(String email);
    boolean existsByRegistration(String registration);
    boolean existsByEmail(String email);
}
