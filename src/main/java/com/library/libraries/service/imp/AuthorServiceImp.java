package com.library.libraries.service.imp;


import com.library.libraries.dto.AuthorDto;
import com.library.libraries.dto.AuthorOneDto;
import com.library.libraries.model.Author;
import com.library.libraries.repository.AuthorRepository;
import com.library.libraries.repository.UserRepository;
import com.library.libraries.service.AuthorService;
import com.library.libraries.service.util.TPage;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImp implements AuthorService {


    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;


    public AuthorServiceImp(ModelMapper modelMapper, UserRepository userRepository, AuthorRepository authorRepository) {

        super();
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
    }

    public AuthorDto save(AuthorDto authorDto) {

        boolean existsByEmail = authorRepository.existsByEmail(authorDto.getEmail());

        if (existsByEmail) {

            throw new EntityExistsException("Author email already exists");
        }

        final Author author = modelMapper.map(authorDto, Author.class);
        final Author savedAuthor = authorRepository.save(author);

        return modelMapper.map(savedAuthor, AuthorDto.class);
    }

    public List<AuthorDto> getAll() throws NotFoundException {

        List<Author> authors = authorRepository.findAll();

        if (authors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No authors found");
        }

        return authors.stream()
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TPage<AuthorDto> getAllPageable(Pageable pageable) {

        Pageable pageRequestWithSort = PageRequest.of(pageable.getPageNumber(),
                                                      pageable.getPageSize(),
                                                      pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id")));

        Page<Author> page = authorRepository.findAll(pageRequestWithSort);

        List<AuthorDto> authorDtos = page.getContent().stream()
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .collect(Collectors.toList());

        if (authorDtos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No authors found");
        }

        TPage<AuthorDto> tPage = new TPage<>();
        tPage.setStat(page, authorDtos);
        return tPage;
    }

    @Transactional(readOnly = true)
    public List<AuthorDto> findAllByName(String name) throws NotFoundException {

        List<Author> authors = authorRepository.findByNameOrLastname(name, name);

        if (authors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No authors found with name or lastname: " + name);
        }

        return authors.stream()
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AuthorOneDto getOne(Long id) {

        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author does not exist: " + id));

        AuthorOneDto authorOneDto = modelMapper.map(author, AuthorOneDto.class);
        authorOneDto.getBooks().forEach(bookDto -> bookDto.setAuthorId(id));
        return authorOneDto;
    }

    @Transactional
    public void delete(Long id) {

        if (!authorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author does not exist: " + id);
        }
        authorRepository.deleteById(id);
    }
}