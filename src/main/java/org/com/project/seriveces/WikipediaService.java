package org.com.project.seriveces;

import lombok.Getter;
import org.apache.jena.query.*;
import org.apache.jena.sparql.exec.http.QueryExecutionHTTP;
import org.com.project.Dataset;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Service
public class WikipediaService {
    private static final String WIKIDATA_ENDPOINT = "https://query.wikidata.org/sparql";
    @Getter
    private TreeMap<String, String> availableGenres;
    private WikidataLoader wikidataLoader;

    public WikipediaService(TreeMap availableGenres, WikidataLoader wikidataLoader) {
        this.availableGenres = availableGenres;
        this.wikidataLoader = wikidataLoader;
    }

    public List<Dataset> getFilms(String genreFilter, int limit, int offset, String sortColumn, String sortOrder, String searchName) {
        String sparqlSortColumn = mapColumnToSparql(sortColumn); // Сопоставление столбцов с SPARQL-полями
        String sparqlOrder = sortOrder.equals("asc") ? "ASC" : "DESC"; // Определение порядка сортировки

        Query query = QueryFactory.create(wikidataLoader.sparqlQueryAllFilms(genreFilter, limit, offset, sparqlSortColumn, sparqlOrder, searchName));

        List<Dataset> movies = new ArrayList<>();
        try (QueryExecution queryExecution = QueryExecutionHTTP.service(WIKIDATA_ENDPOINT, query)) {
            ResultSet results = queryExecution.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.next();
                movies.add(DatasetFactory.fromSolution(solution));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return movies;
    }

    public int countFilms(String genreFilter, String searchName) {
        Query query = QueryFactory.create(wikidataLoader.sparqlQueryCountFilms(genreFilter, searchName));

        int count = -1;
        try (QueryExecution queryExecution = QueryExecutionHTTP.service(WIKIDATA_ENDPOINT, query)) {
            ResultSet results = queryExecution.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.next();
                count = solution.getLiteral("count").getInt();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return count;
    }

    public Dataset getFilmDetails(String filmId) {
        Query query = QueryFactory.create(wikidataLoader.sparqlQueryFilmDetails(filmId));
        Dataset result = null;
        try (QueryExecution queryExecution = QueryExecutionHTTP.service(WIKIDATA_ENDPOINT, query)) {
            ResultSet results = queryExecution.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.next();
                result = DatasetFactory.fromSolution(solution);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }


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

