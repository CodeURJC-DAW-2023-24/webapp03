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
        int bookID = books.getBook(ID).getID();
        String bookTitle = books.getBook(ID).getTitle();
        String bookDescription = books.getBook(ID).getDescription();
        String bookImage = books.getBook(ID).getImage();

        model.addAttribute("bookTitle", bookTitle);
        model.addAttribute("bookDescription", bookDescription);
        model.addAttribute("bookImage", bookImage);
        model.addAttribute("bookID", bookID);

        return "landingPage";
    }
}
