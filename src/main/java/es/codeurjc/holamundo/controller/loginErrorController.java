package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.User;
import es.codeurjc.holamundo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.sql.SQLException;

@Controller
public class loginErrorController {

    //Temporary
    @Autowired
    public UserRepository userRepository;

    @GetMapping("/loginError")
    public String loadLoginErrorPage(Model model) throws SQLException {

        //Temporary user until the current logged in user is accessible by all controllers
        User user = userRepository.findByUsername("YourReader");
        user.setProfileImageString(user.blobToString(user.getProfileImageFile()));
        model.addAttribute("profileImageString", user.getProfileImageString());

        return "loginError";
    }
}
