package org.com.project.repository;

import org.apache.jena.query.*;
import org.apache.jena.sparql.exec.http.QueryExecutionHTTP;

import java.util.TreeMap;

public class BasicWikidataFilmLoader implements WikidataFilmLoader {
    private static final String WIKIDATA_ENDPOINT = "https://query.wikidata.org/sparql";
    private static final String SPARQL_PREFIXES = """
            PREFIX wdt: <http://www.wikidata.org/prop/direct/>
            PREFIX wd: <http://www.wikidata.org/entity/>
            PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
            PREFIX schema: <http://schema.org/>
            PREFIX wikibase: <http://wikiba.se/ontology#>
            PREFIX bd: <http://www.bigdata.com/rdf#>
            """;

    /**
     * Завантажує доступні жанри фільмів із Wikidata.
     *
     * @return {@link TreeMap}, де ключ — локальне ім'я жанру, значення — його назва українською мовою.
     */
    public TreeMap<String, String> getAvailableGenres() {
        TreeMap<String, String> availableGenres = new TreeMap<>();
        String sparql = SPARQL_PREFIXES + """
                SELECT DISTINCT ?genre ?genreLabel WHERE {
                    ?film wdt:P31 wd:Q11424;              # Instance of film
                          wdt:P136 ?genre;                # Genre of the film
                          wdt:P495 wd:Q212.               # Country of origin is Ukraine
                
                    ?genre rdfs:label ?genreLabel.         # Genre label
                    FILTER(LANG(?genreLabel) = "uk")       # Only genres with Ukrainian labels
                }
                ORDER BY ?genreLabel
                
                """;
        Query query = QueryFactory.create(sparql);

        try (QueryExecution queryExecution = QueryExecutionHTTP.service(WIKIDATA_ENDPOINT, query)) {
            ResultSet results = queryExecution.execSelect();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                availableGenres.put(solution.getResource("genre").getLocalName(), solution.getLiteral("genreLabel").getString());
            }
        }

        return availableGenres;
    }

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
    public String sparqlQueryAllFilms(String genre, int limit, int offset, String sortColumn, String sortOrder, String searchName) {
        int year = 0;
        String countryFilter = "wd:Q212";
        String languageFilter = "uk";
        String genreFilter = genre.equals("all") ? "" : String.format("wdt:P136 wd:%s; ", genre);

        String q = String.format(SPARQL_PREFIXES + """ 
                
                 SELECT ?film ?filmLabel ?description  ?posterUrl
                 (GROUP_CONCAT(DISTINCT ?genreLabel; separator=", ") AS ?genres)
                 (SAMPLE(YEAR(?publicationDate)) AS ?samplePublicationDate)
                 (SAMPLE(?ratingLabel) AS ?rating)
                
                
                 WHERE {
                   ?film wdt:P31 wd:Q11424;                  # Instance of film
                         wdt:P495 %s;                        # Country filter
                         %s                                  # Genre filter
                         wdt:P577 ?publicationDate.          # Publication date
                
                   ?film rdfs:label ?filmLabel.  # Связываем фильм с его меткой
                   FILTER(LANG(?filmLabel) = "%s")
                   FILTER(CONTAINS(LCASE(STR(?filmLabel)), LCASE("%s")))
                
                   OPTIONAL {
                     ?film wdt:P136 ?genre.          # Genre
                     ?genre rdfs:label ?genreLabel.
                     FILTER(LANG(?genreLabel) = "%s") # Genre in Ukrainian
                    }
                
                   OPTIONAL {
                     ?film schema:description ?description.
                     FILTER(LANG(?description) = "%s") # Filter for descriptions in Ukrainian
                   }
                
                   OPTIONAL { ?film wdt:P444 ?ratingLabel. }
                   OPTIONAL { ?film wdt:P18 ?posterUrl. }
                
                   FILTER(YEAR(?publicationDate) > %d)
                
                 }
                GROUP BY ?film ?filmLabel ?description ?posterUrl
                ORDER BY %s(%s)                                 # Сортировка
                LIMIT %d
                OFFSET %d                                        # Добавлен OFFSET
                """, countryFilter, genreFilter, languageFilter, searchName, languageFilter, languageFilter, year, sortOrder, sortColumn, limit, offset);
        return q;
    }

    /**
     * Генерує SPARQL-запит для підрахунку загальної кількості фільмів, які відповідають заданим критеріям.
     *
     * @param genre      Жанр фільму (локальне ім'я). Якщо значення "all", жанр не враховується.
     * @param searchName Частина назви фільму для пошуку.
     * @return SPARQL-запит у вигляді рядка.
     */
    public String sparqlQueryCountFilms(String genre, String searchName) {
        String countryFilter = "wd:Q212";
        String genreFilter = genre.equals("all") ? "" : String.format("wdt:P136 wd:%s; ", genre);
        String languageFilter = "uk";

        return String.format(SPARQL_PREFIXES + """ 
                SELECT (COUNT(?film) AS ?count)
                WHERE {
                    ?film wdt:P31 wd:Q11424;                  # Instance of film
                          wdt:P495 %s;                        # Country filter
                          %s                                    # Genre filter
                          wdt:P577 ?publicationDate.          # Publication date
                
                  ?film rdfs:label ?filmLabel.  # Связываем фильм с его меткой
                  FILTER(LANG(?filmLabel) = "%s")
                  FILTER(CONTAINS(LCASE(STR(?filmLabel)), LCASE("%s")))
                
                    OPTIONAL { ?film wdt:P136 ?genre.          # Genre
                               ?genre rdfs:label ?genreLabel.
                               FILTER(LANG(?genreLabel) = "%s") # Genre in Ukrainian
                             }
                
                }
                """, countryFilter, genreFilter, languageFilter, searchName, languageFilter);
    }

    /**
     * Генерує SPARQL-запит для отримання детальної інформації про конкретний фільм.
     *
     * @param filmId Ідентифікатор фільму (локальне ім'я в Wikidata).
     * @return SPARQL-запит у вигляді рядка.
     */
    public String sparqlQueryFilmDetails(String filmId) {
        String languageFilter = "uk,ru,en";

        return String.format(SPARQL_PREFIXES + """ 
                  SELECT ?film ?rating ?description ?duration ?posterUrl ?slogan  ?originalLanguage   ?filmLabel 
                
                   (SAMPLE(YEAR(?publicationDate)) AS ?publicationYear)
                         (GROUP_CONCAT(DISTINCT ?directorLabel; separator=", ") AS ?directors)
                         (GROUP_CONCAT(DISTINCT ?countryLabel; separator=", ") AS ?countries)
                         (GROUP_CONCAT(DISTINCT ?genreLabel; separator=", ") AS ?genres)
                         (GROUP_CONCAT(DISTINCT ?companyLabel; separator=", ") AS ?companies)
                         (GROUP_CONCAT(DISTINCT ?awardLabel; separator=", ") AS ?awards)
                         (GROUP_CONCAT(DISTINCT ?mainSubjectLabel; separator=", ") AS ?mainSubjects)
                  WHERE {
                    VALUES ?film { wd:%s }
                
                    OPTIONAL { ?film wdt:P57 ?director. }
                    OPTIONAL { ?film wdt:P444 ?rating. }
                    OPTIONAL { ?film wdt:P495 ?country. }
                    OPTIONAL { ?film wdt:P136 ?genre. }
                    OPTIONAL { ?film wdt:P2047 ?duration. }
                    OPTIONAL { ?film wdt:P18 ?posterUrl. }
                    OPTIONAL { ?film wdt:P364 ?language. }
                    OPTIONAL { ?film wdt:P2365 ?slogan. }
                    OPTIONAL { ?film wdt:P272 ?company. }
                    OPTIONAL { ?film wdt:P577 ?publicationDate. }
                    OPTIONAL { ?film wdt:P166 ?award.}
                    OPTIONAL { ?film wdt:P921 ?mainSubject. }
                
                    SERVICE wikibase:label {
                      bd:serviceParam wikibase:language "%s".
                      ?film rdfs:label ?filmLabel.
                      ?director rdfs:label ?directorLabel.
                      ?country rdfs:label ?countryLabel.
                      ?genre rdfs:label ?genreLabel.
                      ?company rdfs:label ?companyLabel.
                      ?language rdfs:label ?originalLanguage.
                      ?film schema:description ?description.
                      ?film wdt:441 ?criticReviews.
                      ?award rdfs:label ?awardLabel.
                      ?mainSubject rdfs:label ?mainSubjectLabel.
                    }
                  }
                  GROUP BY ?film ?rating ?description ?duration ?posterUrl ?slogan ?shootingLocation ?originalLanguage ?criticReviews  ?filmLabel
                
                """, filmId, languageFilter);
    }
}