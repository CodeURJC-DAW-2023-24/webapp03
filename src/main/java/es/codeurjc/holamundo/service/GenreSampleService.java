package es.codeurjc.holamundo.service;

import es.codeurjc.holamundo.entity.Book;
import es.codeurjc.holamundo.entity.Genre;
import es.codeurjc.holamundo.repository.BookRepository;
import es.codeurjc.holamundo.repository.GenreRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreSampleService {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostConstruct
    public void init() {
        // Add some genres
        Genre genre1 = new Genre("Fantasy");
        Genre genre2 = new Genre("Science Fiction");
        Genre genre3 = new Genre("Horror");

        // Add some books to genres (Fantasy)
        genre1.addBook(bookRepository.findByTitle("The Hobbit"));
        genre1.addBook(bookRepository.findByTitle("The Fellowship of the Ring"));
        genre1.addBook(bookRepository.findByTitle("The Two Towers"));
        genre1.addBook(bookRepository.findByTitle("The Return of the King"));

        // Add some books to genres (Science Fiction)
        genre2.addBook(bookRepository.findByTitle("A Game of Thrones"));
        genre2.addBook(bookRepository.findByTitle("A Clash of Kings"));
        genre2.addBook(bookRepository.findByTitle("A Storm of Swords"));
        genre2.addBook(bookRepository.findByTitle("A Feast for Crows"));

        // Add some books to genres (Horror)
        genre3.addBook(bookRepository.findByTitle("Harry Potter and the Philosopher's Stone"));
        genre3.addBook(bookRepository.findByTitle("Harry Potter and the Chamber of Secrets"));
        genre3.addBook(bookRepository.findByTitle("Harry Potter and the Prisoner of Azkaban"));
        genre3.addBook(bookRepository.findByTitle("Harry Potter and the Goblet of Fire"));

        // Save the genres
        genreRepository.save(genre1);
        genreRepository.save(genre2);
        genreRepository.save(genre3);

        // Get the books
        Book book1 = bookRepository.findByTitle("The Hobbit");
        Book book2 = bookRepository.findByTitle("The Fellowship of the Ring");
        Book book3 = bookRepository.findByTitle("The Two Towers");
        Book book4 = bookRepository.findByTitle("The Return of the King");
        Book book5 = bookRepository.findByTitle("A Game of Thrones");
        Book book6 = bookRepository.findByTitle("A Clash of Kings");
        Book book7 = bookRepository.findByTitle("A Storm of Swords");
        Book book8 = bookRepository.findByTitle("A Feast for Crows");
        Book book9 = bookRepository.findByTitle("Harry Potter and the Philosopher's Stone");
        Book book10 = bookRepository.findByTitle("Harry Potter and the Chamber of Secrets");
        Book book11 = bookRepository.findByTitle("Harry Potter and the Prisoner of Azkaban");
        Book book12 = bookRepository.findByTitle("Harry Potter and the Goblet of Fire");

        // Set genres to books
        book1.setGenre(genre1);
        book2.setGenre(genre1);
        book3.setGenre(genre1);
        book4.setGenre(genre1);
        book5.setGenre(genre2);
        book6.setGenre(genre2);
        book7.setGenre(genre2);
        book8.setGenre(genre2);
        book9.setGenre(genre3);
        book10.setGenre(genre3);
        book11.setGenre(genre3);
        book12.setGenre(genre3);

        // Save the books
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        bookRepository.save(book4);
        bookRepository.save(book5);
        bookRepository.save(book6);
        bookRepository.save(book7);
        bookRepository.save(book8);
        bookRepository.save(book9);
        bookRepository.save(book10);
        bookRepository.save(book11);
        bookRepository.save(book12);

    }
}
