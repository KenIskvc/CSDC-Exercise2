package at.ac.fhcampuswien.fhmdb.utils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import at.ac.fhcampuswien.fhmdb.models.Movie;

public class MovieUtils {

    public static String getMostPopularActor(List<Movie> movies) {
        return movies.stream()
                .flatMap(movie -> movie.getMainCast().stream()) // Alle Schauspieler extrahieren
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())) // Häufigkeit zählen
                .entrySet().stream()
                .max(Map.Entry.comparingByValue()) // Meistgenannten Schauspieler finden
                .map(Map.Entry::getKey)
                .orElse("Unknown");
    }

    public static int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .map(Movie::getTitle)
                .mapToInt(String::length)
                .max()
                .orElse(0);
    }

    public static long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirectors().stream()
                        .anyMatch(d -> d.equalsIgnoreCase(director)))
                .count();
    }

    public static List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }
}