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
        JSONArray books = new JSONArray(new String(Files.readAllBytes(Paths.get("src/main/resources/static/assets/data/books.json"))));
        //Temporary
        JSONArray lessBooks = new JSONArray();
        for (int i = 0; i < 200; i++) {
            lessBooks.put(books.getJSONObject(i));
        }
        return lessBooks;
    }
}
