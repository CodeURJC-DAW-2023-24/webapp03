package es.codeurjc.webapp03.service;

import es.codeurjc.webapp03.entity.Review;
import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    public Review getReview(long ID) {
        Optional<Review> review = reviewRepository.findById(ID);
        return review.orElse(null);
    }

    public List<Review> findReviews(long ID, int size) { return reviewRepository.findByBookID(ID, PageRequest.of(0, size)).getContent(); }

    public boolean existsByBookIDAndAuthorUsername(long ID, String user) { return reviewRepository.existsByBookIDAndAuthorUsername(ID, user); }

    public Review findByBookIDAndAuthorUsername(long ID, String user) { return reviewRepository.findByBookIDAndAuthorUsername(ID, user); }

    public List<Review> findByBook(Book book) { return reviewRepository.findByBook(book); }

    public List<Review> findByBookID(long ID, int page, int size) { return reviewRepository.findByBookID(ID, PageRequest.of(page, size)).getContent(); }

    public void deleteReview(Review review) {
        reviewRepository.delete(review);
    }

    public double getAverageRating(Book book) {
        List<Double> ratings = bookService.getRatings(book.getID());
        double averageRating = 0;
        if (ratings.size() > 0) {
            for (Double rating : ratings) {
                averageRating += rating;
            }
            averageRating /= ratings.size();
        }
        averageRating = Math.round(averageRating * 100.0) / 100.0;
        return averageRating;
    }

    public Review saveReview(Review r) {
        // Check that the user has not already reviewed the book (if so, do not add the review)
        if (!existsByBookIDAndAuthorUsername(r.getBook().getID(), r.getUser().getUsername())) {
            reviewRepository.save(r);

            double averageRating = getAverageRating(r.getBook());
            r.getBook().setAverageRating(averageRating);
            bookService.saveBook(r.getBook());

            return r;
        } else {
            return null;
        }
    }

    public boolean deleteIfCorrectUser(long reviewID, String authUser) {
        if (reviewRepository.findByID(reviewID).getUser().getUsername().equals(authUser) || userService.getUser(authUser).getRole().contains("ADMIN")) {
            long bookID = reviewRepository.findByID(reviewID).getBook().getID();
            reviewRepository.deleteById(reviewID);
            bookService.updateAverageRating(bookID);
            return true;
        }
        return false;
    }


}
