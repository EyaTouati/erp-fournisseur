-- V5 : sous-processus D (facturation et paiement)

CREATE TABLE facture (
                         id BIGSERIAL PRIMARY KEY,
                         fournisseur_id BIGINT NOT NULL REFERENCES fournisseur(id),
                         commande_id BIGINT NOT NULL REFERENCES commande(id),
                         numero VARCHAR(50) NOT NULL,
                         date_facture DATE NOT NULL,
                         montant NUMERIC(12,3) NOT NULL CHECK (montant > 0),
                         statut VARCHAR(30) NOT NULL DEFAULT 'RECUE',
                         date_enregistrement TIMESTAMP DEFAULT now(),
                         CONSTRAINT uq_facture_fournisseur_numero UNIQUE (fournisseur_id, numero)
);

CREATE TABLE ligne_facture (
                               id BIGSERIAL PRIMARY KEY,
                               facture_id BIGINT NOT NULL REFERENCES facture(id),
                               produit_id BIGINT NOT NULL REFERENCES produit(id),
                               quantite INTEGER NOT NULL CHECK (quantite > 0),
                               prix_unitaire NUMERIC(12,3) NOT NULL
);

CREATE TABLE paiement (
                          id BIGSERIAL PRIMARY KEY,
                          facture_id BIGINT NOT NULL REFERENCES facture(id),
                          montant NUMERIC(12,3) NOT NULL CHECK (montant > 0),
                          date_paiement DATE NOT NULL,
                          mode VARCHAR(20) CHECK (mode IN ('VIREMENT','CHEQUE','ESPECES')),
                          reference VARCHAR(50),
                          paye_par VARCHAR(50) NOT NULL
);

CREATE TABLE litige (
                        id BIGSERIAL PRIMARY KEY,
                        facture_id BIGINT NOT NULL REFERENCES facture(id),
                        motif VARCHAR(255) NOT NULL,
                        statut VARCHAR(30) NOT NULL DEFAULT 'OUVERT',
                        date_ouverture TIMESTAMP DEFAULT now(),
                        date_resolution TIMESTAMP
);