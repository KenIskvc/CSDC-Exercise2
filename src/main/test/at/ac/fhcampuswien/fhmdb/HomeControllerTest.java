package at.ac.fhcampuswien.fhmdb;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeControllerTest {

    private static HomeController homeController;
    @BeforeAll
    public static void init() {
        homeController = new HomeController();
    }

    @Test
    void testGenreIsParsedCorrectly() {

        String requestedGenre = "ACTION";
        Map<String, String> filterValues = homeController.GetFilterValues(null, requestedGenre, "No filter", "No filter");

        String genre = filterValues.get("genre");

        assertEquals(requestedGenre, genre);
    }

//    @Test
//    void testReleaseYearIsParsedCorrectly() {
//        CapturingMovieAPI api = new CapturingMovieAPI();
//        HomeController service = new HomeController(api);
//
//        service.applyFilters(null, "No filter", "2020", "No filter");
//
//        assertEquals(2020, api.capturedYear);
//    }
//
//    @Test
//    void testRatingIsParsedCorrectly() {
//        CapturingMovieAPI api = new CapturingMovieAPI();
//        HomeController service = new HomeController(api);
//
//        service.applyFilters(null, "No filter", "No filter", "8.5");
//
//        assertEquals(8.5, api.capturedRating);
//    }
//
//    @Test
//    void testInvalidReleaseYearResultsInMinus1() {
//        CapturingMovieAPI api = new CapturingMovieAPI();
//        HomeController service = new HomeController(api);
//
//        service.applyFilters(null, "No filter", "invalid-year", "No filter");
//
//        assertEquals(-1, api.capturedYear);
//    }
//
//    @Test
//    void testInvalidRatingResultsInMinus1() {
//        CapturingMovieAPI api = new CapturingMovieAPI();
//        HomeController service = new HomeController(api);
//
//        service.applyFilters(null, "No filter", "No filter", "bad-rating");
//
//        assertEquals(-1, api.capturedRating);
//    }
//
//    @Test
//    void testIOExceptionResultsInEmptyList() {
//        MovieAPI throwingAPI = new MovieAPI() {
//            @Override
//            public List<Movie> GetFilteredMovies(String searchQuery, Genre genre, int releaseYear, double rating) throws IOException {
//                throw new IOException("Simulated failure");
//            }
//        };
//
//        HomeController service = new HomeController(throwingAPI);
//        List<Movie> result = service.applyFilters(null, "No filter", "No filter", "No filter");
//
//        assertTrue(result.isEmpty());
//    }
}
