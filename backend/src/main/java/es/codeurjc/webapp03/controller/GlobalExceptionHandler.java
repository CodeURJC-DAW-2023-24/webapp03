package es.codeurjc.webapp03.controller;

import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    public UserService userService;

    private boolean isUser;

    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, Exception ex, Model model) throws SQLException {

        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            String currentUsername = authentication.getName();
            User user = userService.getUser(currentUsername);
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

        model.addAttribute("errorDetails", ex.getMessage());
        model.addAttribute("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return "errorPage";
    }
}