package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.Review;
import es.codeurjc.holamundo.repository.BookRepository;
import es.codeurjc.holamundo.repository.ReviewRepository;
import es.codeurjc.holamundo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewSectionController {
    // Change this when we have session control
    private String testingCurrentUsername = "FanBook_785"; //This is the username of the current user. This is just for testing purposes

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
                            @RequestParam("comment") String comment) {
        System.out.println("Adding review" + title + " " + rating + " " + comment + " " + id);
        // Add a review to the database
        Review review = new Review(title, userRepository.findByUsername(testingCurrentUsername), rating, comment, bookRepository.findByID(id));
        reviewRepository.save(review);
        return "redirect:/book/" + id;
    }

    @DeleteMapping("/book/{id}/deleteReview/{reviewID}")
    public String deleteReview(Model model, @PathVariable int id, @PathVariable long reviewID) {
        System.out.println("Deleting review" + reviewID);
        // Delete a review from the database
        reviewRepository.deleteById(reviewID);
        return "redirect:/book/" + id;
    }

}
