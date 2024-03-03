package es.codeurjc.webapp03.repository;

import es.codeurjc.webapp03.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, String> {
    Genre findByName(String name);

    //Get most read genres (by all users)
    @Query("SELECT b.genre, COUNT(b.genre) FROM User u JOIN u.readBooks b GROUP BY b.genre.name ORDER BY COUNT(b.genre) DESC")
    List<Genre> getMostReadGenres();

    //Get most read genres (by all users) but return both the name of the genre and the count for such genre
    @Query("SELECT b.genre.name, COUNT(b.genre) FROM User u JOIN u.readBooks b GROUP BY b.genre.name ORDER BY COUNT(b.genre) DESC")
    List<Object[]> getMostReadGenresNameAndCount();
}
