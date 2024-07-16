package com.catalog.literAlura.repository;

import com.catalog.literAlura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByName(String name);

    @Query("SELECT DISTINCT b.language FROM Book b ORDER BY b.language")
    List<String> searchLanguages();

    @Query("SELECT b FROM Book b WHERE b.language = :selectedLanguage")
    List<Book> findByLanguage(String selectedLanguage);

    List<Book> findTop10ByOrderByDownloadCountDesc();

    @Query("SELECT b FROM Book b JOIN b.author a WHERE a.name ILIKE %:search%")
    List<Book> findBooksByAuthor(String search);
}
