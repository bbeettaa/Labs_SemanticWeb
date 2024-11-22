package org.com.project.web.controllers;

import org.com.project.entities.Dataset;
import org.com.project.seriveces.BasicWikidataFilmService;
import org.com.project.seriveces.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.TreeMap;

/**
 * Контролер для обробки запитів, пов'язаних із фільмами.
 * <p>
 * Цей клас забезпечує маршрутизацію та підготовку даних для представлення,
 * використовуючи функції сервісу {@link BasicWikidataFilmService}.
 * </p>
 */
@Controller
public class MovieController {

    /**
     * Сервіс для роботи з фільмами, який забезпечує бізнес-логіку.
     */
    @Autowired // Автоматично підключає залежність FilmService.
    private FilmService filmService;

    /**
     * Обробляє запит для отримання списку фільмів із фільтрацією, сортуванням і пагінацією.
     *
     * @param genre      жанр фільмів для фільтрації (за замовчуванням "all").
     * @param page       номер сторінки для пагінації (за замовчуванням 1).
     * @param size       кількість записів на сторінку (за замовчуванням 10).
     * @param sortColumn колонка для сортування (за замовчуванням "name").
     * @param sortOrder  порядок сортування: "asc" для зростання, "desc" для спадання.
     * @param searchName назва фільму для пошуку (за замовчуванням порожній рядок).
     * @param model      об'єкт для передачі даних у представлення.
     * @return назва представлення "index", яке рендериться для клієнта.
     */
    @GetMapping("/")
    public String getApiMovies(
            @RequestParam(name = "genre", defaultValue = "all") String genre,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortColumn", defaultValue = "name") String sortColumn,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "searchName", defaultValue = "") String searchName,
            Model model) {
        // Обчислення зміщення для пагінації.
        int offset = (page - 1) * size;

        // Отримання списку фільмів з фільтрами, сортуванням і пагінацією.
        List<Dataset> movies = filmService.getFilms(genre, size, offset, sortColumn, sortOrder, searchName);

        // Отримання доступних жанрів фільмів.
        TreeMap<String, String> genres = filmService.getAvailableGenres();

        // Обчислення загальної кількості сторінок на основі кількості фільмів.
        int totalMovies = filmService.countFilms(genre, searchName);
        int maxPages = (int) Math.ceil((double) totalMovies / size);

        // Додавання даних до моделі для передачі у представлення.
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

        // Повертає представлення для рендерингу списку фільмів.
        return "index";
    }

    /**
     * Обробляє запит для отримання деталей конкретного фільму.
     *
     * @param filmId ідентифікатор фільму, який передається в URL.
     * @param model  об'єкт для передачі даних у представлення.
     * @return назва представлення "details", яке рендериться для клієнта.
     */
    @GetMapping("/{filmId}/details") // Обробляє GET-запити на шлях із деталями фільму.
    public String getFilmDetails(@PathVariable("filmId") String filmId, Model model) {
        // Отримання даних про фільм за ідентифікатором.
        Dataset dataset = filmService.getFilmDetails(filmId);

        // Додавання даних фільму до моделі.
        model.addAttribute("dataset", dataset);

        // Повертає представлення для рендерингу деталей фільму.
        return "details";
    }
}
