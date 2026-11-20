
package com.erp.erpfournisseur.fournisseur;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FournisseurService {

    private final FournisseurRepository fournisseurRepository;

    public FournisseurService(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
    }

    // Get all suppliers
    public List<Fournisseur> getAllFournisseurs() {
        return fournisseurRepository.findAll();
    }

    // Get supplier by ID
    public Fournisseur getFournisseurById(Long id) {
        return fournisseurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fournisseur not found"
                ));
    }

    // Create supplier
    public Fournisseur createFournisseur(Fournisseur fournisseur) {

        if (fournisseur.getIdentifiantFiscal() != null
                && fournisseurRepository.existsByIdentifiantFiscal(
                fournisseur.getIdentifiantFiscal())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cet identifiant fiscal existe déjà"
            );
        }

        if (fournisseur.getNom() != null
                && fournisseurRepository.existsByNomIgnoreCase(
                fournisseur.getNom())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce fournisseur existe déjà"
            );
        }

        return fournisseurRepository.save(fournisseur);
    }

    // Update supplier
    public Fournisseur updateFournisseur(Long id, Fournisseur fournisseur) {

        Fournisseur existing = getFournisseurById(id);

        if (fournisseur.getIdentifiantFiscal() != null
                && fournisseurRepository.existsByIdentifiantFiscal(
                fournisseur.getIdentifiantFiscal())
                && !fournisseur.getIdentifiantFiscal().equals(
                existing.getIdentifiantFiscal())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cet identifiant fiscal existe déjà"
            );
        }

        if (fournisseur.getNom() != null
                && fournisseurRepository.existsByNomIgnoreCase(
                fournisseur.getNom())
                && !fournisseur.getNom().equalsIgnoreCase(existing.getNom())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce fournisseur existe déjà"
            );
        }

        existing.setNom(fournisseur.getNom());
        existing.setIdentifiantFiscal(fournisseur.getIdentifiantFiscal());
        existing.setEmail(fournisseur.getEmail());
        existing.setTelephone(fournisseur.getTelephone());
        existing.setAdresse(fournisseur.getAdresse());
        existing.setActif(fournisseur.getActif());

        return fournisseurRepository.save(existing);
    }
    public Fournisseur desactiverFournisseur(Long id, String motif) {

        Fournisseur fournisseur = getFournisseurById(id);

        if (motif == null || motif.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le motif de désactivation est obligatoire"
            );
        }

        fournisseur.setActif(false);
        fournisseur.setMotifDesactivation(motif);

        return fournisseurRepository.save(fournisseur);
    }
    // Delete supplier
    public void deleteFournisseur(Long id) {
        fournisseurRepository.deleteById(id);
    }
}
