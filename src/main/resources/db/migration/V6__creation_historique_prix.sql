CREATE TABLE historique_prix (
                                 id BIGSERIAL PRIMARY KEY,
                                 catalogue_id BIGINT NOT NULL REFERENCES catalogue(id),
                                 ancien_prix NUMERIC(12,3) NOT NULL,
                                 nouveau_prix NUMERIC(12,3) NOT NULL,
                                 date_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);