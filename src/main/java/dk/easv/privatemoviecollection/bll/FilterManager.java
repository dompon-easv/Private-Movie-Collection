package dk.easv.privatemoviecollection.bll;

import dk.easv.privatemoviecollection.model.Category;
import dk.easv.privatemoviecollection.model.Movie;
import javafx.collections.transformation.FilteredList;

public class FilterManager {

    private String currentFilter = "";
    private Integer currentRating = null;

    public void filterLists(String filter, FilteredList<Category> filteredCategories, FilteredList<Movie> filteredMovies)
    {
        if (filteredCategories == null || filteredMovies == null) return;

        currentFilter = filter;

        //filteredMovies.setPredicate(Movie -> Movie.getTitle().toLowerCase().contains(currentFilter.toLowerCase()));
        filteredCategories.setPredicate(Category -> Category.getName().toLowerCase().contains(currentFilter.toLowerCase()));
        updateMoviePredicate(filteredMovies);
    }

    public void filterByRating(Integer rating, FilteredList<Movie> filteredMovies) {
        currentRating = rating;
        updateMoviePredicate(filteredMovies);
    }

    private void updateMoviePredicate(FilteredList<Movie> filteredMovies) {
             filteredMovies.setPredicate(movie -> {
            boolean matchesText = movie.getTitle().toLowerCase().contains(currentFilter.toLowerCase());
            boolean matchesRating = currentRating == null || movie.getImdbRating() >= currentRating;
            return matchesText && matchesRating;
        });
    }

    // all functionalities around filtering the lists (only in gui, we're not touching the database here,
    // only managing what is currently shown in the tables
}
