package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.service.BookReviewService;
import es.codeurjc.webapp03.service.BookService;
import es.codeurjc.webapp03.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class APIBookController {

    @Autowired
    private BookReviewService reviewService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    interface BookBasicView extends Book.BasicInfo {}
    //Get existing book
    @JsonView(BookBasicView.class)
    @GetMapping("api/books/{id}")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> getBook(HttpServletRequest request, @PathVariable int id){
        Book book = bookService.getBook(id);
        // Check if the book exists
        if (book == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
    }

    //Modify existing book
    @JsonView(BookBasicView.class)
    @PutMapping("api/books/{id}")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> editBook(HttpServletRequest request, @PathVariable int id){
        Book book = bookService.getBook(id);
        // Check if the book exists
        if (book == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
    }


    /*@PutMapping("/api/profiles/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    public User editProfile(HttpServletRequest request, @PathVariable String username,
                            @RequestParam(value = "alias", required = false) String newAlias,
                            @RequestParam(value = "email",required = false) String newEmail,
                            @RequestParam(value = "description", required = false) String newDescription,
                            @RequestParam(value = "password", required = false) String newPassword,
                            @RequestParam(value = "image", required = false) MultipartFile newImageFile) throws SQLException, IOException {
        Principal principal = request.getUserPrincipal();
        if(principal == null){
            return null;
        }else{
            if(userService.checkCorrectProfile(username,request)) {
                User user = userService.getUser(username);
                if (newAlias != null) {
                    user.setAlias(newAlias);
                }
                if (newEmail != null) {
                    user.setEmail(newEmail);
                }
                if (newDescription != null) {
                    user.setDescription(newDescription);
                }
                //Should add mail for password change
                if(newPassword != null){
                    String encodedNewPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encodedNewPassword);
                }
                //Should add check to only let images upload
                if(newImageFile != null){
                    user.setProfileImageFile(BlobProxy.generateProxy(newImageFile.getInputStream(), newImageFile.getSize()));
                }
                return userService.saveUser(user);
            }else{
                return null;
            }
        }
    }*/
}
