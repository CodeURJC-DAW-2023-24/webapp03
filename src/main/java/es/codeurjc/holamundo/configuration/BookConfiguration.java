package es.codeurjc.holamundo.configuration;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class BookConfiguration {
    @Bean
    public JSONArray books() throws IOException {
        return new JSONArray(new String(Files.readAllBytes(Paths.get("src/main/resources/static/assets/data/books.json"))));
    }
}
