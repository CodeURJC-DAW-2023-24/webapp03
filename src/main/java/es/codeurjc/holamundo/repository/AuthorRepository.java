package es.codeurjc.holamundo.repository;

import es.codeurjc.holamundo.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByName(String name);

    // Get most read authors (by all users)
    @Query("SELECT a FROM User u JOIN u.readBooks b JOIN b.author a GROUP BY a ORDER BY COUNT(a) DESC")
    List<Author> getMostReadAuthors();
}
