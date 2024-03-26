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
public class APIProfilePageController {

    @Autowired
    private UserService userService;

    // Get information from a user
    @JsonView(Review.BasicInfo.class)
    @GetMapping("/api/user/{username}")
    // return user data as a JSON 
    public ResponseEntity<?> getReviews(@PathVariable String username) {
        // Check if the book exists
        if (userService.getUser(username) == null) {
            return new ResponseEntity<>("User nor found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userService.getUser(username), HttpStatus.OK);
        }
    }

    // Delete user
    @DeleteMapping("/api/user/{username}") // return a message stating that the review was deleted
    public String deleteReview(@PathVariable String username, HttpServletRequest request) {

        boolean isAdmin = false;

        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return "You are not logged in";
        } else {
            //Check if the user exists
            if (userService.getUser(username) == null) {
                return "Review not found";
            } else {/* 
                for (String userRole: userService.getUser(username).getRole()) {
                    if(userRole.equals("ADMIN")){
                        isAdmin = true;
                    }
                }  */
                if (isAdmin == true) {
                    return "Review deleted";
                } else {
                    return "You lack permissions to perform this action.";
                }
            }
        }
    }
}