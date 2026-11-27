package tn.erp.fournisseur.commun.erreurs;

/**
 * Violation d'une règle de gestion (RG-A1 ... RG-D4).
 * Renvoyée au frontend en HTTP 400 : {"code": "RG-C3", "message": "..."}.
 * Exemple : throw new RegleGestionException("RG-C3", "Le créateur ne peut pas valider sa commande");
 */
public class RegleGestionException extends RuntimeException {

    private final String code;

    public RegleGestionException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
