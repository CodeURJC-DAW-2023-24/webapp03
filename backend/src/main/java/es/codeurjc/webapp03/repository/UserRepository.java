package es.codeurjc.webapp03.repository;

import es.codeurjc.webapp03.entity.Author;
import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.entity.Genre;
import es.codeurjc.webapp03.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE " +
            "u.username LIKE %:searchTerm% OR " +
                    "u.alias LIKE %:searchTerm% OR " +
                    "u.description LIKE %:searchTerm% OR " +
                    "u.email LIKE %:searchTerm%")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);

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

    @Query("SELECT u.profileImageString FROM User u WHERE u.username = :username")
    String getProfileImageStringByUsername(String username);

    // get the user names and the number of books they have read
    @Query("SELECT u.username, COUNT(b) FROM User u JOIN u.readBooks b GROUP BY u.username ORDER BY COUNT(b) DESC")
    List<Object[]> getUsersAndNumberOfBooksRead();

    // get number of books in the wanted list of a user
    @Query("SELECT COUNT(b) FROM User u JOIN u.wantedBooks b WHERE u.username = :username")
    int getNumberOfWantedBooks(String username);

    // get number of books in the read list of a user
    @Query("SELECT COUNT(b) FROM User u JOIN u.readBooks b WHERE u.username = :username")
    int getNumberOfReadBooks(String username);

    // get number of books in the reading list of a user
    @Query("SELECT COUNT(b) FROM User u JOIN u.readingBooks b WHERE u.username = :username")
    int getNumberOfReadingBooks(String username);

    // get username by email
    User findByEmail(String email);

}
