package tn.erp.fournisseur.commun.erreurs;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Transforme les exceptions métier en réponses JSON pour le frontend React.
 * Les erreurs de connexion et de rôle (401 / 403) restent gérées par Spring Security.
 */
@RestControllerAdvice
public class GestionnaireErreurs {

    @ExceptionHandler(RegleGestionException.class)
    public ResponseEntity<ErreurDto> regleGestion(RegleGestionException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErreurDto(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(TransitionInterditeException.class)
    public ResponseEntity<ErreurDto> transitionInterdite(TransitionInterditeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErreurDto("TRANSITION", e.getMessage()));
    }

    @ExceptionHandler(IntrouvableException.class)
    public ResponseEntity<ErreurDto> introuvable(IntrouvableException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErreurDto("INTROUVABLE", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErreurDto> validation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + " : " + f.getDefaultMessage())
                .collect(Collectors.joining(" ; "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErreurDto("VALIDATION", message));
    }
}
