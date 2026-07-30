-- ============================================
-- GPRO Support - Migration V2
-- Ajout de la table CLIENT (parc clients)
-- Manquante dans V1 mais mentionnée dans le CDC
-- ============================================

CREATE TABLE client (
    id                  BIGSERIAL PRIMARY KEY,
    nom                 VARCHAR(150) NOT NULL,
    email               VARCHAR(255),
    projet_id           BIGINT NOT NULL REFERENCES projet_erp(id) ON DELETE RESTRICT,
    version_active_id   BIGINT NOT NULL REFERENCES version_erp(id) ON DELETE RESTRICT
);

CREATE INDEX idx_client_projet ON client(projet_id);
CREATE INDEX idx_client_version ON client(version_active_id);
