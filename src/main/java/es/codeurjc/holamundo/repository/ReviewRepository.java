package es.codeurjc.holamundo.repository;

import es.codeurjc.holamundo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
