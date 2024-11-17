package org.com.l1;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SchemaDO;
import org.apache.jena.vocabulary.VCARD4;
import org.com.utils.GraphConverter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Lab1_3 {

//    3. Є ситуація: Кейд живе за адресою 1516 Henry Street, Берклі, Каліфорнія 94709, США.
//    Він має ступінь бакалавра біології в Каліфорнійському університеті з 2011 року. Його
//    інтереси: птахи, екологія, довкілля, фотографія і подорожі. Він відвідав Канаду та
//    Францію.

//    Емма живе за адресою Carrer de la Guardia Civil 20, 46020 Valencia, Spain. Вона має
//    ступінь магістра хімії в Університеті Валенсії з 2015 року. Її сфера знань включає
//    управління відходами , токсичні відходи, забруднення повітря. Її інтереси: їзда на
//    велосипеді, музика та подорожі. Вона відвідала Португалію, Італію, Францію, Німеччину,
//    Данію та Швецію.

    //Кейд знає Емму. Вони зустрілися в Парижі в серпні 2014 року.

    //• використовуючі словники FOAF, RDF, XSD тощо та власні URI (наприклад, створені на базі http://example.org/) створіть RDF граф за допомогою RDFLib;

    //• виконайте візуалізацію та сериалізацію графу у різні формати;
    //• запишіть свій граф у файл у форматі TURTLE;
    //• перегляньте файл і відредагуйте його так, щоб Кейд також відвідував Німеччину і щоб Еммі було 36 років;
    //• виведіть на консоль усі трійки графу;
    //• виведіть на консоль трійки, що стосуються лише про Емму;
    //• виведіть на консоль трійки, що містять імена людей.

    //dot -Tpng output.l1.dot -o output.png
    public static void main(String[] args) throws IOException {
        Model model = ModelFactory.createDefaultModel();

        Resource cade = model.createResource("http://example.org/Cade")
                .addProperty(
                        RDF.type,
                        FOAF.Person)
                .addProperty(
                        FOAF.name,
                        model.createResource()
                                .addProperty(FOAF.firstName, "Cade"))
                .addProperty(
                        VCARD4.hasAddress,
                        model.createResource()
                                .addProperty(RDF.type, VCARD4.Address)
                                .addProperty(VCARD4.street_address, "1516 Henry Street")
                                .addProperty(VCARD4.locality, "Berkeley")
                                .addProperty(VCARD4.postal_code, "94709")
                                .addProperty(VCARD4.region, "California")
                                .addProperty(VCARD4.country_name, "USA"))
                .addProperty(
                        SchemaDO.hasCredential,
                        model.createResource()
                                .addProperty(RDF.type, SchemaDO.EducationalOccupationalProgram)
                                .addProperty(
                                        SchemaDO.educationalCredentialAwarded,
                                        "Biology")
                                .addProperty(
                                        SchemaDO.educationalLevel,
                                        model.createResource("http://example.org/Bachelor")
                                                .addProperty(RDF.type, SchemaDO.EducationalOccupationalCredential)
                                                .addProperty(SchemaDO.name, "Bachelor"))
                                .addProperty(
                                        SchemaDO.startDate,
                                        "2011-06-01")
                                .addProperty(
                                        SchemaDO.alumniOf,
                                        model.createResource()
                                                .addProperty(RDF.type, SchemaDO.CollegeOrUniversity)
                                                .addProperty(SchemaDO.name, "California University")
                                                .addProperty(SchemaDO.location, "http://example.org/California")))
                .addProperty(
                        FOAF.interest,
                        model.createBag()
                                .add(model.createResource("http://example.org/Birds"))
                                .add(model.createResource("http://example.org/Ecology"))
                                .add(model.createResource("http://example.org/Environment"))
                                .add(model.createResource("http://example.org/Photography"))
                                .add(model.createResource("http://example.org/Travel")))
                .addProperty(
                        model.createProperty("http://example.org/hasVisited"),
                        model.createSeq()
                                .add(model.createResource("http://example.org/Canada"))
                                .add(model.createResource("http://example.org/France")));


        Resource emma = model.createResource("http://example.org/Emma")
                .addProperty(
                        RDF.type,
                        FOAF.Person)
                .addProperty(
                        FOAF.name,
                        model.createResource()
                                .addProperty(FOAF.firstName, "Emma"))
                .addProperty(
                        VCARD4.hasAddress,
                        model.createResource()
                                .addProperty(RDF.type, VCARD4.Address)
                                .addProperty(VCARD4.street_address, "Carrer de la Guardia Civil 20")
                                .addProperty(VCARD4.locality, "Benimaclet")
                                .addProperty(VCARD4.postal_code, "46020")
                                .addProperty(VCARD4.region, "Valencia")
                                .addProperty(VCARD4.country_name, "Spain"))
                .addProperty(
                        SchemaDO.hasCredential,
                        model.createResource()
                                .addProperty(RDF.type, SchemaDO.EducationalOccupationalProgram)
                                .addProperty(
                                        SchemaDO.educationalCredentialAwarded,
                                        "Chemistry")
                                .addProperty(
                                        SchemaDO.educationalLevel,
                                        model.createResource("http://example.org/Master")
                                                .addProperty(RDF.type, SchemaDO.EducationalOccupationalCredential)
                                                .addProperty(SchemaDO.name, "Master"))
                                .addProperty(
                                        SchemaDO.startDate,
                                        "2015-06-01")
                                .addProperty(
                                        SchemaDO.alumniOf,
                                        model.createResource()
                                                .addProperty(RDF.type, SchemaDO.CollegeOrUniversity)
                                                .addProperty(SchemaDO.name, "Valencia University")
                                                .addProperty(SchemaDO.location, "http://example.org/Valencia"))
                                .addProperty(
                                        SchemaDO.knowsAbout,
                                        model.createBag()
                                                .add(model.createResource("http://example.org/WasteManagement"))
                                                .add(model.createResource("http://example.org/ToxicWaste"))
                                                .add(model.createResource("http://example.org/AirPollution"))))
                .addProperty(
                        FOAF.interest,
                        model.createBag()
                                .add(model.createResource("http://example.org/Cycling"))
                                .add(model.createResource("http://example.org/Music"))
                                .add(model.createResource("http://example.org/Traveling")))
                .addProperty(
                        model.createProperty("http://example.org/hasVisited"),
                        model.createSeq()
                                .add(model.createResource("http://example.org/Portugal"))
                                .add(model.createResource("http://example.org/Italy"))
                                .add(model.createResource("http://example.org/France"))
                                .add(model.createResource("http://example.org/Germany"))
                                .add(model.createResource("http://example.org/Denmark"))
                                .add(model.createResource("http://example.org/Sweden")));

        model.add(cade, FOAF.knows, emma);
        model.createResource("http://example.org/CadeAndEmmaMeeting")
                .addProperty(RDF.type, model.createResource("http://example.org/Meeting"))
                .addProperty(SchemaDO.location, model.createResource("http://example.org/Paris"))
                .addProperty(SchemaDO.startDate, "2014-08-01")
                .addProperty(SchemaDO.attendee, cade)
                .addProperty(SchemaDO.attendee, emma);


        //  виведіть на консоль усі трійки графу;
        StmtIterator nameIter = model.listStatements(null, null, (RDFNode) null);
        printStatement(nameIter);
        System.out.println("\n\n" + "*".repeat(60) + "\n\n");


        //  виведіть на консоль трійки, що стосуються лише про Емму;
        nameIter = model.listStatements(emma, null, (RDFNode) null);
        printStatementRecursive(nameIter, model);
        printStatementRecursive(nameIter, model);
        System.out.println("\n\n" + "*".repeat(60) + "\n\n");

        //  виведіть на консоль трійки, що містять імена людей.
        nameIter = model.listStatements(null, FOAF.name, (RDFNode) null);
        printStatementRecursive(nameIter, model);
        System.out.println("\n\n" + "*".repeat(60) + "\n\n");


        writeToFile("src/main/resources/output.l3.turtle", model, Lang.TURTLE);
        writeToFile("src/main/resources/output.l3.rdfxml", model, Lang.RDFXML);
        GraphConverter.writeModelToDotFile(model, "src/main/resources/output.l1.dot");
    }

    private static void printStatement(StmtIterator nameIter) {
        while (nameIter.hasNext()) {
            Statement stmt = nameIter.nextStatement();
            System.out.println(stmt);
        }
    }

    private static void printStatementRecursive(StmtIterator nameIter, Model model) {
        while (nameIter.hasNext()) {
            Statement stmt = nameIter.nextStatement();
            RDFNode nameNode = stmt.getObject();

            System.out.println("---------------------------------");
            System.out.printf("Суб'єкт: %s%n", stmt.getSubject().toString());
            System.out.printf("Предикат: %s%n", stmt.getPredicate().toString());

            if (nameNode.isResource()) {
                Resource resource = nameNode.asResource();
                System.out.printf("Об'єкт: %s%n", (resource.getURI() == null) ? "" : resource.getURI());

                StmtIterator labelIter = model.listStatements(resource, null, (RDFNode) null);
                while (labelIter.hasNext()) {
                    Statement labelStmt = labelIter.nextStatement();
                    System.out.printf("    Предикат: %s, Об'єкт: %s%n",
                            labelStmt.getPredicate().toString(),
                            labelStmt.getObject().toString());

                    if (labelStmt.getObject().isResource()) {
                        Resource labelResource = labelStmt.getObject().asResource();
                        StmtIterator nestedLabelIter = model.listStatements(labelResource, null, (RDFNode) null);
                        while (nestedLabelIter.hasNext()) {
                            Statement nestedLabelStmt = nestedLabelIter.nextStatement();
                            System.out.printf("        Предикат: %s, Об'єкт: %s%n",
                                    nestedLabelStmt.getPredicate().toString(),
                                    nestedLabelStmt.getObject().toString());
                        }
                    }
                }
            } else {
                System.out.printf("Об'єкт (літерал): %s%n", nameNode.toString());
            }
        }
    }


    private static void writeToFile(String fileName, Model model, Lang RDFJSON) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            model.write(bw, RDFJSON.getName());
        }
    }

}
