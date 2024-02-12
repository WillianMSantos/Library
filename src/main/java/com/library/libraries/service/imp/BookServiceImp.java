package com.library.libraries.service.imp;

import com.library.libraries.dto.BookDto;
import com.library.libraries.dto.BookOneDto;
import com.library.libraries.exception.AuthorNotFoundException;
import com.library.libraries.exception.BookAlreadyExistsException;
import com.library.libraries.exception.BookDeletionException;
import com.library.libraries.exception.BookNotFoundException;
import com.library.libraries.model.Author;
import com.library.libraries.model.Book;
import com.library.libraries.repository.AuthorRepository;
import com.library.libraries.repository.BookRepository;
import com.library.libraries.repository.UserRepository;
import com.library.libraries.service.BookService;
import com.library.libraries.service.util.TPage;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImp implements BookService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public BookServiceImp(ModelMapper modelMapper, UserRepository userRepository, AuthorRepository authorRepository,
                          BookRepository bookRepository) {
        super();
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public BookOneDto save(BookOneDto bookOneDto) {

        if (bookRepository.existsByTitle(bookOneDto.getTitle().trim())) {
            throw new BookAlreadyExistsException();
        }

        Author author = authorRepository.findById(bookOneDto.getAuthorId())
                .orElseThrow(AuthorNotFoundException::new);

        Book book = modelMapper.map(bookOneDto, Book.class);
        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);

        return modelMapper.map(savedBook, BookOneDto.class);
    }

    public List<BookDto> getAll() throws NotFoundException {
        List<Book> books = bookRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        if (books.isEmpty()) {
            throw new NotFoundException("No books available.");
        }

        return books.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    public TPage<BookDto> getAllPageable(Pageable pageable) throws NotFoundException {
        Page<Book> page = bookRepository.findAll(pageable);
        if (page.isEmpty()) {
            throw new NotFoundException("No books available.");
        }

        List<BookDto> bookDtos = page.getContent().stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());

        TPage<BookDto> tPage = new TPage<>();
        tPage.setStat(page, bookDtos);
        return tPage;
    }

    public BookOneDto getOne(Long id) throws NotFoundException {

        return bookRepository.findById(id)
                .map(book -> modelMapper.map(book, BookOneDto.class))
                .map(bookOneDto -> {
                    bookOneDto.setAuthorId(bookOneDto.getAuthor().getId());
                    return bookOneDto;
                })
                .orElseThrow(() -> new NotFoundException("Book with ID: " + id + " does not exist."));
    }

    @Transactional
    public void delete(Long id) throws BookNotFoundException, BookDeletionException {
        try {
            Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book with ID: " + id + " does not exist."));
            bookRepository.delete(book);
        } catch (BookNotFoundException e) {

            throw e;
        } catch (Exception e) {

            throw new BookDeletionException("Error deleting book with id: " + id, e);
        }
    }


    public List<BookDto> searchBooksByTitle(String title) throws NotFoundException {
        List<Book> books = bookRepository.SearchBooksByTitle(title.trim());
        if (books.isEmpty()) {
            throw new NotFoundException("No books found with the given title: " + title);
        }

        return books.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

}
