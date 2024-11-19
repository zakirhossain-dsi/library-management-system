package com.example.lms.service;

import com.example.lms.dto.BookDTO;
import com.example.lms.entity.Book;
import com.example.lms.entity.Borrower;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.BookRepository;
import com.example.lms.repository.BorrowerRepository;
import com.example.lms.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BorrowerRepository borrowerRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookDTO createBookDTO(String isbn, String title, String author) {
        return new BookDTO(null, isbn, title, author, null);
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRegisterBook() {
        // Given
        BookDTO bookDTO = createBookDTO("978-1-60309-502-1", "The Clean Coder", "Robert C. Martin");
        Book book = new Book(1L, "978-1-60309-502-1", "The Clean Coder", "Robert C. Martin", null);

        // Mocking
        when(modelMapper.map(bookDTO, Book.class)).thenReturn(book);
        when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        BookDTO savedBook = bookService.registerBook(bookDTO);

        // Then
        assertEquals(bookDTO.getIsbn(), savedBook.getIsbn());
        assertEquals(bookDTO.getTitle(), savedBook.getTitle());
        assertEquals(bookDTO.getAuthor(), savedBook.getAuthor());
    }

    @Test
    void testRegisterBookWithExistingISBNAndSameTitleAuthor() {
        // Given
        BookDTO bookDTO = createBookDTO("978-1-60309-502-1", "The Clean Coder", "Robert C. Martin");
        Book existingBook = new Book(1L, "978-1-60309-502-1", "The Clean Coder", "Robert C. Martin", null);
        List<Book> existingBooks = List.of(existingBook);

        // Mocking
        when(bookRepository.findByIsbn(bookDTO.getIsbn())).thenReturn(existingBooks);
        when(modelMapper.map(bookDTO, Book.class)).thenReturn(existingBook);

        // Assertions
        assertDoesNotThrow(() -> bookService.registerBook(bookDTO));
    }

    @Test
    void testRegisterBookWithExistingISBNAndDifferentTitle() {
        // Given
        BookDTO bookDTO = createBookDTO("978-1-60309-502-1", "The Clean Coder", "Robert C. Martin");
        Book existingBook = new Book(1L, "978-1-60309-502-1", "The Clean Code", "Robert C. Martin", null);
        List<Book> existingBooks = List.of(existingBook);

        // Mocking
        when(bookRepository.findByIsbn(bookDTO.getIsbn())).thenReturn(existingBooks);

        // Assertions
        assertThrows(IllegalArgumentException.class, () -> bookService.registerBook(bookDTO));
    }

    @Test
    void testRegisterBookWithExistingISBNAndDifferentAuthor() {
        // Given
        BookDTO bookDTO = createBookDTO("978-1-60309-502-1", "The Clean Coder", "Robert C. Martin");
        Book existingBook = new Book(1L, "978-1-60309-502-1", "The Clean Coder", "Robert Martin", null);
        List<Book> existingBooks = List.of(existingBook);

        // Mocking
        when(bookRepository.findByIsbn(bookDTO.getIsbn())).thenReturn(existingBooks);

        // Assertions
        assertThrows(IllegalArgumentException.class, () -> bookService.registerBook(bookDTO));
    }

    @Test
    void testGetAllBooks() {
        // Given
        Book book1 = new Book(1L, "978-1-60309-502-1", "The Clean Coder", "Robert C. Martin", null);
        Book book2 = new Book(2L, "978-1-60309-502-2", "Head First OOP", "Brett McLaughlin", null);
        List<Book> mockBooks = Arrays.asList(book1, book2);

        // Mocking
        when(bookRepository.findAll()).thenReturn(mockBooks);
        when(modelMapper.map(book1, BookDTO.class)).thenReturn(new BookDTO(book1.getId(), book1.getIsbn(), book1.getTitle(), book1.getAuthor(), null));
        when(modelMapper.map(book2, BookDTO.class)).thenReturn(new BookDTO(book2.getId(), book2.getIsbn(), book2.getTitle(), book2.getAuthor(), null));

        // When
        List<BookDTO> books = bookService.getAllBooks();

        // Then
        assertEquals(2, books.size());
        assertEquals(book1.getIsbn(), books.get(0).getIsbn());
        assertEquals(book2.getIsbn(), books.get(1).getIsbn());
    }


    @Test
    void testRegisterBookWithValidBorrowerId() {
        // Given
        BookDTO bookDTO = createBookDTO("978-1-60309-502-1", "Head First OOP", "Brett McLaughlin");
        bookDTO.setBorrowerId(1L);
        Borrower borrower = new Borrower(1L, "raymond.tang@example.com", "Raymond Tang");

        // Mocking
        when(modelMapper.map(bookDTO, Book.class)).thenReturn(new Book());
        when(modelMapper.map(new Book(), BookDTO.class)).thenReturn(bookDTO);
        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.save(any(Book.class))).thenReturn(new Book());

        // When
        BookDTO savedBook = bookService.registerBook(bookDTO);

        // Assertions
        assertNotNull(savedBook);
        assertEquals(bookDTO.getBorrowerId(), savedBook.getBorrowerId());
    }

    @Test
    void testRegisterBookWithInvalidBorrowerId() {
        // Given
        BookDTO bookDTO = createBookDTO("978-1-60309-502-1", "Head First Design Patterns", "Kathy Sierra");
        bookDTO.setBorrowerId(999L);

        // Mocking
        when(modelMapper.map(bookDTO, Book.class)).thenReturn(new Book());
        when(borrowerRepository.findById(999L)).thenReturn(Optional.empty());

        // Assertions
        assertThrows(ResourceNotFoundException.class, () -> bookService.registerBook(bookDTO));
    }

    @Test
    void testRegisterBookWithNullBorrowerId() {
        // Given
        BookDTO bookDTO = createBookDTO("978-1-60309-502-1", "Head First Design Pattern", "Kathy Sierra");
        bookDTO.setBorrowerId(null);

        // Mocking
        when(modelMapper.map(bookDTO, Book.class)).thenReturn(new Book());
        when(modelMapper.map(new Book(), BookDTO.class)).thenReturn(bookDTO);
        when(bookRepository.save(any(Book.class))).thenReturn(new Book());

        // When
        BookDTO savedBook = bookService.registerBook(bookDTO);

        // Then
        assertNotNull(savedBook);
        assertNull(savedBook.getBorrowerId());
    }

    @Test
    void testRegisterBookWithZeroBorrowerId() {
        // Given
        BookDTO bookDTO = createBookDTO("978-1-60309-502-1", "Head First Design Pattern", "Kathy Sierra");
        bookDTO.setBorrowerId(0L);

        // Mocking
        when(modelMapper.map(bookDTO, Book.class)).thenReturn(new Book());
        when(modelMapper.map(new Book(), BookDTO.class)).thenReturn(bookDTO);
        when(bookRepository.save(any(Book.class))).thenReturn(new Book());

        // When
        BookDTO savedBook = bookService.registerBook(bookDTO);

        // Then
        assertNotNull(savedBook);
        assertEquals(0L, savedBook.getBorrowerId());
    }

}
