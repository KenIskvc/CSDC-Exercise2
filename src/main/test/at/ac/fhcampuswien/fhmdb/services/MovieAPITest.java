package at.ac.fhcampuswien.fhmdb.services;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieAPITest {

    private static MovieAPI movieAPI;
    private static ObjectMapper mapper;

    @BeforeAll
    public static void init() {
        movieAPI = new MovieAPI();
        mapper = new ObjectMapper();
    }
    @Test
    void get_all_movies_without_params_test() throws IOException {
        List<Movie> result = movieAPI.GetAllMovies();
        assertFalse(result.isEmpty());
    }

    @Test
    void get_movie_with_id() throws IOException {
        String id = "81d317b0-29e5-4846-97a6-43c07f3edf4a";
        Movie movie = movieAPI.GetMovie(id);

        assertEquals(id, movie.getId());
    }

    @Test
    void get_movie_from_query() throws IOException {
        String query = "The Godfather";
        List<Movie> movies = movieAPI.GetFilteredMovies(query, null, 0, 0);

        assertEquals(query, movies.get(0).getTitle());
    }


}
