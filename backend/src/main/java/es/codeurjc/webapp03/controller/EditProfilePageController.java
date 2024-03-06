package es.codeurjc.webapp03.controller;

import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.service.EmailService;
import es.codeurjc.webapp03.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@Controller
public class EditProfilePageController {

    @Autowired
    private UserService userService;

    public EditProfilePageController() {

    }

    @GetMapping("/profile/{username}/edit")
    public String loadEditProfilePage(Model model, HttpServletRequest request, @PathVariable String username) throws SQLException {

        //Check for correct username logged in.
        /*User user = userRepository.findByUsername(username);
        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            String currentUsername = authentication.getName();
            if(!currentUsername.equals(username)){
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }*/

        if (!userService.checkCorrectProfile(username, request)) {
            return "redirect:/error";
        }

        User user = userService.getUser(username);

        String alias = user.getAlias();
        String role = user.getRole().toString();
        String description = user.getDescription();
        user.setProfileImageString(user.blobToString(user.getProfileImageFile()));
        String profileImage = user.getProfileImageString();
        String email = user.getEmail();
        String password = user.getPassword();

        model.addAttribute("username", username);
        model.addAttribute("alias", alias);
        model.addAttribute("role", role);
        model.addAttribute("description", description);
        model.addAttribute("profileImageString", profileImage);
        model.addAttribute("email", email);
        model.addAttribute("password", password);

        //Admin
        model.addAttribute("admin", request.isUserInRole("ADMIN"));


        return "editProfilePage";
    }

    @PostMapping("/profile/{username}/edit")
    public String editProfile(Model model, @PathVariable String username,
                              @RequestParam("alias") String newAlias,
                              @RequestParam("email") String newEmail,
                              @RequestParam("description") String newDescription,
                              HttpServletRequest request) throws SQLException {

        if (!userService.checkCorrectProfile(username, request)) {
            return "redirect:/error";
        }

        User user = userService.getUser(username);

        if (user != null) {
            userService.updateUserInfo(user, newAlias, newEmail, newDescription);
        }

        //Admin
        model.addAttribute("admin", request.isUserInRole("ADMIN"));

        model.addAttribute("alias", user.getAlias());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("description", user.getDescription());
        model.addAttribute("profileImageString", user.blobToString(user.getProfileImageFile()));

        return "editProfilePage";
    }


    @PostMapping("/profile/{username}/upload")
    public ResponseEntity<?> uploadProfileImage(@RequestBody Map<String, Object> image, Model model, HttpServletRequest request) throws SQLException {
        Authentication authentication = (Authentication) request.getUserPrincipal();
        ResponseEntity<?> responseEntity = null;
        if (authentication != null) {
            String currentUsername = authentication.getName();
            // Check that the user is the same as the one logged in
            if (!userService.checkCorrectProfile(currentUsername, request)) {
                return new ResponseEntity<>("Error: No se pudo subir la imagen", HttpStatus.FORBIDDEN);
            }
            User user = userService.getUser(currentUsername);
            if (user != null) {
                responseEntity = userService.changeProfileImage(user, (String) image.get("image"));
            }
        } else {
            responseEntity = new ResponseEntity<>("Error: No se pudo subir la imagen", HttpStatus.FORBIDDEN);
        }
        return responseEntity;
    }

    @PostMapping("/profile/{username}/editPassword")
    public ResponseEntity<?> editProfile(@RequestParam("currentPassword") String currentPassword, @RequestBody String newPassword, HttpServletRequest request) throws MessagingException, IOException {
        // Obtén el usuario actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());

        // Check that the user is the same as the one logged in
        if (!userService.checkCorrectProfile(user.getUsername(), request)) {
            return new ResponseEntity<>("Error: No se pudo cambiar la contraseña", HttpStatus.FORBIDDEN);
        }

        return userService.changePassword(user, newPassword, currentPassword);
    }
}
