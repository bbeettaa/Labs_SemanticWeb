package org.com.project.seriveces;

import org.apache.jena.query.QuerySolution;
import org.com.project.Dataset;

public class DatasetFactory {
    public static Dataset fromSolution(QuerySolution solution) {
        Dataset dataset = new Dataset();

        if (solution.contains("film") && solution.getResource("film") != null)
            dataset.setId(solution.getResource("film").getLocalName());
        if (solution.contains("film") && solution.getResource("film") != null)
            dataset.setUri(solution.getResource("film").getURI());
        if (solution.contains("filmLabel") && solution.getLiteral("filmLabel") != null)
            dataset.setFilmLabel(solution.getLiteral("filmLabel").getString());
        if (solution.contains("description") && solution.getLiteral("description") != null)
            dataset.setDescription(solution.getLiteral("description").getString());
        if (solution.contains("genres") && solution.getLiteral("genres") != null)
            dataset.setGenres(solution.getLiteral("genres").getString());
        if (solution.contains("countries") && solution.getLiteral("countries") != null)
            dataset.setCountries(solution.getLiteral("countries").getString());
        if (solution.contains("originalLanguage") && solution.getLiteral("originalLanguage") != null)
            dataset.setOriginalLanguage(solution.getLiteral("originalLanguage").getString());
        if (solution.contains("duration") && solution.getLiteral("duration") != null)
            dataset.setDuration(solution.getLiteral("duration").getString());
        if (solution.contains("directors") && solution.getLiteral("directors") != null)
            dataset.setDirectors(solution.getLiteral("directors").getString());
        if (solution.contains("samplePublicationDate") && solution.getLiteral("samplePublicationDate") != null)
            dataset.setSamplePublicationDate(solution.getLiteral("samplePublicationDate").getString());
        if (solution.contains("countries") && solution.getLiteral("countries") != null)
            dataset.setCountries(solution.getLiteral("countries").getString());
        if (solution.contains("directors") && solution.getLiteral("directors") != null)
            dataset.setDirectors(solution.getLiteral("directors").getString());
        if (solution.contains("rating") && solution.getLiteral("rating") != null)
            dataset.setRating(solution.getLiteral("rating").getString());
        if (solution.contains("posterUrl") && solution.getResource("posterUrl") != null)
            dataset.setPosterUrl(solution.getResource("posterUrl").getURI());
        if (solution.contains("publicationYear") && solution.getLiteral("publicationYear") != null)
            dataset.setSamplePublicationDate(solution.getLiteral("publicationYear").getString());

        if (solution.contains("award") && solution.getLiteral("award") != null)
            dataset.setAward(solution.getLiteral("award").getString());
        if (solution.contains("mainSubjects") && solution.getLiteral("mainSubjects") != null)
            dataset.setMainSubject(solution.getLiteral("mainSubjects").getString());
        if (solution.contains("slogan") && solution.getLiteral("slogan") != null)
            dataset.setSlogan(solution.getLiteral("slogan").getString());

        return dataset;
    }

}
