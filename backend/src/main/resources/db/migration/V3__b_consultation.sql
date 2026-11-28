-- V3 : sous-processus B (membre 2)
CREATE TABLE demande_achat (
    id             BIGSERIAL PRIMARY KEY,
    numero         VARCHAR(20) NOT NULL UNIQUE,
    produit_id     BIGINT NOT NULL REFERENCES produit(id),
    quantite       INTEGER NOT NULL CHECK (quantite > 0),
    date_souhaitee DATE,
    statut         VARCHAR(30) NOT NULL DEFAULT 'BROUILLON',
    motif_refus    VARCHAR(255),
    cree_par       VARCHAR(50) NOT NULL,
    date_creation  TIMESTAMP DEFAULT now()
);

CREATE TABLE demande_prix (
    id               BIGSERIAL PRIMARY KEY,
    numero           VARCHAR(20) NOT NULL UNIQUE,
    demande_achat_id BIGINT NOT NULL REFERENCES demande_achat(id),
    date_limite      DATE NOT NULL,
    statut           VARCHAR(30) NOT NULL DEFAULT 'BROUILLON',
    date_envoi       TIMESTAMP
);

CREATE TABLE demande_prix_fournisseur (
    demande_prix_id BIGINT REFERENCES demande_prix(id),
    fournisseur_id  BIGINT REFERENCES fournisseur(id),
    PRIMARY KEY (demande_prix_id, fournisseur_id)
);

CREATE TABLE offre (
    id               BIGSERIAL PRIMARY KEY,
    demande_prix_id  BIGINT NOT NULL REFERENCES demande_prix(id),
    fournisseur_id   BIGINT NOT NULL REFERENCES fournisseur(id),
    prix_unitaire    NUMERIC(12,3) NOT NULL CHECK (prix_unitaire > 0),
    quantite         INTEGER NOT NULL CHECK (quantite > 0),
    delai_jours      INTEGER NOT NULL,
    conditions       VARCHAR(255),
    date_validite    DATE NOT NULL,
    statut           VARCHAR(30) NOT NULL DEFAULT 'RECUE',
    justification    TEXT,
    date_reception   TIMESTAMP DEFAULT now()
);

-- RG-B2 : une seule offre RETENUE par demande de prix
CREATE UNIQUE INDEX ux_offre_retenue ON offre(demande_prix_id) WHERE statut = 'RETENUE';