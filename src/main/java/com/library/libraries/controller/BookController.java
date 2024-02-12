package com.library.libraries.controller;



import com.library.libraries.dto.AuthorDto;
import com.library.libraries.dto.BookDto;
import com.library.libraries.dto.BookOneDto;
import com.library.libraries.model.Status;
import com.library.libraries.service.AuthorService;
import com.library.libraries.service.BookService;
import com.library.libraries.service.util.TPage;
import io.swagger.annotations.*;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Api(value = "BookController", description = "Operations pertaining to books in the Books Library")
@RestController
@CrossOrigin
@RequestMapping(value = ApiPaths.BookCtrl.CTRL, produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {


    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping
    public ResponseEntity<List<BookDto>> getAll() throws NotFoundException {
        List<BookDto> bookDto = bookService.getAll();
        return ResponseEntity.ok().body(bookDto);
    }


    // localhost:8182/api/book/pagination?page=1&size=3
    @GetMapping("/pagination")
    public ResponseEntity<TPage<BookDto>> getAllByPagination(Pageable pageable) throws NotFoundException {
        TPage<BookDto> data = bookService.getAllPageable(pageable);
        return ResponseEntity.ok(data);
    }


    @ApiOperation(value = "Get an book by ID", notes = "Returns a single book details by ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the book"),
            @ApiResponse(code = 404, message = "The book with the specified ID was not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookOneDto> getOneAuthor(@ApiParam(value = "ID of the book to retrieve", required = true) @PathVariable Long id) throws NotFoundException {
        BookOneDto bookOneDto = bookService.getOne(id);
        return ResponseEntity.ok(bookOneDto);
    }


    @ApiOperation(value = "Find books by title", notes = "Returns a list of books matching the given title.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", dataType = "string", paramType = "query", value = "The title of the book to find", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of books"),
            @ApiResponse(code = 404, message = "No book found with the given title")
    })
    @GetMapping("/find/{title}")
    public ResponseEntity<List<BookDto>> searchBooksByTitle(@RequestParam String title) throws NotFoundException {
        List<BookDto> bookDtos = bookService.searchBooksByTitle(title);
        return ResponseEntity.ok(bookDtos);
    }


    @ApiOperation(value = "Register a new book", notes = "Register a new book with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Book successfully created"),
            @ApiResponse(code = 400, message = "Invalid AuthorDto input")
    })
    @PostMapping
    public ResponseEntity<BookOneDto> createBook(@ApiParam(value = "BookOneDto object to create", required = true) @Valid @RequestBody BookOneDto bookOneDto) throws Exception {
        BookOneDto savedBookDto = bookService.save(bookOneDto);
        return new ResponseEntity<>(savedBookDto, HttpStatus.CREATED);
    }


    @ApiOperation(value = "Delete a book", notes = "Deletes a book with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book successfully deleted"),
            @ApiResponse(code = 404, message = "Book not found with the provided ID"),
            @ApiResponse(code = 500, message = "Internal server error occurred while attempting to delete the book")
    })
    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@ApiParam(value = "The ID of the book to delete", required = true) @PathVariable("id") Long id) {
        try {
            bookService.delete(id);
            return ResponseEntity.ok().body("Book successfully deleted.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting book with id: " + id, e);
        }
    }


    @GetMapping("/statuses")
    public ResponseEntity<List<Status>> getAllBookStatus() {
        return ResponseEntity.ok(Arrays.asList(Status.values()));
    }


}
