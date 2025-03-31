package at.ac.fhcampuswien.fhmdb.services;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieAPITest {

    private static MovieAPI movieAPI;

    @BeforeAll
    public static void init() {
        movieAPI = new MovieAPI();
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
        List<Movie> movies = movieAPI.GetFilteredMovies(query, null, -1, -1);

        assertEquals(query, movies.get(0).getTitle());
    }

    @Test
    void get_movies_from_genre() throws IOException {
        List<Movie> movies = movieAPI.GetFilteredMovies(null, Genre.DRAMA, 0, 0);

        boolean isDrama = true;

        for(Movie movie : movies) {
            if(!movie.getGenres().contains(Genre.DRAMA)) {
                isDrama = false;
                break;
            }
        }
        assertTrue(isDrama);
    }

    @Test
    void get_movies_from_ratingForm() throws IOException {
        double rating = 8;
        List<Movie> movies = movieAPI.GetFilteredMovies(null, null, 0, rating);

        boolean isAboveRating = true;

        for(Movie movie : movies) {
            if(movie.getRating() < rating) {
                isAboveRating = false;
                break;
            }
        }
        assertTrue(isAboveRating);
    }

    @Test
    void get_movies_from_releaseYear() throws IOException {
        int year = 1972;
        List<Movie> movies = movieAPI.GetFilteredMovies(null, null, year, 0);

        boolean matchesReleasYear = true;

        for(Movie movie : movies) {
            if(movie.getReleaseYear() != year) {
                matchesReleasYear = false;
                break;
            }
        }

        assertTrue(matchesReleasYear);
    }
}
