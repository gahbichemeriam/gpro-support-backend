# GPRO Support — Backend

> Module d'aide au support ERP — Spring Boot 3.4.5 / Java 17

## 📋 Description

GPRO Support est une application web dédiée aux agents de support technique d'un ERP industriel (GPRO Industry). Elle centralise la base de connaissances des problèmes/résolutions et guide l'agent à travers un parcours structuré de diagnostic.

## 🏗️ Stack Technique

| Technologie | Version | Rôle |
|---|---|---|
| Java | 17 | Langage |
| Spring Boot | 3.4.5 | Framework backend |
| Spring Security + JWT | - | Authentification |
| Spring Data JPA | - | Persistance |
| Hibernate | 6.x | ORM |
| PostgreSQL | 16 | Base de données |
| Flyway | 10.x | Migrations SQL |
| Lombok | - | Réduction boilerplate |
| Springdoc OpenAPI | 2.8.9 | Documentation API |

## 🚀 Lancement

### Prérequis
- Java 17+
- Maven 3.x (inclus via `mvnw`)
- PostgreSQL 16 sur le port `5433`

### Base de données
Créez la base `gpro_support` dans PostgreSQL. Flyway appliquera automatiquement les migrations au démarrage.

### Démarrage
```bash
$env:MAVEN_OPTS="-Xms256m -Xmx768m -XX:+UseG1GC"
.\mvnw.cmd spring-boot:run
```

L'application démarre sur **http://localhost:8081**

## 📚 Documentation API

Swagger UI disponible sur : **http://localhost:8081/swagger-ui/index.html**

### Authentification
1. `POST /api/auth/login` avec `{ "email": "...", "motDePasse": "..." }`
2. Copier le token JWT retourné
3. Cliquer "Authorize" dans Swagger et coller le token

### Comptes de test
| Email | Mot de passe | Rôle |
|---|---|---|
| meriam.gahbiche@polytechnicien.tn | admin123 | ADMIN |
| agent@gpro.tn | admin123 | AGENT_SUPPORT |
| rd@gpro.tn | admin123 | RD |

## 🗂️ Architecture

```
src/main/java/com/gprosupport/backend/
├── config/          → Spring Security, CORS, Swagger
├── security/        → JWT Filter, UserDetailsService
├── common/          → Exceptions globales, ApiResponse
├── utilisateur/     → Auth (login, register)
├── projet/          → CRUD Projets ERP
├── module/          → CRUD Modules ERP
├── probleme/        → CRUD Problèmes + recherche
├── resolution/      → CRUD Résolutions + validation QA
├── piecejointe/     → Upload/download fichiers
├── version/         → CRUD Versions ERP
├── client/          → CRUD Clients (parc)
├── applicabilite/   → Matrice Bug↔Version + alertes
└── rapport/         → KPI, Top pannes, statistiques
```

## 🗃️ Migrations Flyway

| Version | Description |
|---|---|
| V1 | Schéma initial complet (tables + ENUMs) |
| V2 | Ajout table `client` |
| V3 | Données de test (3 projets, 11 problèmes, 6 résolutions...) |

## 📊 Endpoints principaux

| Méthode | URL | Description |
|---|---|---|
| POST | /api/auth/login | Connexion |
| POST | /api/auth/register | Inscription |
| GET | /api/projets | Liste des projets |
| GET | /api/problemes?recherche=xxx | Recherche textuelle |
| GET | /api/applicabilites/alertes?problemeId=1 | Alertes version |
| GET | /api/rapports/kpi | Indicateurs clés |
| GET | /api/rapports/top-pannes | Top 10 pannes |

## 👩‍💻 Auteur

**Meriam Gahbiche** — Stage d'été 2026  
Département Support — GPRO
