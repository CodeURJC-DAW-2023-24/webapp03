package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.User;
import es.codeurjc.holamundo.repository.UserRepository;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.holamundo.entity.Author;
import es.codeurjc.holamundo.entity.Book;
import es.codeurjc.holamundo.entity.Genre;
import es.codeurjc.holamundo.repository.AuthorRepository;
import es.codeurjc.holamundo.repository.BookRepository;
import es.codeurjc.holamundo.repository.GenreRepository;

import java.io.IOException;
import java.util.ArrayList;

@Controller
public class AdminPageController {

    @Autowired
    private BookRepository booksBD;
    @Autowired
    private AuthorRepository authorsBD;
    @Autowired
    private GenreRepository genreBD;

    //Temporary
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin/**")
    public String loadAdminPage() {

        User user = userRepository.findByUsername("YourReader");

        return "administratorMainPage";
    }

    @GetMapping("/admin/newBook")
    public String loadNewBookPage(Model model) {
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

}
