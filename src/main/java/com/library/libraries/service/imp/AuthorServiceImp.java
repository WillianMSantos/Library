package com.library.libraries.service.imp;


import com.library.libraries.dto.AuthorDto;
import com.library.libraries.dto.AuthorOneDto;
import com.library.libraries.dto.AuthorUpdateDto;
import com.library.libraries.exception.AuthorAlreadyExistsException;
import com.library.libraries.exception.AuthorNotFoundException;
import com.library.libraries.logging.LoggerFacade;
import com.library.libraries.model.Author;
import com.library.libraries.repository.AuthorRepository;
import com.library.libraries.repository.UserRepository;
import com.library.libraries.service.AuthorService;
import com.library.libraries.service.util.TPage;
import org.springframework.data.domain.Page;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImp implements AuthorService {


    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;

    private static final LoggerFacade logger = new LoggerFacade(AuthorServiceImp.class);


    public AuthorServiceImp(ModelMapper modelMapper, UserRepository userRepository, AuthorRepository authorRepository) {

        super();
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
    }


    public AuthorDto save(AuthorDto authorDto) {
        logger.info("Attempting to save author: {}", authorDto.getEmail());

        if (authorRepository.existsByEmail(authorDto.getEmail())) {
            logger.error("Author email already exists: {}", authorDto.getEmail());
            throw new AuthorAlreadyExistsException("Author email already exists: " + authorDto.getEmail());
        }

        Author author = modelMapper.map(authorDto, Author.class);
        Author savedAuthor = authorRepository.save(author);
        logger.info("Author successfully saved with ID: {}", savedAuthor.getId());

        return modelMapper.map(savedAuthor, AuthorDto.class);
    }


    public List<AuthorDto> getAll() {

        logger.info("Retrieving all authors");

        List<Author> authors = authorRepository.findAll();

        if (authors.isEmpty()) {
            logger.info("No authors found");
            return Collections.emptyList();
        }

        return authors.stream()
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public TPage<AuthorDto> getAllPageable(Pageable pageable) {
        logger.info("Fetching pageable list of authors with page number: {} and page size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Author> page = authorRepository.findAll(pageable);

        if (page.isEmpty()) {
            logger.info("No authors found for the provided page request.");
        } else {
            logger.info("Found {} authors for the provided page request.", page.getNumberOfElements());
        }

        List<AuthorDto> authorDtos = page.getContent().stream()
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .collect(Collectors.toList());

        TPage<AuthorDto> tPage = new TPage<>();
        tPage.setStat(page, authorDtos);
        logger.info("Returning pageable response for authors. Total elements: {}, Total pages: {}", page.getTotalElements(),
                page.getTotalPages());
        return tPage;
    }


    @Transactional(readOnly = true)
    public List<AuthorDto> findAllByName(String name) {

        logger.info("Searching for authors with name or lastname matching: {}", name);

        List<Author> authors = authorRepository.findByNameOrLastname(name, name);

        if (authors.isEmpty()) {
            logger.info("No authors found with name or lastname: {}", name);
            return Collections.emptyList();
        }

        logger.info("Found {} authors with name or lastname: {}", authors.size(), name);

        return authors.stream()
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public AuthorOneDto getOne(Long id) {

        logger.info("Attempting to find author with ID: {}", id);

        Author author = authorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Author does not exist: {}", id);
                    return new AuthorNotFoundException(id);
                });

        AuthorOneDto authorOneDto = modelMapper.map(author, AuthorOneDto.class);
        authorOneDto.getBooks().forEach(bookDto -> bookDto.setAuthorId(id));

        logger.info("Successfully found author with ID: {}", id);
        return authorOneDto;
    }

    public AuthorUpdateDto update(Long id, @Valid AuthorUpdateDto authorUpdateDto) {
        logger.info("Attempting to update author with ID: {}", id);

        Author author = authorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Author does not exist: {}", id);
                    return new AuthorNotFoundException(id);
                });

        modelMapper.map(authorUpdateDto, author);
        authorRepository.save(author);

        logger.info("Author with ID: {} successfully updated", author.getId());
        return modelMapper.map(author, AuthorUpdateDto.class);
    }


    @Transactional
    public void delete(Long id) {

        logger.info("Attempting to delete author with ID: {}", id);

        boolean exists = authorRepository.existsById(id);
        if (!exists) {
            logger.error("Attempted to delete non-existent author with ID: {}", id);
            throw new AuthorNotFoundException(id);
        }

        authorRepository.deleteById(id);
        logger.info("Author with ID: {} successfully deleted", id);
    }
}