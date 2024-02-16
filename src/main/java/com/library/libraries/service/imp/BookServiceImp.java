package com.library.libraries.service.imp;

import com.library.libraries.dto.BookDto;
import com.library.libraries.dto.BookOneDto;
import com.library.libraries.dto.BookUpdateDto;
import com.library.libraries.exception.AuthorNotFoundException;
import com.library.libraries.exception.BookAlreadyExistsException;
import com.library.libraries.exception.BookDeletionException;
import com.library.libraries.exception.BookNotFoundException;
import com.library.libraries.logging.LoggerFacade;
import com.library.libraries.model.Author;
import com.library.libraries.model.Book;
import com.library.libraries.model.Status;
import com.library.libraries.repository.AuthorRepository;
import com.library.libraries.repository.BookRepository;
import com.library.libraries.repository.UserRepository;
import com.library.libraries.service.BookService;
import com.library.libraries.service.util.TPage;
import com.library.libraries.specification.BookSpecifications;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImp implements BookService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    private static final LoggerFacade logger = new LoggerFacade(BookServiceImp.class);

    public BookServiceImp(ModelMapper modelMapper, UserRepository userRepository, AuthorRepository authorRepository,
                          BookRepository bookRepository) {
        super();
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public BookOneDto save(BookOneDto bookOneDto) {

        logger.info("Attempting to save a new book with title: \"{}\"", bookOneDto.getTitle());

        if (bookRepository.existsByTitle(bookOneDto.getTitle().trim())) {
            logger.error("Book already exists with title: \"{}\"", bookOneDto.getTitle());
            throw new BookAlreadyExistsException();
        }

        Author author = authorRepository.findById(bookOneDto.getAuthorId())
                .orElseThrow(() -> {
                    logger.error("Author not found for ID: {}", bookOneDto.getAuthorId());
                    return new AuthorNotFoundException();
                });

        Book book = modelMapper.map(bookOneDto, Book.class);
        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);

        logger.info("Book successfully saved with ID: {}", savedBook.getId());

        return modelMapper.map(savedBook, BookOneDto.class);
    }

    public List<BookDto> getAll() {
        logger.info("Retrieving all books");

        List<Book> books = bookRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        if (books.isEmpty()) {
            logger.info("No books found");
            return Collections.emptyList();
        }

        logger.info("Found {} books", books.size());

        return books.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public BookUpdateDto update(Long id, @Valid BookUpdateDto bookUpdateDto) throws NotFoundException {
        logger.info("Attempting to update book with ID: {}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book does not exist id: " + id));

        if (book.getStatus() == Status.RENTED) {
            logger.error("Cannot update book with ID: {} because it is currently RENTED", id);
            throw new IllegalStateException("Cannot update book as it is currently RENTED.");
        }

        Author author = authorRepository.findById(bookUpdateDto.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Author does not exist id: " + bookUpdateDto.getAuthorId()));

        modelMapper.map(bookUpdateDto, book);
        book.setAuthor(author);

        book = bookRepository.save(book);
        logger.info("Book with ID: {} successfully updated", book.getId());

        return modelMapper.map(book, BookUpdateDto.class);
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
        logger.info("Retrieving book with ID: {}", id);

        BookOneDto bookOneDto = bookRepository.findById(id)
                .map(book -> modelMapper.map(book, BookOneDto.class))
                .orElseThrow(() -> {
                    logger.error("Book with ID: {} does not exist", id);
                    return new NotFoundException("Book with ID: " + id + " does not exist.");
                });

        logger.info("Successfully retrieved book with ID: {}", id);
        return bookOneDto;
    }

    @Transactional
    public void delete(Long id) throws BookNotFoundException, BookDeletionException {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = "Book with ID: " + id + " does not exist.";
                    logger.error(errorMessage);
                    return new BookNotFoundException(errorMessage);
                });

        if (book.getStatus() == Status.RENTED) {
            String errorMessage = "Cannot delete book with ID: " + id + " as it is currently RENTED.";
            logger.error(errorMessage);
            throw new BookDeletionException(errorMessage);
        }

        try {
            bookRepository.delete(book);
            logger.info("Successfully deleted book with ID: {}", id);
        } catch (Exception e) {
            String errorMessage = "Error deleting book with ID: " + id;
            logger.error(errorMessage, e);
            throw new BookDeletionException(errorMessage, e);
        }
    }

    public List<BookDto> searchBooksByTitle(String title) {

        logger.info("Searching books with title: \"{}\"", title);
        List<Book> books = bookRepository.searchBooksByTitle(title.trim());

        if (books.isEmpty()) {
            logger.info("No books found with the given title: \"{}\"", title);
            return Collections.emptyList();
        }

        logger.info("{} books found with the given title: \"{}\"", books.size(), title);

        return books.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    public List<BookDto> searchBooks(BookDto bookDto) {

        try {
            logger.info("Initiating search for books with criteria");

            List<Book> books = bookRepository.findAll(BookSpecifications.byAttributes(bookDto));

            if (books.isEmpty()) {
                logger.info("No books found matching the criteria");
                return Collections.emptyList();
            }

            logger.info("{} books found matching the criteria", books.size());

            return books.stream()
                    .map(book -> modelMapper.map(book, BookDto.class))
                    .collect(Collectors.toList());

        } catch (Exception e) {

            logger.error("Error occurred during book search", e);
            throw new RuntimeException("Search operation failed", e);
        }
    }
}