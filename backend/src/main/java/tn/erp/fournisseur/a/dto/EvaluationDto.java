package tn.erp.fournisseur.a.dto;

import tn.erp.fournisseur.a.entity.Evaluation;

import java.time.LocalDateTime;

public record EvaluationDto(Long id, Long fournisseurId, Long commandeId, Integer noteDelai, Integer noteQualite,
                            Integer notePrix, String commentaire, LocalDateTime dateEvaluation, String evaluateur) {
    public static EvaluationDto de(Evaluation e) {
        return new EvaluationDto(e.getId(), e.getFournisseur().getId(), e.getCommandeId(),
                e.getNoteDelai().intValue(), e.getNoteQualite().intValue(), e.getNotePrix().intValue(),
                e.getCommentaire(), e.getDateEvaluation(), e.getEvaluateur());
    }
}
