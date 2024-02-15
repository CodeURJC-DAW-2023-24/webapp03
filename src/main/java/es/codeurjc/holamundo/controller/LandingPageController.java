package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.component.Post;
import es.codeurjc.holamundo.service.Book;
import es.codeurjc.holamundo.service.BookList;
import es.codeurjc.holamundo.service.PostList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LandingPageController {

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
        int ID = 3; //RECOMMENDED BOOK ID
        int bookID = books.getBook(ID).getID();
        String bookTitle = books.getBook(ID).getTitle();
        String bookDescription = books.getBook(ID).getDescription();
        String bookImage = books.getBook(ID).getImage();

        model.addAttribute("bookTitle", bookTitle);
        model.addAttribute("bookDescription", bookDescription);
        model.addAttribute("bookImage", bookImage);
        model.addAttribute("bookID", bookID);

        ArrayList<Post> highlightPosts = new ArrayList<>(posts.getPosts().values()); //This is the getter of the list of posts the admin will choose to display in the landing page

        //Get only the first 4 posts to display in the landing page
        highlightPosts = new ArrayList<>(highlightPosts.subList(0, 4));

        List<Post> highlightPostsLeft;
        List<Post> highlightPostsRight;

        highlightPostsLeft = highlightPosts.subList(0, (highlightPosts.size() / 2));
        highlightPostsRight = highlightPosts.subList((highlightPosts.size() / 2), highlightPosts.size());

        model.addAttribute("postL", highlightPostsLeft);
        model.addAttribute("postR", highlightPostsRight);

        return "landingPage";
    }

    //Method that will load 4 more posts
    @PostMapping("/landingPage/loadMore")
    public ResponseEntity<ArrayList<Post>> loadLandingPagePosts() {
        return new ResponseEntity<>(posts.getMultiplePosts(1, 6), HttpStatus.OK);

    }
}

