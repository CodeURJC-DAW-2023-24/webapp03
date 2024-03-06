package es.codeurjc.webapp03.controller;

import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;

@Controller
public class loginErrorController {

    @Autowired
    public UserService userService;

    @GetMapping("/loginError")
    public String loadLoginErrorPage(Model model, HttpServletRequest request) throws SQLException {

        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            String currentUsername = authentication.getName();
            User user = userService.getUser(currentUsername);
            user.setProfileImageString(user.blobToString(user.getProfileImageFile()));
            model.addAttribute("profileImageString", user.getProfileImageString());
        }

        //Admin
        model.addAttribute("admin", request.isUserInRole("ADMIN"));


        return "loginError";
    }
}
