package com.library.libraries.service.imp;


import com.library.libraries.dto.StudentDto;
import com.library.libraries.dto.StudentPatchDto;
import com.library.libraries.logging.LoggerFacade;
import com.library.libraries.model.Book;
import com.library.libraries.model.Status;
import com.library.libraries.model.Student;
import com.library.libraries.repository.BookRepository;
import com.library.libraries.repository.StudentRepository;
import com.library.libraries.service.StudentService;
import com.library.libraries.service.util.TPage;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImp implements StudentService {

    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;

    private static final LoggerFacade logger = new LoggerFacade(StudentServiceImp.class);

    public StudentServiceImp(ModelMapper modelMapper, StudentRepository studentRepository,
                             BookRepository bookRepository) {
        super();
        this.modelMapper = modelMapper;
        this.studentRepository = studentRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public StudentDto save(@Valid StudentDto studentDto) {
        logger.info("Attempting to save student. Email: {}, Registration: {}", studentDto.getEmail(),
                studentDto.getRegistration());

        validateStudentEmail(studentDto.getEmail().trim());
        validateStudentRegistration(studentDto.getRegistration().trim());

        try {
            Student student = modelMapper.map(studentDto, Student.class);
            student = studentRepository.save(student);
            studentDto.setId(student.getId());

            logger.info("Student successfully saved. ID: {}", student.getId());
            return studentDto;
        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation while saving student - {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error occurred while saving student. StudentDto: {}", studentDto, e);
            throw new RuntimeException("Unexpected error occurred while saving student", e);
        }
    }

    private void validateStudentEmail(String email) {
        if (studentRepository.existsByEmail(email)) {
            logger.warn("Attempted to save student with duplicate email: {}", email);
            throw new DataIntegrityViolationException("Student email already exists: " + email);
        }
    }

    private void validateStudentRegistration(String registration) {
        if (studentRepository.existsByRegistration(registration)) {
            logger.warn("Attempted to save student with duplicate registration: {}", registration);
            throw new DataIntegrityViolationException("Student Registration already exists: " + registration);
        }
    }

    public TPage<StudentDto> getAllPageable(Pageable pageable) {

        Page<Student> studentPage = studentRepository.findAll(pageable);
        List<StudentDto> studentDtos = mapEntityPageIntoDtoPage(studentPage, StudentDto.class);

        TPage<StudentDto> tPage = new TPage<>();
        tPage.setStat(studentPage, studentDtos);
        return tPage;
    }

    public List<StudentDto> getAll() {
        try {
            logger.info("Retrieving all students");
            List<Student> students = studentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

            if (students.isEmpty()) {
                logger.info("No students found");
                return Collections.emptyList();
            }

            logger.info("Found {} students", students.size());
            return students.stream().map(student -> modelMapper.map(student, StudentDto.class)).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving students", e);
            throw e;
        }
    }

    public StudentDto findById(Long id) throws NotFoundException {
        return studentRepository.findById(id)
                .map(student -> modelMapper.map(student, StudentDto.class))
                .orElseThrow(() -> new NotFoundException("Student with ID " + id + " doesn't exist."));
    }

    @Transactional
    public StudentDto getBookForStudent(@Valid StudentPatchDto studentPatchDto) throws NotFoundException {

        logger.info("Attempting to assign book to student. Student ID: {}, Book ID: {}", studentPatchDto.getStudentId(), studentPatchDto.getBookId());

        Student student = findStudentById(studentPatchDto.getStudentId());
        Book book = findBookById(studentPatchDto.getBookId());

        validateBookIsNotRented(book);

        assignBookToStudent(book, student);

        logger.info("Book with ID {} successfully assigned to student with ID {}. Book status set to RENTED.", book.getId(), student.getId());
        return modelMapper.map(student, StudentDto.class);
    }

    @Transactional
    public StudentDto leaveBookForStudent(@Valid StudentPatchDto studentPatchDto) throws NotFoundException {
        logger.info("Attempting to return book for student. Student ID: {}, Book ID: {}", studentPatchDto.getStudentId(), studentPatchDto.getBookId());

        Student student = findStudentById(studentPatchDto.getStudentId());
        Book book = findBookById(studentPatchDto.getBookId());

        validateBookRentalStatus(book, student);

        returnBook(book);

        logger.info("Book with ID {} returned by student with ID {}. Book status set to FREE.", book.getId(), student.getId());
        return modelMapper.map(student, StudentDto.class);
    }

    private Student findStudentById(Long studentId) throws NotFoundException {

        return studentRepository.findById(studentId).orElseThrow(() -> {
            NotFoundException exception = new NotFoundException("Student with ID " + studentId + " doesn't exist");
            logger.error("Student with ID {} doesn't exist", studentId, exception);
            return exception;
        });
    }

    private Book findBookById(Long bookId) throws NotFoundException {
        return bookRepository.findById(bookId).orElseThrow(() -> {
            NotFoundException exception = new NotFoundException("Book with ID " + bookId + " doesn't exist");
            logger.error("Book with ID {} doesn't exist", bookId, exception);
            return exception;
        });
    }

    private void validateBookIsNotRented(Book book) {
        if (book.getStatus() == Status.RENTED) {
            String errorMessage = String.format("Book with ID %d is already rented.", book.getId());
            logger.warn(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
    }

    private void validateBookRentalStatus(Book book, Student student) {
        boolean isRentedByThisStudent = book.getStatus().equals(Status.RENTED)
                && book.getStudent() != null
                && book.getStudent().getId().equals(student.getId());

        if (!isRentedByThisStudent) {
            String errorMessage = String.format("Book with ID %d is not rented by student with ID %d.", book.getId(), student.getId());
            logger.warn(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
    }

    private void assignBookToStudent(Book book, Student student) {
        book.setStudent(student);
        book.setStatus(Status.RENTED);
        bookRepository.save(book);
    }

    private void returnBook(Book book) {
        book.setStudent(null);
        book.setStatus(Status.FREE);
        bookRepository.save(book);
    }

    public Boolean delete(Long id) throws NotFoundException {

        return studentRepository.findById(id)
                .map(student -> {
                    student.getBooks().forEach(book -> {
                        book.setStudent(null);
                        book.setStatus(Status.FREE);
                        bookRepository.save(book);
                    });
                    studentRepository.delete(student);
                    return true;
                })
                .orElseThrow(() -> new NotFoundException("Student doesn't exist"));
    }

    public StudentDto update(Long id, @Valid StudentDto studentDto) throws NotFoundException {

        logger.info("Attempting to update student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Student with ID: {} doesn't exist", id);
                    return new NotFoundException("Student doesn't exist");
                });

        modelMapper.map(studentDto, student);
        student.setId(id);

        Student updatedStudent = studentRepository.save(student);
        logger.info("Student with ID: {} successfully updated", id);

        return modelMapper.map(updatedStudent, StudentDto.class);
    }

    private <D, T> List<D> mapEntityPageIntoDtoPage(Page<T> entityPage, Class<D> dtoClass) {
        ModelMapper modelMapper = new ModelMapper();
        return entityPage.getContent().stream()
                .map(entity -> modelMapper.map(entity, dtoClass))
                .collect(Collectors.toList());
    }

}
