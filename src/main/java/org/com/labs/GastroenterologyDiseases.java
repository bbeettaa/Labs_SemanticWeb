package org.com.labs;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.exec.http.QueryExecutionHTTP;

public class GastroenterologyDiseases {

    public static void main(String[] args) {
        String endpoint = "https://dbpedia.org/sparql";
        String query = """
                PREFIX dbo: <http://dbpedia.org/ontology/>
                PREFIX dbr: <http://dbpedia.org/resource/>
                PREFIX dbp: <http://dbpedia.org/property/>
                PREFIX dbt: <http://dbpedia.org/resource/Template:>
                PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
                
                SELECT (STR(?distype) AS ?type) (STR(?disname) AS ?name) (GROUP_CONCAT(DISTINCT STR(?symptomName); separator=", ") AS ?symptoms)
                WHERE {
                    ?disease a dbo:Disease .
                    ?disease rdfs:label ?disname .
                    ?disease dbp:wikiPageUsesTemplate dbt:Gastroenterology.
                
                    # Получаем тип заболевания
                    dbr:Gastroenterology rdfs:label ?distype.
                
                    OPTIONAL {
                        ?disease dbo:symptom ?symptom .
                        ?symptom rdfs:label ?symptomName .  
                        FILTER (lang(?symptomName) = "en")
                    }
                
                    FILTER (lang(?disname) = "en")
                    FILTER (lang(?distype) = "en")
                } 
                GROUP BY ?disease ?disname ?distype  
                LIMIT 100
                """;

        try (QueryExecution queryExecution = QueryExecutionHTTP.service(endpoint, query)) {
            ResultSet results = queryExecution.execSelect();

            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                String diseaseLabel = soln.getLiteral("name").getString();
                String diseaseType = soln.getLiteral("type") != null ? soln.getLiteral("type").getString() : "Unknown";
                String symptoms = soln.getLiteral("symptoms") != null ? soln.getLiteral("symptoms").getString() : "None";

                System.out.println("Disease: " + diseaseLabel + " (Type: " + diseaseType + ")");
                System.out.println("Symptoms: " + symptoms);
                System.out.println("------------------------");
            }
        }

    }
}
