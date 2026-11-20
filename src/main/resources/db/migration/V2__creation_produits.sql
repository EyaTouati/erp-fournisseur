
CREATE TABLE produit (
                         id BIGSERIAL PRIMARY KEY,
                         reference VARCHAR(30) NOT NULL UNIQUE,
                         libelle VARCHAR(150) NOT NULL,
                         unite VARCHAR(20) DEFAULT 'unité',
                         stock_actuel INTEGER NOT NULL DEFAULT 0
                             CHECK (stock_actuel >= 0),
                         seuil_reappro INTEGER NOT NULL DEFAULT 0
                             CHECK (seuil_reappro >= 0)
);
