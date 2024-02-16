package com.library.libraries.repository;

import com.library.libraries.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

        User user1 = new User();
        user1.setUsername("jon_snow");
        user1.setEmail("jon@example.com");

        User user2 = new User();
        user2.setUsername("daenerys");
        user2.setEmail("daenerys@example.com");

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    void whenGetByUsername_thenReturnList() {
        List<User> users = userRepository.getByUsername("jon_snow");
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getEmail()).isEqualTo("jon@example.com");
    }

    @Test
    void whenFindByEmail_thenReturnUser() {
        Optional<User> foundUser = userRepository.findByEmail("jon@example.com");
        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getUsername()).isEqualTo("jon_snow");
    }

    @Test
    void whenFindByUsername_thenReturnUser() {
        Optional<User> foundUser = userRepository.findByUsername("daenerys");
        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getEmail()).isEqualTo("daenerys@example.com");
    }
}