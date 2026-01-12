package dk.easv.privatemoviecollection.bll;

import dk.easv.privatemoviecollection.model.Category;
import dk.easv.privatemoviecollection.model.Movie;
import javafx.collections.transformation.FilteredList;

public class FilterManager {

    public void filterLists(String filter, FilteredList<Category> filteredCategories, FilteredList<Movie> filteredMovies)
    {
        if (filteredCategories == null || filteredMovies == null) return;

        if (filter == null || filter.isEmpty())
        {
            filteredMovies.setPredicate(movie  -> true);
            filteredCategories.setPredicate(category -> true);
        }
        else {
                    filteredMovies.setPredicate(Movie -> Movie.getTitle().toLowerCase().contains(filter.toLowerCase()));
                    filteredCategories.setPredicate(Category -> Category.getName().toLowerCase().contains(filter.toLowerCase()));
        }
    }

    // all functionalities around filtering the lists (only in gui, we're not touching the database here,
    // only managing what is currently shown in the tables
}
