package com.library.libraries.specification;

import com.library.libraries.dto.BookDto;
import com.library.libraries.model.Book;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BookSpecifications {

    public static Specification<Book> byAttributes(BookDto bookDto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            addIfNotNull(predicates, bookDto.getId(), id -> criteriaBuilder.equal(root.get("id"), id));
            addIfNotNull(predicates, bookDto.getTitle(), title -> criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            addIfNotNull(predicates, bookDto.getIsbn(), isbn -> criteriaBuilder.equal(root.get("isbn"), isbn));
            addIfNotNull(predicates, bookDto.getPublishYear(), publishYear -> criteriaBuilder.equal(root.get("publishYear"), publishYear));
            addIfNotNull(predicates, bookDto.getStudentId(), studentId -> criteriaBuilder.equal(root.get("student").get("id"), studentId));
            addIfNotNull(predicates, bookDto.getStatus(), status -> criteriaBuilder.equal(root.get("status"), status));
            addIfNotNull(predicates, bookDto.getAuthorId(), authorId -> criteriaBuilder.equal(root.get("author").get("id"), authorId));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static <T> void addIfNotNull(List<Predicate> predicates, T value, Function<T, Predicate> predicateCreator) {
        if (value != null) {
            predicates.add(predicateCreator.apply(value));
        }
    }

}
