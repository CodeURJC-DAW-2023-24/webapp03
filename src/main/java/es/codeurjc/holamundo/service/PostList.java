package es.codeurjc.holamundo.service;

import es.codeurjc.holamundo.component.Post;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service

// This contains a list of example posts for the landing page.
public class PostList {

    // Data structure to store the example posts (ID, Title, Description, Images, Post date)

    private Map<Integer, Post> posts = new HashMap<>();

    // Constructor
    public PostList() {
        posts.put(1, new Post("post1", "Los más leídos de este año", "Descubre los libros más leídos de este año", "/assets/presets/71e2MeS4jFL._AC_UF894,1000_QL80_.jpg", "2021-01-01"));
        posts.put(2, new Post("post2", "Descubre nuevos mundos", "Déjate llevar por estos libros y descubre mundos maravillosos", "https://catalogo.noe-sm.com/medias/ES124940.jpg?context=bWFzdGVyfHJvb3R8MTE0MjcxfGltYWdlL2pwZWd8aGE3L2gxMS84OTk5NDIzNzA1MTE4LmpwZ3w4MmEzMzg5ZTg4ZmYxZThkMGYwZTVjNDIwZDQ4Y2ZkZjFmYjM3NjZjNWE0ZTNhZmQzZWMzMGU0M2E2NjEyYjA0", "2021-01-01"));
        posts.put(3, new Post("post3", "Booktubers, sus recomendaciones", "Descubre los libros que recomiendan los booktubers más famosos", "https://multimedia.dideco.es/img/literatura/EAN_9788427220287-5.jpg", "2021-01-01"));
        posts.put(4, new Post("post4", "Lo último de Patrick Rothfuss", "Descubre las últimas novedades del autor de El Estrecho Sendero Entre Deseos", "https://m.media-amazon.com/images/I/81i7bgkg-GL._AC_UF894,1000_QL80_.jpg", "2021-01-01"));
        posts.put(5, new Post("post5", "La maravillosa saga de Harry Potter", "Descubre la saga de Harry Potter y disfruta de la magia", "https://ew.com/thmb/MaxHBP4uhvg9_3eNeWgx_SOSku0=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/9781408855669-6cfb2099b6e84a4899ce368d6facc242.jpg", "2021-01-01"));
        posts.put(6, new Post("post6", "J.K Rowling recomienda", "Descubre los libros que recomienda la autora de Harry Potter", "https://cdn.kobo.com/book-images/6750d058-29cb-4626-9c12-a62e816a80cc/1200/1200/False/harry-potter-and-the-philosopher-s-stone-3.jpg", "2021-01-01"));
    }

    // Method that will return the post list
    public Map<Integer, Post> getPosts() {
        return posts;
    }

    // Method that will return a specific post
    public Post getPost(int postID) {
        return posts.get(postID);
    }

    // Method that will add a post to the list
    public void addPost(Post post) {
        posts.put(posts.size()+1, post);
    }

    // Method that will remove a post from the list
    public void removePost(int postID) {
        posts.remove(postID);
    }

    // Method that will update a post from the list
    public void updatePost(int postID, Post post) {
        posts.put(postID, post);
    }

    // Method that will return the size of the post list
    public int getSize() {
        return posts.size();
    }

    public ArrayList<Post> getMultiplePosts(int page, int size) {
        ArrayList<Post> postsToReturn = new ArrayList<>();

        for(int i = page; i <= size; i++) {
            postsToReturn.add(posts.get(i));
        }

        return postsToReturn;
    }

}
