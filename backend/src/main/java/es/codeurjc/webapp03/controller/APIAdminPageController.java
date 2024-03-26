//package es.codeurjc.webapp03.controller;
//
//import com.fasterxml.jackson.annotation.JsonView;
//import es.codeurjc.webapp03.entity.Book;
//import es.codeurjc.webapp03.service.AuthorService;
//import es.codeurjc.webapp03.service.BookService;
//import es.codeurjc.webapp03.service.GenreService;
//import es.codeurjc.webapp03.service.UserService;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.security.Principal;
//
//@RestController
//public class APIAdminPageController {
//
//    @Autowired
//    private BookService bookService;
//
//    @Autowired
//    private AuthorService authorService;
//
//    @Autowired
//    private GenreService genreService;
//
//    @Autowired
//    private UserService userService;
//
//    @JsonView(APIBookController.BookBasicView.class)
//    @PutMapping("api/author/{username}")
//    public ResponseEntity<?> setAuthor(HttpServletRequest request, @PathVariable String username){
//        //Check if the user exists
//        Principal loggedUser = request.getUserPrincipal();
//    }
//
//
//
//}
