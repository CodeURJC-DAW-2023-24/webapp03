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

    interface UserBasicView extends User.BasicInfo {}
    // Get information from a user
    @JsonView(UserBasicView.class)
    @GetMapping("/api/users/{username}")
    // return user data as a JSON 
    public ResponseEntity<?> getReviews(@PathVariable String username) {
        User user = userService.getUser(username);
        // Check if the book exists
        if (user == null) {
            return new ResponseEntity<>("User nor found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }
}