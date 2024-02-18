package es.codeurjc.holamundo.service;

import es.codeurjc.holamundo.entity.Author;
import es.codeurjc.holamundo.repository.AuthorRepository;
import es.codeurjc.holamundo.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Service
public class AuthorSampleService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostConstruct
    public void init() {
        // Add some authors
        Author author1 = new Author("J.R.R. Tolkien");
        Author author2 = new Author("J.K. Rowling");
        Author author3 = new Author("George R.R. Martin");
        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);

        // Add some books to authors (Tolkien)
        author1.addBook(bookRepository.findByTitle("The Hobbit"));
        author1.addBook(bookRepository.findByTitle("The Fellowship of the Ring"));
        author1.addBook(bookRepository.findByTitle("The Two Towers"));
        author1.addBook(bookRepository.findByTitle("The Return of the King"));

        // Add some books to authors (Rowling)
        author2.addBook(bookRepository.findByTitle("Harry Potter and the Philosopher's Stone"));
        author2.addBook(bookRepository.findByTitle("Harry Potter and the Chamber of Secrets"));
        author2.addBook(bookRepository.findByTitle("Harry Potter and the Prisoner of Azkaban"));
        author2.addBook(bookRepository.findByTitle("Harry Potter and the Goblet of Fire"));

        // Add some books to authors (Martin)
        author3.addBook(bookRepository.findByTitle("A Game of Thrones"));
        author3.addBook(bookRepository.findByTitle("A Clash of Kings"));
        author3.addBook(bookRepository.findByTitle("A Storm of Swords"));
        author3.addBook(bookRepository.findByTitle("A Feast for Crows"));

    }

}
