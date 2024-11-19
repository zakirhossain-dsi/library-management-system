package com.example.lms.service.impl;

import com.example.lms.dto.BorrowerDTO;
import com.example.lms.entity.Book;
import com.example.lms.entity.Borrower;
import com.example.lms.exception.BookAlreadyBorrowedException;
import com.example.lms.exception.BookNotBorrowedException;
import com.example.lms.exception.DuplicateBorrowerEmailException;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.BookRepository;
import com.example.lms.repository.BorrowerRepository;
import com.example.lms.service.BorrowerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private static final String BORROWER_NOT_FOUND = "Borrower not found with id: ";

    @Autowired
    public BorrowerServiceImpl(BorrowerRepository borrowerRepository, BookRepository bookRepository, ModelMapper modelMapper) {
        this.borrowerRepository = borrowerRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BorrowerDTO registerBorrower(BorrowerDTO borrowerDTO) {
        if (borrowerRepository.existsByEmail(borrowerDTO.getEmail())) {
            throw new DuplicateBorrowerEmailException("A borrower with this email already exists.");
        }

        Borrower borrower = modelMapper.map(borrowerDTO, Borrower.class);
        Borrower savedBorrower = borrowerRepository.save(borrower);
        return modelMapper.map(savedBorrower, BorrowerDTO.class);
    }
    @Override
    public void borrowBook(Long borrowerId, Long bookId) {

        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException(BORROWER_NOT_FOUND + borrowerId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        if (book.isAlreadyBorrowed()) {
            throw new BookAlreadyBorrowedException("Book is already borrowed");
        }

        book.setBorrower(borrower);

        bookRepository.save(book);
    }

    @Override
    public void returnBook(Long borrowerId, Long bookId) {

        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException(BORROWER_NOT_FOUND + borrowerId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        if (!book.isBorrowedBy(borrower)) {
            throw new BookNotBorrowedException("Book was not borrowed by this borrower");
        }

        book.setBorrower(null);
        bookRepository.save(book);
    }

    @Override
    public List<BorrowerDTO> getAllBorrowers() {
        return borrowerRepository.findAll().stream()
                .map(borrower -> modelMapper.map(borrower, BorrowerDTO.class))
                .toList();
    }

    @Override
    public BorrowerDTO getBorrowerById(Long borrowerId) {
        return borrowerRepository.findById(borrowerId)
                .map(borrower -> modelMapper.map(borrower, BorrowerDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException(BORROWER_NOT_FOUND + borrowerId));
    }

}