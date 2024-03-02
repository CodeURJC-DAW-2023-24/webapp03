package es.codeurjc.holamundo.service;

import es.codeurjc.holamundo.entity.Book;
import es.codeurjc.holamundo.repository.AuthorRepository;
import es.codeurjc.holamundo.repository.BookRepository;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import org.json.JSONArray;

@Service
public class BookSampleService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private JSONArray books;

    @PostConstruct
    public void init() throws SerialException, SQLException, IOException {
        // Add some books (Tolkien)
        Book book1 = new Book("The Hobbit", "The Hobbit, or There and Back Again is a children's fantasy novel by English author J. R. R. Tolkien.", "https://cdn.kobo.com/book-images/cf32789f-22db-4ad0-bba4-9c0bf69fb872/1200/1200/False/the-hobbit.jpg", "21-09-1937", "978-0261102217", "The Lord of the Rings", 310, "Allen & Unwin");
        Book book2 = new Book("The Fellowship of the Ring", "The Fellowship of the Ring is the first of three volumes of the epic novel The Lord of the Rings by the English author J.R.R. Tolkien. It is followed by The Two Towers and The Return of the King.", "https://m.media-amazon.com/images/I/813UBZ-O8sL._AC_UF1000,1000_QL80_.jpg", "29-07-1954", "978-0261102217", "The Lord of the Rings", 423, "Allen & Unwin");
        Book book3 = new Book("The Two Towers", "The Two Towers is the second volume of J.R.R. Tolkien's high fantasy novel The Lord of the Rings. It is preceded by The Fellowship of the Ring and followed by The Return of the King.", "https://m.media-amazon.com/images/I/81HfbQ8F2UL._AC_UF1000,1000_QL80_.jpg", "11-11-1954", "978-0261102217", "The Lord of the Rings", 352, "Allen & Unwin");
        Book book4 = new Book("The Return of the King", "The Return of the King is the third and final volume of J.R.R. Tolkien's The Lord of the Rings, following The Fellowship of the Ring and The Two Towers.", "https://m.media-amazon.com/images/I/81fMRrrUlqL._AC_UF1000,1000_QL80_.jpg", "20-10-1955", "978-0261102217", "The Lord of the Rings", 416, "Allen & Unwin");
        book1.addAuthor(authorRepository.findByName("J.R.R. Tolkien"));
        book2.addAuthor(authorRepository.findByName("J.R.R. Tolkien"));
        book3.addAuthor(authorRepository.findByName("J.R.R. Tolkien"));
        book4.addAuthor(authorRepository.findByName("J.R.R. Tolkien"));
        book1.setImageFile(book1.URLtoBlob(book1.getImageString()));
        book2.setImageFile(book2.URLtoBlob(book2.getImageString()));
        book3.setImageFile(book3.URLtoBlob(book3.getImageString()));
        book4.setImageFile(book4.URLtoBlob(book4.getImageString()));
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        bookRepository.save(book4);

        // Add some books (Rowling)
        Book book5 = new Book("Harry Potter and the Philosopher's Stone", "Harry Potter and the Philosopher's Stone is a fantasy novel written by British author J. K. Rowling. The first novel in the Harry Potter series and Rowling's debut novel.", "https://m.media-amazon.com/images/I/81IV--KF2yL._AC_UF1000,1000_QL80_.jpg", "26-06-1997", "978-0747532743", "Harry Potter", 223, "Bloomsbury");
        Book book6 = new Book("Harry Potter and the Chamber of Secrets", "Harry Potter and the Chamber of Secrets is a fantasy novel written by British author J. K. Rowling and the second novel in the Harry Potter series.", "https://m.media-amazon.com/images/I/81gOJoEgVoL._AC_UF1000,1000_QL80_.jpg", "02-07-1998", "978-0747538493", "Harry Potter", 251, "Bloomsbury");
        Book book7 = new Book("Harry Potter and the Prisoner of Azkaban", "Harry Potter and the Prisoner of Azkaban is a fantasy novel written by British author J. K. Rowling and is the third in the Harry Potter series.", "https://m.media-amazon.com/images/I/81CSkmCO8PL._AC_UF1000,1000_QL80_.jpg", "08-07-1999", "978-0747546290", "Harry Potter", 317, "Bloomsbury");
        Book book8 = new Book("Harry Potter and the Goblet of Fire", "Harry Potter and the Goblet of Fire is a fantasy novel written by British author J. K. Rowling and the fourth novel in the Harry Potter series.", "https://m.media-amazon.com/images/I/81BownMW+fL._AC_UF1000,1000_QL80_.jpg", "08-07-1999", "978-0747546290", "Harry Potter", 636, "Bloomsbury");
        book5.addAuthor(authorRepository.findByName("J.K. Rowling"));
        book6.addAuthor(authorRepository.findByName("J.K. Rowling"));
        book7.addAuthor(authorRepository.findByName("J.K. Rowling"));
        book8.addAuthor(authorRepository.findByName("J.K. Rowling"));
        book5.setImageFile(book5.URLtoBlob(book5.getImageString()));
        book6.setImageFile(book6.URLtoBlob(book6.getImageString()));
        book7.setImageFile(book7.URLtoBlob(book7.getImageString()));
        book8.setImageFile(book8.URLtoBlob(book8.getImageString()));
        bookRepository.save(book5);
        bookRepository.save(book6);
        bookRepository.save(book7);
        bookRepository.save(book8);

        // Add some books (Martin)
        Book book9 = new Book("A Game of Thrones", "A Game of Thrones is the first novel in A Song of Ice and Fire, a series of fantasy novels by the American author George R. R. Martin.", "https://pictures.abebooks.com/isbn/9780553103540-es.jpg", "06-08-1996", "978-0553103540", "A Song of Ice and Fire", 694, "Bantam Spectra");
        Book book10 = new Book("A Clash of Kings", "A Clash of Kings is the second novel in A Song of Ice and Fire, an epic fantasy series by American author George R. R. Martin expected to consist of seven volumes.", "https://m.media-amazon.com/images/I/71R9pRtC6AL._AC_UF1000,1000_QL80_.jpg", "16-11-1998", "978-0553108033", "A Song of Ice and Fire", 768, "Bantam Spectra");
        Book book11 = new Book("A Storm of Swords", "A Storm of Swords is the third of seven planned novels in A Song of Ice and Fire, a fantasy series by American author George R. R. Martin.", "https://m.media-amazon.com/images/I/71G9CGWZhdL._AC_UF1000,1000_QL80_.jpg", "08-08-2000", "978-0553106633", "A Song of Ice and Fire", 973, "Bantam Spectra");
        Book book12 = new Book("A Feast for Crows", "A Feast for Crows is the fourth of seven planned novels in the epic fantasy series A Song of Ice and Fire by American author George R. R. Martin.", "https://m.media-amazon.com/images/I/81MylCMYnVL._AC_UF1000,1000_QL80_.jpg", "17-10-2005", "978-0553106633", "A Song of Ice and Fire", 753, "Bantam Spectra");
        book9.addAuthor(authorRepository.findByName("George R.R. Martin"));
        book10.addAuthor(authorRepository.findByName("George R.R. Martin"));
        book11.addAuthor(authorRepository.findByName("George R.R. Martin"));
        book12.addAuthor(authorRepository.findByName("George R.R. Martin"));
        book9.setImageFile(book9.URLtoBlob(book9.getImageString()));
        book10.setImageFile(book10.URLtoBlob(book10.getImageString()));
        book11.setImageFile(book11.URLtoBlob(book11.getImageString()));
        book12.setImageFile(book12.URLtoBlob(book12.getImageString()));
        bookRepository.save(book9);
        bookRepository.save(book10);
        bookRepository.save(book11);
        bookRepository.save(book12);

        //Add books from dataset
        for (int i = 0; i < books.length(); i++) {
            if (bookRepository.findByTitle(books.getJSONObject(i).getString("title")) == null) {
                Book book = new Book(books.getJSONObject(i).getString("title"), books.getJSONObject(i).getString("description"), books.getJSONObject(i).getString("imageString"), String.valueOf(books.getJSONObject(i).getInt("releaseDate")), books.getJSONObject(i).getString("ISBN"), books.getJSONObject(i).getString("series"), Integer.parseInt(books.getJSONObject(i).getString("Page Count").replaceAll(" ", "")), books.getJSONObject(i).getString("publisher"));
                book.addAuthor(authorRepository.findByName(books.getJSONObject(i).getString("author")));
                book.setImageFile(book.URLtoBlob(book.getImageString()));
                bookRepository.save(book);
            }
        }
    }


}
