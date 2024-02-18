package es.codeurjc.holamundo.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private long id;

    private String name;

    @ManyToMany
    private List<Book> books = new ArrayList<>();
}
