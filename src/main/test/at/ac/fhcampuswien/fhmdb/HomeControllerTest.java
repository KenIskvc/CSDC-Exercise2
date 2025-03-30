package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.HomeController;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.services.MovieAPI;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeControllerTest {

    static class CapturingMovieAPI extends MovieAPI {
        public Genre capturedGenre;
        public int capturedYear;
        public double capturedRating;

        @Override
        public List<Movie> GetFilteredMovies(String searchQuery, Genre genre, int releaseYear, double rating) {
            this.capturedGenre = genre;
            this.capturedYear = releaseYear;
            this.capturedRating = rating;
            return List.of(); // doesn’t matter
        }
    }

    @Test
    void testGenreIsParsedCorrectly() {
        CapturingMovieAPI api = new CapturingMovieAPI();
        HomeController service = new HomeController(api);

        service.applyFilters(null, "ACTION", "No filter", "No filter");

        assertEquals(Genre.ACTION, api.capturedGenre);
    }

    @Test
    void testReleaseYearIsParsedCorrectly() {
        CapturingMovieAPI api = new CapturingMovieAPI();
        HomeController service = new HomeController(api);

        service.applyFilters(null, "No filter", "2020", "No filter");

        assertEquals(2020, api.capturedYear);
    }

    @Test
    void testRatingIsParsedCorrectly() {
        CapturingMovieAPI api = new CapturingMovieAPI();
        HomeController service = new HomeController(api);

        service.applyFilters(null, "No filter", "No filter", "8.5");

        assertEquals(8.5, api.capturedRating);
    }

    @Test
    void testInvalidReleaseYearResultsInMinus1() {
        CapturingMovieAPI api = new CapturingMovieAPI();
        HomeController service = new HomeController(api);

        service.applyFilters(null, "No filter", "invalid-year", "No filter");

        assertEquals(-1, api.capturedYear);
    }

    @Test
    void testInvalidRatingResultsInMinus1() {
        CapturingMovieAPI api = new CapturingMovieAPI();
        HomeController service = new HomeController(api);

        service.applyFilters(null, "No filter", "No filter", "bad-rating");

        assertEquals(-1, api.capturedRating);
    }

    @Test
    void testIOExceptionResultsInEmptyList() {
        MovieAPI throwingAPI = new MovieAPI() {
            @Override
            public List<Movie> GetFilteredMovies(String searchQuery, Genre genre, int releaseYear, double rating) throws IOException {
                throw new IOException("Simulated failure");
            }
        };

        HomeController service = new HomeController(throwingAPI);
        List<Movie> result = service.applyFilters(null, "No filter", "No filter", "No filter");

        assertTrue(result.isEmpty());
    }
}
