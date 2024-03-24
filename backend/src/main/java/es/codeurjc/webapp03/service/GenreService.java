package es.codeurjc.webapp03.service;

import es.codeurjc.webapp03.entity.Genre;
import es.codeurjc.webapp03.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DependsOn("genreSampleService")
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public Genre getGenre(String genreName){
        return genreRepository.findByName(genreName);
    }

    public void saveGenre(Genre genre){
        genreRepository.save(genre);
    }

    public long countGenres(){
        return genreRepository.count();
    }

    public List<Genre> getMostReadGenres(){
        return genreRepository.getMostReadGenres();
    }

    public List<Object[]> getMostReadGenresNameAndCount(){
        return genreRepository.getMostReadGenresNameAndCount();
    }

    public List<Genre> getAllGenres(){
        return genreRepository.findAll();
    }
}
