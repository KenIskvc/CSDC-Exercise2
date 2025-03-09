package at.ac.fhcampuswien.fhmdb.services;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class MovieAPI {

    private final String BASE_URL = "https://prog2.fh-campuswien.ac.at";
    OkHttpClient client;

    public MovieAPI() {
         client = new OkHttpClient();
    }

    //not tested yet
    public String GetMovies() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/movies")
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
        throw new IOException();
    }
}
