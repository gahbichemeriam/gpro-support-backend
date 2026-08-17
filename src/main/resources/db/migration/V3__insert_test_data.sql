-- ============================================
-- GPRO Support - Migration V3
-- Données de test réalistes
-- ============================================

-- ===== UTILISATEURS =====
-- Mot de passe : admin123 (hashé avec BCrypt)
INSERT INTO utilisateur (nom, email, mot_de_passe_hash, role) VALUES
('Meriam Gahbiche',  'meriam.gahbiche@polytechnicien.tn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVLGf4A8RW', 'ADMIN'),
('Agent Support 1',  'agent1@gpro.tn',                   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVLGf4A8RW', 'AGENT_SUPPORT'),
('Ingénieur R&D',    'rd@gpro.tn',                       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVLGf4A8RW', 'RD')
ON CONFLICT (email) DO NOTHING;

-- ===== PROJETS ERP =====
INSERT INTO projet_erp (nom, description, code_produit) VALUES
('GPRO Industry SaaS', 'ERP industriel principal - gestion complète de production', 'GPRO-IND-001'),
('GPRO Retail',        'ERP pour la distribution et le commerce de détail',          'GPRO-RET-002'),
('GPRO Finance',       'Module comptabilité et gestion financière',                  'GPRO-FIN-003')
ON CONFLICT (code_produit) DO NOTHING;

-- ===== MODULES ERP =====
INSERT INTO module_erp (projet_id, nom, description) VALUES
((SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'), 'Production',      'Planification et suivi de la production industrielle'),
((SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'), 'Stocks',          'Gestion des stocks et des entrepôts'),
((SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'), 'Ventes',          'Commandes clients et facturation'),
((SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'), 'Achats',          'Fournisseurs et commandes d''achat'),
((SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'), 'Ressources Humaines', 'Paie, congés et gestion du personnel'),
((SELECT id FROM projet_erp WHERE code_produit='GPRO-RET-002'), 'Caisse',          'Point de vente et encaissements'),
((SELECT id FROM projet_erp WHERE code_produit='GPRO-RET-002'), 'Fidélité',        'Programme de fidélisation clients'),
((SELECT id FROM projet_erp WHERE code_produit='GPRO-FIN-003'), 'Comptabilité',    'Comptabilité générale et analytique'),
((SELECT id FROM projet_erp WHERE code_produit='GPRO-FIN-003'), 'Trésorerie',      'Gestion des flux de trésorerie');

-- ===== VERSIONS ERP =====
INSERT INTO version_erp (projet_id, code_version, date_release, statut) VALUES
((SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'), '1.6.0', '2023-01-15', 'OBSOLETE'),
((SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'), '1.7.0', '2023-07-01', 'OBSOLETE'),
((SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'), '1.8.2', '2024-02-10', 'PRODUCTION'),
((SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'), '1.9.0', '2024-09-01', 'STAGING'),
((SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'), '2.0.1', '2025-01-20', 'DEVELOPPEMENT'),
((SELECT id FROM projet_erp WHERE code_produit='GPRO-RET-002'), '3.1.0', '2023-11-01', 'PRODUCTION'),
((SELECT id FROM projet_erp WHERE code_produit='GPRO-RET-002'), '3.2.0', '2024-06-15', 'STAGING'),
((SELECT id FROM projet_erp WHERE code_produit='GPRO-FIN-003'), '2.0.0', '2024-01-01', 'PRODUCTION')
ON CONFLICT (projet_id, code_version) DO NOTHING;

-- ===== CLIENTS =====
INSERT INTO client (nom, email, projet_id, version_active_id) VALUES
('Société Alpha SARL',      'alpha@societe.tn',    (SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'), (SELECT id FROM version_erp WHERE code_version='1.8.2' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'))),
('Industries Beta SA',      'beta@industries.tn',  (SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'), (SELECT id FROM version_erp WHERE code_version='1.7.0' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'))),
('Groupe Gamma',            'gamma@groupe.tn',     (SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'), (SELECT id FROM version_erp WHERE code_version='1.8.2' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'))),
('Distribution Delta',      'delta@distrib.tn',   (SELECT id FROM projet_erp WHERE code_produit='GPRO-RET-002'), (SELECT id FROM version_erp WHERE code_version='3.1.0' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-RET-002'))),
('Epsilon Commerce',        'epsilon@commerce.tn', (SELECT id FROM projet_erp WHERE code_produit='GPRO-RET-002'), (SELECT id FROM version_erp WHERE code_version='3.2.0' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-RET-002'))),
('Finance Zeta',            'zeta@finance.tn',     (SELECT id FROM projet_erp WHERE code_produit='GPRO-FIN-003'), (SELECT id FROM version_erp WHERE code_version='2.0.0' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-FIN-003')));

-- ===== PROBLÈMES =====
INSERT INTO probleme_fonctionnalite (module_id, titre, code_erreur, priorite) VALUES
-- Module Production
((SELECT id FROM module_erp WHERE nom='Production' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001')),
 'Erreur de calcul du stock en temps réel', 'ERR-PROD-201', 'HAUTE'),

((SELECT id FROM module_erp WHERE nom='Production' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001')),
 'Blocage lors de la clôture d''un ordre de fabrication', 'ERR-PROD-202', 'CRITIQUE'),

((SELECT id FROM module_erp WHERE nom='Production' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001')),
 'Rapport de production incomplet après export PDF', 'ERR-PROD-203', 'MOYENNE'),

-- Module Stocks
((SELECT id FROM module_erp WHERE nom='Stocks' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001')),
 'Inventaire négatif sur produit référencé', 'ERR-STOCK-101', 'CRITIQUE'),

((SELECT id FROM module_erp WHERE nom='Stocks' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001')),
 'Transfert inter-dépôts échoue silencieusement', 'ERR-STOCK-102', 'HAUTE'),

-- Module Ventes
((SELECT id FROM module_erp WHERE nom='Ventes' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001')),
 'Numéro de facture dupliqué sur exercice fiscal', 'ERR-VENTE-301', 'CRITIQUE'),

((SELECT id FROM module_erp WHERE nom='Ventes' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001')),
 'Remise client non appliquée à la validation', 'ERR-VENTE-302', 'MOYENNE'),

-- Module Achats
((SELECT id FROM module_erp WHERE nom='Achats' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001')),
 'Bon de commande fournisseur ne se génère pas en PDF', 'ERR-ACHAT-401', 'BASSE'),

-- Module RH
((SELECT id FROM module_erp WHERE nom='Ressources Humaines' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001')),
 'Calcul des heures supplémentaires incorrect en décembre', 'ERR-RH-501', 'HAUTE'),

-- Module Caisse (Retail)
((SELECT id FROM module_erp WHERE nom='Caisse' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-RET-002')),
 'Ticket de caisse non imprimé après paiement CB', 'ERR-CAISSE-601', 'HAUTE'),

-- Module Comptabilité (Finance)
((SELECT id FROM module_erp WHERE nom='Comptabilité' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-FIN-003')),
 'Balance générale ne s''équilibre pas après import bancaire', 'ERR-COMPTA-701', 'CRITIQUE')

ON CONFLICT (code_erreur) DO NOTHING;

-- ===== RÉSOLUTIONS =====
INSERT INTO resolution (probleme_id, type_resolution, description_etapes, validation_qa) VALUES

-- ERR-PROD-201
((SELECT id FROM probleme_fonctionnalite WHERE code_erreur='ERR-PROD-201'),
 'SQL',
 'UPDATE mouvement_stock
SET quantite_reel = quantite_theorique
WHERE date_mouvement >= CURRENT_DATE - INTERVAL ''7 days''
  AND statut = ''EN_ATTENTE'';

-- Vérification après correction
SELECT COUNT(*) as nb_corrections
FROM mouvement_stock
WHERE quantite_reel != quantite_theorique;',
 true),

-- ERR-PROD-202
((SELECT id FROM probleme_fonctionnalite WHERE code_erreur='ERR-PROD-202'),
 'PROCEDURE',
 '1. Aller dans Production > Ordres de Fabrication
2. Identifier l''OF bloqué (statut "EN_COURS" depuis > 24h)
3. Cliquer sur "Forcer clôture" (menu contextuel)
4. Saisir le motif : "Correction manuelle v1.8.2"
5. Valider et vérifier que le statut passe à "CLOTURE"
6. Si le problème persiste, appliquer le patch SQL :
   UPDATE ordre_fabrication SET statut = ''CLOTURE'', date_cloture = NOW()
   WHERE id = [ID_OF] AND statut = ''BLOQUE'';',
 false),

-- ERR-STOCK-101
((SELECT id FROM probleme_fonctionnalite WHERE code_erreur='ERR-STOCK-101'),
 'SQL',
 '-- Identifier les produits avec stock négatif
SELECT p.reference, p.designation, s.quantite
FROM stock s
JOIN produit p ON p.id = s.produit_id
WHERE s.quantite < 0;

-- Corriger en remettant à zéro avec création d''un écart d''inventaire
UPDATE stock SET quantite = 0,
  date_derniere_maj = NOW(),
  commentaire = ''Correction automatique - stock négatif détecté''
WHERE quantite < 0;

-- Créer une entrée dans le journal des corrections
INSERT INTO journal_stock (produit_id, type_operation, quantite, commentaire, date_operation)
SELECT produit_id, ''CORRECTION'', ABS(quantite), ''Stock négatif corrigé'', NOW()
FROM stock WHERE quantite < 0;',
 true),

-- ERR-VENTE-301
((SELECT id FROM probleme_fonctionnalite WHERE code_erreur='ERR-VENTE-301'),
 'SQL',
 '-- Identifier les doublons de numéros de facture
SELECT numero_facture, COUNT(*) as nb_doublons
FROM facture
WHERE EXTRACT(YEAR FROM date_creation) = EXTRACT(YEAR FROM CURRENT_DATE)
GROUP BY numero_facture
HAVING COUNT(*) > 1;

-- Renommer les doublons (ajouter suffixe -BIS)
UPDATE facture f
SET numero_facture = numero_facture || ''-BIS''
WHERE id IN (
  SELECT MAX(id) FROM facture
  GROUP BY numero_facture
  HAVING COUNT(*) > 1
);',
 true),

-- ERR-RH-501
((SELECT id FROM probleme_fonctionnalite WHERE code_erreur='ERR-RH-501'),
 'PARAMETRAGE',
 '1. Aller dans Administration > Paramètres Paie > Calendrier
2. Vérifier que le mois de Décembre a bien 31 jours configurés
3. Dans "Règles heures supplémentaires" :
   - Seuil journalier : 8h
   - Taux majoration : 125% (jour), 150% (nuit/weekend)
4. Cocher "Inclure jours fériés nationaux (Tunisie)"
5. Sauvegarder et recalculer les bulletins de Décembre
6. Valider avec le responsable RH avant envoi',
 false),

-- ERR-COMPTA-701
((SELECT id FROM probleme_fonctionnalite WHERE code_erreur='ERR-COMPTA-701'),
 'SQL',
 '-- Trouver les écritures déséquilibrées
SELECT e.id, e.reference, e.date_ecriture,
       SUM(CASE WHEN sens=''D'' THEN montant ELSE 0 END) as total_debit,
       SUM(CASE WHEN sens=''C'' THEN montant ELSE 0 END) as total_credit,
       ABS(SUM(CASE WHEN sens=''D'' THEN montant ELSE -montant END)) as ecart
FROM ecriture_comptable e
JOIN ligne_ecriture l ON l.ecriture_id = e.id
WHERE e.date_ecriture >= CURRENT_DATE - INTERVAL ''30 days''
GROUP BY e.id, e.reference, e.date_ecriture
HAVING ABS(SUM(CASE WHEN sens=''D'' THEN montant ELSE -montant END)) > 0.01
ORDER BY ecart DESC;',
 false)

ON CONFLICT DO NOTHING;

-- ===== MATRICE APPLICABILITE =====
INSERT INTO applicabilite_version (probleme_id, version_id, statut_probleme, version_corrective_id) VALUES
-- ERR-PROD-201 : présent en 1.7.0 et 1.8.2, corrigé en 2.0.1
((SELECT id FROM probleme_fonctionnalite WHERE code_erreur='ERR-PROD-201'),
 (SELECT id FROM version_erp WHERE code_version='1.7.0' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001')),
 'PRESENT',
 (SELECT id FROM version_erp WHERE code_version='2.0.1' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'))),

((SELECT id FROM probleme_fonctionnalite WHERE code_erreur='ERR-PROD-201'),
 (SELECT id FROM version_erp WHERE code_version='1.8.2' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001')),
 'PRESENT',
 (SELECT id FROM version_erp WHERE code_version='2.0.1' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001'))),

-- ERR-STOCK-101 : corrigé en 1.9.0
((SELECT id FROM probleme_fonctionnalite WHERE code_erreur='ERR-STOCK-101'),
 (SELECT id FROM version_erp WHERE code_version='1.8.2' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001')),
 'PRESENT',
 (SELECT id FROM version_erp WHERE code_version='1.9.0' AND projet_id=(SELECT id FROM projet_erp WHERE code_produit='GPRO-IND-001')))

ON CONFLICT (probleme_id, version_id) DO NOTHING;
