package es.codeurjc.holamundo.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class ErrorPageController {

    @GetMapping("/errorPage/**")
    public ResponseEntity<Resource> loadErrorPage() throws IOException {
        Resource resource = new ClassPathResource("templates/errorPage.html");
        if (!resource.exists()) {
            throw new IOException("Error page not found");

        }
        return ResponseEntity.ok(resource);
    }
}
