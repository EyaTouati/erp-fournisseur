-- V4 : sous-processus C (membre 3)
CREATE TABLE commande (
    id              BIGSERIAL PRIMARY KEY,
    numero          VARCHAR(20) NOT NULL UNIQUE,
    offre_id        BIGINT NOT NULL UNIQUE REFERENCES offre(id),
    fournisseur_id  BIGINT NOT NULL REFERENCES fournisseur(id),
    montant_total   NUMERIC(12,3) NOT NULL CHECK (montant_total > 0),
    statut          VARCHAR(30) NOT NULL DEFAULT 'BROUILLON',
    cree_par        VARCHAR(50) NOT NULL,
    valide_par      VARCHAR(50) CHECK (valide_par IS NULL OR valide_par <> cree_par),
    motif_refus     VARCHAR(255),
    date_creation   TIMESTAMP,
    date_validation TIMESTAMP,
    date_envoi      TIMESTAMP
);

CREATE TABLE ligne_commande (
    id             BIGSERIAL PRIMARY KEY,
    commande_id    BIGINT  NOT NULL REFERENCES commande(id),
    produit_id     BIGINT  NOT NULL REFERENCES produit(id),
    quantite       INTEGER NOT NULL CHECK (quantite > 0),
    prix_unitaire  NUMERIC(12,3) NOT NULL,
    quantite_recue INTEGER NOT NULL DEFAULT 0,
    CHECK (quantite_recue <= quantite)
);

CREATE TABLE reception (
    id              BIGSERIAL PRIMARY KEY,
    numero          VARCHAR(20) NOT NULL UNIQUE,
    commande_id     BIGINT NOT NULL REFERENCES commande(id),
    statut          VARCHAR(30) NOT NULL DEFAULT 'EN_COURS',
    receptionne_par VARCHAR(50) NOT NULL,
    date_reception  TIMESTAMP DEFAULT now(),
    commentaire     TEXT
);

CREATE TABLE ligne_reception (
    id                BIGSERIAL PRIMARY KEY,
    reception_id      BIGINT  NOT NULL REFERENCES reception(id),
    ligne_commande_id BIGINT  NOT NULL REFERENCES ligne_commande(id),
    quantite_recue    INTEGER NOT NULL CHECK (quantite_recue >= 0),
    conforme          BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE reclamation (
    id               BIGSERIAL PRIMARY KEY,
    reception_id     BIGINT NOT NULL REFERENCES reception(id),
    motif            VARCHAR(255) NOT NULL,
    statut           VARCHAR(30)  NOT NULL DEFAULT 'OUVERTE',
    date_reclamation TIMESTAMP DEFAULT now()
);