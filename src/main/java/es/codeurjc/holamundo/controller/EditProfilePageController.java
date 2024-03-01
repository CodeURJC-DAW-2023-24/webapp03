package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.User;
import es.codeurjc.holamundo.repository.UserRepository;
import es.codeurjc.holamundo.service.EmailService;
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
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

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

        if(!checkCorrectProfile(username,request)){
            return "redirect:/error";
        }

        User user = userRepository.findByUsername(username);
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

        User user = userRepository.findByUsername(username);

        if (user != null) {
            user.setAlias(newAlias);
            user.setEmail(newEmail);
            user.setDescription(newDescription);
            userRepository.save(user);
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
            User user = userRepository.findByUsername(currentUsername);
            if (user != null) {
                user.setProfileImageFile(new SerialBlob(Base64.decodeBase64((String) image.get("image"))));
                userRepository.save(user);
                responseEntity = new ResponseEntity<>("Imagen subida con éxito", HttpStatus.OK);
            }
        } else {
            responseEntity = new ResponseEntity<>("Error: No se pudo subir la imagen", HttpStatus.FORBIDDEN);
        }
        return responseEntity;
    }

    @PostMapping("/profile/{username}/editPassword")
    public ResponseEntity<?> editProfile(@RequestParam("currentPassword") String currentPassword, @RequestBody String newPassword) throws MessagingException, IOException {
        // Obtén el usuario actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());


        // Compara la contraseña actual con la almacenada en la base de datos
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return new ResponseEntity<>("La contraseña actual es incorrecta", HttpStatus.BAD_REQUEST);
        } else {
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedNewPassword);
            userRepository.save(user);
            // notify via email
            emailService.sendEmail(user.getEmail(), "Contraseña actualizada", "Su contraseña ha sido actualizada con éxito");
            return new ResponseEntity<>("Contraseña actualizada con éxito", HttpStatus.OK);
        }
    }

    private boolean checkCorrectProfile(String username, HttpServletRequest request){
        Authentication authentication = (Authentication) request.getUserPrincipal();
        User user = userRepository.findByUsername(authentication.getName());
        if (authentication != null && user != null) {
            if(!user.getUsername().equals(username) && !user.getRole().contains("ADMIN")){
                return false;
            }else{
                return true;
            }
        } else {
            return false;
        }
    }
}
