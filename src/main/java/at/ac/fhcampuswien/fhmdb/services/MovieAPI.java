package at.ac.fhcampuswien.fhmdb.services;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MovieAPI {

    private final String BASE_URL = "https://prog2.fh-campuswien.ac.at";
    OkHttpClient client;
    ObjectMapper mapper;

    public MovieAPI() {
        client = new OkHttpClient();
        mapper = new ObjectMapper();
    }

    //not tested yet
    public List<Movie> GetAllMovies() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/movies")
                .header("User-Agent", "UserAgentGroup/1.3")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            List<Movie> movies = mapper.readValue(body,  new TypeReference<List<Movie>>(){});
            return movies;
        } catch (IOException e) {
            System.out.println("Fetching movies failed");
            System.out.println("GetAllMovies()-Error: " + e.getMessage());
            return null;
        }
    }

    public List<Movie> GetFilteredMovies(String query, String genre, int releaseYear, double ratingForm) throws IOException {
        HttpUrl.Builder urlBuilder
                = Objects.requireNonNull(HttpUrl.parse(BASE_URL + "/movies")).newBuilder();

        if (query != null && !query.isEmpty()) urlBuilder.addQueryParameter("query", query);
        if(genre != null) urlBuilder.addQueryParameter("genre", genre);
        //if(releaseYear != -1) urlBuilder.addQueryParameter("releaseYear", String.valueOf(releaseYear));
        if(ratingForm != -1) urlBuilder.addQueryParameter("ratingForm", String.valueOf(ratingForm));

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "UserAgentGroup/1.3")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            List<Movie> filteredMovies = mapper.readValue(body,  new TypeReference<List<Movie>>(){});
            return filteredMovies;
        } catch (IOException e) {
            System.out.println("Fetching movies failed");
            System.out.println("GetFilteredMovies()-Error: " + e.getMessage());
            return null;
        }
    }

    public Movie GetMovie(String id) throws IOException {
        if (id == null || id.isEmpty()) return null;

        HttpUrl.Builder urlBuilder
                = Objects.requireNonNull(HttpUrl.parse(BASE_URL + "/movies/" + id)).newBuilder();

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "UserAgentGroup/1.3")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String json = response.body().string();
            Movie movie = mapper.readValue(json, Movie.class);
            return movie;
        } catch (IOException e) {
            System.out.println("Fetching movies failed");
            System.out.println("GetMovie()-Error: " + e.getMessage());
            return null;
        }
    }
}
