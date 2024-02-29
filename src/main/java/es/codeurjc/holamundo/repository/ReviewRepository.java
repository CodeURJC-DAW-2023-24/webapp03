package es.codeurjc.holamundo.repository;

import es.codeurjc.holamundo.entity.Book;
import es.codeurjc.holamundo.entity.Review;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByID(long ID);

    Page<Review> findByBookID(long bookID, Pageable pageable);

    List<Review> findByBook(Book book);

    boolean existsByBookIDAndAuthorUsername(long bookID, String username);

    Review findByBookIDAndAuthorUsername(long bookID, String username);
}
