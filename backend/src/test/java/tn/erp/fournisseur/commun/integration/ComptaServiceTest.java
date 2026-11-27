package tn.erp.fournisseur.commun.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComptaServiceTest {

    @Mock
    EcritureComptableRepository ecritures;

    @InjectMocks
    ComptaService service;

    @Test
    void unPaiementCreeUneEcritureComptable() {
        when(ecritures.save(any(EcritureComptable.class))).thenAnswer(i -> i.getArgument(0));

        EcritureComptable e = service.envoyer("PAIEMENT", 3L, new BigDecimal("6000.000"));

        assertEquals("PAIEMENT", e.getType());
        assertEquals(3L, e.getReferenceId());
        assertEquals(0, new BigDecimal("6000").compareTo(e.getMontant()));
        verify(ecritures).save(any(EcritureComptable.class));
    }

    @Test
    void unMontantNulOuNegatifEstRefuse() {
        assertThrows(RegleGestionException.class, () -> service.envoyer("PAIEMENT", 3L, BigDecimal.ZERO));
        assertThrows(RegleGestionException.class, () -> service.envoyer("PAIEMENT", 3L, null));
        verifyNoInteractions(ecritures);
    }
}