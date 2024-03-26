package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

@RestController
public class APISignUpController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    interface UserBasicView extends User.BasicInfo {}

    // Add User
    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created correctly", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "409", description = "Username not available", content = @Content), //Conflict
            @ApiResponse(responseCode = "500", description = "Image not supported. Try different file", content = @Content) //Internal server error

    })
    @JsonView(APISignUpController.UserBasicView.class)
    @PostMapping("/api/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addUser(@RequestParam("username") String inputName,
                                     @RequestParam("alias") String inputAlias,
                                     @RequestParam("email") String inputEmail,
                                     @RequestParam("password") String password,
                                     @RequestParam(value = "imageFile", required = false)MultipartFile image) throws SQLException, IOException {

        if(!userService.isUsernameAvailable(inputName)) return new ResponseEntity<>("Username not available",HttpStatus.CONFLICT);

        //Should add checker for email
        User newUser = new User(inputName, inputAlias, "Hi, im new!","", inputEmail, passwordEncoder.encode(password), "USER");

        //Check if image file is added as param
        if (image != null && !image.isEmpty()) {
            //Check if image file is correct
            try (InputStream input = image.getInputStream()) {
                try {
                    ImageIO.read(input).toString(); //If it doesn't fail, this is an image.
                    newUser.setProfileImageFile(BlobProxy.generateProxy(image.getInputStream(), image.getSize()));
                } catch (Exception e) {
                    // It's not an image.
                    return new ResponseEntity<>("Image not supported. Try different file", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        userService.saveUser(newUser);
        return new ResponseEntity<>(newUser,HttpStatus.OK);
    }

}
