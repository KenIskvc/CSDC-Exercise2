package at.ac.fhcampuswien.fhmdb.services;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieUtilsTest {

    private List<Movie> testMovies;

    @BeforeEach
    void setUp() {
        testMovies = List.of(
                new Movie("1", "Inception", "Dream inside a dream", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), 2010, "url", 148,
                        List.of("Christopher Nolan"), List.of("Nolan"), List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt"), 8.8),

                new Movie("2", "The Dark Knight", "Batman vs Joker", List.of(Genre.ACTION), 2008, "url", 152,
                        List.of("Christopher Nolan"), List.of("Jonathan Nolan"), List.of("Christian Bale", "Heath Ledger"), 9.0),

                new Movie("3", "Interstellar", "Space and Time", List.of(Genre.DRAMA, Genre.SCIENCE_FICTION), 2014, "url", 169,
                        List.of("Christopher Nolan"), List.of("Jonathan Nolan"), List.of("Matthew McConaughey", "Anne Hathaway"), 8.6),

                new Movie("4", "Titanic", "Ship hits iceberg", List.of(Genre.DRAMA, Genre.ROMANCE), 1997, "url", 195,
                        List.of("James Cameron"), List.of("James Cameron"), List.of("Leonardo DiCaprio", "Kate Winslet"), 7.8)
        );
    }

    @Test
    void testGetMostPopularActor() {
        String result = MovieUtils.getMostPopularActor(testMovies);
        assertEquals("Leonardo DiCaprio", result);
    }

    @Test
    void testGetLongestMovieTitle() {
        int result = MovieUtils.getLongestMovieTitle(testMovies);
        assertEquals("The Dark Knight".length(), result);
    }

    @Test
    void testCountMoviesFrom() {
        long nolanCount = MovieUtils.countMoviesFrom(testMovies, "Christopher Nolan");
        assertEquals(3, nolanCount);

        long cameronCount = MovieUtils.countMoviesFrom(testMovies, "James Cameron");
        assertEquals(1, cameronCount);
    }

    @Test
    void testGetMoviesBetweenYears() {
        List<Movie> result = MovieUtils.getMoviesBetweenYears(testMovies, 2000, 2015);
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(m -> m.getTitle().equals("Inception")));
    }
}
