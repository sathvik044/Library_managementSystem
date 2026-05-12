package com.example.Libary_backend.service;

import java.util.List;

import com.example.Libary_backend.dto.request.BookResponseDTO;
import com.example.Libary_backend.dto.response.BookRequestDTO;

public interface BookService {

    BookResponseDTO addBook(BookRequestDTO requestDTO);

    List<BookResponseDTO> getAllBooks();

    List<BookResponseDTO> getAvailableBooks();

    List<BookResponseDTO> searchBooks(String keyword);
}