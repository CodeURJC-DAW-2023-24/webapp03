package es.codeurjc.holamundo.repository;

import es.codeurjc.holamundo.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
