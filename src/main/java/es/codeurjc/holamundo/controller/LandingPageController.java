package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.service.BookList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LandingPageController {

    //@Autowired
    private BookList books;

    //Constructor
    public LandingPageController() {
        this.books = new BookList();
    }

    //Method that will load the landing page
    @GetMapping("/")
    public String loadLandingPage(Model model) {
        int ID = 2;
        String bookID = books.getBook(ID)[0];
        String bookTitle = books.getBook(ID)[1];
        String bookDescription = books.getBook(ID)[3];
        String bookImage = books.getBook(ID)[4];

        model.addAttribute("bookTitle", bookTitle);
        model.addAttribute("bookDescription", bookDescription);
        model.addAttribute("bookImage", bookImage);
        model.addAttribute("bookID", bookID);

        return "landingPage";
    }
}
