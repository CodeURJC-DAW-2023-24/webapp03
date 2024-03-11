package es.codeurjc.webapp03.service;

import es.codeurjc.webapp03.entity.Review;
import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    public List<Review> findReviews(long ID, int size) { return reviewRepository.findByBookID(ID, PageRequest.of(0, size)).getContent(); }

    public boolean existsByBookIDAndAuthorUsername(long ID, String user) { return reviewRepository.existsByBookIDAndAuthorUsername(ID, user); }

    public Review findByBookIDAndAuthorUsername(long ID, String user) { return reviewRepository.findByBookIDAndAuthorUsername(ID, user); }

    public List<Review> findByBook(Book book) { return reviewRepository.findByBook(book); }

    public List<Review> findByBookID(long ID, int page, int size) { return reviewRepository.findByBookID(ID, PageRequest.of(page, size)).getContent(); }

    public void deleteReview(Review review) {
        reviewRepository.delete(review);
    }

    public void saveReview(Review r) {
        reviewRepository.save(r);
    }

    public void deleteIfCorrectUser(long reviewID, String authUser) {
        if (reviewRepository.findByID(reviewID).getUser().getUsername().equals(authUser) || userService.getUser(authUser).getRole().contains("ADMIN")) {
            reviewRepository.deleteById(reviewID);
        }
    }


}
