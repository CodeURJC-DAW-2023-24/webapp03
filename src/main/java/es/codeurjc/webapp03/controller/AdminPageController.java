package es.codeurjc.webapp03.controller;

import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.webapp03.entity.Author;
import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.entity.Genre;
import es.codeurjc.webapp03.repository.AuthorRepository;
import es.codeurjc.webapp03.repository.BookRepository;
import es.codeurjc.webapp03.repository.GenreRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminPageController {

    @Autowired
    private BookRepository booksBD;
    @Autowired
    private AuthorRepository authorsBD;
    @Autowired
    private GenreRepository genreBD;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping("/admin/**")
    public String loadAdminPage(Model model, HttpServletRequest request) throws SQLException {

        // Get the current logged in user
        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            String currentUsername = authentication.getName();
            User user = userRepository.findByUsername(currentUsername);
            String imageString = user.blobToString(user.getProfileImageFile());
            model.addAttribute("profileImageString", imageString);
            model.addAttribute("username", currentUsername);
        } else {
            return "redirect:/login";
        }

        return "administratorMainPage";
    }

    @GetMapping("/admin/newBook")
    public String loadNewBookPage(Model model, HttpServletRequest request) throws SQLException {
        // Get the current logged in user
        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            String currentUsername = authentication.getName();
            User user = userRepository.findByUsername(currentUsername);
            String imageString = user.blobToString(user.getProfileImageFile());
            model.addAttribute("profileImageString", imageString);
            model.addAttribute("username", currentUsername);
        } else {
            return "redirect:/login";
        }
        //load blank form
        model.addAttribute("admin", true);
        return "modifyBookPage";
    }

    @PostMapping("/admin/newBook/done")
    public String loadIntoBD(Model model, @RequestParam("inputBookName") String inputBookName
    , @RequestParam("inputBookAuthorName") String inputBookAuthorName, @RequestParam("inputBookISBN") String inputBookISBN
    , @RequestParam("inputBookPages") int inputBookPages, @RequestParam("inputBookGenre") String inputBookGenre
    , @RequestParam("inputBookDate") String inputBookDate
    , @RequestParam("inputBookPublisher") String inputBookPublisher, @RequestParam("inputBookSeries") String inputBookSeries
    , @RequestParam("inputBookDescription") String inputBookDescription, @RequestParam("inputImageFile") MultipartFile inputImageFile) throws IOException  {

        Book newBook = new Book(inputBookName, inputBookDescription, "placeholderImage", inputBookDate
                            ,inputBookISBN, inputBookSeries, inputBookPages, inputBookPublisher);
        
        //Check if authors exist, they are separated by ","
        String[] authorsSplit = inputBookAuthorName.split(",");
        ArrayList<Author> authorList = new ArrayList<>();
        for (int i=0; i<authorsSplit.length;i++){
            Author found = authorsBD.findByName(authorsSplit[i]);
            if(found != null){
                authorList.add(found);
            }else{
                Author newAuthor = new Author(authorsSplit[i]); 
                newAuthor.addBook(newBook); //Add author to DB
                authorList.add(newAuthor); //Add to list
                authorsBD.save(newAuthor); 
            }
        }
        newBook.setAuthor(authorList); //Add author/s to list

        //Check if Genre exist
        
        Genre genreFound = genreBD.findByName(inputBookGenre);
        if(genreFound != null){
            newBook.setGenre(genreFound);
        }else{
            Genre newGenre = new Genre(inputBookGenre); 
            newBook.setGenre(newGenre); //Add genre to Book
            newGenre.addBook(newBook);
            genreBD.save(newGenre);
        }

        //Check for image File
        //If no picture was added, check for old pic. If there never was a pic, insert placeholder.
        if (inputImageFile.isEmpty()) {
            newBook.setImageFile(newBook.URLtoBlob("https://fissac.com/wp-content/uploads/2020/11/placeholder.png"));
        }else{
            //URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
            //book.setImage(location.toString());
            newBook.setImageFile(BlobProxy.generateProxy(inputImageFile.getInputStream(), inputImageFile.getSize()));
        }

        booksBD.save(newBook);
        return "redirect:/admin";
    }

    @GetMapping("/mostReadAuthorsTotal")
    public ResponseEntity<List<Object[]>> getMostReadAuthorsTotal() {
        return new ResponseEntity<>(authorRepository.getMostReadAuthorsNameAndCount(), HttpStatus.OK);
    }

    @GetMapping("/bestReaders")
    public ResponseEntity<List<Object[]>> getBestReaders() {
        return new ResponseEntity<>(userRepository.getUsersAndNumberOfBooksRead(), HttpStatus.OK);
    }


}
