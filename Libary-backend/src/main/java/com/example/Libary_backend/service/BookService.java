package com.example.Libary_backend.service;

import java.util.List;

import com.example.Libary_backend.dto.BookRequestDTO;
import com.example.Libary_backend.dto.BookResponseDTO;

public interface BookService {

    BookResponseDTO addBook(BookRequestDTO requestDTO);

    List<BookResponseDTO> getAllBooks();

    List<BookResponseDTO> getAvailableBooks();

    List<BookResponseDTO> searchBooks(String keyword);
}