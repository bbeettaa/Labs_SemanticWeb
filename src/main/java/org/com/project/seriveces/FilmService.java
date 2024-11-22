package org.com.project.seriveces;

import org.com.project.entities.Dataset;
import org.com.project.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeMap;

/**
 * Service class for managing films.
 * <p>
 * This class provides access to film data through the {@link FilmRepository} repository
 * and handles business logic related to film information processing.
 * </p>
 */
@Service
public class FilmService {

    /**
     * Repository for working with film data.
     */
    @Autowired
    private FilmRepository filmRepository;

    /**
     * Retrieves a list of films based on filters, pagination, sorting, and search.
     *
     * @param genre      the genre of films for filtering (use "all" for all genres).
     * @param limit      the number of records to fetch.
     * @param offset     the offset for pagination.
     * @param sortColumn the column to sort by.
     * @param sortOrder  the order of sorting ("asc" for ascending, "desc" for descending).
     * @param searchName a partial film title for search.
     * @return a list of films matching the provided criteria.
     */
    public List<Dataset> getFilms(String genre, int limit, int offset, String sortColumn, String sortOrder, String searchName) {
        return filmRepository.findAll(genre, limit, offset, sortColumn, sortOrder, searchName);
    }

    /**
     * Counts the total number of films matching the genre and search criteria.
     *
     * @param genre      the genre of films for filtering (use "all" for all genres).
     * @param searchName a partial film title for search.
     * @return the count of films matching the criteria.
     */
    public int countFilms(String genre, String searchName) {
        return filmRepository.count(genre, searchName);
    }

    /**
     * Retrieves detailed information about a specific film by its ID.
     *
     * @param filmId the ID of the film.
     * @return a {@link Dataset} object containing the film's details.
     */
    public Dataset getFilmDetails(String filmId) {
        return filmRepository.findById(filmId);
    }

    /**
     * Retrieves available film genres in a {@link TreeMap} structure.
     * <p>
     * The keys of the map represent genre IDs, and the values represent the genre's description or name.
     * </p>
     *
     * @return a map of available film genres.
     */
    public TreeMap<String, String> getAvailableGenres() {
        return filmRepository.getAvailableGenres();
    }
}
