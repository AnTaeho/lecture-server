package com.example.lectureserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LectureServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LectureServerApplication.class, args);
    }

}
