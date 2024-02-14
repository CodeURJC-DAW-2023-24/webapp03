package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.service.UserBookLists;
import es.codeurjc.holamundo.service.UserList;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfilePageController {
    private UserList users;
    private UserBookLists userBList;

    public ProfilePageController() {
        this.users = new UserList();
        this.userBList = new UserBookLists();
    }

    @GetMapping("/profile/{username}")
    public String loadProfilePage(Model model, @PathVariable String username) {

        //UserList
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

        //UserBookLists
        int nReadedBooks = userBList.getReadedList(username).length;
        int nReadingBooks = userBList.getReadingList(username).length;
        int nWantedBooks = userBList.getWantedList(username).length;

        model.addAttribute("nReadedBooks", nReadedBooks);
        model.addAttribute("nReadingBooks", nReadingBooks);
        model.addAttribute("nWantedBooks", nWantedBooks);
        
        return "profilePage";
    }
}
