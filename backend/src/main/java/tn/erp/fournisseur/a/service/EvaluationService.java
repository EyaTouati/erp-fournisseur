package tn.erp.fournisseur.a.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.erp.fournisseur.a.dto.EvaluationCreationDto;
import tn.erp.fournisseur.a.dto.EvaluationDto;
import tn.erp.fournisseur.a.dto.EvaluationListeDto;
import tn.erp.fournisseur.a.entity.Evaluation;
import tn.erp.fournisseur.a.entity.Fournisseur;
import tn.erp.fournisseur.a.repository.EvaluationRepository;
import tn.erp.fournisseur.a.repository.FournisseurRepository;
import tn.erp.fournisseur.commun.erreurs.IntrouvableException;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;
import tn.erp.fournisseur.commun.securite.UtilisateurCourant;

import java.util.List;

@Service
public class EvaluationService {

    private final EvaluationRepository evaluations;
    private final FournisseurRepository fournisseurs;
    private final UtilisateurCourant utilisateur;

    public EvaluationService(EvaluationRepository evaluations, FournisseurRepository fournisseurs,
                             UtilisateurCourant utilisateur) {
        this.evaluations = evaluations;
        this.fournisseurs = fournisseurs;
        this.utilisateur = utilisateur;
    }

    /** Utilisée par l'endpoint et (via FournisseurService) par C après une réception validée. */
    @Transactional
    public Evaluation enregistrer(Long fournisseurId, Long commandeId, int noteDelai, int noteQualite,
                                  int notePrix, String commentaire, String evaluateur) {
        controlerNote("délai", noteDelai);          // RG-A4
        controlerNote("qualité", noteQualite);
        controlerNote("prix", notePrix);
        Fournisseur f = fournisseurs.findById(fournisseurId)
                .orElseThrow(() -> new IntrouvableException("Fournisseur " + fournisseurId + " introuvable"));
        return evaluations.save(new Evaluation(f, commandeId, (short) noteDelai, (short) noteQualite,
                (short) notePrix, commentaire, evaluateur));
    }

    @Transactional
    public EvaluationDto creer(Long fournisseurId, EvaluationCreationDto dto) {
        Evaluation e = enregistrer(fournisseurId, dto.commandeId(), dto.noteDelai(), dto.noteQualite(),
                dto.notePrix(), dto.commentaire(), utilisateur.nom());
        return EvaluationDto.de(e);
    }

    @Transactional(readOnly = true)
    public EvaluationListeDto lister(Long fournisseurId) {
        if (!fournisseurs.existsById(fournisseurId))
            throw new IntrouvableException("Fournisseur " + fournisseurId + " introuvable");
        List<Evaluation> liste = evaluations.findByFournisseurIdOrderByDateEvaluationDesc(fournisseurId);
        Double moyenne = null;
        if (!liste.isEmpty()) {
            double somme = 0;
            for (Evaluation e : liste) somme += e.getNoteDelai() + e.getNoteQualite() + e.getNotePrix();
            moyenne = Math.round(somme / (liste.size() * 3.0) * 100.0) / 100.0;
        }
        return new EvaluationListeDto(liste.stream().map(EvaluationDto::de).toList(), moyenne);
    }

    private void controlerNote(String nom, int note) {
        if (note < 1 || note > 5)
            throw new RegleGestionException("RG-A4", "La note de " + nom + " doit être comprise entre 1 et 5");
    }
}
