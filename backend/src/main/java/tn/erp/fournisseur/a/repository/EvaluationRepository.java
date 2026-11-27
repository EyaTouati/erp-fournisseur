package tn.erp.fournisseur.a.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.erp.fournisseur.a.entity.Evaluation;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByFournisseurIdOrderByDateEvaluationDesc(Long fournisseurId);
}
