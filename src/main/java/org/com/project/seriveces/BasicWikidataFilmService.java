package org.com.project.seriveces;

import org.com.project.entities.Dataset;
import org.com.project.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeMap;

/**
 * Сервіс для роботи з фільмами.
 * <p>
 * Цей клас забезпечує доступ до даних фільмів через репозиторій {@link FilmRepository}
 * та виконує бізнес-логіку, пов'язану з обробкою інформації про фільми.
 * </p>
 */
@Service
public class BasicWikidataFilmService extends FilmService {

    /**
     * Репозиторій для роботи з базою даних фільмів.
     */
    @Autowired // Автоматично ін'єктує залежність FilmRepository.
    private FilmRepository filmRepository;

    /**
     * Отримує список фільмів із застосуванням фільтрації, пагінації, сортування та пошуку.
     *
     * @param genre      жанр фільмів для фільтрації (або "all" для всіх жанрів).
     * @param limit      кількість записів для отримання.
     * @param offset     зміщення для пагінації.
     * @param sortColumn колонка для сортування.
     * @param sortOrder  порядок сортування ("asc" для зростання, "desc" для спадання).
     * @param searchName частина назви фільму для пошуку.
     * @return список фільмів, які відповідають заданим критеріям.
     */
    public List<Dataset> getFilms(String genre, int limit, int offset, String sortColumn, String sortOrder, String searchName) {
        return filmRepository.findAll(genre, limit, offset, sortColumn, sortOrder, searchName);
    }

    /**
     * Підраховує загальну кількість фільмів, які відповідають критеріям жанру та пошуку.
     *
     * @param genre      жанр фільмів для фільтрації (або "all" для всіх жанрів).
     * @param searchName частина назви фільму для пошуку.
     * @return кількість фільмів, які відповідають критеріям.
     */
    public int countFilms(String genre, String searchName) {
        return filmRepository.count(genre, searchName);
    }

    /**
     * Отримує детальну інформацію про конкретний фільм за його ідентифікатором.
     *
     * @param filmId ідентифікатор фільму.
     * @return об'єкт {@link Dataset}, що містить дані про фільм.
     */
    public Dataset getFilmDetails(String filmId) {
        return filmRepository.findById(filmId);
    }

    /**
     * Отримує доступні жанри фільмів у вигляді структури {@link TreeMap}.
     * <p>
     * Ключі карти представляють ідентифікатори жанрів, а значення — їх опис чи назву.
     * </p>
     *
     * @return карта доступних жанрів.
     */
    public TreeMap<String, String> getAvailableGenres() {
        return filmRepository.getAvailableGenres();
    }
}
