package es.codeurjc.holamundo.repository;

import es.codeurjc.holamundo.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByName(String name);

    //Get most read genres (by all users)
    @Query("SELECT b.genre, COUNT(b.genre) FROM User u JOIN u.readBooks b GROUP BY b.genre.name ORDER BY COUNT(b.genre) DESC")
    List<Genre> getMostReadGenres();
}
