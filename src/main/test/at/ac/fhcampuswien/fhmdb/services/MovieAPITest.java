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
    void get_movie_from_criteria() throws IOException {
        String result = movieAPI.GetFilteredMovies("The Godfather", null, 0, 0);

        Object json = mapper.readValue("[\n" +
                "  {\n" +
                "    \"id\": \"81d317b0-29e5-4846-97a6-43c07f3edf4a\",\n" +
                "    \"title\": \"The Godfather\",\n" +
                "    \"genres\": [\n" +
                "      \"DRAMA\"\n" +
                "    ],\n" +
                "    \"releaseYear\": 1972,\n" +
                "    \"description\": \"The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.\",\n" +
                "    \"imgUrl\": \"https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg\",\n" +
                "    \"lengthInMinutes\": 175,\n" +
                "    \"directors\": [\n" +
                "      \"Francis Ford Coppola\"\n" +
                "    ],\n" +
                "    \"writers\": [\n" +
                "      \"Mario Puzo\",\n" +
                "      \"Francis Ford Coppola\"\n" +
                "    ],\n" +
                "    \"mainCast\": [\n" +
                "      \"Marlon Brando\",\n" +
                "      \"Al Pacino\",\n" +
                "      \"James Caan\"\n" +
                "    ],\n" +
                "    \"rating\": 9.2\n" +
                "  }\n" +
                "]", Object.class);
        String compactJson = mapper.writeValueAsString(json);
        assertEquals(compactJson, result);
    }

    @Test
    void get_movie_with_id() throws IOException {
        String id = "81d317b0-29e5-4846-97a6-43c07f3edf4a";
        Movie movie = movieAPI.GetMovie(id);

        //assertTrue(movie.getId().Equals(id));
    }
}
