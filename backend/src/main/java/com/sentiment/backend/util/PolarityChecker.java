package com.sentiment.backend.util;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PolarityChecker {

  public boolean isPositive(String palavra) {
    return checkPolarity(palavra, SentimentLexicon.POSITIVAS);
  }

  public boolean isNegative(String palavra) {
    return checkPolarity(palavra, SentimentLexicon.NEGATIVAS);
  }

  private boolean checkPolarity(String palavra, List<String> lexicon) {
    return lexicon.stream().anyMatch(palavra::startsWith);
  }
}
