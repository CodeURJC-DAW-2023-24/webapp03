package es.codeurjc.holamundo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    @GetMapping("/admin/**")
    public String loadAdminPage() {
        return "administratorMainPage";
    }
}
