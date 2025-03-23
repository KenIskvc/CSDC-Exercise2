package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.stream.Collectors;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final Label yearAndRating = new Label();
    private final VBox layout = new VBox(title, detail, genre, yearAndRating);

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setGraphic(null);
            setText(null);
        } else {
            this.getStyleClass().add("movie-cell");

            // Title
            title.setText(movie.getTitle());

            // Description
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );

            // Genres
            String genres = movie.getGenres()
                    .stream()
                    .map(Enum::toString)
                    .collect(Collectors.joining(", "));
            genre.setText(genres);

            // Release Year + Rating
            String releaseYear = movie.getReleaseYear() > 0 ? String.valueOf(movie.getReleaseYear()) : "Unknown year";
            String rating = movie.getRating() > 0 ? String.format("%.1f / 10", movie.getRating()) : "No rating";
            yearAndRating.setText("Year: " + releaseYear + "    Rating: " + rating);

            // Styling
            title.getStyleClass().add("text-yellow");
            detail.getStyleClass().add("text-white");
            genre.getStyleClass().add("text-white");
            yearAndRating.getStyleClass().add("text-white");

            genre.setStyle("-fx-font-style: italic");
            yearAndRating.setStyle("-fx-font-style: italic");

            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // Layout
            title.fontProperty().set(title.getFont().font(20));
            detail.setMaxWidth(this.getScene().getWidth() - 30);
            detail.setWrapText(true);

            layout.setPadding(new Insets(10));
            layout.spacingProperty().set(10);
            layout.alignmentProperty().set(javafx.geometry.Pos.CENTER_LEFT);

            setGraphic(layout);
        }
    }

}

