package tn.erp.fournisseur.b;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.erp.fournisseur.a.service.FournisseurService;
import tn.erp.fournisseur.b.dto.DemandePrixCreationDto;
import tn.erp.fournisseur.b.entity.DemandeAchat;
import tn.erp.fournisseur.b.entity.StatutDemandeAchat;
import tn.erp.fournisseur.b.repository.DemandeAchatRepository;
import tn.erp.fournisseur.b.repository.DemandePrixRepository;
import tn.erp.fournisseur.b.service.DemandePrixService;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;
import tn.erp.fournisseur.commun.securite.UtilisateurCourant;
import tn.erp.fournisseur.commun.workflow.WorkflowService;

@ExtendWith(MockitoExtension.class)
class DemandePrixServiceTest {

    @Mock DemandePrixRepository demandesPrix;
    @Mock DemandeAchatRepository demandesAchat;
    @Mock FournisseurService fournisseurs;
    @Mock WorkflowService workflow;
    @Mock UtilisateurCourant utilisateur;
    @InjectMocks DemandePrixService service;

    private DemandeAchat demande(StatutDemandeAchat statut) {
        DemandeAchat d = new DemandeAchat("DA-2026-0001", 1L, 10, null, "magasinier");
        d.setStatut(statut);
        return d;
    }

    private DemandePrixCreationDto dto(Long... ids) {
        return new DemandePrixCreationDto(1L, LocalDate.now().plusDays(5), List.of(ids));
    }

    @Test
    void seuleUneDemandeAccepteeDonneUneDemandeDePrix() {
        when(demandesAchat.findById(1L)).thenReturn(Optional.of(demande(StatutDemandeAchat.SOUMISE)));
        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.creer(dto(1L, 2L)));
        assertEquals("B-ETAT", e.getCode());
        verify(demandesPrix, never()).save(any());
    }

    @Test
    void unFournisseurInactifEstRefuse() {
        when(demandesAchat.findById(1L)).thenReturn(Optional.of(demande(StatutDemandeAchat.ACCEPTEE)));
        when(fournisseurs.estActif(1L)).thenReturn(true);
        when(fournisseurs.estActif(2L)).thenReturn(false);
        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.creer(dto(1L, 2L)));
        assertEquals("RG-B1", e.getCode());
    }

    @Test
    void moinsDeDeuxFournisseursEstRefuse() {
        when(demandesAchat.findById(1L)).thenReturn(Optional.of(demande(StatutDemandeAchat.ACCEPTEE)));
        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.creer(dto(1L, 1L)));
        assertEquals("B-FOURNISSEURS", e.getCode());
    }
}