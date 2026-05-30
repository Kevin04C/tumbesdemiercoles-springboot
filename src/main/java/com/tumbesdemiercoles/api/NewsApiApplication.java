package com.tumbesdemiercoles.api;

import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@SpringBootApplication
@EnableR2dbcAuditing(dateTimeProviderRef = "offsetDateTimeProvider")
public class NewsApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(NewsApiApplication.class, args);
  }

  @Bean
  public DateTimeProvider offsetDateTimeProvider() {
    return () -> Optional.of(OffsetDateTime.now());
  }

}
