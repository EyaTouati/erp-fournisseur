package tn.erp.fournisseur.a.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluation")
public class Evaluation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    // la table commande est dans C : simple colonne, sans FK
    @Column(name = "commande_id")
    private Long commandeId;

    @Column(name = "note_delai", nullable = false)
    private Short noteDelai;

    @Column(name = "note_qualite", nullable = false)
    private Short noteQualite;

    @Column(name = "note_prix", nullable = false)
    private Short notePrix;

    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;

    @Column(name = "date_evaluation")
    private LocalDateTime dateEvaluation;

    @Column(name = "evaluateur", length = 50)
    private String evaluateur;

    protected Evaluation() { }

    public Evaluation(Fournisseur fournisseur, Long commandeId, Short noteDelai, Short noteQualite,
                      Short notePrix, String commentaire, String evaluateur) {
        this.fournisseur = fournisseur;
        this.commandeId = commandeId;
        this.noteDelai = noteDelai;
        this.noteQualite = noteQualite;
        this.notePrix = notePrix;
        this.commentaire = commentaire;
        this.evaluateur = evaluateur;
        this.dateEvaluation = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Fournisseur getFournisseur() { return fournisseur; }
    public Long getCommandeId() { return commandeId; }
    public Short getNoteDelai() { return noteDelai; }
    public Short getNoteQualite() { return noteQualite; }
    public Short getNotePrix() { return notePrix; }
    public String getCommentaire() { return commentaire; }
    public LocalDateTime getDateEvaluation() { return dateEvaluation; }
    public String getEvaluateur() { return evaluateur; }
}
