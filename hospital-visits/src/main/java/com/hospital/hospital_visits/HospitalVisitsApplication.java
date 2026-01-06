package com.hospital.hospital_visits;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;

@SpringBootApplication
public class HospitalVisitsApplication {

    public static void main(String[] args) {

        // control if the file exists in the right directory
        Path envPath = Path.of("example.env").toAbsolutePath();
        System.out.println("Looking for example.env at: " + envPath);


        Dotenv dotenv = Dotenv.configure()
                .filename("example.env")
                .load();

        dotenv.entries().forEach(e -> {
            if (System.getenv(e.getKey()) == null && System.getProperty(e.getKey()) == null) {
                System.setProperty(e.getKey(), e.getValue());
            }
        });


        System.out.println("JWT_SECRET loaded? " + (System.getProperty("JWT_SECRET") != null));

        SpringApplication.run(HospitalVisitsApplication.class, args);
    }
}
