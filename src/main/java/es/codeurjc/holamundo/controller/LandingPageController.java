package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.component.Post;
import es.codeurjc.holamundo.service.BookList;
import es.codeurjc.holamundo.service.PostList;
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
    private PostList posts;

    //Constructor
    public LandingPageController() {
        this.books = new BookList();
        this.posts = new PostList();
    }

    //Method that will load the landing page
    @GetMapping("/")
    public String loadLandingPage(Model model) {
        int ID = 4;
        int bookID = books.getBook(ID).getID();
        String bookTitle = books.getBook(ID).getTitle();
        String bookDescription = books.getBook(ID).getDescription();
        String bookImage = books.getBook(ID).getImage();

        model.addAttribute("bookTitle", bookTitle);
        model.addAttribute("bookDescription", bookDescription);
        model.addAttribute("bookImage", bookImage);
        model.addAttribute("bookID", bookID);

        String postID = "post1";
        Post post = posts.getPost(postID);
        model.addAttribute("post", post);


        return "landingPage";
    }
}
