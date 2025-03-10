package at.ac.fhcampuswien.fhmdb.services;

import okhttp3.*;

import java.io.IOException;

public class MovieAPI {

    private final String BASE_URL = "https://prog2.fh-campuswien.ac.at";
    OkHttpClient client;

    public MovieAPI() {
         client = new OkHttpClient();
    }

    //not tested yet
    public String GetAllMovies() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/movies")
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
    public String GetMoviesFromQuery(String query) throws IOException {
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(BASE_URL + "/movies").newBuilder();
        urlBuilder.addQueryParameter("query", query);
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

    public String GetFilteredMovies(String query, String genre, int releaseYear, double ratingForm) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/movies")
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

    public String GetMovie() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/movies")
                .build();
        return null;
    }
}
