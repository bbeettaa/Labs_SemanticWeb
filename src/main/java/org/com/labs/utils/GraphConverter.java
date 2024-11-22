package org.com.labs.utils;

import org.apache.commons.rdf.api.Graph;
import org.apache.commons.rdf.api.RDFTerm;
import org.apache.commons.rdf.api.Triple;
import org.apache.jena.irix.IRIException;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RiotException;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Iterator;


public class GraphConverter {

    public static void writeToJson(String jsonGraph) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/output.json"))) {
            writer.write(jsonGraph.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeToFile(String name, Graph graph, Lang TTL) {
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(name))) {
            RDFDataMgr.write(outputStream, convertCommonsRdfToJena(graph), TTL);
        } catch (RiotException | IRIException | IOException e) {
            e.printStackTrace();
            System.err.println("Unsupported for " + TTL.getName());
        }
    }


    public static Model convertCommonsRdfToJena(Graph commonsGraph) {
        Model jenaModel = ModelFactory.createDefaultModel();

        commonsGraph.stream().forEach(triple -> {
            RDFTerm subject = triple.getSubject();
            RDFTerm predicate = triple.getPredicate();
            RDFTerm object = triple.getObject();

            // Проверка на валидность триплета
            if (subject == null || predicate == null || object == null) {
                System.err.println("Invalid triple encountered: " + triple);
                return; // Пропускать недействительные триплеты
            }

            try {
                String subjectUri = cleanUri(subject.toString());
                String predicateUri = cleanUri(predicate.toString());
                Resource jenaSubject = jenaModel.createResource(subjectUri);
                org.apache.jena.rdf.model.Property jenaPredicate = jenaModel.createProperty(predicateUri);

                if (object instanceof org.apache.commons.rdf.api.IRI) {
                    // Если объект - ресурс
                    String objectUri = cleanUri(object.ntriplesString());
                    if (isValidURI(objectUri)) {
                        Resource jenaObject = jenaModel.createResource(objectUri);
                        jenaModel.add(jenaSubject, jenaPredicate, jenaObject);
                    } else {
                        System.err.println("Invalid object URI: " + objectUri);
                    }
                } else {
                    // Если объект - литерал
                    String literalValue = escapeSpecialSymb(object.ntriplesString());
                    org.apache.jena.rdf.model.Literal jenaLiteral = jenaModel.createLiteral(literalValue);
                    jenaModel.add(jenaSubject, jenaPredicate, jenaLiteral);
                }
            } catch (Exception e) {
                System.err.println("Error processing triple: " + triple);
                e.printStackTrace();
            }
        });

        return jenaModel;
    }

    private static String cleanUri(String uri) {
        return uri.replaceAll("^<|>$", "").trim();
    }

    private static boolean isValidURI(String uri) {
        try {
            new java.net.URI(uri); // Проверка валидности URI
            return true;
        } catch (URISyntaxException e) {
            return false; // Неверный URI
        }
    }

    public static void writeDotFile(Graph graph) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph RDF {\n");

        Iterator<? extends Triple> triples = graph.stream().iterator();
        while (triples.hasNext()) {
            Triple triple = triples.next();

            RDFTerm subject = triple.getSubject();
            RDFTerm predicate = triple.getPredicate();
            RDFTerm object = triple.getObject();

            dot.append("  \"")
                    .append(escapeSpecialSymb(subject.ntriplesString())).append("\" -> \"")
                    .append(escapeSpecialSymb(object.ntriplesString())).append("\" [label=\"")
                    .append(escapeSpecialSymb(predicate.ntriplesString())).append("\"];\n");
        }

        dot.append("}");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/output.dot"))) {
            writer.write(dot.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String escapeSpecialSymb(String value) {
        // Экранирование специальных символов для DOT
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public static void writeModelToDotFile(Model model, String outputFilePath) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph RDF {\n");

        // Проход по всем триплетам модели
        StmtIterator iter = model.listStatements();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Resource subject = stmt.getSubject();
            Resource predicate = stmt.getPredicate();
            String object = stmt.getObject().toString();

            // Экранирование данных для DOT
            String subjectLabel = escapeForDot(subject.toString());
            String predicateLabel = escapeForDot(predicate.toString());
            String objectLabel = escapeForDot(object);

            // Добавление триплета в DOT-формат
            dot.append("  \"").append(subjectLabel).append("\" -> \"")
                    .append(objectLabel).append("\" [label=\"")
                    .append(predicateLabel).append("\"];\n");
        }

        dot.append("}");

        // Запись в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(dot.toString());
        } catch (IOException e) {
            throw new RuntimeException("Error writing DOT file", e);
        }
    }

    // Метод для экранирования строк в DOT-файле
    private static String escapeForDot(String input) {
        return input.replaceAll("\"", "\\\\\"")  // Экранирование кавычек
                .replaceAll("<", "\\<")      // Экранирование угловых скобок
                .replaceAll(">", "\\>");     // Экранирование угловых скобок
    }
}
