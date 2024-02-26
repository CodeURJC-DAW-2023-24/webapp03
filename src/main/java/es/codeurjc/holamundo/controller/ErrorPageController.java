package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.User;
import es.codeurjc.holamundo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.sql.SQLException;

@Controller
public class ErrorPageController {

    @Autowired
    public UserRepository userRepository;

    @GetMapping({"/errorPage/**", "/error/**", "/errorPage", "/error"})
    public ResponseEntity<Resource> loadErrorPage(Model model, HttpServletRequest request) throws IOException, SQLException {

        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            String currentUsername = authentication.getName();
            User user = userRepository.findByUsername(currentUsername);
            user.setProfileImageString(user.blobToString(user.getProfileImageFile()));
            model.addAttribute("profileImageString", user.getProfileImageString());
        }

        Resource resource = new ClassPathResource("templates/errorPage.html");
        if (!resource.exists()) {
            throw new IOException("Error page not found");
        }

        return ResponseEntity.ok(resource);
    }
}
