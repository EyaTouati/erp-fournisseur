package tn.erp.fournisseur.commun.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.erp.fournisseur.commun.erreurs.IntrouvableException;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    ProduitRepository produits;

    @Mock
    MouvementStockRepository mouvements;

    @InjectMocks
    StockService service;

    @Test
    void uneEntreeAjouteAuStockEtTraceLeMouvement() {
        Produit ram = new Produit("RAM-16G", "Barrette RAM 16 Go", 5, 10);
        when(produits.findById(1L)).thenReturn(Optional.of(ram));

        service.entreeStock(1L, 45, 7L);

        assertEquals(50, ram.getStockActuel());
        ArgumentCaptor<MouvementStock> captor = ArgumentCaptor.forClass(MouvementStock.class);
        verify(mouvements).save(captor.capture());
        assertEquals(45, captor.getValue().getQuantite());
        assertEquals("RECEPTION", captor.getValue().getSource());
        assertEquals(7L, captor.getValue().getSourceId());
    }

    @Test
    void uneQuantiteNulleOuNegativeEstRefusee() {
        assertThrows(RegleGestionException.class, () -> service.entreeStock(1L, 0, 7L));
        assertThrows(RegleGestionException.class, () -> service.entreeStock(1L, -3, 7L));
        verifyNoInteractions(mouvements);
    }

    @Test
    void unProduitInconnuEstSignale() {
        when(produits.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IntrouvableException.class, () -> service.entreeStock(99L, 5, 7L));
        verifyNoInteractions(mouvements);
    }

    @Test
    void unProduitSousLeSeuilEstEnAlerte() {
        Produit ram = new Produit("RAM-16G", "Barrette RAM 16 Go", 5, 10);
        Produit ssd = new Produit("SSD-512", "Disque SSD 512 Go", 20, 5);
        assertTrue(ram.estEnAlerte());
        assertFalse(ssd.estEnAlerte());

        when(produits.produitsEnAlerte()).thenReturn(List.of(ram));
        assertEquals(List.of(ram), service.produitsEnAlerte());
    }
}