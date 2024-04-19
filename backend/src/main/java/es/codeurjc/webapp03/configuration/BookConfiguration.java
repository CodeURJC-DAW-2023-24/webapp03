package es.codeurjc.webapp03.configuration;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Configuration
public class BookConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BookConfiguration.class);
        String booksPath = "backend/src/main/resources/static/assets/data/books.json";
    @Bean()
    public JSONArray books() throws IOException {
        String runningInDocker = System.getenv("RUNNING_IN_DOCKER");

        JSONArray books = null;

        if ((runningInDocker) != null && runningInDocker.equals("true")) {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("static/assets/data/books.json")) {
                if (is == null) {
                    throw new IOException("Cannot find resource");
                }

                String json = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));

                books = new JSONArray(json);
            }
        } else {
            String baseDir = System.getProperty("user.dir").replace("\\", "/").replace("/backend", "");
            booksPath = baseDir + "/" + booksPath;
            books = new JSONArray(new String(Files.readAllBytes(Paths.get(booksPath))));
        }

        try {

            JSONArray lessBooks = new JSONArray();

            for (int i = 0; i < 200; i++) {
                lessBooks.put(books.getJSONObject(i));
            }
            return lessBooks;
        } catch (Exception e) {
            logger.error("Error creating books bean", e);
            throw e;
        }


    }
}
