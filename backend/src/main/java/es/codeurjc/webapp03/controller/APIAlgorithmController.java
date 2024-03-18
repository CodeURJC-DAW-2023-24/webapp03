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
    @GetMapping("/api/mostReadGenres/general")
    public ResponseEntity<?> getMostReadGenresGeneral() {

        // This can be executed both for a logged user and for a non-logged user

        // Get the most read genres from the database and return them as a JSON (include the genre name and the number of times it has been read)
        List<Genre> mostReadGenres = genreService.getMostReadGenres();

        //Check if the list is empty
        if (mostReadGenres.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(mostReadGenres);
        }
    }

    @JsonView(Genre.BasicInfo.class)
    @GetMapping("/api/mostReadGenres/user")
    public ResponseEntity<?> getMostReadGenresUser(HttpServletRequest  request){
        Principal principal = request.getUserPrincipal();

        if(principal == null){
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

    public interface BookAdvancedInfo extends Book.BasicInfo, Book.AuthorInfo, Author.BasicInfo {}

    @JsonView(BookAdvancedInfo.class)
    @GetMapping("/api/mostReadGenres/user/books")
    public ResponseEntity<?> getMostReadGenresUserBooks(HttpServletRequest  request,
                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size){
        Principal principal = request.getUserPrincipal();

        if(principal == null){
            return ResponseEntity.status(401).build();
        } else {
            // Get the most read genres from the database and return them as a JSON (include the genre name and the number of times it has been read)
            List<Genre> mostReadGenres = userService.getMostReadGenres(principal.getName());

            //Check if the list is empty
            if (mostReadGenres.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                List<Book> booksFromMostReadGenres = bookService.getBooksByGenreIn(mostReadGenres, PageRequest.of(page, size));
                return ResponseEntity.ok(booksFromMostReadGenres);
            }
        }
    }

    @JsonView(Genre.BasicInfo.class)
    @GetMapping("/api/mostReadGenres/general/count")
    public ResponseEntity<?> getMostReadGenresCount() {

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



}
