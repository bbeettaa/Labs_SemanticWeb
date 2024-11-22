package org.com.project.repository;

import java.util.TreeMap;

/**
 * Інтерфейс для завантаження даних про фільми з Wikidata.
 * Містить методи для отримання жанрів фільмів, а також формування SPARQL-запитів
 * для отримання фільмів із заданими критеріями, підрахунку кількості фільмів та отримання
 * детальної інформації про фільми.
 */
public interface WikidataFilmLoader {

    /**
     * Завантажує доступні жанри фільмів із Wikidata.
     *
     * @return {@link TreeMap}, де ключ — локальне ім'я жанру, значення — його назва українською мовою.
     */
    TreeMap<String, String> getAvailableGenres();

    /**
     * Генерує SPARQL-запит для отримання фільмів із Wikidata із заданими фільтрами, сортуванням та пагінацією.
     *
     * @param genre      Жанр фільмів (якщо "all", жанр не враховується).
     * @param limit      Кількість фільмів для завантаження.
     * @param offset     Зсув для пагінації.
     * @param sortColumn Колонка для сортування (наприклад, "samplePublicationDate").
     * @param sortOrder  Порядок сортування ("ASC" або "DESC").
     * @param searchName Частковий пошук за назвою фільму.
     * @return SPARQL-запит у вигляді строки.
     */
    String sparqlQueryAllFilms(String genre, int limit, int offset, String sortColumn, String sortOrder, String searchName);

    /**
     * Генерує SPARQL-запит для підрахунку загальної кількості фільмів, які відповідають заданим критеріям.
     *
     * @param genre      Жанр фільму (локальне ім'я). Якщо значення "all", жанр не враховується.
     * @param searchName Частина назви фільму для пошуку.
     * @return SPARQL-запит у вигляді рядка.
     */
    String sparqlQueryCountFilms(String genre, String searchName);

    /**
     * Генерує SPARQL-запит для отримання детальної інформації про конкретний фільм.
     *
     * @param filmId Ідентифікатор фільму (локальне ім'я в Wikidata).
     * @return SPARQL-запит у вигляді рядка.
     */
    String sparqlQueryFilmDetails(String filmId);
}
