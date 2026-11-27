package tn.erp.fournisseur.c;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import tn.erp.fournisseur.c.dto.LigneReceptionCreationDto;
import tn.erp.fournisseur.c.dto.ReceptionCreationDto;
import tn.erp.fournisseur.c.dto.ReceptionDto;
import tn.erp.fournisseur.c.entity.*;
import tn.erp.fournisseur.c.repository.CommandeRepository;
import tn.erp.fournisseur.c.repository.ReceptionRepository;
import tn.erp.fournisseur.c.repository.ReclamationRepository;
import tn.erp.fournisseur.c.service.ReceptionService;
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
class ReceptionServiceTest {

    @Mock CommandeRepository commandes;
    @Mock ReceptionRepository receptions;
    @Mock ReclamationRepository reclamations;
    @Mock StockService stock;
    @Mock WorkflowService workflow;
    @Mock UtilisateurCourant utilisateur;
    @InjectMocks ReceptionService service;

    private Commande commande;
    private LigneCommande ligne;

    @BeforeEach
    void preparer() {
        commande = new Commande("BC-2026-0001", 7L, 1L, "achats");
        ligne = commande.ajouterLigne(1L, 50, new BigDecimal("120"));   // 50 RAM à 120 DT
        ReflectionTestUtils.setField(ligne, "id", 30L);
        commande.setStatut(StatutCommande.ENVOYEE);
    }

    private void magasinierConnecte() {
        when(utilisateur.nom()).thenReturn("magasinier");
        when(utilisateur.role()).thenReturn("MAGASINIER");
    }

    private ReceptionCreationDto livraison(int quantite, boolean conforme, String commentaire) {
        return new ReceptionCreationDto(List.of(new LigneReceptionCreationDto(30L, quantite, conforme)), commentaire);
    }

    @Test
    void uneCommandeNonEnvoyeeNePeutPasEtreReceptionnee() {
        commande.setStatut(StatutCommande.VALIDEE);
        when(commandes.findById(12L)).thenReturn(Optional.of(commande));

        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.receptionner(12L, livraison(10, true, null)));
        assertEquals("RG-C4", e.getCode());
        verifyNoInteractions(stock);
    }

    @Test
    void uneQuantiteSuperieureAuResteEstBloquee() {
        when(commandes.findById(12L)).thenReturn(Optional.of(commande));
        magasinierConnecte();

        assertThrows(RegleGestionException.class, () -> service.receptionner(12L, livraison(60, true, null)));
        verifyNoInteractions(stock);
        verify(receptions, never()).save(any());
    }

    @Test
    void uneReceptionPartielleDonnePartiellementRecueEtMetAJourLeStock() {
        when(commandes.findById(12L)).thenReturn(Optional.of(commande));
        magasinierConnecte();
        when(receptions.save(any(Reception.class))).thenAnswer(i -> i.getArgument(0));

        ReceptionDto dto = service.receptionner(12L, livraison(45, true, null));

        assertEquals("CONFORME", dto.statut());
        assertEquals(45, ligne.getQuantiteRecue());
        assertEquals(StatutCommande.PARTIELLEMENT_RECUE, commande.getStatut());
        verify(stock).entreeStock(eq(1L), eq(45), any());
        verify(workflow).changerStatut("COMMANDE", null, "ENVOYEE", "PARTIELLEMENT_RECUE", "magasinier", "MAGASINIER");
    }

    @Test
    void leResteRecuDonneUneCommandeRecue() {
        ligne.ajouterRecu(45);
        commande.setStatut(StatutCommande.PARTIELLEMENT_RECUE);
        when(commandes.findById(12L)).thenReturn(Optional.of(commande));
        magasinierConnecte();
        when(receptions.save(any(Reception.class))).thenAnswer(i -> i.getArgument(0));

        service.receptionner(12L, livraison(5, true, null));

        assertEquals(50, ligne.getQuantiteRecue());
        assertEquals(StatutCommande.RECUE, commande.getStatut());
        verify(stock).entreeStock(eq(1L), eq(5), any());
    }

    @Test
    void uneLigneNonConformeCreeUneReclamationSansToucherAuStock() {
        when(commandes.findById(12L)).thenReturn(Optional.of(commande));
        magasinierConnecte();
        when(receptions.save(any(Reception.class))).thenAnswer(i -> i.getArgument(0));
        when(reclamations.save(any(Reclamation.class))).thenAnswer(i -> i.getArgument(0));

        ReceptionDto dto = service.receptionner(12L, livraison(10, false, "10 barrettes défectueuses"));

        assertEquals("NON_CONFORME", dto.statut());
        assertEquals(0, ligne.getQuantiteRecue());
        assertEquals(StatutCommande.ENVOYEE, commande.getStatut());
        verify(stock, never()).entreeStock(anyLong(), anyInt(), any());
        verify(reclamations).save(any(Reclamation.class));
    }
}