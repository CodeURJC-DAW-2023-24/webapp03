package es.codeurjc.holamundo.repository;

import es.codeurjc.holamundo.entity.Author;
import es.codeurjc.holamundo.entity.Book;
import es.codeurjc.holamundo.entity.Genre;
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

    List<Book> findByGenre(Genre genre);

    // return page of books that match a given genre
    Page<Book> findByGenre(Genre genre, Pageable pageable);

    Page<Book> findByGenreIn(List<Genre> genres, Pageable pageable);

    Page<Book> findByAuthorString(String author, Pageable pageable);


}
