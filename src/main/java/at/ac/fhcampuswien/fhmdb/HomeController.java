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
import java.util.stream.Collectors;

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
        MovieAPI movieAPI = new MovieAPI();
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

    /***
     * holt alle Filme mit der API und filter nach distinct releaseYears
     * zuvor hat man oft keine Filme gefunden, weil es nicht zu jedem Jahr auch einen Film gab
     * nicht optimal aber funktioniert
     */
    public void populateReleaseYearComboBox() {
        releaseYearComboBox.getItems().clear();
        releaseYearComboBox.getItems().add("No filter");

        try {
            // Hol dir ALLE Filme (ohne Filter)
            List<Movie> allMovies = movieAPI.GetFilteredMovies(null, null, -1, -1);

            // Hole alle releaseYears und entferne Duplicates
            Set<Integer> years = allMovies.stream()
                    .map(Movie::getReleaseYear)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // Sortieren (optional)
            List<Integer> sortedYears = years.stream()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());

            // Items hinzufügen
            for (Integer year : sortedYears) {
                releaseYearComboBox.getItems().add(String.valueOf(year));
            }

            releaseYearComboBox.setPromptText("Filter by Release Year");

        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Filme: " + e.getMessage());
        }
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



    public List<Movie> filterMovies(String searchQuery, Object genre, Object releaseYear, Object ratings) {
        try {
            // Genre: prüfe, ob ein Filter gesetzt ist
            Genre genreFilter = null;
            if (genre != null && !genre.toString().equals("No filter")) {
                genreFilter = Genre.valueOf(genre.toString());
            }

            // Release Year: wenn gesetzt, konvertieren
            int releaseYearFilter = -1;  // -1 = kein Filter
            if (releaseYear != null && !releaseYear.toString().equals("No filter")) {
                try {
                    releaseYearFilter = Integer.parseInt(releaseYear.toString());
                } catch (NumberFormatException e) {
                    System.err.println("Ungültiges Release Year: " + releaseYear);
                }
            }

            // Rating: wenn gesetzt, konvertieren
            double ratingFilter = -1;
            if (ratings != null && !ratings.toString().equals("No filter")) {
                try {
                    ratingFilter = Double.parseDouble(ratings.toString());
                } catch (NumberFormatException e) {
                    System.err.println("Ungültiges Rating: " + ratings);
                }
            }

            // Rufe die API-Methode auf und hole die gefilterte Liste
            List<Movie> filteredMovies = movieAPI.GetFilteredMovies(searchQuery, genreFilter, releaseYearFilter, ratingFilter);

            // Wenn nichts zurückkommt, gib eine leere Liste zurück
            return Objects.requireNonNullElse(filteredMovies, Collections.emptyList());

        } catch (IOException e) {
            System.err.println("Fehler beim Filtern der Filme über die API: " + e.getMessage());
            return Collections.emptyList();
        }
    }



    public void applyAllFilters(String searchQuery, Object genre, Object releaseYear, Object ratings) {
        System.out.println("Applying filters...");  // optional debug
        List<Movie> filteredMovies = filterMovies(searchQuery, genre, releaseYear, ratings);

        observableMovies.clear();
        observableMovies.addAll(filteredMovies);

        System.out.println("Filters applied. Movies found: " + filteredMovies.size());  // optional debug
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
