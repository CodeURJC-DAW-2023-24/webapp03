package es.codeurjc.webapp03.configuration;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class BookConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BookConfiguration.class);
        String booksPath = "backend/src/main/resources/static/assets/data/books.json";
    @Bean
    public JSONArray books() throws IOException {
        String runningInDocker = System.getenv("RUNNING_IN_DOCKER");

        if ((runningInDocker) != null && runningInDocker.equals("true")) {
            booksPath = "/" + booksPath;
        } else {
            String baseDir = System.getProperty("user.dir").replace("\\", "/").replace("/backend", "");
            booksPath = baseDir + "/" + booksPath;
        }

        logger.info("Creating books bean...");
        try {
            JSONArray books = new JSONArray(new String(Files.readAllBytes(Paths.get(booksPath))));

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
