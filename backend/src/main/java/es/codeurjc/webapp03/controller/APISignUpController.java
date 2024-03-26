package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.service.UserService;
import jakarta.activation.MimetypesFileTypeMap;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

@RestController
public class APISignUpController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Add User
    interface UserBasicView extends User.BasicInfo {}
    @JsonView(APISignUpController.UserBasicView.class)
    @PostMapping("/api/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addUser(@RequestParam("username") String inputName,
                                     @RequestParam("alias") String inputAlias,
                                     @RequestParam("email") String inputEmail,
                                     @RequestParam("password") String password,
                                     @RequestParam(value = "imageFile", required = false)MultipartFile image) throws SQLException, IOException {

        if(!userService.isUsernameAvailable(inputName)) return new ResponseEntity<>("Username not available",HttpStatus.CONFLICT);
        User newUser = new User(inputName, inputAlias, "Hi, im new!","", inputEmail, passwordEncoder.encode(password), "USER");

        //Check if image file is added as param
        if (image != null) {
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
