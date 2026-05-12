package com.library.service;

import com.library.exception.BusinessRuleException;
import com.library.exception.ResourceNotFoundException;
import com.library.model.Book;
import com.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Add a new book to the library.
     */
    public Book addBook(Book book) {
        book.setAvailability(true);
        return bookRepository.save(book);
    }

    /**
     * Get all books in the library.
     */
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Get all available books.
     */
    @Transactional(readOnly = true)
    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailabilityTrue();
    }

    /**
     * Search books by title or author.
     */
    @Transactional(readOnly = true)
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BusinessRuleException("Search keyword cannot be empty");
        }
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                keyword.trim(), keyword.trim());
    }

    /**
     * Get a book by its ID.
     */
    @Transactional(readOnly = true)
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));
    }

    /**
     * Update book availability.
     */
    public void updateAvailability(Long bookId, boolean available) {
        Book book = getBookById(bookId);
        book.setAvailability(available);
        bookRepository.save(book);
    }
}
