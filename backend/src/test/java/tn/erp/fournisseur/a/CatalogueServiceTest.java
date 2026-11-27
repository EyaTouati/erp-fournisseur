package tn.erp.fournisseur.a;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import tn.erp.fournisseur.a.dto.CatalogueCreationDto;
import tn.erp.fournisseur.a.dto.PrixDto;
import tn.erp.fournisseur.a.entity.Catalogue;
import tn.erp.fournisseur.a.entity.Fournisseur;
import tn.erp.fournisseur.a.entity.HistoriquePrix;
import tn.erp.fournisseur.a.repository.CatalogueRepository;
import tn.erp.fournisseur.a.repository.FournisseurRepository;
import tn.erp.fournisseur.a.repository.HistoriquePrixRepository;
import tn.erp.fournisseur.a.service.CatalogueService;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;
import tn.erp.fournisseur.commun.integration.ProduitRepository;
import tn.erp.fournisseur.commun.securite.UtilisateurCourant;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogueServiceTest {

    @Mock CatalogueRepository catalogues;
    @Mock HistoriquePrixRepository historiques;
    @Mock FournisseurRepository fournisseurs;
    @Mock ProduitRepository produits;
    @Mock UtilisateurCourant utilisateur;
    @InjectMocks CatalogueService service;

    private Fournisseur fournisseur() {
        Fournisseur f = new Fournisseur("FRN-0001", "Kingston", "IF1", "Tunis", "71000000", "k@x.tn", "30 jours");
        ReflectionTestUtils.setField(f, "id", 1L);
        return f;
    }

    @Test
    void produitDejaAuCatalogueEstRefuse() {
        when(fournisseurs.findById(1L)).thenReturn(Optional.of(fournisseur()));
        when(catalogues.existsByFournisseurIdAndProduitId(1L, 5L)).thenReturn(true);

        RegleGestionException e = assertThrows(RegleGestionException.class,
                () -> service.ajouter(1L, new CatalogueCreationDto(5L, new BigDecimal("120"), 5)));

        assertEquals("RG-A7", e.getCode());
        verify(catalogues, never()).save(any());
    }

    @Test
    void modifierLePrixHistoriseLAncienPrix() {
        Catalogue c = new Catalogue(fournisseur(), 5L, new BigDecimal("120.000"), 5);
        when(catalogues.findById(1L)).thenReturn(Optional.of(c));
        when(utilisateur.nom()).thenReturn("achats");

        service.modifierPrix(1L, new PrixDto(new BigDecimal("130.000"), 5));

        ArgumentCaptor<HistoriquePrix> captor = ArgumentCaptor.forClass(HistoriquePrix.class);
        verify(historiques).save(captor.capture());
        assertEquals(0, new BigDecimal("120.000").compareTo(captor.getValue().getAncienPrix()));
        assertEquals(0, new BigDecimal("130.000").compareTo(captor.getValue().getNouveauPrix()));
        assertEquals("achats", captor.getValue().getModifiePar());
        assertEquals(0, new BigDecimal("130.000").compareTo(c.getPrixUnitaire()));
    }

    @Test
    void memePrixNeCreePasDHistorique() {
        Catalogue c = new Catalogue(fournisseur(), 5L, new BigDecimal("120.000"), 5);
        when(catalogues.findById(1L)).thenReturn(Optional.of(c));

        service.modifierPrix(1L, new PrixDto(new BigDecimal("120"), 3));

        verify(historiques, never()).save(any());
        assertEquals(3, c.getDelaiJours());
    }
}
