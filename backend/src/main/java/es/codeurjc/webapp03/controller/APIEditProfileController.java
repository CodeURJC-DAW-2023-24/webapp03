package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.POJONode;
import es.codeurjc.webapp03.entity.Review;
import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.service.BookReviewService;
import es.codeurjc.webapp03.service.BookService;
import es.codeurjc.webapp03.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Map;

@RestController
public class APIEditProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    interface UserBasicView extends User.BasicInfo {}
    @JsonView(UserBasicView.class)
    @PutMapping("/api/users/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    public User editProfile(HttpServletRequest request, @PathVariable String username,
                            @RequestParam(value = "alias", required = false) String newAlias,
                            @RequestParam(value = "email",required = false) String newEmail,
                            @RequestParam(value = "description", required = false) String newDescription,
                            @RequestParam(value = "password", required = false) String newPassword,
                            @RequestParam(value = "image", required = false) MultipartFile newImageFile) throws SQLException, IOException {
        Principal principal = request.getUserPrincipal();
        if(principal == null){
            return null;
        }else{
            if(userService.checkCorrectProfile(username,request)) {
                User user = userService.getUser(username);
                if (newAlias != null) {
                    user.setAlias(newAlias);
                }
                if (newEmail != null) {
                    user.setEmail(newEmail);
                }
                if (newDescription != null) {
                    user.setDescription(newDescription);
                }
                //Should add mail for password change
                if(newPassword != null){
                    String encodedNewPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encodedNewPassword);
                }
                //Should add check to only let images upload
                if(newImageFile != null){
                    user.setProfileImageFile(BlobProxy.generateProxy(newImageFile.getInputStream(), newImageFile.getSize()));
                }
                return userService.saveUser(user);
            }else{
                return null;
            }
        }
    }
}
