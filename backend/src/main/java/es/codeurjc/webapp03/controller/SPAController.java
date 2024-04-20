package es.codeurjc.webapp03.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class SPAController {
    @GetMapping({ "/new/*/{path:[^\\.]*}", "/{path:new[^\\.]*}" })
    public String redirect() {
        return "forward:/new/index.html";
    }
}
