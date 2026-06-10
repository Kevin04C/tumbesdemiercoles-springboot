package com.tumbesdemiercoles.api.news.infrastructure.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Client;
import com.tumbesdemiercoles.api.news.application.ports.out.NewsSearchPort;
import com.tumbesdemiercoles.api.news.domain.model.News;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeiliSearchAdapter implements NewsSearchPort {

  private static final String INDEX_NAME = "news";
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private final Client client;

  @PostConstruct
  void configureIndex() {
    Mono.<Void>fromRunnable(() -> {
          var index = client.index(INDEX_NAME);
          index.updateSearchableAttributesSettings(new String[]{"title", "headline", "content"});
          index.updateFilterableAttributesSettings(new String[]{"categoryId", "isActive", "isPremium"});
          index.updateSortableAttributesSettings(new String[]{"createdAt"});
          log.info("MeiliSearch index '{}' configured", INDEX_NAME);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
  }

  @Override
  public Mono<Void> indexNews(News news) {
    return Mono.<Void>fromRunnable(() -> {
          client.index(INDEX_NAME).addDocuments(serialize(news));
          log.debug("Indexed news {}", news.getId());
        })
        .subscribeOn(Schedulers.boundedElastic())
        .onErrorResume(e -> {
          log.error("Failed to index news {}: {}", news.getId(), e.getMessage());
          return Mono.empty();
        });
  }

  @Override
  public Mono<Void> updateIndexedNews(News news) {
    return Mono.<Void>fromRunnable(() -> {
          client.index(INDEX_NAME).addDocuments(serialize(news));
          log.debug("Updated indexed news {}", news.getId());
        })
        .subscribeOn(Schedulers.boundedElastic())
        .onErrorResume(e -> {
          log.error("Failed to update indexed news {}: {}", news.getId(), e.getMessage());
          return Mono.empty();
        });
  }

  @Override
  public Mono<Void> removeIndexedNews(UUID id) {
    return Mono.<Void>fromRunnable(() -> {
          client.index(INDEX_NAME).deleteDocument(id.toString());
          log.debug("Removed indexed news {}", id);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .onErrorResume(e -> {
          log.error("Failed to remove indexed news {}: {}", id, e.getMessage());
          return Mono.empty();
        });
  }

  @Override
  public Mono<Void> indexAll(List<News> batch) {
    return Mono.<Void>fromRunnable(() -> {
          client.index(INDEX_NAME).addDocuments(serializeList(batch));
          log.debug("Indexed batch of {} news", batch.size());
        })
        .subscribeOn(Schedulers.boundedElastic())
        .onErrorResume(e -> {
          log.error("Failed to index batch: {}", e.getMessage());
          return Mono.empty();
        });
  }

  private String serialize(News news) {
    try {
      return objectMapper.writeValueAsString(NewsSearchDocument.from(news));
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize news document", e);
    }
  }

  private String serializeList(List<News> newsList) {
    try {
      return objectMapper.writeValueAsString(
          newsList.stream().map(NewsSearchDocument::from).toList()
      );
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize news batch", e);
    }
  }
}
