-- V2 : sous-processus A (membre 1)
CREATE TABLE fournisseur (
    id                  BIGSERIAL PRIMARY KEY,
    code                VARCHAR(20)  NOT NULL UNIQUE,
    nom                 VARCHAR(150) NOT NULL,
    identifiant_fiscal  VARCHAR(30)  NOT NULL UNIQUE,
    adresse             VARCHAR(255),
    telephone           VARCHAR(30),
    email               VARCHAR(150),
    conditions_paiement VARCHAR(100),
    statut              VARCHAR(30)  NOT NULL DEFAULT 'ACTIF'
                        CHECK (statut IN ('ACTIF', 'INACTIF')),
    motif_desactivation VARCHAR(255),
    date_creation       TIMESTAMP DEFAULT now(),
    CHECK (statut = 'ACTIF' OR motif_desactivation IS NOT NULL)
);

CREATE TABLE catalogue (
    id             BIGSERIAL PRIMARY KEY,
    fournisseur_id BIGINT NOT NULL REFERENCES fournisseur(id),
    produit_id     BIGINT NOT NULL REFERENCES produit(id),
    prix_unitaire  NUMERIC(12,3) NOT NULL CHECK (prix_unitaire > 0),
    delai_jours    INTEGER NOT NULL CHECK (delai_jours >= 0),
    UNIQUE (fournisseur_id, produit_id)
);

CREATE TABLE historique_prix (
    id                BIGSERIAL PRIMARY KEY,
    catalogue_id      BIGINT NOT NULL REFERENCES catalogue(id),
    ancien_prix       NUMERIC(12,3) NOT NULL,
    nouveau_prix      NUMERIC(12,3) NOT NULL,
    date_modification TIMESTAMP DEFAULT now(),
    modifie_par       VARCHAR(50)
);

CREATE TABLE evaluation (
    id              BIGSERIAL PRIMARY KEY,
    fournisseur_id  BIGINT NOT NULL REFERENCES fournisseur(id),
    commande_id     BIGINT,
    note_delai      SMALLINT NOT NULL CHECK (note_delai   BETWEEN 1 AND 5),
    note_qualite    SMALLINT NOT NULL CHECK (note_qualite BETWEEN 1 AND 5),
    note_prix       SMALLINT NOT NULL CHECK (note_prix    BETWEEN 1 AND 5),
    commentaire     TEXT,
    date_evaluation TIMESTAMP DEFAULT now(),
    evaluateur      VARCHAR(50)
);