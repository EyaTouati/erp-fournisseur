package tn.erp.fournisseur.commun.workflow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.erp.fournisseur.commun.erreurs.IntrouvableException;
import tn.erp.fournisseur.commun.erreurs.TransitionInterditeException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests du moteur de workflow, sans base de données (Mockito simule les repositories).
 */
@ExtendWith(MockitoExtension.class)
class WorkflowServiceTest {

    @Mock
    private TransitionStatutRepository transitionRepository;
    @Mock
    private HistoriqueStatutRepository historiqueRepository;
    @Mock
    private ParametreRepository parametreRepository;

    @InjectMocks
    private WorkflowService workflow;

    @Test
    void uneTransitionAutoriseeEstAccepteeEtHistorisee() {
        when(transitionRepository.findByTypeDocumentAndStatutDepartAndStatutArrivee(
                "COMMANDE", "EN_ATTENTE_VALIDATION", "VALIDEE"))
                .thenReturn(List.of(new TransitionStatut("COMMANDE", "EN_ATTENTE_VALIDATION", "VALIDEE", "DIRECTEUR")));
        when(historiqueRepository.save(any(HistoriqueStatut.class))).thenAnswer(i -> i.getArgument(0));

        HistoriqueStatut h = workflow.changerStatut("COMMANDE", 12L, "EN_ATTENTE_VALIDATION", "VALIDEE",
                "directeur", "DIRECTEUR");

        assertEquals("VALIDEE", h.getNouveauStatut());
        assertEquals("EN_ATTENTE_VALIDATION", h.getAncienStatut());
        assertEquals("directeur", h.getUtilisateur());
        assertEquals(12L, h.getDocumentId());
        assertNotNull(h.getDateChangement());
        verify(historiqueRepository).save(any(HistoriqueStatut.class));
    }

    @Test
    void uneTransitionInexistanteEstRefusee() {
        when(transitionRepository.findByTypeDocumentAndStatutDepartAndStatutArrivee("COMMANDE", "BROUILLON", "PAYEE"))
                .thenReturn(List.of());

        assertThrows(TransitionInterditeException.class,
                () -> workflow.changerStatut("COMMANDE", 12L, "BROUILLON", "PAYEE", "achats", "ACHATS"));
        verify(historiqueRepository, never()).save(any());
    }

    @Test
    void unRoleNonAutoriseEstRefuse() {
        when(transitionRepository.findByTypeDocumentAndStatutDepartAndStatutArrivee(
                "COMMANDE", "EN_ATTENTE_VALIDATION", "VALIDEE"))
                .thenReturn(List.of(new TransitionStatut("COMMANDE", "EN_ATTENTE_VALIDATION", "VALIDEE", "DIRECTEUR")));

        assertThrows(TransitionInterditeException.class,
                () -> workflow.changerStatut("COMMANDE", 12L, "EN_ATTENTE_VALIDATION", "VALIDEE", "achats", "ACHATS"));
        verify(historiqueRepository, never()).save(any());
    }

    @Test
    void laCreationEstHistoriseeSansAncienStatut() {
        when(historiqueRepository.save(any(HistoriqueStatut.class))).thenAnswer(i -> i.getArgument(0));

        HistoriqueStatut h = workflow.enregistrerCreation("COMMANDE", 12L, "BROUILLON", "achats");

        assertNull(h.getAncienStatut());
        assertEquals("BROUILLON", h.getNouveauStatut());
    }

    @Test
    void leSeuilDeValidationEstLuDansLesParametres() {
        when(parametreRepository.findById("seuil_validation"))
                .thenReturn(Optional.of(new Parametre("seuil_validation", "5000", "Seuil RG-C2")));

        assertEquals(new BigDecimal("5000"), workflow.parametreDecimal("seuil_validation"));
    }

    @Test
    void unParametreAbsentLanceUneErreur() {
        when(parametreRepository.findById("inconnu")).thenReturn(Optional.empty());

        assertThrows(IntrouvableException.class, () -> workflow.parametre("inconnu"));
    }
}
