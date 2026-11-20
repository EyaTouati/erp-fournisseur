package com.erp.erpfournisseur.catalogue;

import com.erp.erpfournisseur.dto.CatalogueDTO;
import com.erp.erpfournisseur.dto.CatalogueMapper;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogues")
public class CatalogueController {

    private final CatalogueService catalogueService;

    public CatalogueController(CatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }

    @GetMapping
    public List<CatalogueDTO> getAll() {
        return catalogueService.getAll()
                .stream()
                .map(CatalogueMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public CatalogueDTO getById(@PathVariable Long id) {
        return CatalogueMapper.toDTO(catalogueService.getById(id));
    }

    @PostMapping
    public CatalogueDTO create(@Valid @RequestBody CatalogueDTO dto) {

        Catalogue catalogue = new Catalogue();
        catalogue.setPrixUnitaire(dto.getPrixUnitaire());
        catalogue.setDelaiJours(dto.getDelaiJours());

        Catalogue saved = catalogueService.create(
                catalogue,
                dto.getFournisseurId(),
                dto.getProduitId()
        );

        return CatalogueMapper.toDTO(saved);
    }

    @PutMapping("/{id}")
    public CatalogueDTO update(
            @PathVariable Long id,
            @Valid @RequestBody CatalogueDTO dto) {

        Catalogue catalogue = new Catalogue();
        catalogue.setPrixUnitaire(dto.getPrixUnitaire());
        catalogue.setDelaiJours(dto.getDelaiJours());

        Catalogue updated = catalogueService.update(
                id,
                catalogue,
                dto.getFournisseurId(),
                dto.getProduitId()
        );

        return CatalogueMapper.toDTO(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        catalogueService.delete(id);
    }
}