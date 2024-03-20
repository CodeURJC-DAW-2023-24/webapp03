package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.entity.Review;
import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.service.BookReviewService;
import es.codeurjc.webapp03.service.BookService;
import es.codeurjc.webapp03.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;

import java.net.URI;
import java.security.Principal;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
public class APIReviewController {

    @Autowired
    private BookReviewService reviewService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    // Get reviews for a book
    @JsonView(Review.BasicInfo.class)
    @GetMapping("/api/reviews/book/{id}")
    // return a list of reviews as a JSON (returns as many reviews as the size parameter)
    public ResponseEntity<?> getReviews(@PathVariable int id, @RequestParam(value = "size", defaultValue = "10") int size) {
        // Check if the book exists
        if (bookService.getBook(id) == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(reviewService.findReviews(id, size), HttpStatus.OK);
        }
    }

    // Add review
    interface ReviewBasicView extends Review.BasicInfo {}
    @JsonView(ReviewBasicView.class)
    @PostMapping("/api/review/book/{id}") // return the added review as a JSON
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Review> addReview(HttpServletRequest request,
                                            @PathVariable int id,
                                            @RequestBody Review review) {

        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            User user = userService.getUser(principal.getName());
            Book book = bookService.getBook(id);
            if (user == null || book == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                review.setAuthor(user);
                review.setBook(book);
                Review savedReview = reviewService.saveReview(review); // This method returns the saved review if successful

                if (savedReview == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }

                URI location = fromCurrentRequest().path("/{id}").buildAndExpand(savedReview.getID()).toUri();

                return ResponseEntity.created(location).body(review);
            }
        }
    }
    // Delete review
    @DeleteMapping("/api/review/{reviewID}") // return a message stating that the review was deleted
    public String deleteReview(@PathVariable long reviewID, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return "You are not logged in";
        } else {
            //Check if the review exists
            if (reviewService.getReview(reviewID) == null) {
                return "Review not found";
            } else {
                if (reviewService.deleteIfCorrectUser(reviewID, principal.getName())) {
                    return "Review deleted";
                } else {
                    return "You are not the owner of the review or you lack permissions to perform this action.";
                }
            }
        }
    }

    // Get review
    @JsonView(Review.BasicInfo.class)
    @GetMapping("/api/review/{reviewID}") // return the review as a JSON
    public ResponseEntity<?> getReview(@PathVariable long reviewID) {
        Review review = reviewService.getReview(reviewID);
        if (review == null) {
            return new ResponseEntity<>("Review not found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(review, HttpStatus.OK);
        }
    }


}
