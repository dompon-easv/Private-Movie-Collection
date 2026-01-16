package dk.easv.privatemoviecollection.bll;

import dk.easv.privatemoviecollection.be.Category;
import dk.easv.privatemoviecollection.be.Movie;
import javafx.collections.transformation.FilteredList;

public class FilterManager {

    private String currentFilter = "";
    private Integer currentRating = null;

    public void filterLists(String filter, FilteredList<Category> filteredCategories, FilteredList<Movie> filteredMovies)
    {
        if (filteredCategories == null || filteredMovies == null) return;

        //saving the filter so we can use it in the combined filter later
        currentFilter = filter;

        //categories are filtered here cause there is only one filter we apply
        filteredCategories.setPredicate(Category -> Category.getName().toLowerCase().contains(currentFilter.toLowerCase()));

        //movies are filtered in a seperate method cause we apply 2 filters
        updateMoviePredicate(filteredMovies);
    }

    public void filterByRating(Integer rating, FilteredList<Movie> filteredMovies) {
        currentRating = rating;

        //movies are filtered in a seperate method cause we apply 2 filters
        updateMoviePredicate(filteredMovies);
    }

    private void updateMoviePredicate(FilteredList<Movie> filteredMovies) {
             filteredMovies.setPredicate(movie -> {
                 //check which entries match the filter
            boolean matchesText = movie.getTitle().toLowerCase().contains(currentFilter.toLowerCase());

                //check which entries are rated according to filter OR there's no filter at all
            boolean matchesRating = currentRating == null || movie.getImdbRating() >= currentRating;

                //return the entries that passed through both filterings
            return matchesText && matchesRating;
        });
    }
}
