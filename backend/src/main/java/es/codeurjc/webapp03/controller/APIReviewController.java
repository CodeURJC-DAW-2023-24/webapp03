package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
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

import java.security.Principal;

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
    @GetMapping("/api/book/{id}/reviews")
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
    public Review addReview(HttpServletRequest request,
                            @PathVariable int id,
                            @RequestParam("rating") int rating,
                            @RequestParam("reviewTitle") String title,
                            @RequestParam("comment") String comment) {

        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return null;
        } else {
            Review review = new Review(title, userService.getUser(principal.getName()), rating, comment, bookService.getBook(id));
            return reviewService.saveReview(review);
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


}
