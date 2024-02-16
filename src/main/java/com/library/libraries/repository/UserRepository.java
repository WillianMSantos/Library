package com.library.libraries.repository;

import com.library.libraries.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.username like %:username%")
    List<User> getByUsername(@Param("username") String username);

    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.username like %:username%")
    Optional<User> findByUsername(@Param("username")String username);}
