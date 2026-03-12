package com.mikeyy.nbafantasybackend.model;
import com.mikeyy.nbafantasybackend.repository.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PlayerConfig {

    @Bean
    CommandLineRunner commandLineRunner(PlayerRepository repository) {
        return args -> {
            Player gabriel = new Player(
                    "Jalen"
            );

            Player alex = new Player(
                    "Johnson"
            );
        };
    }
}


