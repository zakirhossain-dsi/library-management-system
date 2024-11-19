package com.example.lms.service;

import com.example.lms.dto.BorrowerDTO;
import com.example.lms.entity.Book;
import com.example.lms.entity.Borrower;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.BookRepository;
import com.example.lms.repository.BorrowerRepository;
import com.example.lms.service.impl.BorrowerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BorrowerServiceTest {

    @Mock
    private BorrowerRepository borrowerRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BorrowerServiceImpl borrowerService;

    @Test
    void testRegisterBorrower() {
        // Given
        BorrowerDTO borrowerDTO = new BorrowerDTO(null, "raymond.tang@example.com", "Raymond Tang");
        Borrower savedBorrower = new Borrower(1L, "raymond.tang@example.com", "Raymond Tang");

        when(modelMapper.map(borrowerDTO, Borrower.class)).thenReturn(new Borrower());
        when(borrowerRepository.save(any(Borrower.class))).thenReturn(savedBorrower);
        when(modelMapper.map(savedBorrower, BorrowerDTO.class)).thenReturn(borrowerDTO);

        BorrowerDTO result = borrowerService.registerBorrower(borrowerDTO);

        // Assertions
        assertNotNull(result);
        assertEquals("raymond.tang@example.com", result.getEmail());
        assertEquals("Raymond Tang", result.getName());
    }

    @Test
    void testBorrowBook() {
        // Given
        Borrower borrower = new Borrower(1L, "raymond.tang@example.com", "Raymond Tang");
        Book book = new Book(1L, "978-1-60309-502-1", "The Clean Coder", "Robert C. Martin", null);

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        assertDoesNotThrow(() -> borrowerService.borrowBook(1L, 1L));

        // Assertions
        assertNotNull(book.getBorrower());
        assertEquals(borrower, book.getBorrower());
    }

    @Test
    void testBorrowBookAlreadyBorrowed() {
        // Given
        Borrower borrower = new Borrower(1L, "raymond.tang@example.com", "Raymond Tang");
        Book book = new Book(1L, "978-1-60309-502-1", "The Clean Coder", "Robert C. Martin", null);
        book.setBorrower(borrower);

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Assertions
        RuntimeException exception = assertThrows(RuntimeException.class, () -> borrowerService.borrowBook(1L, 1L));
        assertEquals("Book is already borrowed", exception.getMessage());
    }

    @Test
    void testReturnBook() {
        // Given
        Borrower borrower = new Borrower(1L, "raymond.tang@example.com", "Raymond Tang");
        Book book = new Book(1L, "978-1-60309-502-1", "The Clean Coder", "Robert C. Martin", null);
        book.setBorrower(borrower);

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        assertDoesNotThrow(() -> borrowerService.returnBook(1L, 1L));

        // Then
        assertNull(book.getBorrower());
    }

    @Test
    void testReturnBookNotBorrowed() {
        // Given
        Borrower borrower = new Borrower(1L, "raymond.tang@example.com", "Raymond Tang");
        Borrower anotherBorrower = new Borrower(2L, "crystal@example.com", "Crystal");
        Book book = new Book(1L, "978-1-60309-502-1", "The Clean Coder", "Robert C. Martin", null);
        book.setBorrower(anotherBorrower);

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // When, Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> borrowerService.returnBook(1L, 1L));
        assertEquals("Book was not borrowed by this borrower", exception.getMessage());
    }

    @Test
    void testGetAllBorrowers() {
        // Given
        Borrower borrower1 = new Borrower(1L, "raymond.tang@example.com", "Raymond Tang");
        Borrower borrower2 = new Borrower(2L, "crystal@example.com", "Crystal");
        List<Borrower> borrowers = Arrays.asList(borrower1, borrower2);

        when(borrowerRepository.findAll()).thenReturn(borrowers);
        when(modelMapper.map(borrower1, BorrowerDTO.class)).thenReturn(new BorrowerDTO(1L, "raymond.tang@example.com", "Raymond Tang"));
        when(modelMapper.map(borrower2, BorrowerDTO.class)).thenReturn(new BorrowerDTO(2L, "crystal@example.com", "Crystal"));

        List<BorrowerDTO> result = borrowerService.getAllBorrowers();

        // Assertions
        assertEquals(2, result.size());
        assertEquals("raymond.tang@example.com", result.get(0).getEmail());
        assertEquals("Crystal", result.get(1).getName());
    }

    @Test
    void testBorrowBook_BorrowerNotFound() {
        // Given
        Long borrowerId = 1L;
        Long bookId = 1L;

        when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.empty());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> borrowerService.borrowBook(borrowerId, bookId));

        // Then
        assertEquals("Borrower not found with id: " + borrowerId, exception.getMessage());
        verify(bookRepository, never()).findById(anyLong());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testBorrowBook_BookNotFound() {
        // Given
        Long borrowerId = 1L;
        Long bookId = 1L;
        Borrower borrower = new Borrower(borrowerId, "raymond.tang@example.com", "Raymond Tang");

        when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> borrowerService.borrowBook(borrowerId, bookId));

        // Then
        assertEquals("Book not found with id: " + bookId, exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, never()).save(any(Book.class));
    }

}