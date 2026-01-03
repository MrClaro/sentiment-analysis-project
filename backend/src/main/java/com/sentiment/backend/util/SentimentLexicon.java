package com.sentiment.backend.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SentimentLexicon {

  public static final List<String> POSITIVAS = List.of(
      "bom", "bem", "otim", "excelen", "perfeit", "maravilh", "fantast", "sensacion",
      "impec", "qualific", "superior", "melhor",
      "agrad", "satisf", "feliz", "contentament", "praz",
      "eficient", "rapid", "pratico", "facil", "simpl", "eficaz",
      "recomend", "indic", "vale", "top", "legal",
      "atencios", "gentil", "educad", "profissional", "confiavel", "cort",
      "util", "bonit", "limp", "claro", "divertid", "surpreend",
      "agradavel", "confortavel",
      "ador", "am", "encant", "apaixon");

  public static final List<String> NEGATIVAS = List.of(
      "ruim", "pessim", "terriv", "horriv", "pior", "pobr", "inferior",
      "falh", "problem", "defeit", "quebrad", "estrag", "danific",
      "lent", "demor", "complic", "dific", "confus", "ineficient",
      "frustr", "decepcion", "insatisf", "desagrad", "inaceit",
      "desrespeit", "mal", "gross", "ignorant", "desprep",
      "deficien", "insuport", "inconvenient", "irrit", "chate",
      "caro", "absurd", "abus", "prejuiz",
      "odi", "detest", "raiv", "triste",
      "errad", "atrás", "suj", "lixo", "insucess");

  public static final Set<String> ADVERSATIVAS = Set.of(
      "mas", "porem", "contudo", "entretanto", "todavia", "no entanto");

  public static final Set<String> NEGACOES = Set.of(
      "nao", "nunca", "jamais", "nem", "sem", "nenhum", "tampouco");

  public static final Set<String> INTENSIFICADORES = Set.of(
      "muito", "extremament", "super", "demais", "totalment",
      "altament", "bastant", "incrivelment", "absolutament",
      "completament", "profundament", "imensament");
}
