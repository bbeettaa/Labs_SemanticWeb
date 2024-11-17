package org.com.l1;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;
import org.com.utils.GraphConverter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

//dot -Tpng output.dot -o output.png
public class Lab1_1 {
    public static void main(String[] args) throws IOException {
        // Створення моделі RDF
        Model model = ModelFactory.createDefaultModel();

        // Латунь – це сплав міді та цинку
        brass(model);

        //SPIEGEL — німецький інформаційний журнал зі штаб-квартирою в Гамбурзі.
        spiegel(model);

        // Есе складається зі вступу, основної частини та висновку
        essay(model);

        // Павло знає, що Олена живе в Полтаві
        pavloKnowsOlena(model);

        // Олена каже, що її друг живе в Києві
        olenaSaysFriend(model);

        // Стефан думає, що Анна знає, що він знає її батька
        stefanThinks(model);

        // Іван знає, що Рим є столицею Італії
        IvanKnowsRome(model);

        // Вивід моделі
        writeToFile("src/main/resources/output.l1.turtle", model, Lang.TURTLE);
        writeToFile("src/main/resources/output.l1.rdfxml", model, Lang.RDFXML);
        GraphConverter.writeModelToDotFile(model, "src/main/resources/output.l1.dot");
    }

    private static void IvanKnowsRome(Model model) {
        // Іван знає, що Рим є столицею Італії
        Resource ivan = model.createResource("http://example.org/Ivan");
        Resource rome = model.createResource("http://example.org/Rome");
        Resource italy = model.createResource("http://example.org/Italy");

        Property capitalOf = model.createProperty("http://example.org/capitalOf");

        Resource romeIsCapitalOfItalyStatement = model.createResource()
                .addProperty(RDF.type, RDF.Statement)
                .addProperty(RDF.subject, rome)
                .addProperty(RDF.predicate, capitalOf)
                .addProperty(RDF.object, italy);

        model.add(ivan, FOAF.knows, romeIsCapitalOfItalyStatement);
    }

    private static void stefanThinks(Model model) {
        // Стефан думає, що Анна знає, що він знає її батька
        Resource stefan = model.createResource("http://example.org/Stefan");
        Resource anna = model.createResource("http://example.org/Anna");
        Resource annaFather = model.createResource("http://example.org/Father");

        Property thinks = model.createProperty("http://example.org/thinks");

        Resource stefanKnowAnnaFatherStatement = model.createResource()
                .addProperty(RDF.type, RDF.Statement)
                .addProperty(RDF.subject, stefan)
                .addProperty(RDF.predicate, FOAF.knows)
                .addProperty(RDF.object, annaFather);

        Resource annaKnowStefanStatement = model.createResource()
                .addProperty(RDF.type, RDF.Statement)
                .addProperty(RDF.subject, anna)
                .addProperty(RDF.predicate, FOAF.knows)
                .addProperty(RDF.object, stefanKnowAnnaFatherStatement);

        model.add(stefan, thinks, annaKnowStefanStatement);
    }

    private static void olenaSaysFriend(Model model) {
        // Олена каже, що її друг живе в Києві
        Resource olena = model.createResource("http://example.org/Olena");
        Resource friend = model.createResource("http://example.org/Friend");
        Resource kyiv = model.createResource("http://example.org/Kyiv");

        Property says = model.createProperty("http://example.org/says");
        Property lives = model.createProperty("http://example.org/lives");

        model.add(olena, says, model.createResource()
                .addProperty(RDF.type, RDF.Statement)
                .addProperty(RDF.subject, friend)
                .addProperty(RDF.predicate, lives)
                .addProperty(RDF.object, kyiv));
    }

    private static void pavloKnowsOlena(Model model) {
        // Павло знає, що Олена живе в Полтаві
        Resource pavlo = model.createResource("http://example.org/Pavlo");
        Resource olena = model.createResource("http://example.org/Olena");
        Resource poltava = model.createResource("http://example.org/Poltava");

        Property knows = model.createProperty("http://example.org/knows");
        Property lives = model.createProperty("http://example.org/lives");

        Resource olenaLivesInPoltava = model.createResource()
                .addProperty(RDF.type, RDF.Statement)
                .addProperty(RDF.subject, olena)
                .addProperty(RDF.predicate, lives)
                .addProperty(RDF.object, poltava);

        model.add(pavlo, knows, olenaLivesInPoltava);
    }

    // Есе складається зі вступу, основної частини та висновку
    private static void essay(Model model) {
        Resource essay = model.createResource("http://example.org/Essay");
        Resource introduction = model.createResource("http://example.org/Introduction");
        Resource body = model.createResource("http://example.org/Body");
        Resource conclusion = model.createResource("http://example.org/Conclusion");

        Property consistOf = model.createProperty("http://example.org/consistsOf");

        model.createSeq()
                .add(0, introduction)
                .add(1, body)
                .add(2, conclusion);
//        model.add(essay, consistOf, introduction);
//        model.add(essay, consistOf, body);
//        model.add(essay, consistOf, conclusion);
    }

    private static void spiegel(Model model) {
        //SPIEGEL — німецький інформаційний журнал зі штаб-квартирою в Гамбурзі.
        Resource spiegel = model.createResource("http://example.org/SPIEGEL");
        Resource informationJournal = model.createResource("http://example.org/InformationJournal");
        Resource hamburg = model.createResource("http://example.org/Hamburg");
        Resource german = model.createResource("http://example.org/German");

        Property hasHeadquarter = model.createProperty("http://example.org/hasHeadquarter");

        model.add(spiegel, RDF.type, informationJournal);
        model.add(spiegel, model.createProperty("http://example.org/hasLanguage"), german);
        model.add(spiegel, hasHeadquarter, hamburg);
    }

    private static void brass(Model model) {
        // Латунь – це сплав міді та цинку
        Resource brass = model.createResource("http://example.org/Brass");
        Resource alloy = model.createResource("http://example.org/Alloy");
        Resource copper = model.createResource("http://example.org/Copper");
        Resource zinc = model.createResource("http://example.org/Zinc");

        Property consistOf = model.createProperty("http://example.org/consistsOf");

        Resource alloyRes = model.createResource()
                .addProperty(RDF.type, alloy)
                .addProperty(consistOf, copper)
                .addProperty(consistOf, zinc);
        model.add(brass, RDF.type, alloyRes);
    }

    private static void writeToFile(String fileName, Model model, Lang RDFJSON) throws IOException {
        model.setNsPrefix("ex", "http://example.org/");
        model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            model.write(bw, RDFJSON.getName());

        }
    }
}
