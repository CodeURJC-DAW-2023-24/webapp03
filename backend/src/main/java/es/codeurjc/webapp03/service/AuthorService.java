package es.codeurjc.webapp03.service;

import es.codeurjc.webapp03.entity.Author;
import es.codeurjc.webapp03.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Author getAuthor(long id) {
        return authorRepository.findById(id).orElse(null);
    }

    public Author getAuthor(String name) {
        return authorRepository.findByName(name);
    }

    public void saveAuthor(Author author) {
        authorRepository.save(author);
    }

    public List<Object[]> getMostReadAuthorsNameAndCount() {
        return authorRepository.getMostReadAuthorsNameAndCount();
    }

    public long getTotalAuthors() {
        return authorRepository.count();
    }

    public List<Author> getMostReadAuthors() {
        return authorRepository.getMostReadAuthors();
    }


}
