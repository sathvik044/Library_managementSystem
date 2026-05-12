package com.example.Libary_backend.mapper;

import org.springframework.stereotype.Component;

import com.example.Libary_backend.dto.request.BookResponseDTO;
import com.example.Libary_backend.dto.response.BookRequestDTO;
import com.example.Libary_backend.entity.Book;

@Component
public class BookMapper {

    public Book toEntity(BookRequestDTO requestDTO) {
        Book book = new Book();
        book.setTitle(requestDTO.getTitle());
        book.setAuthor(requestDTO.getAuthor());
        book.setAvailability(true);
        return book;
    }

    public BookResponseDTO toResponseDTO(Book book) {
        return new BookResponseDTO(
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getAvailability()
        );
    }
}
