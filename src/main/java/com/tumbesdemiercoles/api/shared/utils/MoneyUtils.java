package com.tumbesdemiercoles.api.shared.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyUtils {

  public static BigDecimal normalizeMoney(BigDecimal v) {
    if (v == null) return BigDecimal.ZERO;
    return v.setScale(2, RoundingMode.HALF_UP);
  }

  public static int toMinor(BigDecimal amount) {
    return amount.movePointRight(2)
        .setScale(0, RoundingMode.HALF_UP)
        .intValueExact();
  }
}
