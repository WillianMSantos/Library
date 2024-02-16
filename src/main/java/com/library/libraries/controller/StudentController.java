package com.library.libraries.controller;

import com.library.libraries.dto.StudentDto;
import com.library.libraries.dto.StudentPatchDto;
import com.library.libraries.service.StudentService;
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
import java.util.List;

@Api(value = "StudentController", description = "Operations pertaining to books in the Books Library")
@RestController
@CrossOrigin
@RequestMapping(value = ApiPaths.StudentCtrl.CTRL, produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @ApiOperation(value = "Get all students", notes = "Retrieve a list of all students")
    @GetMapping
    public ResponseEntity<List<StudentDto>> getAll() throws NotFoundException {
        List<StudentDto> studentDtos = studentService.getAll();
        return ResponseEntity.ok(studentDtos);
    }

    @ApiOperation(value = "Get all students with pagination", notes = "Retrieve a paginated list of students")
    @GetMapping("/pagination")
    public ResponseEntity<TPage<StudentDto>> getAllByPagination(Pageable pageable) throws NotFoundException {
        TPage<StudentDto> data = studentService.getAllPageable(pageable);
        return ResponseEntity.ok(data);
    }

    @ApiOperation(value = "Get a single student by ID", notes = "Retrieve details of a specific student by their ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the student"),
            @ApiResponse(code = 404, message = "Student not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getOne(@ApiParam(value = "ID of the student to retrieve", required = true) @PathVariable Long id) {
        try {
            StudentDto studentDto = studentService.findById(id);
            return ResponseEntity.ok(studentDto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found", e);
        }
    }

    @ApiOperation(value = "Create a new student", notes = "Register a new student with the provided information")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Student successfully created"),
            @ApiResponse(code = 400, message = "Invalid StudentDto input")
    })
    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@ApiParam(value = "StudentDto object to create", required = true) @Valid @RequestBody StudentDto studentDto) {
        try {
            StudentDto savedStudentDto = studentService.save(studentDto);
            return new ResponseEntity<>(savedStudentDto, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error creating student", e);
        }
    }

    @ApiOperation(value = "Assign a book to a student", notes = "Associate a book with a student based on their IDs")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book successfully assigned to the student"),
            @ApiResponse(code = 400, message = "Invalid operation or input")
    })
    @PostMapping("/assign-book")
    public ResponseEntity<StudentDto> assignBookToStudent(@ApiParam(value = "StudentPatchDto object to assign a book to a student", required = true) @Valid @RequestBody StudentPatchDto studentPatchDto) {
        try {
            StudentDto updatedStudentDto = studentService.getBookForStudent(studentPatchDto);
            return ResponseEntity.ok(updatedStudentDto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error assigning book to student", e);
        }
    }

    @ApiOperation(value = "Remove a book from a student", notes = "Disassociate a book from a student based on their IDs")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book successfully removed from the student"),
            @ApiResponse(code = 400, message = "Invalid operation or input")
    })
    @PostMapping("/leave-book")
    public ResponseEntity<StudentDto> leaveBookForStudent(@ApiParam(value = "StudentPatchDto object to remove a book from a student", required = true) @Valid @RequestBody StudentPatchDto studentPatchDto) {
        try {
            StudentDto updatedStudentDto = studentService.leaveBookForStudent(studentPatchDto);
            return ResponseEntity.ok(updatedStudentDto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error removing book from student", e);
        }
    }

    @ApiOperation(value = "Delete a student by ID", notes = "Remove a student from the library system by their ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Student successfully deleted"),
            @ApiResponse(code = 404, message = "Student not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@ApiParam(value = "The ID of the student to delete", required = true) @PathVariable Long id) throws NotFoundException {
            studentService.delete(id);
            return ResponseEntity.ok().body("Student successfully deleted.");
    }
}