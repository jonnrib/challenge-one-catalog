package com.catalog.literAlura.repository;

import com.catalog.literAlura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Boolean existsByName(String name);

    Author findByName(String name);

    @Query("SELECT a FROM Author a WHERE a.deathYear >= :selectedYear AND :selectedYear >= a.birthYear")
    List<Author> findLivingAuthorsByYear(int selectedYear);

    @Query("SELECT a FROM Author a WHERE a.name ILIKE %:search%")
    List<Author> findByNameContainingIgnoreCase(String search);
}
