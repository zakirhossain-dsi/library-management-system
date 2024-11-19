package com.example.lms.controller;

import com.example.lms.dto.BookDTO;
import com.example.lms.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookDTO mockBookDTO;

    @BeforeEach
    void setUp() {
        mockBookDTO = new BookDTO();
        mockBookDTO.setId(1L);
        mockBookDTO.setTitle("The Clean Coder");
        mockBookDTO.setAuthor("Robert C. Martin");
        mockBookDTO.setIsbn("978-1-60309-502-1");
        mockBookDTO.setBorrowerId(null);
    }

    @Test
    void testRegisterBook() {
        // Mocking behavior of BookService
        when(bookService.registerBook(any(BookDTO.class))).thenReturn(mockBookDTO);

        // Call the controller method
        ResponseEntity<BookDTO> responseEntity = bookController.registerBook(mockBookDTO);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(mockBookDTO, responseEntity.getBody());
    }

    @Test
    void testGetAllBooks() {
        // Mocking behavior of BookService
        List<BookDTO> mockBooks = Collections.singletonList(mockBookDTO);
        when(bookService.getAllBooks()).thenReturn(mockBooks);

        // Call the controller method
        ResponseEntity<List<BookDTO>> responseEntity = bookController.getAllBooks();

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockBooks, responseEntity.getBody());
    }
}
