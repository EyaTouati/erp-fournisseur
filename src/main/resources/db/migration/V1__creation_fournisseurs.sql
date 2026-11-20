CREATE TABLE fournisseur (
                             id BIGSERIAL PRIMARY KEY,
                             nom VARCHAR(150) NOT NULL,
                             email VARCHAR(150),
                             telephone VARCHAR(30),
                             adresse TEXT,
                             actif BOOLEAN NOT NULL DEFAULT TRUE,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);