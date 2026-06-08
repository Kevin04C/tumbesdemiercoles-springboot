package com.tumbesdemiercoles.api.news.application.dto;

import java.time.OffsetDateTime;

public record SitemapNewsDto(String slug, String title, OffsetDateTime createdAt, String imageUrl) {
}
