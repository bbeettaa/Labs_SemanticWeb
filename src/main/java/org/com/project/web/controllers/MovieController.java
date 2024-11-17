package org.com.project.web.controllers;

import org.com.project.Dataset;
import org.com.project.seriveces.WikipediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.TreeMap;

@Controller
public class MovieController {
    private final WikipediaService wikipediaService;

    @Autowired
    public MovieController(WikipediaService wikipediaService) {
        this.wikipediaService = wikipediaService;
    }

    @GetMapping("/")
    public String getApiMovies(
            @RequestParam(name = "genre", defaultValue = "all") String genre,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortColumn", defaultValue = "name") String sortColumn,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "searchName", defaultValue = "") String searchName,
            Model model) {
        int offset = (page - 1) * size;

        List<Dataset> movies = wikipediaService.getFilms(genre, size, offset, sortColumn, sortOrder, searchName);
        TreeMap<String, String> genres = wikipediaService.getAvailableGenres();

        int totalMovies = wikipediaService.countFilms(genre, searchName);
        int maxPages = (int) Math.ceil((double) totalMovies / size);
        model.addAttribute("maxPages", maxPages);
        model.addAttribute("movies", movies);
        model.addAttribute("genres", genres);
        model.addAttribute("genre", genre);
        model.addAttribute("previousPage", page - 1);
        model.addAttribute("nextPage", page + 1);
        model.addAttribute("size", size);
        model.addAttribute("sortColumn", sortColumn);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("page", page);
        model.addAttribute("searchName", searchName);
        return "moviesFragment";
    }

    @GetMapping("/{filmId}/details")
    public String getFilmDetails(@PathVariable("filmId") String filmId, Model model) {
        Dataset dataset = wikipediaService.getFilmDetails(filmId);
        model.addAttribute("dataset", dataset);
        return "details :: detailsFragment";
    }


}
