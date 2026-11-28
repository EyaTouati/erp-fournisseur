package tn.erp.fournisseur.a;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.erp.fournisseur.a.repository.EvaluationRepository;
import tn.erp.fournisseur.a.repository.FournisseurRepository;
import tn.erp.fournisseur.a.service.EvaluationService;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;
import tn.erp.fournisseur.commun.securite.UtilisateurCourant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceTest {

    @Mock EvaluationRepository evaluations;
    @Mock FournisseurRepository fournisseurs;
    @Mock UtilisateurCourant utilisateur;
    @InjectMocks EvaluationService service;

    @Test
    void uneNoteHorsDe1a5EstRefusee() {
        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.enregistrer(1L, 10L, 6, 3, 3, null, "achats"));
        assertEquals("RG-A4", e.getCode());
        verify(evaluations, never()).save(any());
    }

    @Test
    void uneNoteDeZeroEstRefusee() {
        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.enregistrer(1L, 10L, 4, 0, 3, null, "achats"));
        assertEquals("RG-A4", e.getCode());
    }
}
