package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.User;
import es.codeurjc.holamundo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;


@ControllerAdvice
public class GlobalExceptionHandler {

    //Temporary
    @Autowired
    public UserRepository userRepository;

    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, Exception ex, Model model) throws SQLException {

        //Temporary user until the current logged in user is accessible by all controllers
        User user = userRepository.findByUsername("YourReader");
        user.setProfileImageString(user.blobToString(user.getProfileImageFile()));
        model.addAttribute("profileImageString", user.getProfileImageString());

        model.addAttribute("errorDetails", ex.getMessage());
        model.addAttribute("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return "errorPage";
    }
}