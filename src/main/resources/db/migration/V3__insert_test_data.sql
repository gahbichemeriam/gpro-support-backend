-- ============================================
-- GPRO Support - Migration V3
-- Donnees de test
-- ============================================

-- ===== UTILISATEURS =====
-- Mot de passe : admin123
INSERT INTO utilisateur (nom, email, mot_de_passe_hash, role) VALUES
('Meriam Gahbiche', 'meriam.gahbiche@polytechnicien.tn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVLGf4A8RW', 'ADMIN'),
('Agent Support',   'agent@gpro.tn',                    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVLGf4A8RW', 'AGENT_SUPPORT'),
('Ingenieur RD',    'rd@gpro.tn',                       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVLGf4A8RW', 'RD')
ON CONFLICT (email) DO NOTHING;

-- ===== PROJETS ERP =====
INSERT INTO projet_erp (nom, description, code_produit) VALUES
('GPRO Industry SaaS', 'ERP industriel principal', 'GPRO-IND-001'),
('GPRO Retail',        'ERP pour la distribution', 'GPRO-RET-002'),
('GPRO Finance',       'Module comptabilite',       'GPRO-FIN-003')
ON CONFLICT (code_produit) DO NOTHING;

-- ===== MODULES ERP =====
INSERT INTO module_erp (projet_id, nom, description)
SELECT id, 'Production',         'Planification et suivi de la production'   FROM projet_erp WHERE code_produit='GPRO-IND-001'
UNION ALL
SELECT id, 'Stocks',             'Gestion des stocks et entrepots'            FROM projet_erp WHERE code_produit='GPRO-IND-001'
UNION ALL
SELECT id, 'Ventes',             'Commandes clients et facturation'           FROM projet_erp WHERE code_produit='GPRO-IND-001'
UNION ALL
SELECT id, 'Achats',             'Fournisseurs et commandes achat'            FROM projet_erp WHERE code_produit='GPRO-IND-001'
UNION ALL
SELECT id, 'Ressources Humaines','Paie et gestion du personnel'               FROM projet_erp WHERE code_produit='GPRO-IND-001'
UNION ALL
SELECT id, 'Caisse',             'Point de vente et encaissements'            FROM projet_erp WHERE code_produit='GPRO-RET-002'
UNION ALL
SELECT id, 'Fidelite',           'Programme de fidelisation clients'          FROM projet_erp WHERE code_produit='GPRO-RET-002'
UNION ALL
SELECT id, 'Comptabilite',       'Comptabilite generale et analytique'        FROM projet_erp WHERE code_produit='GPRO-FIN-003'
UNION ALL
SELECT id, 'Tresorerie',         'Gestion des flux de tresorerie'             FROM projet_erp WHERE code_produit='GPRO-FIN-003';

-- ===== VERSIONS ERP =====
INSERT INTO version_erp (projet_id, code_version, date_release, statut)
SELECT id, '1.6.0', '2023-01-15'::date, 'DEVELOPPEMENT'::statut_version FROM projet_erp WHERE code_produit='GPRO-IND-001'
UNION ALL
SELECT id, '1.7.0', '2023-07-01'::date, 'DEVELOPPEMENT'::statut_version FROM projet_erp WHERE code_produit='GPRO-IND-001'
UNION ALL
SELECT id, '1.8.2', '2024-02-10'::date, 'PRODUCTION'::statut_version    FROM projet_erp WHERE code_produit='GPRO-IND-001'
UNION ALL
SELECT id, '1.9.0', '2024-09-01'::date, 'STAGING'::statut_version       FROM projet_erp WHERE code_produit='GPRO-IND-001'
UNION ALL
SELECT id, '2.0.1', '2025-01-20'::date, 'DEVELOPPEMENT'::statut_version FROM projet_erp WHERE code_produit='GPRO-IND-001'
UNION ALL
SELECT id, '3.1.0', '2023-11-01'::date, 'PRODUCTION'::statut_version    FROM projet_erp WHERE code_produit='GPRO-RET-002'
UNION ALL
SELECT id, '3.2.0', '2024-06-15'::date, 'STAGING'::statut_version       FROM projet_erp WHERE code_produit='GPRO-RET-002'
UNION ALL
SELECT id, '2.0.0', '2024-01-01'::date, 'PRODUCTION'::statut_version    FROM projet_erp WHERE code_produit='GPRO-FIN-003'
ON CONFLICT (projet_id, code_version) DO NOTHING;

-- ===== CLIENTS =====
INSERT INTO client (nom, email, projet_id, version_active_id)
SELECT 'Societe Alpha SARL', 'alpha@societe.tn',
       p.id,
       v.id
FROM projet_erp p
JOIN version_erp v ON v.projet_id = p.id AND v.code_version = '1.8.2'
WHERE p.code_produit = 'GPRO-IND-001';

INSERT INTO client (nom, email, projet_id, version_active_id)
SELECT 'Industries Beta SA', 'beta@industries.tn',
       p.id,
       v.id
FROM projet_erp p
JOIN version_erp v ON v.projet_id = p.id AND v.code_version = '1.7.0'
WHERE p.code_produit = 'GPRO-IND-001';

INSERT INTO client (nom, email, projet_id, version_active_id)
SELECT 'Groupe Gamma', 'gamma@groupe.tn',
       p.id,
       v.id
FROM projet_erp p
JOIN version_erp v ON v.projet_id = p.id AND v.code_version = '1.8.2'
WHERE p.code_produit = 'GPRO-IND-001';

INSERT INTO client (nom, email, projet_id, version_active_id)
SELECT 'Distribution Delta', 'delta@distrib.tn',
       p.id,
       v.id
FROM projet_erp p
JOIN version_erp v ON v.projet_id = p.id AND v.code_version = '3.1.0'
WHERE p.code_produit = 'GPRO-RET-002';

INSERT INTO client (nom, email, projet_id, version_active_id)
SELECT 'Finance Zeta', 'zeta@finance.tn',
       p.id,
       v.id
FROM projet_erp p
JOIN version_erp v ON v.projet_id = p.id AND v.code_version = '2.0.0'
WHERE p.code_produit = 'GPRO-FIN-003';

-- ===== PROBLEMES =====
INSERT INTO probleme_fonctionnalite (module_id, titre, code_erreur, priorite)
SELECT m.id, 'Erreur de calcul du stock en temps reel', 'ERR-PROD-201', 'HAUTE'
FROM module_erp m JOIN projet_erp p ON p.id = m.projet_id
WHERE m.nom = 'Production' AND p.code_produit = 'GPRO-IND-001'
ON CONFLICT (code_erreur) DO NOTHING;

INSERT INTO probleme_fonctionnalite (module_id, titre, code_erreur, priorite)
SELECT m.id, 'Blocage lors de la cloture ordre de fabrication', 'ERR-PROD-202', 'CRITIQUE'
FROM module_erp m JOIN projet_erp p ON p.id = m.projet_id
WHERE m.nom = 'Production' AND p.code_produit = 'GPRO-IND-001'
ON CONFLICT (code_erreur) DO NOTHING;

INSERT INTO probleme_fonctionnalite (module_id, titre, code_erreur, priorite)
SELECT m.id, 'Rapport de production incomplet apres export PDF', 'ERR-PROD-203', 'MOYENNE'
FROM module_erp m JOIN projet_erp p ON p.id = m.projet_id
WHERE m.nom = 'Production' AND p.code_produit = 'GPRO-IND-001'
ON CONFLICT (code_erreur) DO NOTHING;

INSERT INTO probleme_fonctionnalite (module_id, titre, code_erreur, priorite)
SELECT m.id, 'Inventaire negatif sur produit reference', 'ERR-STOCK-101', 'CRITIQUE'
FROM module_erp m JOIN projet_erp p ON p.id = m.projet_id
WHERE m.nom = 'Stocks' AND p.code_produit = 'GPRO-IND-001'
ON CONFLICT (code_erreur) DO NOTHING;

INSERT INTO probleme_fonctionnalite (module_id, titre, code_erreur, priorite)
SELECT m.id, 'Transfert inter-depots echoue silencieusement', 'ERR-STOCK-102', 'HAUTE'
FROM module_erp m JOIN projet_erp p ON p.id = m.projet_id
WHERE m.nom = 'Stocks' AND p.code_produit = 'GPRO-IND-001'
ON CONFLICT (code_erreur) DO NOTHING;

INSERT INTO probleme_fonctionnalite (module_id, titre, code_erreur, priorite)
SELECT m.id, 'Numero de facture duplique sur exercice fiscal', 'ERR-VENTE-301', 'CRITIQUE'
FROM module_erp m JOIN projet_erp p ON p.id = m.projet_id
WHERE m.nom = 'Ventes' AND p.code_produit = 'GPRO-IND-001'
ON CONFLICT (code_erreur) DO NOTHING;

INSERT INTO probleme_fonctionnalite (module_id, titre, code_erreur, priorite)
SELECT m.id, 'Remise client non appliquee a la validation', 'ERR-VENTE-302', 'MOYENNE'
FROM module_erp m JOIN projet_erp p ON p.id = m.projet_id
WHERE m.nom = 'Ventes' AND p.code_produit = 'GPRO-IND-001'
ON CONFLICT (code_erreur) DO NOTHING;

INSERT INTO probleme_fonctionnalite (module_id, titre, code_erreur, priorite)
SELECT m.id, 'Calcul heures supplementaires incorrect en decembre', 'ERR-RH-501', 'HAUTE'
FROM module_erp m JOIN projet_erp p ON p.id = m.projet_id
WHERE m.nom = 'Ressources Humaines' AND p.code_produit = 'GPRO-IND-001'
ON CONFLICT (code_erreur) DO NOTHING;

INSERT INTO probleme_fonctionnalite (module_id, titre, code_erreur, priorite)
SELECT m.id, 'Ticket de caisse non imprime apres paiement CB', 'ERR-CAISSE-601', 'HAUTE'
FROM module_erp m JOIN projet_erp p ON p.id = m.projet_id
WHERE m.nom = 'Caisse' AND p.code_produit = 'GPRO-RET-002'
ON CONFLICT (code_erreur) DO NOTHING;

INSERT INTO probleme_fonctionnalite (module_id, titre, code_erreur, priorite)
SELECT m.id, 'Balance generale ne equilibre pas apres import bancaire', 'ERR-COMPTA-701', 'CRITIQUE'
FROM module_erp m JOIN projet_erp p ON p.id = m.projet_id
WHERE m.nom = 'Comptabilite' AND p.code_produit = 'GPRO-FIN-003'
ON CONFLICT (code_erreur) DO NOTHING;

-- ===== RESOLUTIONS =====
INSERT INTO resolution (probleme_id, type_resolution, description_etapes, validation_qa)
SELECT id,
'SQL',
'UPDATE mouvement_stock SET quantite_reel = quantite_theorique WHERE statut = ''EN_ATTENTE'';',
true
FROM probleme_fonctionnalite WHERE code_erreur = 'ERR-PROD-201';

INSERT INTO resolution (probleme_id, type_resolution, description_etapes, validation_qa)
SELECT id,
'PROCEDURE',
'1. Aller dans Production > Ordres de Fabrication
2. Identifier l ordre de fabrication bloque
3. Cliquer sur Forcer cloture dans le menu contextuel
4. Valider et verifier le statut passe a CLOTURE',
false
FROM probleme_fonctionnalite WHERE code_erreur = 'ERR-PROD-202';

INSERT INTO resolution (probleme_id, type_resolution, description_etapes, validation_qa)
SELECT id,
'SQL',
'SELECT reference, quantite FROM stock WHERE quantite < 0;
UPDATE stock SET quantite = 0 WHERE quantite < 0;',
true
FROM probleme_fonctionnalite WHERE code_erreur = 'ERR-STOCK-101';

INSERT INTO resolution (probleme_id, type_resolution, description_etapes, validation_qa)
SELECT id,
'SQL',
'SELECT numero_facture, COUNT(*) as nb FROM facture GROUP BY numero_facture HAVING COUNT(*) > 1;',
true
FROM probleme_fonctionnalite WHERE code_erreur = 'ERR-VENTE-301';

INSERT INTO resolution (probleme_id, type_resolution, description_etapes, validation_qa)
SELECT id,
'PARAMETRAGE',
'1. Aller dans Administration > Parametres Paie > Calendrier
2. Verifier que Decembre a 31 jours configures
3. Taux heures sup : 125% jour, 150% nuit/weekend
4. Sauvegarder et recalculer les bulletins',
false
FROM probleme_fonctionnalite WHERE code_erreur = 'ERR-RH-501';

INSERT INTO resolution (probleme_id, type_resolution, description_etapes, validation_qa)
SELECT id,
'SQL',
'SELECT reference, SUM(montant) as ecart FROM ecriture_comptable GROUP BY reference HAVING ABS(SUM(montant)) > 0.01;',
false
FROM probleme_fonctionnalite WHERE code_erreur = 'ERR-COMPTA-701';

-- ===== MATRICE APPLICABILITE =====
INSERT INTO applicabilite_version (probleme_id, version_id, statut_probleme, version_corrective_id)
SELECT
    pr.id,
    v.id,
    'PRESENT',
    vc.id
FROM probleme_fonctionnalite pr
JOIN module_erp m ON m.id = pr.module_id
JOIN projet_erp p ON p.id = m.projet_id
JOIN version_erp v  ON v.projet_id = p.id AND v.code_version = '1.8.2'
JOIN version_erp vc ON vc.projet_id = p.id AND vc.code_version = '2.0.1'
WHERE pr.code_erreur = 'ERR-PROD-201'
ON CONFLICT (probleme_id, version_id) DO NOTHING;

INSERT INTO applicabilite_version (probleme_id, version_id, statut_probleme, version_corrective_id)
SELECT
    pr.id,
    v.id,
    'PRESENT',
    vc.id
FROM probleme_fonctionnalite pr
JOIN module_erp m ON m.id = pr.module_id
JOIN projet_erp p ON p.id = m.projet_id
JOIN version_erp v  ON v.projet_id = p.id AND v.code_version = '1.8.2'
JOIN version_erp vc ON vc.projet_id = p.id AND vc.code_version = '1.9.0'
WHERE pr.code_erreur = 'ERR-STOCK-101'
ON CONFLICT (probleme_id, version_id) DO NOTHING;
