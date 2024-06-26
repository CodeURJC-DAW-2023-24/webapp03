package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.codeurjc.webapp03.entity.User;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class APIAdminController {

    @Autowired
    private UserService userService;

    interface UserBasicView extends User.BasicInfo {}

    @Operation(summary = "Add or remove author role to the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role changed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "You don't have permission to do this", content = @Content),
    })
    @JsonView(APIAdminController.UserBasicView.class)
    @PutMapping("/api/authors/{username}")
    public ResponseEntity<?> changeAuthorRole(HttpServletRequest request, @PathVariable String username){
        //Check if the user exists
        Principal loggedUser = request.getUserPrincipal();
        User user = userService.getUser(loggedUser.getName());

        if(user.getRole().contains("ADMIN")){
            User userToChangeRole = userService.getUser(username);
            if(userToChangeRole != null){
                if(userToChangeRole.getRole().contains("AUTHOR")){
                    userService.removeAuthor(userToChangeRole);
                    return new ResponseEntity<>("Author role removed", HttpStatus.OK);
                } else {
                    userService.makeAuthor(userToChangeRole);
                    return new ResponseEntity<>("Author role added", HttpStatus.OK);
                }
            } else{
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

        } else{
            return new ResponseEntity<>("You don't have permission to do this", HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @JsonView(APIAdminController.UserBasicView.class)
    @DeleteMapping("/api/users/{username}")
    public ResponseEntity<?> deleteUser(HttpServletRequest request, @PathVariable String username){
        User user = userService.getUser(username);
        // Check if the user exists
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } else {
            Principal loggedUser = request.getUserPrincipal();
            User currentUser = userService.getUser(loggedUser.getName());
            if(currentUser.getRole().contains("ADMIN")){
                //Delete from users lists
                userService.deleteUser(user);

                return new ResponseEntity<>(user.getUsername()+" has been deleted", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("You dont have permission to do this!", HttpStatus.FORBIDDEN);
            }
        }
    }
}
