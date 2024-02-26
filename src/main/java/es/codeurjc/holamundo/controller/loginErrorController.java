package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.User;
import es.codeurjc.holamundo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.sql.SQLException;

@Controller
public class loginErrorController {

    @Autowired
    public UserRepository userRepository;

    @GetMapping("/loginError")
    public String loadLoginErrorPage(Model model, HttpServletRequest request) throws SQLException {

        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            String currentUsername = authentication.getName();
            User user = userRepository.findByUsername(currentUsername);
            user.setProfileImageString(user.blobToString(user.getProfileImageFile()));
            model.addAttribute("profileImageString", user.getProfileImageString());
        }

        return "loginError";
    }
}
