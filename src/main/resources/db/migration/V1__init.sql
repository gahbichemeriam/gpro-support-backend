-- ============================================
-- GPRO Support - Schema de base de donnees
-- Migration V1 : creation initiale
-- ============================================

-- ===== TYPES ENUM =====

CREATE TYPE role_utilisateur AS ENUM ('AGENT_SUPPORT', 'RD', 'ADMIN');

CREATE TYPE statut_version AS ENUM ('DEVELOPPEMENT', 'STAGING', 'PRODUCTION', 'OBSOLETE');

CREATE TYPE priorite_probleme AS ENUM ('BASSE', 'MOYENNE', 'HAUTE', 'CRITIQUE');

CREATE TYPE type_resolution AS ENUM ('SQL', 'PARAMETRAGE', 'PATCH_CODE', 'PROCEDURE');

CREATE TYPE statut_applicabilite AS ENUM ('PRESENT', 'CORRIGE', 'NON_TESTE');

-- ===== TABLE : UTILISATEUR =====

CREATE TABLE utilisateur (
                             id                  BIGSERIAL PRIMARY KEY,
                             nom                 VARCHAR(150) NOT NULL,
                             email               VARCHAR(255) NOT NULL UNIQUE,
                             mot_de_passe_hash   VARCHAR(255) NOT NULL,
                             role                role_utilisateur NOT NULL,
                             date_creation       TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ===== TABLE : PROJET_ERP =====

CREATE TABLE projet_erp (
                            id              BIGSERIAL PRIMARY KEY,
                            nom             VARCHAR(150) NOT NULL,
                            description     TEXT,
                            code_produit    VARCHAR(50) NOT NULL UNIQUE
);

-- ===== TABLE : MODULE_ERP =====

CREATE TABLE module_erp (
                            id              BIGSERIAL PRIMARY KEY,
                            projet_id       BIGINT NOT NULL REFERENCES projet_erp(id) ON DELETE CASCADE,
                            nom             VARCHAR(150) NOT NULL,
                            description     TEXT
);

CREATE INDEX idx_module_erp_projet ON module_erp(projet_id);

-- ===== TABLE : VERSION_ERP =====

CREATE TABLE version_erp (
                             id              BIGSERIAL PRIMARY KEY,
                             projet_id       BIGINT NOT NULL REFERENCES projet_erp(id) ON DELETE CASCADE,
                             code_version    VARCHAR(30) NOT NULL,
                             date_release    DATE,
                             statut          statut_version NOT NULL DEFAULT 'DEVELOPPEMENT',
                             UNIQUE (projet_id, code_version)
);

CREATE INDEX idx_version_erp_projet ON version_erp(projet_id);

-- ===== TABLE : PROBLEME_FONCTIONNALITE =====

CREATE TABLE probleme_fonctionnalite (
                                         id              BIGSERIAL PRIMARY KEY,
                                         module_id       BIGINT NOT NULL REFERENCES module_erp(id) ON DELETE CASCADE,
                                         titre           VARCHAR(255) NOT NULL,
                                         code_erreur     VARCHAR(50) NOT NULL UNIQUE,
                                         priorite        priorite_probleme NOT NULL DEFAULT 'MOYENNE',
                                         date_creation   TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_probleme_module ON probleme_fonctionnalite(module_id);
CREATE INDEX idx_probleme_code_erreur ON probleme_fonctionnalite(code_erreur);

-- ===== TABLE : RESOLUTION =====

CREATE TABLE resolution (
                            id                  BIGSERIAL PRIMARY KEY,
                            probleme_id         BIGINT NOT NULL REFERENCES probleme_fonctionnalite(id) ON DELETE CASCADE,
                            type_resolution     type_resolution NOT NULL,
                            description_etapes  TEXT,
                            validation_qa       BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_resolution_probleme ON resolution(probleme_id);

-- ===== TABLE : PIECE_JOINTE =====

CREATE TABLE piece_jointe (
                              id                  BIGSERIAL PRIMARY KEY,
                              resolution_id       BIGINT NOT NULL REFERENCES resolution(id) ON DELETE CASCADE,
                              nom_fichier         VARCHAR(255) NOT NULL,
                              chemin_stockage     VARCHAR(500) NOT NULL,
                              type_mime           VARCHAR(100)
);

CREATE INDEX idx_piece_jointe_resolution ON piece_jointe(resolution_id);

-- ===== TABLE : APPLICABILITE_VERSION (matrice de compatibilite) =====

CREATE TABLE applicabilite_version (
                                       id                      BIGSERIAL PRIMARY KEY,
                                       probleme_id             BIGINT NOT NULL REFERENCES probleme_fonctionnalite(id) ON DELETE CASCADE,
                                       version_id              BIGINT NOT NULL REFERENCES version_erp(id) ON DELETE RESTRICT,
                                       statut_probleme         statut_applicabilite NOT NULL DEFAULT 'PRESENT',
                                       version_corrective_id   BIGINT REFERENCES version_erp(id) ON DELETE RESTRICT,
                                       UNIQUE (probleme_id, version_id)
);

CREATE INDEX idx_applicabilite_probleme ON applicabilite_version(probleme_id);
CREATE INDEX idx_applicabilite_version ON applicabilite_version(version_id);