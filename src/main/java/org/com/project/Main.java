package org.com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class Main {


    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(Main.class);
        System.out.printf("%s:%s%n",
                "http://localhost",
                app.getEnvironment().getProperty("local.server.port"));
    }


}
