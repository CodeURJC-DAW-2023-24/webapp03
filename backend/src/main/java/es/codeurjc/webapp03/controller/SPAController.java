package es.codeurjc.webapp03.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.PathResourceResolver;
import java.io.IOException;

@Controller
public class SPAController {

    private final ResourceLoader resourceLoader;

    public SPAController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @RequestMapping(value = "/new/**", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public Resource redirect() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/public/new/index.html");
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Could not find file: " + resource);
        }
    }
}
