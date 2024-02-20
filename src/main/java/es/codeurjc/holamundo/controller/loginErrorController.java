package es.codeurjc.holamundo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class loginErrorController {

    @GetMapping("/loginError")
    public String loadLoginErrorPage(Model model) {
        return "loginError";
    }
}
