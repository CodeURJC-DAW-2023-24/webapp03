package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.component.Post;
import es.codeurjc.holamundo.entity.Author;
import es.codeurjc.holamundo.entity.Book;
import es.codeurjc.holamundo.entity.Genre;
import es.codeurjc.holamundo.repository.AuthorRepository;
import es.codeurjc.holamundo.repository.BookRepository;
import es.codeurjc.holamundo.repository.GenreRepository;
import es.codeurjc.holamundo.repository.UserRepository;
import es.codeurjc.holamundo.service.BookList;
import es.codeurjc.holamundo.service.PostList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LandingPageController {

    private String testingCurrentUsername = "FanBook_785"; //This is the username of the current user. This is just for testing purposes

    private boolean isUser = true; //This is just for testing purposes

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorRepository authorRepository;


    //Method that will load the landing page
    @GetMapping("/")
    public String loadLandingPage(Model model) {

        model.addAttribute("user", isUser);
        model.addAttribute("username", testingCurrentUsername);

        // Recommendations algorithm---------------------------------------------------------------
        // If it is a registered user, get the most read genres
        // Get current user most read genres
        List<Genre> mostReadGenres;

        //Get current user most read authors
        List<Author> mostReadAuthors;

        // Get books from the most read genres (these will be the recommended books)
        List<Book> booksFromMostReadGenres;

        // Get books from the most read author (the first one on the list)
        List<Book> booksFromMostReadAuthor;

        // Load lists if a user is logged in
        if (isUser) {
            mostReadGenres = userRepository.getMostReadGenres(testingCurrentUsername);
            mostReadAuthors = userRepository.getMostReadAuthors(testingCurrentUsername);
            model.addAttribute("profilePicture", userRepository.getProfileImageByUsername(testingCurrentUsername));

            // check if the user has read any books
            if (mostReadGenres.size() == 0) {
                // If the user has not read any books, get the most read genres from the database
                mostReadGenres = genreRepository.getMostReadGenres();
            }
            if (mostReadAuthors.size() == 0) {
                // If the user has not read any books, get the most read authors from the database
                mostReadAuthors = authorRepository.getMostReadAuthors();
            }

        } else {
            // If it is not a registered user, get the most read genres from the database
            mostReadGenres = genreRepository.getMostReadGenres();
            mostReadAuthors = authorRepository.getMostReadAuthors();
        }
        booksFromMostReadGenres = bookRepository.findByGenreIn(mostReadGenres, PageRequest.of(0, 4)).getContent();
        booksFromMostReadAuthor = bookRepository.findByAuthorString(mostReadAuthors.get(0).getName(), PageRequest.of(0, 5)).getContent();
        //Get info of the book recommended in the big card (recommended by author)

        long bookID = booksFromMostReadAuthor.get(0).getID();
        String bookTitle = booksFromMostReadAuthor.get(0).getTitle();
        String bookAuthor = booksFromMostReadAuthor.get(0).getAuthorString();
        String bookDescription = booksFromMostReadAuthor.get(0).getDescription();
        String bookImage = booksFromMostReadAuthor.get(0).getImage();

        model.addAttribute("mostReadAuthor", mostReadAuthors.get(0).getName());

        model.addAttribute("bookTitle", bookTitle);
        model.addAttribute("bookAuthor", bookAuthor);
        model.addAttribute("bookDescription", bookDescription);
        model.addAttribute("bookImage", bookImage);
        model.addAttribute("bookID", bookID);
        model.addAttribute("bookDate", booksFromMostReadGenres.get(0).getReleaseDate());
        model.addAttribute("genre", booksFromMostReadGenres.get(0).getGenre().getName());


        // Split the list of recommended books into two lists to display them in two columns
        List<Book> recommendedBooksLeft;
        List<Book> recommendedBooksRight;

        recommendedBooksLeft = booksFromMostReadGenres.subList(0, (booksFromMostReadGenres.size() / 2));
        recommendedBooksRight = booksFromMostReadGenres.subList((booksFromMostReadGenres.size() / 2), booksFromMostReadGenres.size());

        model.addAttribute("postL", recommendedBooksLeft);
        model.addAttribute("postR", recommendedBooksRight);

        return "landingPage";
    }

    //Method that will load 4 more posts
    @GetMapping("/landingPage/loadMore")
    public String loadLandingPagePosts(Model model, @RequestParam int page, @RequestParam int size) {
        // If it is a registered user, get the most read genres
        // Get current user most read genres
        List<Genre> mostReadGenres = userRepository.getMostReadGenres(testingCurrentUsername);

        // Get books from the most read genres (these will be the recommended books)
        List<Book> booksFromMostReadGenres = bookRepository.findByGenreIn(mostReadGenres, PageRequest.of(page, size)).getContent();

        model.addAttribute("post", booksFromMostReadGenres);

        return "landingPagePostTemplate";

    }

    @GetMapping("/landingPage/mostReadGenres")
    public ResponseEntity<List<Genre>> getMostReadGenres() {
        List<Genre> mostReadGenres = genreRepository.getMostReadGenres();
        System.out.println(mostReadGenres);
        return new ResponseEntity<>(mostReadGenres, HttpStatus.OK);
    }
}

