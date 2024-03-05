package es.codeurjc.webapp03.repository;

import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.entity.Review;

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
