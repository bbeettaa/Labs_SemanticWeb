package org.com.labs;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

//dot  -Tpng output.dot -o output.png
public class Main {
    public static void main(String[] args) throws IOException {
//        RDF rdf = new SimpleRDF();
//        Graph graph = rdf.createGraph();
//
//        IRI alice = rdf.createIRI("http://example.org/Alice");
//        IRI knows = rdf.createIRI("http://example.org/knows");
//        IRI bob = rdf.createIRI("http://example.org/Bob");
//
//        IRI charlie = rdf.createIRI("http://example.org/Charlie");
//        IRI plays = rdf.createIRI("http://example.org/plays");
//        IRI football = rdf.createIRI("http://example.org/Football");
//        IRI tennis = rdf.createIRI("http://example.org/Tennis");
//
//        graph.add(alice, knows, charlie);
//        graph.add(alice, plays, tennis);
//        graph.add(bob, knows, charlie);
//        graph.add(bob, plays, football);
//        graph.add(charlie, plays, tennis);
//
//        Literal aliceName = rdf.createLiteral("Alice W. Land");
//        IRI name = rdf.createIRI("http://example.org/name");
//        graph.add(alice, name, aliceName);


        // Создание модели RDF
        Model model = ModelFactory.createDefaultModel();

        // Определение IRI
        Resource alice = model.createResource("http://example.org/Alice");
        Property knows = model.createProperty("http://example.org/knows");
        Resource bob = model.createResource("http://example.org/Bob");

        Resource charlie = model.createResource("http://example.org/Charlie");
        Property plays = model.createProperty("http://example.org/plays");
        Resource football = model.createResource("http://example.org/Football");
        Resource tennis = model.createResource("http://example.org/Tennis");

        // Добавление триплетов в модель
        model.add(alice, knows, charlie);
        model.add(alice, plays, tennis);
        model.add(bob, knows, charlie);
        model.add(bob, plays, football);
        model.add(charlie, plays, tennis);

        // Добавление имени Алисы
        org.apache.jena.rdf.model.Literal aliceName = model.createLiteral("Alice W. Land");
        Property name = model.createProperty("http://example.org/name");
        model.add(alice, name, aliceName);

        // Вывод модели в формате Turtle
        writeToFile("src/main/resources/output.TURTLE", model, Lang.TURTLE);
        writeToFile("src/main/resources/output.RDFXML", model, Lang.RDFXML);
        writeToFile("src/main/resources/output.RDFJSON", model, Lang.RDFJSON);


//        GraphConverter.writeDotFile(graph);
//        String jsonGraph = new GsonBuilder().setPrettyPrinting().create().toJson(graph);
//        GraphConverter.writeToJson(jsonGraph);
//        GraphConverter.writeToFile("src/main/resources/output.RDFJSON", graph, Lang.RDFJSON);
//        GraphConverter.writeToFile("src/main/resources/output.RDFXML", graph, Lang.RDFXML);

//        GraphConverter.writeGraphToRDFXML(graph,"src/main/resources/output.xml");

//        convertGraphToRdfXml(graph, "src/main/resources/output.xml");

//        // Конвертация и запись в RDF/XML
//        try (FileOutputStream outputStream = new FileOutputStream("src/main/resources/output.rdf")) {
//            RdfXmlWriter rdfXmlWriter = new RdfXmlWriter();
//            rdfXmlWriter.write(graph, outputStream);
//        } catch (IOException e) {
//            throw new RuntimeException("Error writing RDF/XML file", e);
//        }

    }

    private static void writeToFile(String fileName, Model model, Lang RDFJSON) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            model.write(bw, RDFJSON.getName());
        }
    }


}
