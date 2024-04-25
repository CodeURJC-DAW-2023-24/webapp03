package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.service.EmailService;
import es.codeurjc.webapp03.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Map;

@RestController
public class APIEditProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    interface UserBasicView extends User.BasicInfo {
    }

    @Operation(summary = "Modify user info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User modified correctly", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid parameter", content = @Content), //Bad request
            @ApiResponse(responseCode = "401", description = "You don't have permission to do this!", content = @Content), //Unauthorized
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content), //Not found
            @ApiResponse(responseCode = "500", description = "Error with operation", content = @Content) //Internal server error
    })
    @JsonView(UserBasicView.class)
    @PutMapping("/api/users/{username}")
    public ResponseEntity<?> editProfile(HttpServletRequest request, @PathVariable String username,
                                         @RequestBody Map<String,String> userInfo) throws SQLException, IOException {


        Principal principal = request.getUserPrincipal();
        if(principal == null){
            return new ResponseEntity<>("You don't have permission to do this!", HttpStatus.UNAUTHORIZED);
        }
        User loggeduser = userService.getUser(principal.getName());

        //Check for correct credentials
        if(loggeduser.getRole().contains("ADMIN") || loggeduser.getUsername().equals(username)){
            User user = userService.getUser(username);
            if (user == null) return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND); //If user is not found
            String newDescription = userInfo.get("description");
            String newEmail = userInfo.get("email");
            String newAlias = userInfo.get("alias");
            if (newDescription != null) user.setDescription(newDescription); //Description can be blank

            //Email check
            if (newEmail != null && !newEmail.equals(user.getEmail())){
                if(emailService.isCorrectEmail(newEmail)){ //Check email is valid direction
                    user.setEmail(newEmail);
                    userService.saveUser(user); //must save beforehand since sending an email checks from db before saving new user state
                }else{
                    return new ResponseEntity<>("New email is not valid.", HttpStatus.BAD_REQUEST);
                }
            }

            //Check alias
            if (newAlias != null) {
                if(!newAlias.isBlank()) {
                    user.setAlias(newAlias);
                } else {
                    return new ResponseEntity<>("Alias can't be blank!", HttpStatus.BAD_REQUEST);
                }
            }

            //Save user in db
            userService.saveUser(user);
            return new ResponseEntity<>(user,HttpStatus.OK);
        } else {
            return new ResponseEntity<>("You don't have permission to do this!", HttpStatus.UNAUTHORIZED);
        }

    }

    //Change the user's password
    @Operation(summary = "Change user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid parameter", content = @Content), //Bad request
            @ApiResponse(responseCode = "401", description = "You don't have permission to do this!", content = @Content), //Unauthorized
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content), //Not found
            @ApiResponse(responseCode = "500", description = "Error with operation", content = @Content) //Internal server error
    })
    @JsonView(UserBasicView.class)
    @PutMapping(value = "/api/users/{username}/password", consumes = "application/json")
    public ResponseEntity<?> changePassword(HttpServletRequest request, @PathVariable String username,
                                            @RequestBody Map<String, String> password) throws SQLException, IOException {

        String newPassword = password.get("newPassword");
        String oldPassword = password.get("oldPassword");
        String confirmPassword = password.get("confirmPassword");
        User user = userService.getUser(username);

        //Password change
        if (newPassword != null) {
            if (newPassword.isBlank()) {
                return new ResponseEntity<>("Password can't be blank!", HttpStatus.BAD_REQUEST);
            } else if (newPassword.length() < 8) {
                return new ResponseEntity<>("Password must be at least 8 characters long", HttpStatus.BAD_REQUEST);
            } else if (!newPassword.matches(".*\\d.*")) {
                return new ResponseEntity<>("Password must include at least one numeric character", HttpStatus.BAD_REQUEST);
            } else if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                return new ResponseEntity<>("Your current password is incorrect", HttpStatus.BAD_REQUEST);
            } else if (!newPassword.equals(confirmPassword)) {
                return new ResponseEntity<>("Passwords do not match", HttpStatus.BAD_REQUEST);
            }
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            try {
                user.setPassword(encodedNewPassword);
                // notify via email
                emailService.sendEmail(user.getEmail(), "Contraseña actualizada", "Su contraseña ha sido actualizada con éxito");
            } catch (MessagingException e) {
                return new ResponseEntity<>("Error changing passwords.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            //Save user in db
            userService.saveUser(user);
            return new ResponseEntity<>(user,HttpStatus.OK);
        } else {
            return new ResponseEntity<>("You don't have permission to do this!", HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Upload profile image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid parameter", content = @Content), //Bad request
            @ApiResponse(responseCode = "401", description = "You don't have permission to do this!", content = @Content), //Unauthorized
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content), //Bad request
            @ApiResponse(responseCode = "500", description = "Error with operation", content = @Content) //Internal server error
    })
    @JsonView(UserBasicView.class)
    @PutMapping("/api/users/{username}/image")
    public ResponseEntity<Object> updateUserImage(HttpServletRequest request, @PathVariable String username, @RequestParam MultipartFile image) throws IOException {
        User user = userService.getUser(username);
        Principal loggedUser = request.getUserPrincipal();
        User userLogged = userService.getUser(loggedUser.getName());
        // Check if the book exists
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        if (!userLogged.getRole().contains("ADMIN") && !userService.checkCorrectProfile(username, request)) {
            return new ResponseEntity<>("You don't have permission to do this!", HttpStatus.UNAUTHORIZED);
        }
        //Image check
        if (image != null) { //If image is added...
            //Check if image file is correct
            try (InputStream input = image.getInputStream()) {
                try {
                    ImageIO.read(input).toString(); //If it doesn't fail, this is an image.
                    long fileInKB = image.getSize() / 1024 / 1024; //get mb of pic
                    if (fileInKB >= 5) {
                        return new ResponseEntity<>("Image must be smaller than 5 MB!", HttpStatus.BAD_REQUEST);
                    }
                    user.setProfileImageFile(BlobProxy.generateProxy(image.getInputStream(), image.getSize()));
                    userService.saveUser(user);
                    return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
                } catch (Exception e) {
                    // It's not an image.
                    return new ResponseEntity<>("Image not supported. Try different file", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            return new ResponseEntity<>("File could not be uploaded. Try again", HttpStatus.BAD_REQUEST);
        }
    }

    /*public ResponseEntity<?> editProfile(HttpServletRequest request, @PathVariable String username,
                                            @RequestParam(value = "alias", required = false) String newAlias,
                                            @RequestParam(value = "email",required = false) String newEmail,
                                            @RequestParam(value = "description", required = false) String newDescription,
                                            @RequestParam(value = "password", required = false) String newPassword,
                                            @RequestParam(value = "image", required = false) MultipartFile newImageFile) throws SQLException, IOException {

        Principal principal = request.getUserPrincipal();
        if(principal == null){
            return new ResponseEntity<>("You don't have permission to do this!", HttpStatus.UNAUTHORIZED);
        }
        User loggeduser = userService.getUser(principal.getName());

        //Check for correct credentials
        if(loggeduser.getRole().contains("ADMIN") || loggeduser.getUsername().equals(username)){
            User user = userService.getUser(username);
            if (user == null) return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND); //If user is not found
            if (newDescription != null) user.setDescription(newDescription); //Description can be blank

            //Email check
            if (newEmail != null && !newEmail.equals(user.getEmail())){
                if(emailService.isCorrectEmail(newEmail)){ //Check email is valid direction
                    user.setEmail(newEmail);
                    userService.saveUser(user); //must save beforehand since sending an email checks from db before saving new user state
                }else{
                    return new ResponseEntity<>("New email is not valid.", HttpStatus.BAD_REQUEST);
                }
            }

            //Check alias
            if (newAlias != null) {
                if(!newAlias.isBlank()) {
                    user.setAlias(newAlias);
                }else{
                    return new ResponseEntity<>("Alias can't be blank!", HttpStatus.BAD_REQUEST);
                }
            }

            //Image check
            if(newImageFile != null){ //If image is added...
                //Check if image file is correct
                try (InputStream input = newImageFile.getInputStream()) {
                    try {
                        ImageIO.read(input).toString(); //If it doesn't fail, this is an image.
                        long fileInKB = newImageFile.getSize() / 1024 / 1024; //get mb of pic
                        if(fileInKB >= 5){
                            return new ResponseEntity<>("Image must be smaller than 5 MB!", HttpStatus.BAD_REQUEST);
                        }
                        user.setProfileImageFile(BlobProxy.generateProxy(newImageFile.getInputStream(), newImageFile.getSize()));
                    } catch (Exception e) {
                        // It's not an image.
                        return new ResponseEntity<>("Image not supported. Try different file", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }

            //Password change
            if(newPassword != null){
                if(newPassword.isBlank()){
                    return new ResponseEntity<>("Password can't be blank!", HttpStatus.BAD_REQUEST);
                }
                String encodedNewPassword = passwordEncoder.encode(newPassword);
                try{
                    user.setPassword(encodedNewPassword);
                    // notify via email
                    emailService.sendEmail(user.getEmail(), "Contraseña actualizada", "Su contraseña ha sido actualizada con éxito");
                } catch (MessagingException e) {
                    return new ResponseEntity<>("Error changing passwords.",HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            //Save user in db
            userService.saveUser(user);
            return new ResponseEntity<>(user,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("You don't have permission to do this!", HttpStatus.UNAUTHORIZED);
        }
    }*/
}

