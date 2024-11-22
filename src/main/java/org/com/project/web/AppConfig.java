package org.com.project.web;

import org.com.project.repository.BasicFilmRepository;
import org.com.project.repository.BasicWikidataFilmLoader;
import org.com.project.repository.FilmRepository;
import org.com.project.repository.WikidataFilmLoader;
import org.com.project.seriveces.BasicWikidataFilmService;
import org.com.project.seriveces.FilmService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Конфігураційний клас, що визначає біні для налаштування інфраструктури додатку
 * у рамках Spring контексту. Цей клас визначає біні для підключення до репозиторіїв
 * та сервісів, які використовуються для взаємодії з фільмами.
 */
@Configuration
public class AppConfig {

    /**
     * Створює бін для класу {@link BasicWikidataFilmLoader}, який відповідає за завантаження
     * жанрів фільмів з Wikidata.
     *
     * @return об'єкт {@link BasicWikidataFilmLoader}
     */
    @Bean
    public WikidataFilmLoader wikidataLoader() {
        return new BasicWikidataFilmLoader();
    }

    /**
     * Створює бін для репозиторію {@link FilmRepository}, що використовується для взаємодії
     * з фільмами у Wikidata. Цей бін позначено як {@link Primary}, що дозволяє Spring
     * вибирати його за замовчуванням, коли є кілька можливих варіантів.
     *
     * @param wikidataLoader об'єкт, що відповідає за завантаження даних з Wikidata
     * @return об'єкт {@link FilmRepository}, конкретно {@link BasicFilmRepository}
     */
    @Primary
    @Bean
    public FilmRepository filmRepository(WikidataFilmLoader wikidataLoader) {
        return new BasicFilmRepository(wikidataLoader);
    }

    /**
     * Створює бін для сервісу {@link BasicWikidataFilmService}, який надає бізнес-логіку
     * для роботи з фільмами.
     *
     * @return об'єкт {@link BasicWikidataFilmService}
     */
    @Bean
    public FilmService filmService() {
        return new BasicWikidataFilmService();
    }

}
