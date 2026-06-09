package com.tumbesdemiercoles.api.news.infrastructure.search;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeiliSearchConfig {

  @Bean
  public Client meiliSearchClient(
      @Value("${meili.host}") String host,
      @Value("${meili.api-key}") String apiKey
  ) {
    return new Client(new Config(host, apiKey));
  }
}
