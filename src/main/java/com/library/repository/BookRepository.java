package com.library.repository;

import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Find all books that are currently available.
     */
    List<Book> findByAvailabilityTrue();

    /**
     * Search books by title (case-insensitive, partial match).
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Search books by author (case-insensitive, partial match).
     */
    List<Book> findByAuthorContainingIgnoreCase(String author);

    /**
     * Search books by title or author (case-insensitive, partial match).
     */
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
}
