package com.library.libraries.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "book")
public class Book {


    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "title", length = 300)
    private String title;

    @Column(name = "isbn", length = 100, unique = true)
    private String isbn;

    @Column(name = "status", length = 50)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "publishYear", length = 300)
    private Integer publishYear;

    @NotNull
    @JoinColumn(name = "author_id")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Author author;

    @JoinColumn(name = "student_id")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Student student;
}
