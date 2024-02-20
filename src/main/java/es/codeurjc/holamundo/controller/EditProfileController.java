package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.service.UserList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EditProfileController {
    private UserList users;

    public EditProfileController() {
        this.users = new UserList();
    }

    @GetMapping("/profile/{username}/edit")
    public String loadEditProfilePage(Model model, @PathVariable String username) {

        String alias = users.getUserInfo(username)[1];
        String role = users.getUserInfo(username)[2];
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

        return "editProfilePage";
    }
}
