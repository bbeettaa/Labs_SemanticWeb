package org.com.project.repository;

import org.com.project.entities.Dataset;

import java.util.List;
import java.util.TreeMap;

public interface FilmRepository {

    /**
     * Отримує список фільмів на основі заданих параметрів.
     *
     * @param genre      Жанр фільму (локальне ім'я). Якщо значення "all", жанр не враховується.
     * @param limit      Максимальна кількість записів у результатах.
     * @param offset     Зсув для посторінкової навігації.
     * @param sortColumn Колонка для сортування (наприклад, "name", "publicationYear").
     * @param sortOrder  Порядок сортування ("asc" або "desc").
     * @param searchName Частина назви фільму для пошуку.
     * @return Список об'єктів {@link Dataset}, що відповідають заданим параметрам.
     */
    List<Dataset> findAll(String genre, int limit, int offset, String sortColumn, String sortOrder, String searchName);

    /**
     * Підраховує загальну кількість фільмів, які відповідають заданим критеріям.
     *
     * @param genre      Жанр фільму (локальне ім'я). Якщо значення "all", жанр не враховується.
     * @param searchName Частина назви фільму для пошуку.
     * @return Кількість фільмів, що відповідають умовам.
     */
    int count(String genre, String searchName);

    /**
     * Отримує детальну інформацію про фільм за його ідентифікатором.
     *
     * @param filmId Ідентифікатор фільму (локальне ім'я в Wikidata).
     * @return Об'єкт {@link Dataset}, що містить деталі фільму.
     */
    Dataset findById(String filmId);

    /**
     * Отримує список доступних жанрів фільмів.
     *
     * @return {@link TreeMap}, де ключ — локальне ім'я жанру, а значення — назва жанру українською мовою.
     */
    TreeMap<String, String> getAvailableGenres();
}
