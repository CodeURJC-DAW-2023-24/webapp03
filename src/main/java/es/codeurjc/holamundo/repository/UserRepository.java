package es.codeurjc.holamundo.repository;

import es.codeurjc.holamundo.entity.Book;
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

}
