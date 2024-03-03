package es.codeurjc.webapp03.controller;

import es.codeurjc.webapp03.entity.Author;
import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.entity.Genre;
import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.repository.AuthorRepository;
import es.codeurjc.webapp03.repository.BookRepository;
import es.codeurjc.webapp03.repository.GenreRepository;
import es.codeurjc.webapp03.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;


@Controller
public class LandingPageController {

    private String testingCurrentUsername;

    private boolean isUser;

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
    public String loadLandingPage(Model model, HttpServletRequest request) throws SQLException, IOException {

        User user;
        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            testingCurrentUsername = authentication.getName();

            user = userRepository.findByUsername(testingCurrentUsername);
            user.setProfileImageString(user.blobToString(user.getProfileImageFile()));
            model.addAttribute("profileImageString", user.getProfileImageString());

            isUser = true;
        } else {
            isUser = false;
        }


        model.addAttribute("user", isUser);
        model.addAttribute("username", testingCurrentUsername);

        // Get total site genres count
        long totalGenres = genreRepository.count();

        // Get total site authors count
        long totalAuthors = authorRepository.count();

        // Get total site books count
        long totalBooks = bookRepository.count();

        // Get total site users count
        long totalUsers = userRepository.count();

        model.addAttribute("totalSiteGenres", totalGenres);
        model.addAttribute("totalSiteAuthors", totalAuthors);
        model.addAttribute("totalSiteBooks", totalBooks);
        model.addAttribute("totalSiteUsers", totalUsers);


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
            model.addAttribute("profilePicture", userRepository.getProfileImageStringByUsername(testingCurrentUsername));

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
        //String bookImage = booksFromMostReadAuthor.get(0).getImage();

        //Convert the imageFile to Base64 for it to appear in html
        Blob blob = booksFromMostReadAuthor.get(0).getImageFile();
        byte[] bytes = blob.getBytes(1, (int) blob.length());
        String bookImage = Base64.getEncoder().encodeToString(bytes);

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

        //Adding images into the Strings since inside iteration you cant pass blob to string
        for (int i = 0; i < recommendedBooksRight.size(); i++) {
            recommendedBooksRight.get(i).setImageString(recommendedBooksRight.get(i).blobToString(recommendedBooksRight.get(i).getImageFile()));
        }

        for (int i = 0; i < recommendedBooksLeft.size(); i++) {
            recommendedBooksLeft.get(i).setImageString(recommendedBooksLeft.get(i).blobToString(recommendedBooksLeft.get(i).getImageFile()));
        }

        model.addAttribute("postL", recommendedBooksLeft);
        model.addAttribute("postR", recommendedBooksRight);

        //Admin
        model.addAttribute("admin", request.isUserInRole("ADMIN"));

        return "landingPage";
    }

    //Method that will load 4 more posts
    @GetMapping("/landingPage/loadMore")
    public String loadLandingPagePosts(Model model, @RequestParam int page, @RequestParam int size) throws SQLException {
        // If it is a registered user, get the most read genres
        // Get current user most read genres
        List<Genre> mostReadGenres = userRepository.getMostReadGenres(testingCurrentUsername);

        // Get books from the most read genres (these will be the recommended books)
        List<Book> booksFromMostReadGenres = bookRepository.findByGenreIn(mostReadGenres, PageRequest.of(page, size)).getContent();

        for (int i = 0; i < booksFromMostReadGenres.size(); i++) {
            booksFromMostReadGenres.get(i).setImageString(booksFromMostReadGenres.get(i).blobToString(booksFromMostReadGenres.get(i).getImageFile()));
        }

        model.addAttribute("post", booksFromMostReadGenres);

        return "landingPagePostTemplate";

    }

    @GetMapping("/mostReadGenres") // should return a json with a list of the most read genres and their count
    public ResponseEntity<List<Object[]>> getMostReadGenres() {
        return new ResponseEntity<>(genreRepository.getMostReadGenresNameAndCount(), HttpStatus.OK);
    }

}

