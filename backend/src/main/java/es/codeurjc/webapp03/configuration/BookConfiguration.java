package es.codeurjc.webapp03.configuration;

import org.json.JSONArray;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class BookConfiguration {
    @Bean
    public JSONArray books() throws IOException {
        String booksPath = "backend/src/main/resources/static/assets/data/books.json";
        String runningInDocker = System.getenv("RUNNING_IN_DOCKER");
        if ((runningInDocker) != null && runningInDocker.equals("true")) {
            booksPath = "/" + booksPath;
        }
        JSONArray books = new JSONArray(new String(Files.readAllBytes(Paths.get(booksPath))));
        //Temporary
        JSONArray lessBooks = new JSONArray();
        for (int i = 0; i < 200; i++) {
            lessBooks.put(books.getJSONObject(i));
        }
        return lessBooks;
    }
}
