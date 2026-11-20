CREATE TABLE catalogue (
                           id BIGSERIAL PRIMARY KEY,

                           fournisseur_id BIGINT NOT NULL
                               REFERENCES fournisseur(id),

                           produit_id BIGINT NOT NULL
                               REFERENCES produit(id),

                           prix_unitaire NUMERIC(12,3) NOT NULL
                               CHECK (prix_unitaire > 0),

                           delai_jours INTEGER NOT NULL
                               CHECK (delai_jours >= 0),

                           CONSTRAINT unique_fournisseur_produit
                               UNIQUE (fournisseur_id, produit_id)
);