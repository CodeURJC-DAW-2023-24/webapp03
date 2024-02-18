package es.codeurjc.holamundo.repository;

import es.codeurjc.holamundo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByTitle(String title);

    Book findByID(long ID);

}
