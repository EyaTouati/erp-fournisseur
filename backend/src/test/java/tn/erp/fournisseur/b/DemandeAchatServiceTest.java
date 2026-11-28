package tn.erp.fournisseur.b;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.erp.fournisseur.b.repository.DemandeAchatRepository;
import tn.erp.fournisseur.b.service.DemandeAchatService;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;
import tn.erp.fournisseur.commun.integration.ProduitRepository;
import tn.erp.fournisseur.commun.securite.UtilisateurCourant;
import tn.erp.fournisseur.commun.workflow.WorkflowService;

@ExtendWith(MockitoExtension.class)
class DemandeAchatServiceTest {

    @Mock DemandeAchatRepository demandes;
    @Mock ProduitRepository produits;
    @Mock WorkflowService workflow;
    @Mock UtilisateurCourant utilisateur;
    @InjectMocks DemandeAchatService service;

    @Test
    void demandeRefuseeSansMotifEstRefusee() {
        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.refuser(1L, "  "));
        assertEquals("B-MOTIF", e.getCode());
        verifyNoInteractions(workflow);
    }
}