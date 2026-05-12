package com.example.Libary_backend.service.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Libary_backend.dto.request.BookResponseDTO;
import com.example.Libary_backend.dto.response.BookRequestDTO;
import com.example.Libary_backend.entity.Book;
import com.example.Libary_backend.exception.BookAlreadyExistsException;
import com.example.Libary_backend.exception.InvalidSearchKeywordException;
import com.example.Libary_backend.mapper.BookMapper;
import com.example.Libary_backend.repository.BookRepository;
import com.example.Libary_backend.service.BookService;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookResponseDTO addBook(BookRequestDTO requestDTO) {

        boolean bookExists = bookRepository.existsByTitleIgnoreCaseAndAuthorIgnoreCase(
                requestDTO.getTitle(),
                requestDTO.getAuthor());

        if (bookExists) {
            throw new BookAlreadyExistsException("Book already exists with the same title and author");
        }

        Book book = bookMapper.toEntity(requestDTO);
        Book savedBook = bookRepository.save(book);

        return bookMapper.toResponseDTO(savedBook);
    }

    @Override
    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<BookResponseDTO> getAvailableBooks() {
        return bookRepository.findByAvailabilityTrue()
                .stream()
                .map(bookMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<BookResponseDTO> searchBooks(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            throw new InvalidSearchKeywordException("Search keyword cannot be empty");
        }

        return bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(bookMapper::toResponseDTO)
                .toList();
    }
}