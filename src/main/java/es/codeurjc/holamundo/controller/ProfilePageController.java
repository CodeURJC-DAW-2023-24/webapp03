package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.service.UserList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfilePageController {
    private UserList users;

    public ProfilePageController() {
        this.users = new UserList();
    }

    @GetMapping("/profile/{username}")
    public String loadProfilePage(Model model, @PathVariable String username) {

        String role = users.getUserInfo(username)[1];
        String alias = users.getUserInfo(username)[2];
        String description = users.getUserInfo(username)[3];
        String profileImage = users.getUserInfo(username)[4];
        String email = users.getUserInfo(username)[5];
        String password = users.getUserInfo(username)[6];

        model.addAttribute("username", username);
        model.addAttribute("alias", alias);
        model.addAttribute("role", role);
        model.addAttribute("description", description);
        model.addAttribute("profileImage", profileImage);
        model.addAttribute("email", email);
        model.addAttribute("password", password);

        return "profilePage";
    }
}
