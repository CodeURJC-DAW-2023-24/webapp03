package es.codeurjc.holamundo.component;

import org.springframework.stereotype.Component;

@Component
public class Post {
    private String postID;
    private String title;
    private String description;
    private String image;
    private String postDate;

    public Post() {
    }

    public Post(String postID, String title, String description, String image, String postDate) {
        this.postID = postID;
        this.title = title;
        this.description = description;
        this.image = image;
        this.postDate = postDate;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }
}
