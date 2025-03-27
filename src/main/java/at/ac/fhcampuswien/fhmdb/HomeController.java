package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.services.MovieAPI;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public ComboBox<String> releaseYearComboBox;

    @FXML
    public ComboBox<String> ratingsComboBox;

    @FXML
    public JFXButton sortBtn;

    @FXML
    public JFXButton resetBtn;

    public List<Movie> allMovies;

    protected ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    protected SortedState sortedState;

    private final MovieAPI movieAPI = new MovieAPI();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeState();
        initializeLayout();
    }

    public void initializeState() {
        try {
            allMovies = movieAPI.GetAllMovies();
            observableMovies.clear();
            observableMovies.addAll(allMovies); // add all movies to the observable list
            sortedState = SortedState.NONE;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void populateReleaseYearComboBox() {
        releaseYearComboBox.getItems().clear();
        releaseYearComboBox.getItems().add("No filter");
        for (int year = 2025; year >= 1950; year--) {
            releaseYearComboBox.getItems().add(String.valueOf(year));
        }
        releaseYearComboBox.setPromptText("Filter by Release Year");
    }
    public void populateRatingComboBox() {
        ratingsComboBox.getItems().clear();
        ratingsComboBox.getItems().add("No filter");

        for (double rating = 10.0; rating >= 5.0; rating -= 0.5) {
            // WICHTIG: Locale.US sorgt für Punkt statt Komma!
            ratingsComboBox.getItems().add(String.format(Locale.US, "%.1f", rating));
        }

        ratingsComboBox.setPromptText("Filter by Rating");
    }




    public void initializeLayout() {
        movieListView.setItems(observableMovies);   // set the items of the listview to the observable list
        movieListView.setCellFactory(movieListView -> new MovieCell()); // apply custom cells to the listview

        Object[] genres = Genre.values();   // get all genres
        genreComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        genreComboBox.getItems().addAll(genres);// add all genres to the combobox

        populateRatingComboBox();
        populateReleaseYearComboBox();

    }

    public void sortMovies(){
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            sortMovies(SortedState.ASCENDING);
        } else if (sortedState == SortedState.ASCENDING) {
            sortMovies(SortedState.DESCENDING);
        }
    }
    // sort movies based on sortedState
    // by default sorted state is NONE
    // afterwards it switches between ascending and descending
    public void sortMovies(SortedState sortDirection) {
        if (sortDirection == SortedState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.ASCENDING;
            sortBtn.setText("Sort (Asc)");
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.DESCENDING;
            sortBtn.setText("Sort (Desc)");
        }
    }

    public List<Movie> filterByQuery(List<Movie> movies, String query){
        if(query == null || query.isEmpty()) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie ->
                    movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    movie.getDescription().toLowerCase().contains(query.toLowerCase())
                )
                .toList();
    }

    public List<Movie> filterByGenre(List<Movie> movies, Genre genre){
        if(genre == null) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie -> movie.getGenres().contains(genre))
                .toList();
    }
    public List<Movie> filterByReleaseYear(List<Movie> movies, int releaseYear) {
        if (movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie -> movie.getReleaseYear() == releaseYear)
                .toList();
    }

    public List<Movie> filterByRating(List<Movie> movies, float ratings) {
        if (movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie -> movie.getRating() >= ratings)
                .toList();
    }


    public void applyAllFilters(String searchQuery, Object genre, Object releaseYear, Object ratings) {
        List<Movie> filteredMovies = allMovies;

        if (!searchQuery.isEmpty()) {
            filteredMovies = filterByQuery(filteredMovies, searchQuery);
        }

        if (genre != null && !genre.toString().equals("No filter")) {
            filteredMovies = filterByGenre(filteredMovies, Genre.valueOf(genre.toString()));
        }

        if (releaseYear != null && !releaseYear.toString().equals("No filter")) {
            try {
                int year = Integer.parseInt(releaseYear.toString());
                filteredMovies = filterByReleaseYear(filteredMovies, year);
            } catch (NumberFormatException e) {
                System.err.println("Ungültiges Release Year: " + releaseYear);
            }
        }

        if (ratings != null && !ratings.toString().equals("No filter")) {
            try {
                float rating = Float.parseFloat(ratings.toString());
                filteredMovies = filterByRating(filteredMovies, rating);
            } catch (NumberFormatException e) {
                System.err.println("Ungültiges Rating: " + ratings);
            }
        }

        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
    }

    public void searchBtnClicked(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().trim().toLowerCase();
        Object genre = genreComboBox.getSelectionModel().getSelectedItem();
        Object  releaseYear = releaseYearComboBox.getSelectionModel().getSelectedItem();
        Object ratings = ratingsComboBox.getSelectionModel().getSelectedItem();
        applyAllFilters(searchQuery, genre, releaseYear, ratings);
        sortMovies(sortedState);
    }

    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }

    /***
     * reset Button implementiert
     * cleared die Suchfelder und Combobxen
     * sortedState auf default Wert(NONE)
     * @param actionEvent
     */
    public void resetBtnClicked(ActionEvent actionEvent) {

    searchField.clear();

    genreComboBox.getSelectionModel().clearSelection();
    releaseYearComboBox.getSelectionModel().clearSelection();
    ratingsComboBox.getSelectionModel().clearSelection();

    observableMovies.clear();
    observableMovies.addAll(allMovies);
    sortedState= sortedState.NONE;
    sortBtn.setText("Sort (unsorted)");
    }

}
