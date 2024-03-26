package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.entity.Genre;
import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.service.AuthorService;
import es.codeurjc.webapp03.service.BookService;
import es.codeurjc.webapp03.service.GenreService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class APIAdminPageController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private UserService userService;

    interface UserBasicView extends User.BasicInfo {}

    @Operation(summary = "Add author role to user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role changed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "You don't have permission to do this", content = @Content),
            @ApiResponse(responseCode = "409", description = "User already has this role", content = @Content)
    })
    @JsonView(APIAdminPageController.UserBasicView.class)
    @PutMapping("api/authors/{username}")
    public ResponseEntity<?> setAuthor(HttpServletRequest request, @PathVariable String username){
        //Check if the user exists
        Principal loggedUser = request.getUserPrincipal();
        User user = userService.getUser(loggedUser.getName());

        if(user.getRole().contains("ADMIN")){
            User userToAuthor = userService.getUser(username);
            if(userToAuthor != null){
                if(userToAuthor.getRole().contains("AUTHOR")) return new ResponseEntity<>("User already has this role", HttpStatus.CONFLICT);
                userService.makeAuthor(userToAuthor);
                return new ResponseEntity<>(userToAuthor, HttpStatus.OK);
            } else{
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

        } else{
            return new ResponseEntity<>("You don't have permission to do this", HttpStatus.UNAUTHORIZED);
        }
    }




}
