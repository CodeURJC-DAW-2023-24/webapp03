package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.User;
import es.codeurjc.holamundo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;
import java.util.Map;

@Controller
public class EditProfilePageController {

    @Autowired
    UserRepository userRepository;

    public EditProfilePageController() {

    }

    @GetMapping("/profile/{username}/edit")
    public String loadEditProfilePage(Model model, @PathVariable String username) throws SQLException {

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

        return "editProfilePage";
    }

    @PostMapping("/profile/{username}/edit")
    public String editProfile(Model model, @PathVariable String username,
                              @RequestParam("alias") String newAlias,
                              @RequestParam("email") String newEmail,
                              @RequestParam("description") String newDescription,
                              @RequestParam("password") String newPassword) throws SQLException {

        User user = userRepository.findByUsername(username);

        if (user != null) {
            user.setAlias(newAlias);
            user.setEmail(newEmail);
            user.setDescription(newDescription);
            user.setPassword(newPassword);
            userRepository.save(user);
        }

        model.addAttribute("alias", user.getAlias());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("description", user.getDescription());
        model.addAttribute("password", user.getPassword());
        model.addAttribute("profileImageString", user.blobToString(user.getProfileImageFile()));

        return "editProfilePage";
    }



    @PostMapping("/profile/{username}/upload")
    public ResponseEntity<?> uploadProfileImage(@RequestBody Map<String, Object> image, Model model, HttpServletRequest request) throws SQLException {
        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            String currentUsername = authentication.getName();
            User user = userRepository.findByUsername(currentUsername);
            user.setProfileImageFile(new SerialBlob(Base64.decodeBase64((String) image.get("image"))));
            userRepository.save(user);
        }
        return new ResponseEntity<>("Imagen subida con éxito", HttpStatus.OK);
    }

    @PostMapping("/profile/{username}/editPassword")
    public ResponseEntity<?> editProfile(@RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword) {
        // Obtén el usuario actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        // Compara la contraseña actual con la almacenada en la base de datos
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(currentPassword, userDetails.getPassword())) {
            return new ResponseEntity<>("La contraseña actual es incorrecta", HttpStatus.BAD_REQUEST);
        }

        // Encripta la nueva contraseña
        String encodedNewPassword = encoder.encode(newPassword);

        // Aquí debes implementar la lógica para actualizar la contraseña del usuario en la base de datos

        return new ResponseEntity<>("Contraseña actualizada con éxito", HttpStatus.OK);
    }
}
