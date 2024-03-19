package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.codeurjc.webapp03.entity.Author;
import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.entity.Genre;
import es.codeurjc.webapp03.service.AuthorService;
import es.codeurjc.webapp03.service.BookService;
import es.codeurjc.webapp03.service.GenreService;
import es.codeurjc.webapp03.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class APIAlgorithmController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private AuthorService authorService;


    // GENRE ALGORITHM
    @JsonView(Genre.BasicInfo.class)
    @GetMapping("/api/genres")
    public ResponseEntity<?> getMostReadGenresGeneral(@RequestParam("count") boolean count) {

        // This can be executed both for a logged user and for a non-logged user

        if (count) {
            return getMostReadGenresCount();
        } else {
            // Get the most read genres from the database and return them as a JSON (include the genre name and the number of times it has been read)
            List<Genre> mostReadGenres = genreService.getMostReadGenres();

            //Check if the list is empty
            if (mostReadGenres.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(mostReadGenres);
            }
        }
    }

    @JsonView(Genre.BasicInfo.class)
    @GetMapping("/api/genres/me")
    public ResponseEntity<?> getMostReadGenresUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return ResponseEntity.status(401).build();
        } else {
            // Get the most read genres from the database and return them as a JSON (include the genre name and the number of times it has been read)
            List<Genre> mostReadGenres = userService.getMostReadGenres(principal.getName());

            //Check if the list is empty
            if (mostReadGenres.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(mostReadGenres);
            }
        }
    }


    @JsonView(Book.BasicInfo.class)
    @GetMapping("/api/books/me/recommended")
    public ResponseEntity<?> recommendedBooks(HttpServletRequest request,
                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "size", defaultValue = "10") int size,
                                              @RequestParam(value = "by") String recommendedBy) {
        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return ResponseEntity.status(401).build();
        } else {

            if (recommendedBy.equals("genre")) {
                // Get the most read genres from the database and return them as a JSON (include the genre name and the number of times it has been read)
                List<Genre> mostReadGenres = userService.getMostReadGenres(principal.getName());

                //Check if the list is empty
                if (mostReadGenres.isEmpty()) {
                    return ResponseEntity.noContent().build();
                } else {
                    List<Book> booksFromMostReadGenres = bookService.getBooksByGenreIn(mostReadGenres, PageRequest.of(page, size));
                    return ResponseEntity.ok(booksFromMostReadGenres);
                }
            } else if (recommendedBy.equals("author")) {
                // Get the most read authors from the database and return them as a JSON (include the author name and the number of times it has been read)
                List<Author> mostReadAuthors = userService.getMostReadAuthors(principal.getName());

                //Check if the list is empty
                if (mostReadAuthors.isEmpty()) {
                    return ResponseEntity.noContent().build();
                } else {
                    List<Book> booksFromMostReadAuthors = bookService.getBooksByAuthor(mostReadAuthors.get(0).getName(), PageRequest.of(page, size));
                    return ResponseEntity.ok(booksFromMostReadAuthors);
                }
            } else {
                return ResponseEntity.badRequest().build();
            }


        }
    }

    public ResponseEntity<?> getMostReadGenresCount() { // This is what's used to build the chart

        // This can be executed both for a logged user and for a non-logged user

        // Get the most read genres from the database and return them as a JSON (include the genre name and the number of times it has been read)
        List<Object[]> mostReadGenres = genreService.getMostReadGenresNameAndCount();

        //Check if the list is empty
        if (mostReadGenres.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(mostReadGenres);
        }
    }

    // AUTHOR ALGORITHM

    @JsonView(Author.BasicInfo.class)
    @GetMapping("/api/authors")
    public ResponseEntity<?> getMostReadAuthorsGeneral(@RequestParam("count") boolean count) {

        // This can be executed both for a logged user and for a non-logged user

        if (count) {
            return getMostReadAuthorsCount();
        } else {
            // Get the most read authors from the database and return them as a JSON (include the author name and the number of times it has been read)
            List<Author> mostReadAuthors = authorService.getMostReadAuthors();

            //Check if the list is empty
            if (mostReadAuthors.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(mostReadAuthors);
            }
        }


    }

    @JsonView(Author.BasicInfo.class)
    @GetMapping("/api/authors/me")
    public ResponseEntity<?> getMostReadAuthorsUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return ResponseEntity.status(401).build();
        } else {
            // Get the most read authors from the database and return them as a JSON (include the author name and the number of times it has been read)
            List<Author> mostReadAuthors = userService.getMostReadAuthors(principal.getName());

            //Check if the list is empty
            if (mostReadAuthors.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(mostReadAuthors);
            }
        }
    }

    public ResponseEntity<?> getMostReadAuthorsCount() { // This is what's used to build the chart

        // This can be executed both for a logged user and for a non-logged user

        // Get the most read authors from the database and return them as a JSON (include the author name and the number of times it has been read)
        List<Object[]> mostReadAuthors = authorService.getMostReadAuthorsNameAndCount();

        //Check if the list is empty
        if (mostReadAuthors.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(mostReadAuthors);
        }
    }


}
