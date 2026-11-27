-- V1 : schéma commun (membre 2 / B)

CREATE TABLE parametre (
                           cle         VARCHAR(50)  PRIMARY KEY,
                           valeur      VARCHAR(100) NOT NULL,
                           description VARCHAR(255)
);

CREATE TABLE transition_statut (
                                   id             BIGSERIAL PRIMARY KEY,
                                   type_document  VARCHAR(30) NOT NULL,
                                   statut_depart  VARCHAR(30) NOT NULL,
                                   statut_arrivee VARCHAR(30) NOT NULL,
                                   role_autorise  VARCHAR(30) NOT NULL
                                       CHECK (role_autorise IN ('MAGASINIER','ACHATS','DIRECTEUR','COMPTABLE','SYSTEME')),
                                   UNIQUE (type_document, statut_depart, statut_arrivee)
);

CREATE TABLE historique_statut (
                                   id              BIGSERIAL PRIMARY KEY,
                                   type_document   VARCHAR(30) NOT NULL,
                                   document_id     BIGINT      NOT NULL,
                                   ancien_statut   VARCHAR(30),
                                   nouveau_statut  VARCHAR(30) NOT NULL,
                                   utilisateur     VARCHAR(50) NOT NULL,
                                   date_changement TIMESTAMP   NOT NULL DEFAULT now(),
                                   commentaire     TEXT
);

CREATE TABLE produit (
                         id            BIGSERIAL PRIMARY KEY,
                         reference     VARCHAR(30)  NOT NULL UNIQUE,
                         libelle       VARCHAR(150) NOT NULL,
                         unite         VARCHAR(20)  DEFAULT 'unité',
                         stock_actuel  INTEGER      NOT NULL DEFAULT 0 CHECK (stock_actuel >= 0),
                         seuil_reappro INTEGER      NOT NULL DEFAULT 0
);

CREATE TABLE mouvement_stock (
                                 id             BIGSERIAL PRIMARY KEY,
                                 produit_id     BIGINT    NOT NULL REFERENCES produit(id),
                                 quantite       INTEGER   NOT NULL CHECK (quantite > 0),
                                 source         VARCHAR(30),
                                 source_id      BIGINT,
                                 date_mouvement TIMESTAMP DEFAULT now()
);

CREATE TABLE ecriture_comptable (
                                    id            BIGSERIAL PRIMARY KEY,
                                    type          VARCHAR(20)   NOT NULL,
                                    reference_id  BIGINT,
                                    montant       NUMERIC(12,3) NOT NULL,
                                    date_ecriture TIMESTAMP DEFAULT now()
);

-- Paramètre
INSERT INTO parametre (cle, valeur, description) VALUES
    ('seuil_validation', '5000', 'Montant (DT) au-dessus duquel le directeur doit valider une commande');

-- Transitions autorisées (section 10 du dossier de référence)
INSERT INTO transition_statut (type_document, statut_depart, statut_arrivee, role_autorise) VALUES
                                                                                                ('FOURNISSEUR',    'ACTIF',                 'INACTIF',               'ACHATS'),

                                                                                                ('DEMANDE_ACHAT',  'BROUILLON',             'SOUMISE',               'MAGASINIER'),
                                                                                                ('DEMANDE_ACHAT',  'SOUMISE',               'ACCEPTEE',              'ACHATS'),
                                                                                                ('DEMANDE_ACHAT',  'SOUMISE',               'REFUSEE',               'ACHATS'),

                                                                                                ('DEMANDE_PRIX',   'BROUILLON',             'ENVOYEE',               'ACHATS'),
                                                                                                ('DEMANDE_PRIX',   'ENVOYEE',               'OFFRES_RECUES',         'ACHATS'),
                                                                                                ('DEMANDE_PRIX',   'OFFRES_RECUES',         'CLOTUREE',              'ACHATS'),

                                                                                                ('OFFRE',          'RECUE',                 'RETENUE',               'ACHATS'),
                                                                                                ('OFFRE',          'RECUE',                 'REJETEE',               'ACHATS'),

                                                                                                ('COMMANDE',       'BROUILLON',             'VALIDEE',               'SYSTEME'),
                                                                                                ('COMMANDE',       'BROUILLON',             'EN_ATTENTE_VALIDATION', 'SYSTEME'),
                                                                                                ('COMMANDE',       'EN_ATTENTE_VALIDATION', 'VALIDEE',               'DIRECTEUR'),
                                                                                                ('COMMANDE',       'EN_ATTENTE_VALIDATION', 'BROUILLON',             'DIRECTEUR'),
                                                                                                ('COMMANDE',       'BROUILLON',             'ANNULEE',               'ACHATS'),
                                                                                                ('COMMANDE',       'VALIDEE',               'ENVOYEE',               'ACHATS'),
                                                                                                ('COMMANDE',       'ENVOYEE',               'PARTIELLEMENT_RECUE',   'MAGASINIER'),
                                                                                                ('COMMANDE',       'ENVOYEE',               'RECUE',                 'MAGASINIER'),
                                                                                                ('COMMANDE',       'PARTIELLEMENT_RECUE',   'RECUE',                 'MAGASINIER'),
                                                                                                ('COMMANDE',       'RECUE',                 'FACTUREE',              'COMPTABLE'),
                                                                                                ('COMMANDE',       'FACTUREE',              'PAYEE',                 'COMPTABLE'),
                                                                                                ('COMMANDE',       'PAYEE',                 'CLOTUREE',              'SYSTEME'),

                                                                                                ('RECEPTION',      'EN_COURS',              'CONFORME',              'MAGASINIER'),
                                                                                                ('RECEPTION',      'EN_COURS',              'NON_CONFORME',          'MAGASINIER'),

                                                                                                ('FACTURE',        'RECUE',                 'VERIFIEE',              'SYSTEME'),
                                                                                                ('FACTURE',        'RECUE',                 'EN_LITIGE',             'SYSTEME'),
                                                                                                ('FACTURE',        'EN_LITIGE',             'VERIFIEE',              'COMPTABLE'),
                                                                                                ('FACTURE',        'VERIFIEE',              'VALIDEE',               'COMPTABLE'),
                                                                                                ('FACTURE',        'VALIDEE',               'PAYEE',                 'COMPTABLE');