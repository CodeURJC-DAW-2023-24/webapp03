package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.service.BookService;
import es.codeurjc.webapp03.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class APISearchResultsPageController {


    private List<Book> bookQueries;
    private List<User> userQueries;

    @Autowired
    private BookService bookService;

    @Autowired
    public UserService userService;

    public APISearchResultsPageController() {
    }

    @Operation(summary = "Load search results page books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results page books loaded", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @JsonView(Book.BasicInfo.class)
    @GetMapping("/api/books")
    public ResponseEntity<?> loadSearchResultsPageBooks(@RequestParam String query, @RequestParam int page) throws SQLException {

        Page<Book> filteredBooks = bookService.searchBook(query, PageRequest.of(page, 4));
        bookQueries = filteredBooks.getContent();

        for (int i = 0; i < bookQueries.size(); i++) {
            bookQueries.get(i).setImageString(bookQueries.get(i).blobToString(bookQueries.get(i).getImageFile()));
        }

        List<Double> ratings = new ArrayList<>();
        bookQueries.forEach((book) -> {
            List<Double> bookRatings = bookService.getRatings(book.getID());
            double averageRating = 0;
            if (bookRatings.size() > 0) {
                for (Double rating : bookRatings) {
                    averageRating += rating;
                }
                averageRating /= bookRatings.size();
            }
            ratings.add(averageRating);
        });

        return new ResponseEntity<>(bookQueries, HttpStatus.OK);
    }

    @JsonView(User.BasicInfo.class)
    @GetMapping("/api/users")
    public ResponseEntity<?> loadSearchResultsPageUsers(@RequestParam String query, @RequestParam int page) throws SQLException {

        Page<User> filteredUsers = userService.getUsersPageable(query, PageRequest.of(page, 4));
        userQueries = filteredUsers.getContent();

        for (int i = 0; i < userQueries.size(); i++) {
            userQueries.get(i).setProfileImageString(userQueries.get(i).blobToString(userQueries.get(i).getProfileImageFile()));
        }

        return new ResponseEntity<>(userQueries, HttpStatus.OK);

    }

}
