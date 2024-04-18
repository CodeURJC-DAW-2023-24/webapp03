package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.service.EmailService;
import es.codeurjc.webapp03.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@RestController
public class APISignUpController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    interface UserBasicView extends User.BasicInfo {}

    // Add User
    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created correctly", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "409", description = "Invalid parameter", content = @Content), //Conflict
            @ApiResponse(responseCode = "400", description = "Parameter missing.", content = @Content) //Bad request
            //@ApiResponse(responseCode = "500", description = "Image not supported. Try different file", content = @Content) //Internal server error

    })
    @JsonView(APISignUpController.UserBasicView.class)
    @PostMapping("/api/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addUser(@RequestBody Map<String, String> userInfo) throws SQLException, IOException {

        if (userInfo == null || userInfo.isEmpty()){
            return new ResponseEntity<>("User info incomplete. Try again",HttpStatus.BAD_REQUEST);
        }

        String inputName = userInfo.get("username");
        String inputEmail = userInfo.get("email");
        String inputAlias = userInfo.get("alias");
        String password = userInfo.get("password");

        //Check all params.
        if(inputName == null || inputName.isBlank()) return new ResponseEntity<>("Username can't be blank!", HttpStatus.CONFLICT);
        if(!userService.isUsernameAvailable(inputName)) return new ResponseEntity<>("Username not available.",HttpStatus.CONFLICT); //Available username
        if(!emailService.isCorrectEmail(inputEmail)) return new ResponseEntity<>("Email is not valid.", HttpStatus.CONFLICT); //Valid email
        if(inputAlias == null || inputAlias.isBlank()) return new ResponseEntity<>("Alias can't be blank!", HttpStatus.CONFLICT); //Valid alias
        if(password == null || password.isBlank()) return new ResponseEntity<>("Password can't be blank", HttpStatus.CONFLICT); //Valid password

        //Should add checker for email
        User newUser = new User(inputName, inputAlias, "Hi, im new!","", inputEmail, passwordEncoder.encode(password), "USER");
        userService.saveUser(newUser);
        return new ResponseEntity<>(newUser,HttpStatus.OK);
    }

    //AddUser with RequestParams
    /*public ResponseEntity<?> addUser(@RequestParam("username") String inputName,
                                     @RequestParam("alias") String inputAlias,
                                     @RequestParam("email") String inputEmail,
                                     @RequestParam("password") String password,
                                     @RequestParam(value = "imageFile", required = false)MultipartFile image) throws SQLException, IOException {

        //Check all params.
        if(inputName == null || inputName.isBlank()) return new ResponseEntity<>("Username can't be blank!", HttpStatus.CONFLICT);
        if(!userService.isUsernameAvailable(inputName)) return new ResponseEntity<>("Username not available.",HttpStatus.CONFLICT); //Available username
        if(!emailService.isCorrectEmail(inputEmail)) return new ResponseEntity<>("Email is not valid.", HttpStatus.CONFLICT); //Valid email
        if(inputAlias == null || inputAlias.isBlank()) return new ResponseEntity<>("Alias can't be blank!", HttpStatus.CONFLICT); //Valid alias
        if(password == null || password.isBlank()) return new ResponseEntity<>("Password can't be blank", HttpStatus.CONFLICT); //Valid password

        //Should add checker for email
        User newUser = new User(inputName, inputAlias, "Hi, im new!","", inputEmail, passwordEncoder.encode(password), "USER");

        //Check if image file is added as param
        if (image != null) {
            //Check if image file is correct
            try (InputStream input = image.getInputStream()) {
                try {
                    ImageIO.read(input).toString(); //If it doesn't fail, this is an image.
                    long fileInKB = image.getSize() / 1024 / 1024; //get mb of pic
                    if(fileInKB >= 5){
                        return new ResponseEntity<>("Image must be smaller than 5 MB!", HttpStatus.BAD_REQUEST);
                    }
                    newUser.setProfileImageFile(BlobProxy.generateProxy(image.getInputStream(), image.getSize()));
                } catch (Exception e) {
                    // It's not an image.
                    return new ResponseEntity<>("Image not supported. Try different file", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        userService.saveUser(newUser);
        return new ResponseEntity<>(newUser,HttpStatus.OK);
    }*/

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        return new ResponseEntity<>(name + " parameter is missing", HttpStatus.BAD_REQUEST);
    }
}
