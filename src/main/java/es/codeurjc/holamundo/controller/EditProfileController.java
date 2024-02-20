package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.service.UserList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EditProfileController {
    private User userInfo;
    private UserList users;
    private UserRepository userBD;

    public EditProfileController() {
        this.users = new UserList();
    }

    @GetMapping("/profile/{username}/edit")
    public String loadEditProfilePage(Model model, @PathVariable String username) {
        this.userInfo = userBD.findByUsername();

        /*
         * if (userInfo.isPresent()) {
         * return ResponseEntity.ok(userInfo.get());
         * } else {
         * return ResponseEntity.of(userInfo);
         * }
         */

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

    @PutMapping("/modifyDone/{username}") // Parametros del formulario
    public String modifyDone(Model model, @PathVariable("inputUsername") String inputUsername,
            @RequestParam("inputAlias") String inputAlias,
            @RequestParam("inputBookAuthorName") String inputBookAuthorName,
            @RequestParam("inputBookISBN") String inputBookISBN, @RequestParam("inputRole") String inputRole,
            @RequestParam("inputDescription") String inputDescription,
            @RequestParam("inputProfileImage") String inputProfileImage, @RequestParam("inputEmail") String inputEmail,
            @RequestParam("inputPassword") String inputPassword) {
               
        // Se puede realizar con setter en la clase Book
        users.setUserInfo(username, inputAlias + inputBookAuthorName + inputBookISBN + inputRole + inputDescription
                + inputProfileImage + inputEmail + inputPassword);

        // Tambien se puede crear una clase Book y sobreescribir con updateBook y el id
        // Book newBook = new Book(bookID, inputBookName, inputBookAuthorName,
        // inputBookDescription, inputBookDate, inputBookDescription, inputBookISBN,
        // inputBookGenre, inputBookSeries, inputBookPages, inputBookPublisher);
        // books.updateBook(bookID, newBook);

        userBD.replace(users.getUser(username));

        return "redirect:/profile/" + username;
    }
}
