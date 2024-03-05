package es.codeurjc.webapp03.repository;

import es.codeurjc.webapp03.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByName(String name);

    // Get most read authors (by all users)
    @Query("SELECT a FROM User u JOIN u.readBooks b JOIN b.author a GROUP BY a ORDER BY COUNT(a) DESC")
    List<Author> getMostReadAuthors();

    // Get most read authors (by all users) but return both the name of the author and the count for such author
    @Query("SELECT a.name, COUNT(a) FROM User u JOIN u.readBooks b JOIN b.author a GROUP BY a.name ORDER BY COUNT(a) DESC")
    List<Object[]> getMostReadAuthorsNameAndCount();
}
