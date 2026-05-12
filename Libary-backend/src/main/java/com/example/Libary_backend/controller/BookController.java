package com.example.Libary_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.Libary_backend.dto.request.BookResponseDTO;
import com.example.Libary_backend.dto.response.BookRequestDTO;
import com.example.Libary_backend.service.BookService;
import org.springframework.http.HttpStatus;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Add a new book
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDTO addBook(@Valid @RequestBody BookRequestDTO requestDTO) {
        return bookService.addBook(requestDTO);
    }

    // View all books
    @GetMapping
    public List<BookResponseDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    // View available books
    @GetMapping("/available")
    public List<BookResponseDTO> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }

    // Search by title or author
    @GetMapping("/search")
    public List<BookResponseDTO> searchBooks(@RequestParam String keyword) {
        return bookService.searchBooks(keyword);
    }
}