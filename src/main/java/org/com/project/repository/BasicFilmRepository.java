package org.com.project.repository;


import org.apache.jena.query.*;
import org.apache.jena.sparql.exec.http.QueryExecutionHTTP;
import org.com.project.entities.Dataset;
import org.com.project.seriveces.DatasetFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Реалізація інтерфейсу {@link FilmRepository}, яка забезпечує доступ до даних фільмів через SPARQL запити до Wikidata.
 * Відповідає за пошук фільмів, отримання кількості фільмів, детальної інформації про фільми та доступні жанри.
 */
@Repository
public class BasicFilmRepository implements FilmRepository {
    private static final String WIKIDATA_ENDPOINT = "https://query.wikidata.org/sparql";
    private final WikidataFilmLoader wikidataLoader;

    private final TreeMap<String, String> availableGenres;

    /**
     * Конструктор для ініціалізації репозиторію.
     *
     * @param wikidataLoader клас, що завантажує жанри та генерує SPARQL запити для отримання фільмів
     */
    public BasicFilmRepository(WikidataFilmLoader wikidataLoader) {
        this.wikidataLoader = wikidataLoader;
        this.availableGenres = wikidataLoader.getAvailableGenres();
    }


    /**
     * Отримує список фільмів, які відповідають заданим критеріям (жанр, обмеження за кількістю, пошук за назвою тощо).
     *
     * @param genre      жанр фільмів (якщо "all", то всі жанри)
     * @param limit      максимальна кількість фільмів, які потрібно повернути
     * @param offset     позиція для початку вибірки
     * @param sortColumn стовпець, за яким буде відбуватись сортування
     * @param sortOrder  порядок сортування ("asc" або "desc")
     * @param searchName текст для пошуку у назвах фільмів
     * @return список фільмів, що відповідають умовам запиту
     */
    @Override
    public List<Dataset> findAll(String genre, int limit, int offset, String sortColumn, String sortOrder, String searchName) {
        String sparqlSortColumn = mapColumnToSparql(sortColumn);
        String sparqlOrder = sortOrder.equals("asc") ? "ASC" : "DESC";

        Query query = QueryFactory.create(wikidataLoader.sparqlQueryAllFilms(genre, limit, offset, sparqlSortColumn, sparqlOrder, searchName));

        List<Dataset> movies = new ArrayList<>();
        try (QueryExecution queryExecution = QueryExecutionHTTP.service(WIKIDATA_ENDPOINT, query)) {
            ResultSet results = queryExecution.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.next();
                movies.add(DatasetFactory.fromSolution(solution));
            }
        }
        return movies;
    }

    /**
     * Підраховує кількість фільмів, які відповідають заданим критеріям (жанр, пошук за назвою тощо).
     *
     * @param genre      жанр фільмів (якщо "all", то всі жанри)
     * @param searchName текст для пошуку у назвах фільмів
     * @return кількість фільмів, що відповідають умовам
     */
    @Override
    public int count(String genre, String searchName) {
        Query query = QueryFactory.create(wikidataLoader.sparqlQueryCountFilms(genre, searchName));

        int count = -1;
        try (QueryExecution queryExecution = QueryExecutionHTTP.service(WIKIDATA_ENDPOINT, query)) {
            ResultSet results = queryExecution.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.next();
                count = solution.getLiteral("count").getInt();
            }
        }
        return count;
    }

    /**
     * Отримує детальну інформацію про фільм за його унікальним ідентифікатором.
     *
     * @param filmId унікальний ідентифікатор фільму
     * @return об'єкт {@link Dataset}, що містить детальну інформацію про фільм
     */
    @Override
    public Dataset findById(String filmId) {
        Query query = QueryFactory.create(wikidataLoader.sparqlQueryFilmDetails(filmId));
        Dataset result = null;
        try (QueryExecution queryExecution = QueryExecutionHTTP.service(WIKIDATA_ENDPOINT, query)) {
            ResultSet results = queryExecution.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.next();
                result = DatasetFactory.fromSolution(solution);
            }
        }
        return result;
    }

    /**
     * Повертає доступні жанри фільмів, що завантажуються з Wikidata.
     *
     * @return TreeMap, де ключ — це ідентифікатор жанру, а значення — назва жанру
     */
    @Override
    public TreeMap<String, String> getAvailableGenres() {
        return availableGenres;
    }

    /**
     * Перетворює назву стовпця для сортування в відповідну змінну SPARQL.
     *
     * @param sortColumn назва стовпця для сортування
     * @return відповідну змінну для SPARQL-запиту
     */
    private String mapColumnToSparql(String sortColumn) {
        return switch (sortColumn) {
            case "name" -> "?filmLabel";
            case "description" -> "?description";
            case "date" -> "?samplePublicationDate";
            case "rating" -> "?rating";
            default -> "?filmLabel";
        };
    }
}
