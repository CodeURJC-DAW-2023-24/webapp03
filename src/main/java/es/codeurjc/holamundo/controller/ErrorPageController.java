package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.User;
import es.codeurjc.holamundo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.sql.SQLException;

@Controller
public class ErrorPageController {

    //Temporary
    @Autowired
    public UserRepository userRepository;

    @GetMapping({"/errorPage/**", "/error/**"})
    public ResponseEntity<Resource> loadErrorPage(Model model) throws IOException, SQLException {

        //Temporary user until the current logged in user is accessible by all controllers
        User user = userRepository.findByUsername("YourReader");
        user.setProfileImageString(user.blobToString(user.getProfileImageFile()));
        model.addAttribute("profileImageString", user.getProfileImageString());

        Resource resource = new ClassPathResource("templates/errorPage.html");
        if (!resource.exists()) {
            throw new IOException("Error page not found");
        }

        return ResponseEntity.ok(resource);
    }
}
