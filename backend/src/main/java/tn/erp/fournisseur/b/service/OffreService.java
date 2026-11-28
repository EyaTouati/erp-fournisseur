package tn.erp.fournisseur.b.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.erp.fournisseur.b.dto.OffreCreationDto;
import tn.erp.fournisseur.b.dto.OffreDto;
import tn.erp.fournisseur.b.entity.DemandePrix;
import tn.erp.fournisseur.b.entity.Offre;
import tn.erp.fournisseur.b.entity.StatutDemandePrix;
import tn.erp.fournisseur.b.entity.StatutOffre;
import tn.erp.fournisseur.b.repository.DemandePrixRepository;
import tn.erp.fournisseur.b.repository.OffreRepository;
import tn.erp.fournisseur.commun.erreurs.IntrouvableException;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;
import tn.erp.fournisseur.commun.securite.UtilisateurCourant;
import tn.erp.fournisseur.commun.workflow.WorkflowService;

@Service
public class OffreService {

    private final OffreRepository offres;
    private final DemandePrixRepository demandesPrix;
    private final WorkflowService workflow;
    private final UtilisateurCourant utilisateur;

    public OffreService(OffreRepository offres, DemandePrixRepository demandesPrix,
                        WorkflowService workflow, UtilisateurCourant utilisateur) {
        this.offres = offres;
        this.demandesPrix = demandesPrix;
        this.workflow = workflow;
        this.utilisateur = utilisateur;
    }

    @Transactional
    public OffreDto saisir(Long demandePrixId, OffreCreationDto dto) {
        DemandePrix dp = trouverDemandePrix(demandePrixId);
        if (dp.getStatut() != StatutDemandePrix.ENVOYEE && dp.getStatut() != StatutDemandePrix.OFFRES_RECUES)
            throw new RegleGestionException("B-ETAT", "La demande de prix doit être envoyée");
        if (!dp.getFournisseurIds().contains(dto.fournisseurId()))
            throw new RegleGestionException("B-FOURNISSEUR", "Ce fournisseur n'a pas été consulté");
        if (offres.existsByDemandePrixIdAndFournisseurId(dp.getId(), dto.fournisseurId()))
            throw new RegleGestionException("B-DOUBLON", "Ce fournisseur a déjà une offre pour cette demande");

        Offre o = offres.save(new Offre(dp, dto.fournisseurId(), dto.prixUnitaire(), dto.quantite(),
                dto.delaiJours(), dto.conditions(), dto.dateValidite()));
        workflow.enregistrerCreation("OFFRE", o.getId(), "RECUE", utilisateur.nom());

        if (dp.getStatut() == StatutDemandePrix.ENVOYEE) {      // première offre reçue
            workflow.changerStatut("DEMANDE_PRIX", dp.getId(), "ENVOYEE", "OFFRES_RECUES",
                    utilisateur.nom(), utilisateur.role());
            dp.setStatut(StatutDemandePrix.OFFRES_RECUES);
        }
        List<Offre> toutes = offres.findByDemandePrixId(dp.getId());
        return versDto(o, toutes);
    }

    @Transactional(readOnly = true)
    public List<OffreDto> lister(Long demandePrixId) {
        trouverDemandePrix(demandePrixId);
        List<Offre> toutes = offres.findByDemandePrixId(demandePrixId);
        return toutes.stream()
                .sorted(Comparator.comparing(Offre::getPrixUnitaire).thenComparing(Offre::getDelaiJours))
                .map(o -> versDto(o, toutes))
                .toList();
    }

    @Transactional
    public OffreDto retenir(Long offreId, String justification) {
        Offre o = trouver(offreId);
        if (o.getStatut() != StatutOffre.RECUE)
            throw new RegleGestionException("B-ETAT", "Seule une offre reçue peut être retenue");
        if (o.getDateValidite().isBefore(LocalDate.now()))                       // RG-B4
            throw new RegleGestionException("RG-B4", "Cette offre est expirée");

        DemandePrix dp = o.getDemandePrix();
        List<Offre> toutes = offres.findByDemandePrixId(dp.getId());
        if (toutes.stream().anyMatch(x -> x.getStatut() == StatutOffre.RETENUE)) // RG-B2
            throw new RegleGestionException("RG-B2", "Une offre est déjà retenue pour cette demande");
        if (dp.getStatut() != StatutDemandePrix.OFFRES_RECUES)
            throw new RegleGestionException("B-ETAT", "La demande de prix doit être à l'état OFFRES_RECUES");

        boolean justif = justification != null && !justification.isBlank();
        if (!estMoinsChere(o, toutes) && !justif)                                // RG-B3
            throw new RegleGestionException("RG-B3", "Justification obligatoire : ce n'est pas l'offre la moins chère");

        if (justif) {
            workflow.changerStatut("OFFRE", o.getId(), "RECUE", "RETENUE",
                    utilisateur.nom(), utilisateur.role(), justification);
            o.setJustification(justification);
        } else {
            workflow.changerStatut("OFFRE", o.getId(), "RECUE", "RETENUE",
                    utilisateur.nom(), utilisateur.role());
        }
        o.setStatut(StatutOffre.RETENUE);

        for (Offre autre : toutes) {
            if (autre != o && autre.getStatut() == StatutOffre.RECUE) {
                workflow.changerStatut("OFFRE", autre.getId(), "RECUE", "REJETEE",
                        utilisateur.nom(), utilisateur.role());
                autre.setStatut(StatutOffre.REJETEE);
            }
        }
        workflow.changerStatut("DEMANDE_PRIX", dp.getId(), "OFFRES_RECUES", "CLOTUREE",
                utilisateur.nom(), utilisateur.role());
        dp.setStatut(StatutDemandePrix.CLOTUREE);
        return versDto(o, toutes);
    }

    @Transactional
    public OffreDto rejeter(Long offreId) {
        Offre o = trouver(offreId);
        workflow.changerStatut("OFFRE", o.getId(), o.getStatut().name(), "REJETEE",
                utilisateur.nom(), utilisateur.role());
        o.setStatut(StatutOffre.REJETEE);
        return versDto(o, offres.findByDemandePrixId(o.getDemandePrix().getId()));
    }

    /** Contrat pour C (section 4.6). */
    @Transactional(readOnly = true)
    public Offre getOffreRetenue(Long offreId) {
        Offre o = trouver(offreId);
        if (o.getStatut() != StatutOffre.RETENUE)
            throw new RegleGestionException("RG-C1", "L'offre n'est pas retenue");
        return o;
    }

    private boolean estMoinsChere(Offre o, List<Offre> toutes) {
        return toutes.stream().noneMatch(x -> x.getPrixUnitaire().compareTo(o.getPrixUnitaire()) < 0);
    }

    private OffreDto versDto(Offre o, List<Offre> toutes) {
        boolean expiree = o.getDateValidite().isBefore(LocalDate.now());
        return OffreDto.de(o, estMoinsChere(o, toutes), expiree);
    }

    private Offre trouver(Long id) {
        return offres.findById(id)
                .orElseThrow(() -> new IntrouvableException("Offre " + id + " introuvable"));
    }

    private DemandePrix trouverDemandePrix(Long id) {
        return demandesPrix.findById(id)
                .orElseThrow(() -> new IntrouvableException("Demande de prix " + id + " introuvable"));
    }
}