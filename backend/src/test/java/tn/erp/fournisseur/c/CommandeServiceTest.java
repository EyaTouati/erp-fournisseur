package tn.erp.fournisseur.c;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.erp.fournisseur.c.dto.CommandeDto;
import tn.erp.fournisseur.c.dto.LigneQuantiteDto;
import tn.erp.fournisseur.c.entity.Commande;
import tn.erp.fournisseur.c.entity.StatutCommande;
import tn.erp.fournisseur.c.repository.CommandeRepository;
import tn.erp.fournisseur.c.service.CommandeService;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;
import tn.erp.fournisseur.commun.integration.StockService;
import tn.erp.fournisseur.commun.securite.UtilisateurCourant;
import tn.erp.fournisseur.commun.workflow.WorkflowService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandeServiceTest {

    @Mock CommandeRepository commandes;
    @Mock WorkflowService workflow;
    @Mock UtilisateurCourant utilisateur;
    @Mock StockService stock;
    @InjectMocks CommandeService service;

    /** Commande de 50 unités au prix donné, créée par "achats". */
    private Commande commande(String prix, StatutCommande statut) {
        Commande c = new Commande("BC-2026-0001", 7L, 1L, "achats");
        c.ajouterLigne(1L, 50, new BigDecimal(prix));
        c.setStatut(statut);
        return c;
    }

    @Test
    void deuxCommandesPourLaMemeOffreSontRefusees() {
        when(commandes.existsByOffreId(7L)).thenReturn(true);
        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.creerDepuisOffre(7L, 1L, 1L, 50, new BigDecimal("120")));
        assertEquals("RG-C1", e.getCode());
        verify(commandes, never()).save(any());
    }

    @Test
    void laCreationCalculeLeMontantEtCommenceEnBrouillon() {
        when(commandes.existsByOffreId(7L)).thenReturn(false);
        when(utilisateur.nom()).thenReturn("achats");
        when(commandes.save(any(Commande.class))).thenAnswer(i -> i.getArgument(0));

        CommandeDto dto = service.creerDepuisOffre(7L, 1L, 1L, 50, new BigDecimal("120"));

        assertEquals("BROUILLON", dto.statut());
        assertEquals(0, new BigDecimal("6000").compareTo(dto.montantTotal()));
        assertTrue(dto.numero().startsWith("BC-"));
        assertEquals("achats", dto.creePar());
        verify(workflow).enregistrerCreation(eq("COMMANDE"), any(), eq("BROUILLON"), eq("achats"));
    }

    @Test
    void unMontantSousLeSeuilEstValideAutomatiquement() {
        Commande c = commande("60", StatutCommande.BROUILLON);              // 3 000 DT
        when(commandes.findById(12L)).thenReturn(Optional.of(c));
        when(workflow.parametreDecimal("seuil_validation")).thenReturn(new BigDecimal("5000"));
        when(utilisateur.nom()).thenReturn("achats");

        service.soumettre(12L);

        assertEquals(StatutCommande.VALIDEE, c.getStatut());
        assertEquals("SYSTEME", c.getValidePar());
        verify(workflow).changerStatut(eq("COMMANDE"), any(), eq("BROUILLON"), eq("VALIDEE"),
                eq("achats"), eq("SYSTEME"), anyString());
    }

    @Test
    void unMontantAuDessusDuSeuilAttendLeDirecteur() {
        Commande c = commande("120", StatutCommande.BROUILLON);             // 6 000 DT
        when(commandes.findById(12L)).thenReturn(Optional.of(c));
        when(workflow.parametreDecimal("seuil_validation")).thenReturn(new BigDecimal("5000"));
        when(utilisateur.nom()).thenReturn("achats");

        service.soumettre(12L);

        assertEquals(StatutCommande.EN_ATTENTE_VALIDATION, c.getStatut());
        assertNull(c.getValidePar());
    }

    @Test
    void leCreateurNePeutPasValider() {
        Commande c = commande("120", StatutCommande.EN_ATTENTE_VALIDATION);
        when(commandes.findById(12L)).thenReturn(Optional.of(c));
        when(utilisateur.nom()).thenReturn("achats");

        RegleGestionException e = assertThrows(RegleGestionException.class, () -> service.valider(12L));

        assertEquals("RG-C3", e.getCode());
        assertEquals(StatutCommande.EN_ATTENTE_VALIDATION, c.getStatut());
        verify(workflow, never()).changerStatut(anyString(), any(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void leDirecteurValideLaCommande() {
        Commande c = commande("120", StatutCommande.EN_ATTENTE_VALIDATION);
        when(commandes.findById(12L)).thenReturn(Optional.of(c));
        when(utilisateur.nom()).thenReturn("directeur");
        when(utilisateur.role()).thenReturn("DIRECTEUR");

        service.valider(12L);

        assertEquals(StatutCommande.VALIDEE, c.getStatut());
        assertEquals("directeur", c.getValidePar());
    }

    @Test
    void unRefusSansMotifEstRefuse() {
        RegleGestionException e = assertThrows(RegleGestionException.class, () -> service.refuser(12L, "  "));
        assertEquals("MOTIF", e.getCode());
        verifyNoInteractions(commandes, workflow);
    }

    @Test
    void uneCommandeValideeNestPlusModifiable() {
        Commande c = commande("120", StatutCommande.VALIDEE);
        when(commandes.findById(12L)).thenReturn(Optional.of(c));

        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.modifier(12L, List.of(new LigneQuantiteDto(30L, 10))));
        assertEquals("RG-C5", e.getCode());
    }

    @Test
    void uneFactureNeSeRattacheQuAUneCommandeRecue() {
        Commande c = commande("120", StatutCommande.ENVOYEE);
        when(commandes.findById(12L)).thenReturn(Optional.of(c));

        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.getCommandeReceptionnee(12L));
        assertEquals("RG-D4", e.getCode());
    }

    @Test
    void lePaiementClotureLaCommande() {
        Commande c = commande("120", StatutCommande.FACTUREE);
        when(commandes.findById(12L)).thenReturn(Optional.of(c));

        service.marquerPayee(12L, "comptable");

        assertEquals(StatutCommande.CLOTUREE, c.getStatut());
        verify(workflow).changerStatut("COMMANDE", null, "FACTUREE", "PAYEE", "comptable", "COMPTABLE");
        verify(workflow).changerStatut("COMMANDE", null, "PAYEE", "CLOTUREE", "comptable", "SYSTEME");
    }
}