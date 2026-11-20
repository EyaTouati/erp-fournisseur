package com.erp.erpfournisseur.historique;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historique-prix")
public class HistoriquePrixController {

    private final HistoriquePrixRepository historiquePrixRepository;

    public HistoriquePrixController(
            HistoriquePrixRepository historiquePrixRepository) {
        this.historiquePrixRepository = historiquePrixRepository;
    }

    @GetMapping("/catalogue/{catalogueId}")
    public List<HistoriquePrix> getByCatalogueId(
            @PathVariable Long catalogueId) {

        return historiquePrixRepository.findByCatalogueId(catalogueId);
    }
}