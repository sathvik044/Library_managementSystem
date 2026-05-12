package com.library.controller;

import com.library.dto.ApiResponse;
import com.library.model.Book;
import com.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * POST /books - Add a new book.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Book>> addBook(@Valid @RequestBody Book book) {
        Book saved = bookService.addBook(book);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Book added successfully", saved));
    }

    /**
     * GET /books - View all books.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Book>>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(
                ApiResponse.success("Retrieved all books (" + books.size() + ")", books));
    }

    /**
     * GET /books/available - View available books only.
     */
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<Book>>> getAvailableBooks() {
        List<Book> books = bookService.getAvailableBooks();
        return ResponseEntity.ok(
                ApiResponse.success("Retrieved available books (" + books.size() + ")", books));
    }

    /**
     * GET /books/search?keyword=... - Search by title or author.
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Book>>> searchBooks(@RequestParam String keyword) {
        List<Book> books = bookService.searchBooks(keyword);
        return ResponseEntity.ok(
                ApiResponse.success("Found " + books.size() + " book(s) matching '" + keyword + "'", books));
    }

    /**
     * GET /books/{bookId} - View a specific book.
     */
    @GetMapping("/{bookId}")
    public ResponseEntity<ApiResponse<Book>> getBookById(@PathVariable Long bookId) {
        Book book = bookService.getBookById(bookId);
        return ResponseEntity.ok(ApiResponse.success("Book retrieved", book));
    }
}
