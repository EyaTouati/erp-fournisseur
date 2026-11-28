package tn.erp.fournisseur.b;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.erp.fournisseur.b.dto.OffreCreationDto;
import tn.erp.fournisseur.b.entity.*;
import tn.erp.fournisseur.b.repository.DemandePrixRepository;
import tn.erp.fournisseur.b.repository.OffreRepository;
import tn.erp.fournisseur.b.service.OffreService;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;
import tn.erp.fournisseur.commun.securite.UtilisateurCourant;
import tn.erp.fournisseur.commun.workflow.WorkflowService;

@ExtendWith(MockitoExtension.class)
class OffreServiceTest {

    @Mock OffreRepository offres;
    @Mock DemandePrixRepository demandesPrix;
    @Mock WorkflowService workflow;
    @Mock UtilisateurCourant utilisateur;
    @InjectMocks OffreService service;

    private DemandePrix demandePrix(StatutDemandePrix statut, Long... fournisseurs) {
        DemandeAchat da = new DemandeAchat("DA-2026-0001", 1L, 10, null, "magasinier");
        DemandePrix dp = new DemandePrix("DP-2026-0001", da, LocalDate.now().plusDays(5),
                new HashSet<>(Arrays.asList(fournisseurs)));
        dp.setStatut(statut);
        return dp;
    }

    private Offre offre(DemandePrix dp, long fournisseur, String prix, LocalDate validite) {
        return new Offre(dp, fournisseur, new BigDecimal(prix), 10, 7, null, validite);
    }

    private OffreCreationDto creation(Long fournisseurId) {
        return new OffreCreationDto(fournisseurId, new BigDecimal("100"), 10, 7, null,
                LocalDate.now().plusDays(10));
    }

    @Test
    void offreDUnFournisseurNonConsulteEstRefusee() {
        DemandePrix dp = demandePrix(StatutDemandePrix.ENVOYEE, 1L, 2L);
        when(demandesPrix.findById(5L)).thenReturn(Optional.of(dp));
        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.saisir(5L, creation(9L)));
        assertEquals("B-FOURNISSEUR", e.getCode());
    }

    @Test
    void premiereOffrePasseLaDemandeEnOffresRecues() {
        DemandePrix dp = demandePrix(StatutDemandePrix.ENVOYEE, 1L, 2L);
        when(demandesPrix.findById(5L)).thenReturn(Optional.of(dp));
        when(offres.save(any(Offre.class))).thenAnswer(i -> i.getArgument(0));

        service.saisir(5L, creation(1L));

        assertEquals(StatutDemandePrix.OFFRES_RECUES, dp.getStatut());
        verify(workflow).changerStatut(eq("DEMANDE_PRIX"), any(), eq("ENVOYEE"),
                eq("OFFRES_RECUES"), any(), any());
    }

    @Test
    void offreExpireeNePeutPasEtreRetenue() {
        DemandePrix dp = demandePrix(StatutDemandePrix.OFFRES_RECUES, 1L, 2L);
        Offre o = offre(dp, 1L, "100", LocalDate.now().minusDays(1));
        when(offres.findById(1L)).thenReturn(Optional.of(o));
        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.retenir(1L, null));
        assertEquals("RG-B4", e.getCode());
    }

    @Test
    void deuxiemeOffreRetenueEstRefusee() {
        DemandePrix dp = demandePrix(StatutDemandePrix.OFFRES_RECUES, 1L, 2L);
        Offre deja = offre(dp, 1L, "100", LocalDate.now().plusDays(5));
        deja.setStatut(StatutOffre.RETENUE);
        Offre autre = offre(dp, 2L, "120", LocalDate.now().plusDays(5));
        when(offres.findById(2L)).thenReturn(Optional.of(autre));
        when(offres.findByDemandePrixId(any())).thenReturn(List.of(deja, autre));
        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.retenir(2L, "raison"));
        assertEquals("RG-B2", e.getCode());
    }

    @Test
    void offrePasLaMoinsChereSansJustificationEstRefusee() {
        DemandePrix dp = demandePrix(StatutDemandePrix.OFFRES_RECUES, 1L, 2L);
        Offre moinsChere = offre(dp, 1L, "100", LocalDate.now().plusDays(5));
        Offre plusChere = offre(dp, 2L, "120", LocalDate.now().plusDays(5));
        when(offres.findById(2L)).thenReturn(Optional.of(plusChere));
        when(offres.findByDemandePrixId(any())).thenReturn(List.of(moinsChere, plusChere));
        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.retenir(2L, null));
        assertEquals("RG-B3", e.getCode());
    }

    @Test
    void retenirRejetteLesAutresEtClotureLaDemande() {
        DemandePrix dp = demandePrix(StatutDemandePrix.OFFRES_RECUES, 1L, 2L);
        Offre moinsChere = offre(dp, 1L, "100", LocalDate.now().plusDays(5));
        Offre plusChere = offre(dp, 2L, "120", LocalDate.now().plusDays(5));
        when(offres.findById(1L)).thenReturn(Optional.of(moinsChere));
        when(offres.findByDemandePrixId(any())).thenReturn(List.of(moinsChere, plusChere));

        service.retenir(1L, null);

        assertEquals(StatutOffre.RETENUE, moinsChere.getStatut());
        assertEquals(StatutOffre.REJETEE, plusChere.getStatut());
        assertEquals(StatutDemandePrix.CLOTUREE, dp.getStatut());
    }

    @Test
    void getOffreRetenueRefuseUneOffreNonRetenue() {
        DemandePrix dp = demandePrix(StatutDemandePrix.OFFRES_RECUES, 1L, 2L);
        Offre o = offre(dp, 1L, "100", LocalDate.now().plusDays(5));
        when(offres.findById(1L)).thenReturn(Optional.of(o));
        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.getOffreRetenue(1L));
        assertEquals("RG-C1", e.getCode());
    }
}