package es.codeurjc.webapp03.controller;

import es.codeurjc.webapp03.entity.Review;
import es.codeurjc.webapp03.repository.BookRepository;
import es.codeurjc.webapp03.repository.ReviewRepository;
import es.codeurjc.webapp03.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReviewSectionController {
    // Change this when we have session control
    private String testingCurrentUsername;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserService userService;

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
        Review review = new Review(title, userService.getUser(testingCurrentUsername), rating, comment, bookRepository.findByID(id));
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

            if (reviewRepository.findByID(reviewID).getUser().getUsername().equals(testingCurrentUsername) || userService.getUser(testingCurrentUsername).getRole().contains("ADMIN")) {
                reviewRepository.deleteById(reviewID);
            }
        }

        return "redirect:/book/" + id;
    }

}
