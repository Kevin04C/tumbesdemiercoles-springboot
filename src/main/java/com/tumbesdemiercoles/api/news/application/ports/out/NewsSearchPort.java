package com.tumbesdemiercoles.api.news.application.ports.out;

import com.tumbesdemiercoles.api.news.domain.model.News;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface NewsSearchPort {

  Mono<Void> indexNews(News news);

  Mono<Void> updateIndexedNews(News news);

  Mono<Void> removeIndexedNews(UUID id);

  Mono<Void> indexAll(List<News> batch);
}
