package es.codeurjc.webapp03.controller;

import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

    private boolean isUser;

    @GetMapping({"/errorPage/**", "/error/**", "/errorPage", "/error"})
    public String loadErrorPage(Model model, HttpServletRequest request) throws IOException, SQLException {

        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            String currentUsername = authentication.getName();
            User user = userRepository.findByUsername(currentUsername);
            model.addAttribute("username", currentUsername);
            user.setProfileImageString(user.blobToString(user.getProfileImageFile()));
            model.addAttribute("profileImageString", user.getProfileImageString());
            isUser = true;
        } else {
            isUser = false;
        }
        //Admin
        model.addAttribute("admin", request.isUserInRole("ADMIN"));

        model.addAttribute("user", isUser);
        model.addAttribute("errorDetails", "Ha ocurrido un error inesperado. Por favor, inténtelo de nuevo más tarde.");
        model.addAttribute("errorCode", "000");

        return "errorPage";
    }
}
