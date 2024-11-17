package org.com.project.web;


import org.com.project.seriveces.WikidataLoader;
import org.com.project.seriveces.WikipediaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WikipediaService wikipediaService() {
        WikidataLoader wikidataLoader = new WikidataLoader();
        return new WikipediaService(wikidataLoader.getAvailableGenres(), wikidataLoader);
    }


}
