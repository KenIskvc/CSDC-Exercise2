package at.ac.fhcampuswien.fhmdb.services;

import at.ac.fhcampuswien.fhmdb.models.Movie;
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
            return mapper.readValue(body, List.class);

        } catch (IOException e) {
            System.out.println("Fetching movies failed");
            System.out.println("GetMovies()-Error: " + e.getMessage());
            return null;
        }
    }

    public String GetFilteredMovies(String query, String genre, int releaseYear, double ratingForm) throws IOException {
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
            return response.body().string();
        } catch (IOException e) {
            System.out.println("Fetching movies failed");
            System.out.println("GetMovies()-Error: " + e.getMessage());
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
            return mapper.readValue(json, Movie.class);
        } catch (IOException e) {
            System.out.println("Fetching movies failed");
            System.out.println("GetMovies()-Error: " + e.getMessage());
            return null;
        }
    }
}
