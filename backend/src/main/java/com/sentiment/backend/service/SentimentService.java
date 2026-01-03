package com.sentiment.backend.service;

import com.sentiment.backend.dto.SentimentRequest;
import com.sentiment.backend.dto.SentimentResponse;
import com.sentiment.backend.dto.SentimentStatsResponse;
import com.sentiment.backend.mapper.SentimentAnalysisMapper;
import com.sentiment.backend.model.SentimentAnalysis;
import com.sentiment.backend.model.SentimentType;
import com.sentiment.backend.repository.SentimentAnalysisRepository;
import com.sentiment.backend.util.SentimentAnalysisResult;
import com.sentiment.backend.util.SentimentAnalyzer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SentimentService {

  private final SentimentAnalysisRepository repository;
  private final BusinessRuleService businessRuleService;
  private final SentimentAnalysisMapper mapper;
  private final SentimentAnalyzer sentimentAnalyzer;

  @Transactional
  public SentimentResponse analisarSentimento(SentimentRequest request) {
    String texto = request.getText();

    log.debug("Iniciando análise para texto com {} caracteres", texto.length());

    SentimentAnalysisResult resultado = sentimentAnalyzer.analisar(texto);
    SentimentType tipoSentimento = mapearTipoSentimento(resultado.getType());

    SentimentResponse response = construirResposta(texto, resultado, tipoSentimento);

    persistirAnalise(request, response);

    log.info("Análise concluída - Sentimento: {}, Prioridade: {}, Setor: {}",
        tipoSentimento, response.getPrioridade(), response.getSetor());

    return response;
  }

  @Transactional(readOnly = true)
  public List<SentimentAnalysis> buscarUltimasDezAnalises() {
    return repository.findTop10ByOrderByCreatedAtDesc();
  }

  @Transactional(readOnly = true)
  public List<SentimentStatsResponse> gerarEstatisticas() {
    long total = repository.count();

    log.debug("Gerando estatísticas para {} análises", total);

    return java.util.Arrays.stream(SentimentType.values())
        .map(tipo -> {
          long quantidade = repository.countByPrediction(tipo);
          return new SentimentStatsResponse(tipo, quantidade, (double) total);
        })
        .collect(Collectors.toList());
  }

  private SentimentResponse construirResposta(
      String texto,
      SentimentAnalysisResult resultado,
      SentimentType tipoSentimento) {

    String setor = businessRuleService.identificarSetor(texto);

    return SentimentResponse.builder()
        .previsao(tipoSentimento)
        .probabilidade(resultado.getProbabilidade())
        .prioridade(businessRuleService.identificarPrioridade(texto, tipoSentimento))
        .setor(setor)
        .tags(businessRuleService.extrairTags(texto))
        .sugestaoResposta(businessRuleService.gerarSugestao(tipoSentimento, setor))
        .build();
  }

  private void persistirAnalise(SentimentRequest request, SentimentResponse response) {
    SentimentAnalysis analysis = mapper.toEntity(request, response);
    repository.save(analysis);
    log.debug("Análise persistida com sucesso");
  }

  private SentimentType mapearTipoSentimento(SentimentAnalysisResult.SentimentType tipo) {
    return switch (tipo) {
      case POSITIVO -> SentimentType.POSITIVO;
      case NEGATIVO -> SentimentType.NEGATIVO;
      case NEUTRO -> SentimentType.NEUTRO;
    };
  }
}
