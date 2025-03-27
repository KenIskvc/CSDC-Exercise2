package at.ac.fhcampuswien.fhmdb.models;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieTest {

    @Test
    void movies_with_same_data_should_be_equal() {
        Movie m1 = new Movie("id1", "Inception", "Dreaming", List.of(Genre.ACTION), 2010, "url", 148,
                List.of("Nolan"), List.of("Nolan"), List.of("Leo"), 8.8);
        Movie m2 = new Movie("id2", "Inception", "Dreaming", List.of(Genre.ACTION), 2010, "url", 148,
                List.of("Someone"), List.of("Other"), List.of("Leo"), 8.8);

        assertEquals(m1, m2);
    }

    @Test
    void movies_with_different_titles_should_not_be_equal() {
        Movie m1 = new Movie("id1", "Inception", "Dreaming", List.of(Genre.ACTION), 2010, "url", 148,
                List.of("Nolan"), List.of("Nolan"), List.of("Leo"), 8.8);
        Movie m2 = new Movie("id2", "Interstellar", "Dreaming", List.of(Genre.ACTION), 2010, "url", 148,
                List.of("Nolan"), List.of("Nolan"), List.of("Leo"), 8.8);

        assertNotEquals(m1, m2);
    }
}