package com.library.libraries.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "registration", length = 11, unique = true)
    private String registration;

    @NotNull
    @Column(name = "fullname", length = 100, unique = true)
    private String fullname;

    @NotNull
    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "university", length = 3000)
    private String university;

    @Column(name = "course", length = 3000)
    private String course;

    @NotNull
    @Column(name = "phone", length = 100)
    private String phone;

    @Column(name = "address", length = 100)
    private String address;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> books;

}
