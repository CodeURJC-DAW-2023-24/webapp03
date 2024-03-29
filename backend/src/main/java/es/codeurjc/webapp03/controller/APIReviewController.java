package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.entity.Review;
import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.service.BookReviewService;
import es.codeurjc.webapp03.service.BookService;
import es.codeurjc.webapp03.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/reviews")
public class APIReviewController {

    @Autowired
    private BookReviewService reviewService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    // Get reviews for a book
    @JsonView(Review.BasicInfo.class)
    @Operation(summary = "Get reviews for a specific book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class))
            }),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "400", description = "Invalid book ID")
    })
    @GetMapping("")
    // return a list of reviews as a JSON (returns as many reviews as the size parameter)
    public ResponseEntity<?> getReviews(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size,
                                        @RequestParam("book") int book) {
        //Check if ID is valid
        if (book < 0) {
            return new ResponseEntity<>("Invalid book ID", HttpStatus.BAD_REQUEST);
        }

        // Check if the book exists
        if (bookService.getBook(book) == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(reviewService.findByBookID(book, page, size), HttpStatus.OK);
        }
    }

    // Add review
    interface ReviewBasicView extends Review.BasicInfo {}

    @Operation(summary = "Add a review for a specific book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review added", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class))
            }),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "400", description = "Invalid book ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @JsonView(ReviewBasicView.class)
    @PostMapping("") // return the added review as a JSON
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Review> addReview(HttpServletRequest request,
                                            @RequestParam("book") int id,
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

                // Check if the book id is valid
                if (id < 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }

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
    @Operation(summary = "Delete a review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted"),
            @ApiResponse(responseCode = "404", description = "Review not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Invalid review ID")
    })
    @DeleteMapping("/{reviewID}") // return a message stating that the review was deleted
    public ResponseEntity<?> deleteReview(@PathVariable long reviewID, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return new ResponseEntity<>("You are not logged in", HttpStatus.UNAUTHORIZED);
        } else {
            // Check if the review id is valid
            if (reviewID < 0) {
                return new ResponseEntity<>("Invalid review ID", HttpStatus.BAD_REQUEST);
            }

            //Check if the review exists
            if (reviewService.getReview(reviewID) == null) {
                return new ResponseEntity<>("Review not found", HttpStatus.NOT_FOUND);
            } else {
                if (reviewService.deleteIfCorrectUser(reviewID, principal.getName())) {
                    return new ResponseEntity<>("Review deleted", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("You are not the author of this review or you lack permissions to perform this action", HttpStatus.UNAUTHORIZED);
                }
            }
        }
    }

    // Get review
    @Operation(summary = "Get a review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class))
            }),
            @ApiResponse(responseCode = "404", description = "Review not found"),
            @ApiResponse(responseCode = "400", description = "Invalid review ID")
    })
    @JsonView(Review.BasicInfo.class)
    @GetMapping("/{reviewID}") // return the review as a JSON
    public ResponseEntity<?> getReview(@PathVariable long reviewID) {
        //Check if the review id is valid
        if (reviewID < 0) {
            return new ResponseEntity<>("Invalid review ID", HttpStatus.BAD_REQUEST);
        }

        Review review = reviewService.getReview(reviewID);
        if (review == null) {
            return new ResponseEntity<>("Review not found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(review, HttpStatus.OK);
        }
    }

    //Get reviews by user
    @Operation(summary = "Get reviews by a specific user (if count = true, return the number of reviews)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class)),
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))
            }),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @JsonView(Review.BasicInfo.class)
    @GetMapping("/users/{username}") // return a list of reviews as a JSON
    public ResponseEntity<?> getReviewsByUser(@PathVariable String username,
                                              @RequestParam("count") boolean count) {
        User user = userService.getUser(username);
        //Check if the user exists
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } else {
            if (count) {
                return new ResponseEntity<>(user.getReviews().size(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(user.getReviews(), HttpStatus.OK);
            }
        }
    }


}
