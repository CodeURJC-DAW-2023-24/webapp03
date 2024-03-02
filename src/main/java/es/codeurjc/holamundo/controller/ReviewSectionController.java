package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.Review;
import es.codeurjc.holamundo.repository.BookRepository;
import es.codeurjc.holamundo.repository.ReviewRepository;
import es.codeurjc.holamundo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewSectionController {
    // Change this when we have session control
    private String testingCurrentUsername;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/book/{id}/addReview")
    public String addReview(Model model, @PathVariable int id,
                            @RequestParam("rating") int rating,
                            @RequestParam("reviewTitle") String title,
                            @RequestParam("comment") String comment, HttpServletRequest request) {

        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            testingCurrentUsername = authentication.getName();
        }

        // Add a review to the database
        Review review = new Review(title, userRepository.findByUsername(testingCurrentUsername), rating, comment, bookRepository.findByID(id));
        reviewRepository.save(review);
        return "redirect:/book/" + id;
    }

    @DeleteMapping("/book/{id}/deleteReview/{reviewID}")
    public String deleteReview(Model model, @PathVariable int id, @PathVariable long reviewID, HttpServletRequest request) {

        // Check that the user is the owner of the review
        Authentication authentication = (Authentication) request.getUserPrincipal();

        // Check role (if admin, allow to delete any review)
        if (authentication != null) {
            testingCurrentUsername = authentication.getName();

            if (reviewRepository.findByID(reviewID).getUser().getUsername().equals(testingCurrentUsername) || userRepository.findByUsername(testingCurrentUsername).getRole().contains("ADMIN")) {
                reviewRepository.deleteById(reviewID);
            }
        }

        return "redirect:/book/" + id;
    }

}
