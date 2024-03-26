package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class APIProfileController {

    @Autowired
    private UserService userService;

    interface UserBasicView extends User.BasicInfo {}

    // Get information from a user
    @JsonView(UserBasicView.class)

    @Operation(summary = "Get information for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserBasicView.class))
            }),
            @ApiResponse(responseCode = "404", description = "User not found"),
    })

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