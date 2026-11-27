package tn.erp.fournisseur.a.dto;

import java.util.List;

/** Liste des évaluations + moyenne générale (null s'il n'y a aucune évaluation). */
public record EvaluationListeDto(List<EvaluationDto> evaluations, Double moyenne) { }
