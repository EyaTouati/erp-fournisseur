package tn.erp.fournisseur.a.fournisseur;

import tn.erp.fournisseur.a.dto.FournisseurDTO;
import tn.erp.fournisseur.a.dto.FournisseurMapper;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/fournisseurs")
public class FournisseurController {

    private final FournisseurService fournisseurService;

    public FournisseurController(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    @GetMapping
    public List<FournisseurDTO> getAllFournisseurs() {
        return fournisseurService.getAllFournisseurs()
                .stream()
                .map(FournisseurMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public FournisseurDTO getFournisseurById(@PathVariable Long id) {
        return FournisseurMapper.toDTO(
                fournisseurService.getFournisseurById(id)
        );
    }

    @PostMapping
    public FournisseurDTO createFournisseur(
            @Valid @RequestBody FournisseurDTO dto) {

        return FournisseurMapper.toDTO(
                fournisseurService.createFournisseur(
                        FournisseurMapper.toEntity(dto)
                )
        );
    }

    @PutMapping("/{id}")
    public FournisseurDTO updateFournisseur(
            @PathVariable Long id,
            @Valid @RequestBody FournisseurDTO dto) {

        return FournisseurMapper.toDTO(
                fournisseurService.updateFournisseur(
                        id,
                        FournisseurMapper.toEntity(dto)
                )
        );
    }

    @DeleteMapping("/{id}")
    public void deleteFournisseur(@PathVariable Long id) {
        fournisseurService.deleteFournisseur(id);
    }
    @PatchMapping("/{id}/desactiver")
    public FournisseurDTO desactiverFournisseur(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String motif = body.get("motif");

        Fournisseur fournisseur =
                fournisseurService.desactiverFournisseur(id, motif);

        return FournisseurMapper.toDTO(fournisseur);
    }
}
