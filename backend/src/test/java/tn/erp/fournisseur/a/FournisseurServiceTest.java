package tn.erp.fournisseur.a;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import tn.erp.fournisseur.a.dto.FournisseurCreationDto;
import tn.erp.fournisseur.a.dto.FournisseurDto;
import tn.erp.fournisseur.a.entity.Fournisseur;
import tn.erp.fournisseur.a.repository.FournisseurRepository;
import tn.erp.fournisseur.a.service.EvaluationService;
import tn.erp.fournisseur.a.service.FournisseurService;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;
import tn.erp.fournisseur.commun.securite.UtilisateurCourant;
import tn.erp.fournisseur.commun.workflow.WorkflowService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FournisseurServiceTest {

    @Mock FournisseurRepository fournisseurs;
    @Mock EvaluationService evaluations;
    @Mock WorkflowService workflow;
    @Mock UtilisateurCourant utilisateur;
    @InjectMocks FournisseurService service;

    private Fournisseur fournisseur(Long id, String nom) {
        Fournisseur f = new Fournisseur("FRN-000" + id, nom, "IF" + id, "Tunis", "71000000", "a@b.tn", "30 jours");
        ReflectionTestUtils.setField(f, "id", id);
        return f;
    }

    private FournisseurCreationDto dto(String identifiantFiscal) {
        return new FournisseurCreationDto("Dell", identifiantFiscal, "Tunis", "71000000", "dell@x.tn", "30 jours");
    }

    @Test
    void identifiantFiscalEnDoubleEstRefuse() {
        when(fournisseurs.existsByIdentifiantFiscal("123")).thenReturn(true);

        RegleGestionException e = assertThrows(RegleGestionException.class, () -> service.creer(dto("123")));

        assertEquals("RG-A1", e.getCode());
        verify(fournisseurs, never()).save(any());
    }

    @Test
    void creationEnregistreLeStatutActifDansLHistorique() {
        when(fournisseurs.save(any(Fournisseur.class))).thenAnswer(inv -> {
            Fournisseur f = inv.getArgument(0);
            ReflectionTestUtils.setField(f, "id", 5L);
            return f;
        });
        when(utilisateur.nom()).thenReturn("achats");

        FournisseurDto cree = service.creer(dto("123"));

        assertEquals("ACTIF", cree.statut());
        assertEquals("FRN-0001", cree.code());
        verify(workflow).enregistrerCreation("FOURNISSEUR", 5L, "ACTIF", "achats");
    }

    @Test
    void desactiverSansMotifEstRefuse() {
        when(fournisseurs.findById(1L)).thenReturn(Optional.of(fournisseur(1L, "Kingston")));

        RegleGestionException e = assertThrows(RegleGestionException.class, () -> service.desactiver(1L, "  "));

        assertEquals("RG-A5", e.getCode());
        verify(workflow, never()).changerStatut(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void desactiverAvecCommandesEnCoursEstRefuse() {
        when(fournisseurs.findById(1L)).thenReturn(Optional.of(fournisseur(1L, "Kingston")));
        when(fournisseurs.compterCommandesNonCloturees(1L)).thenReturn(2L);

        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.desactiver(1L, "Retards répétés"));

        assertEquals("RG-A6", e.getCode());
        verify(workflow, never()).changerStatut(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void desactiverSansCommandeEnCoursPasseParLeWorkflow() {
        when(fournisseurs.findById(1L)).thenReturn(Optional.of(fournisseur(1L, "Kingston")));
        when(fournisseurs.compterCommandesNonCloturees(1L)).thenReturn(0L);
        when(utilisateur.nom()).thenReturn("achats");
        when(utilisateur.role()).thenReturn("ACHATS");

        FournisseurDto dto = service.desactiver(1L, "Retards répétés");

        assertEquals("INACTIF", dto.statut());
        verify(workflow).changerStatut("FOURNISSEUR", 1L, "ACTIF", "INACTIF", "achats", "ACHATS", "Retards répétés");
    }

    @Test
    void unFournisseurInactifNePeutPasEtreModifie() {
        Fournisseur f = fournisseur(1L, "Kingston");
        f.desactiver("Fermé");
        when(fournisseurs.findById(1L)).thenReturn(Optional.of(f));

        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.modifier(1L, dto("IF1")));

        assertEquals("RG-A3", e.getCode());
    }

    @Test
    void fournisseursActifsPourProduitIgnoreLesInactifs() {
        Fournisseur actif = fournisseur(1L, "Kingston");
        Fournisseur inactif = fournisseur(2L, "Fournisseur X");
        inactif.desactiver("Fermé");
        when(fournisseurs.findFournisseursByProduitId(10L)).thenReturn(List.of(actif, inactif));

        List<Fournisseur> resultat = service.fournisseursActifsPourProduit(10L);

        assertEquals(1, resultat.size());
        assertEquals("Kingston", resultat.get(0).getNom());
    }
}
