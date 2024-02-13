package es.codeurjc.holamundo.service;

import es.codeurjc.holamundo.component.Post;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service

// This contains a list of example posts for the landing page.
public class PostList {

    // Data structure to store the example posts (ID, Title, Description, Images, Post date)

    private Map<String, Post> posts = new HashMap<>();

    // Constructor
    public PostList() {
        posts.put("post1", new Post("post1", "Los más leídos de este año", "Descubre los libros más leídos de este año", "/assets/presets/71e2MeS4jFL._AC_UF894,1000_QL80_.jpg", "2021-01-01"));
        posts.put("post2", new Post("post2", "Descubre nuevos mundos", "Déjate llevar por estos libros y descubre mundos maravillosos", "https://www.lecturalia.com/wp-content/uploads/2019/12/los-libros-mas-leidos-de-2019.jpg", "2021-01-01"));
        posts.put("post3", new Post("post3", "Booktubers, sus recomendaciones", "Descubre los libros que recomiendan los booktubers más famosos", "https://www.lecturalia.com/wp-content/uploads/2019/12/los-libros-mas-leidos-de-2019.jpg", "2021-01-01"));
        posts.put("post4", new Post("post4", "Lo último de Patrick Rothfuss", "Descubre las últimas novedades del autor de El Estrecho Sendero Entre Deseos", "https://www.lecturalia.com/wp-content/uploads/2019/12/los-libros-mas-leidos-de-2019.jpg", "2021-01-01"));
    }

    // Method that will return the post list
    public Map<String, Post> getPosts() {
        return posts;
    }

    // Method that will return a specific post
    public Post getPost(String postID) {
        return posts.get(postID);
    }

    // Method that will add a post to the list
    public void addPost(Post post) {
        posts.put(post.getPostID(), post);
    }

    // Method that will remove a post from the list
    public void removePost(String postID) {
        posts.remove(postID);
    }

    // Method that will update a post from the list
    public void updatePost(String postID, Post post) {
        posts.put(postID, post);
    }

    // Method that will return the size of the post list
    public int getSize() {
        return posts.size();
    }

}
