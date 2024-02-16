package com.library.libraries.controller;


import com.library.libraries.dto.AuthorDto;
import com.library.libraries.dto.AuthorOneDto;
import com.library.libraries.dto.AuthorUpdateDto;
import com.library.libraries.exception.AuthorNotFoundException;
import com.library.libraries.service.AuthorService;
import com.library.libraries.service.util.TPage;
import io.swagger.annotations.*;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "AuthorController", description = "Operations pertaining to authors in the Books Library")
@RestController
@CrossOrigin
@RequestMapping(value = ApiPaths.AuthorCtrl.CTRL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }


    @ApiOperation(value = "View a list of available authors", response = AuthorDto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAll() throws NotFoundException {
        List<AuthorDto> authorDtos = authorService.getAll();
        return ResponseEntity.ok().body(authorDtos);
    }


    @ApiOperation(value = "Get a paginated list of authors", notes = "Provides a paginated list of authors based on pageable parameters.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", value = "The page number (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", value = "The size of the page to be returned"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.")
    })
    @GetMapping("/pagination")
    public ResponseEntity<TPage<AuthorDto>> getAllByPagination(Pageable pageable) throws NotFoundException {
        TPage<AuthorDto> data = authorService.getAllPageable(pageable);
        return ResponseEntity.ok(data);
    }


    @ApiOperation(value = "Find authors by name or surname", notes = "Returns a list of authors matching the given name or surname.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", dataType = "string", paramType = "query", value = "The name or surname of the author to find", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of authors"),
            @ApiResponse(code = 404, message = "No authors found with the given name or surname")
    })
    @GetMapping("/find")
    public ResponseEntity<List<AuthorDto>> findAllByName(@RequestParam String name) throws NotFoundException {
        List<AuthorDto> authorDtos = authorService.findAllByName(name);
        return ResponseEntity.ok(authorDtos);
    }


    @ApiOperation(value = "Get an author by ID", notes = "Returns a single author details by ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the author"),
            @ApiResponse(code = 404, message = "The author with the specified ID was not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuthorOneDto> getOneAuthor(@ApiParam(value = "ID of the author to retrieve", required = true) @PathVariable Long id) throws NotFoundException {
        AuthorOneDto authorDto = authorService.getOne(id);
        return ResponseEntity.ok(authorDto);
    }

    @ApiOperation(value = "Update an author", notes = "Updates the details of an existing author by ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the author"),
            @ApiResponse(code = 404, message = "The author with the specified ID was not found"),
            @ApiResponse(code = 400, message = "Invalid request data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AuthorUpdateDto> updateAuthor(
            @ApiParam(value = "ID of the author to update", required = true) @PathVariable Long id,
            @ApiParam(value = "Updated author data", required = true) @Valid @RequestBody AuthorUpdateDto authorUpdateDto) {

        try {
            AuthorUpdateDto updatedAuthor = authorService.update(id, authorUpdateDto);
            return ResponseEntity.ok(updatedAuthor);
        } catch (AuthorNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }



    @ApiOperation(value = "Delete an author by ID", notes = "Deletes an author with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Author successfully deleted"),
            @ApiResponse(code = 404, message = "The author with the specified ID was not found"),
            @ApiResponse(code = 401, message = "You are not authorized to delete the resource"),
            @ApiResponse(code = 403, message = "Deleting the resource is forbidden")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@ApiParam(value = "ID of the author to delete", required = true) @PathVariable Long id) {
        try {
            authorService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (AuthorNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiOperation(value = "Create a new author", notes = "Creates a new author with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Author successfully created"),
            @ApiResponse(code = 400, message = "Invalid AuthorDto input")
    })
    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(@ApiParam(value = "AuthorDto object to create", required = true) @Valid @RequestBody AuthorDto authorDto) {
        AuthorDto savedAuthorDto = authorService.save(authorDto);
        return new ResponseEntity<>(savedAuthorDto, HttpStatus.CREATED);
    }
}
