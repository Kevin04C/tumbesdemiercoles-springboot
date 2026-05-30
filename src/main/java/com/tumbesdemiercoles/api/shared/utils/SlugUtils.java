package com.tumbesdemiercoles.api.shared.utils;

import java.text.Normalizer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SlugUtils {

  public static String toSlug(String text) {
    if (text == null || text.isBlank()) {
      return "";
    }

    String normalized = Normalizer.normalize(text.trim(), Normalizer.Form.NFD);
    normalized = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    normalized = normalized.replaceAll("[^a-zA-Z0-9\\s-]", "");
    normalized = normalized.replaceAll("\\s+", "-");
    normalized = normalized.replaceAll("-+", "-");
    normalized = normalized.toLowerCase();
    normalized = normalized.replaceAll("^-|-$", "");

    return normalized;
  }
}
