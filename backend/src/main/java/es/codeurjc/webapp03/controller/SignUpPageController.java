package es.codeurjc.webapp03.controller;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.repository.UserRepository;


@Controller
public class SignUpPageController {
    
    @Autowired
    UserRepository usersBD;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String signUpPage() {
        return "signupPage";
    }

    @PostMapping("/signup/done")
    public String signUpDone(@RequestParam("inputUsername") String inputName
    , @RequestParam("inputAlias") String inputAlias
    , @RequestParam("inputEmail") String inputEmail
    , @RequestParam("password-field") String password
    , @RequestParam("password-field-Confirm") String passwordConfirm) throws SQLException, IOException {
        
        if(!password.equals(passwordConfirm) || usersBD.findByUsername(inputName) != null){
            return "/error";
        }
        User newUser = new User(inputName, inputAlias, "Hi, im new!","", inputEmail, passwordEncoder.encode(password), "USER");
        usersBD.save(newUser);
        return "redirect:/login";
    }

    @GetMapping("/signup/checkAvailability")
    public ResponseEntity<Boolean> getMethodName(@RequestParam String username) {
        if(usersBD.findByUsername(username) == null){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }
    }
    
}
