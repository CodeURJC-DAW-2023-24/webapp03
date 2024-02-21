package es.codeurjc.holamundo.repository;

import es.codeurjc.holamundo.entity.Author;
import es.codeurjc.holamundo.entity.Book;
import es.codeurjc.holamundo.entity.Genre;
import es.codeurjc.holamundo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT u.readBooks FROM User u WHERE u.username = :username")
    Page<Book> getReadBooks(String username, Pageable pageable);

    @Query("SELECT u.readingBooks FROM User u WHERE u.username = :username")
    Page<Book> getReadingBooks(String username, Pageable pageable);

    @Query("SELECT u.wantedBooks FROM User u WHERE u.username = :username")
    Page<Book> getWantedBooks(String username, Pageable pageable);

    // Get most read books genres by a specific user
    @Query("SELECT b.genre, COUNT(b.genre) FROM User u JOIN u.readBooks b WHERE u.username = :username GROUP BY b.genre.name ORDER BY COUNT(b.genre) DESC")
    List<Genre> getMostReadGenres(String username);

    // Get most read books authors by a specific user
    @Query("SELECT a.name, COUNT(a) FROM User u JOIN u.readBooks b JOIN b.author a WHERE u.username = :username GROUP BY a.name ORDER BY COUNT(a) DESC")
    List<Author> getMostReadAuthors(String username);

    @Query("SELECT u.profileImage FROM User u WHERE u.username = :username")
    String getProfileImageByUsername(String username);

}
