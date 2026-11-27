package tn.erp.fournisseur.commun.erreurs;

/** Changement de statut non autorisé par le moteur de workflow. Renvoyé en HTTP 409. */
public class TransitionInterditeException extends RuntimeException {

    public TransitionInterditeException(String message) {
        super(message);
    }
}
