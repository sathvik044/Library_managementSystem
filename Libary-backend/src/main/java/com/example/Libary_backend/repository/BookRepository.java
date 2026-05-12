package com.example.Libary_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Libary_backend.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAvailabilityTrue();

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title,
            String author
    );

    boolean existsByTitleIgnoreCaseAndAuthorIgnoreCase(String title, String author);
}