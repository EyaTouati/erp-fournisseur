package tn.erp.fournisseur.commun.erreurs;

/** Format JSON commun des erreurs (dossier de référence, section 12.1). */
public record ErreurDto(String code, String message) {
}
