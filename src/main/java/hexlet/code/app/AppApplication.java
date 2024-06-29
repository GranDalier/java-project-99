package hexlet.code.app;

import net.datafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Locale;

@EnableJpaAuditing
@SpringBootApplication
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    /**
     * Bean Faker.
     @return Faker
     */
    @Bean
    public Faker getFaker() {
        var locale = new Locale.Builder().setLanguageTag("en-US").build();
        return new Faker(locale);
    }
}
