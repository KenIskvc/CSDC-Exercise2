package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie {
    private final String title;
    private final String description;
    private final List<Genre> genres;
    private final double rating;
    private final int releaseYear;
    private final String directors;
    private final String mainCast;
    private  final String writers;
    private final int lengthInMinutes;
    private final String imgUrl;
    private final String id;



    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Movie other)) {
            return false;
        }
        return this.title.equals(other.title) && this.description.equals(other.description) && this.genres.equals(other.genres);
    }

    public Movie(String id, String title, String description, List<Genre> genres, double rating,
                 int releaseYear, String directors, String mainCast, String writers,
                 int lengthInMinutes, String imgUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.rating = rating;
        this.releaseYear = releaseYear;
        this.directors = directors;
        this.mainCast = mainCast;
        this.writers = writers;
        this.lengthInMinutes = lengthInMinutes;
        this.imgUrl = imgUrl;
    }

    // Getter für alle Felder (keine Setter, da immutable)
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<Genre> getGenres() { return genres; }
    public double getRating() { return rating; }
    public int getReleaseYear() { return releaseYear; }
    public String getDirectors() { return directors; }
    public String getMainCast() { return mainCast; }
    public String getWriters() { return writers; }
    public int getLengthInMinutes() { return lengthInMinutes; }
    public String getImgUrl() { return imgUrl; }


}

