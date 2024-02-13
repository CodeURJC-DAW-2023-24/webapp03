package es.codeurjc.holamundo.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

public class Review {
    private int ID;
    private String title;
    private String author;
    private int rating;
    private String content;

    public Review(int ID, String title, String author, int rating, String content){
        this.ID = ID;
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.content = content;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getContent() {
        return this.content;
    }

    public int getRating() {
        return this.rating;
    }
}