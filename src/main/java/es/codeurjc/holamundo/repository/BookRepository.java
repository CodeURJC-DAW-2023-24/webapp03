package es.codeurjc.holamundo.repository;

import es.codeurjc.holamundo.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByTitle(String title);

    Book findByID(long ID);

    @Query("SELECT b FROM Book b " +
            "JOIN b.author a " +
            "WHERE b.title LIKE %:searchQuery% " +
            "OR a.name LIKE %:searchQuery% " +
            "OR b.description LIKE %:searchQuery% " +
            "OR b.ISBN LIKE %:searchQuery% " +
            "OR b.publisher LIKE %:searchQuery% " +
            "OR b.series LIKE %:searchQuery% " +
            "OR b.genre.name LIKE %:searchQuery%")
    Page<Book> searchBooks(String searchQuery, Pageable pageable);

    @Query(value = "SELECT r.rating FROM Review r JOIN r.book b WHERE b.ID = :bookId")
    List<Double> getRatingsByBookId(long bookId);


}
