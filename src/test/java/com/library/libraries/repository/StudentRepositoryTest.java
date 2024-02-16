package com.library.libraries.repository;

import com.library.libraries.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {

        Student student1 = new Student();
        student1.setFullname("Alice Rita");
        student1.setEmail("alice@example.com");
        student1.setRegistration("REG123");

        Student student2 = new Student();
        student2.setFullname("Bob Marley");
        student2.setEmail("bob@example.com");
        student2.setRegistration("REG456");

        studentRepository.save(student1);
        studentRepository.save(student2);
    }

    @Test
    void whenFindByEmail_thenReturnList() {

        List<Student> foundStudents = studentRepository.findByEmail("alice@example.com");
        assertThat(foundStudents).hasSize(1);
        assertThat(foundStudents.get(0).getFullname()).isEqualTo("Alice Rita");
    }

    @Test
    void whenExistsByRegistration_thenReturnsTrue() {

        boolean exists = studentRepository.existsByRegistration("REG123");
        assertThat(exists).isTrue();

        boolean notExists = studentRepository.existsByRegistration("REG999");
        assertThat(notExists).isFalse();
    }

    @Test
    void whenExistsByEmail_thenReturnsTrue() {

        boolean exists = studentRepository.existsByEmail("bob@example.com");
        assertThat(exists).isTrue();

        boolean notExists = studentRepository.existsByEmail("nobody@example.com");
        assertThat(notExists).isFalse();
    }
}