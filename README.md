# 🤝 Gestion des Actions de Charité

Une plateforme centralisée pour connecter **organisations** et **donateurs**, facilitant la collecte de fonds et la participation à des initiatives caritatives.

---

## ✨ Fonctionnalités

- **Utilisateurs** : Inscription, authentification (email / Google OAuth 2.0), historique des dons
- **Organisations** : Gestion de profil, création d'actions de charité, import de médias
- **Exploration** : Filtrage par catégorie, recommandations personnalisées
- **Dons** : Paiement sécurisé via PayPal / Stripe, suivi des contributions
- **Multilinguisme** : Français / Arabe

---

## 🏗️ Stack technique

| Couche | Technologies                                                |
|--------|-------------------------------------------------------------|
| Backend | Spring Boot 3.x, Spring MVC, Spring Security, Spring Data JPA |
| Base de données | postgresql                                    |
| Frontend | Thymeleaf                                                   |
| Outils | IntelliJ IDEA, Postman, Git, Maven, Docker                  |

---

## ⚙️ Ce dont vous avez besoin

Ce projet nécessite les outils suivants :

| Outil | Version | Lien |
|-------|---------|------|
| Java | 17+ | [Télécharger](https://adoptium.net/) |
| Maven | 3.9+ | [Télécharger](https://maven.apache.org/) |
| Docker Desktop | Dernière version | [Télécharger](https://www.docker.com/products/docker-desktop/) |
| Git | Dernière version | [Télécharger](https://git-scm.com/) |

---

## 🚀 Lancement du projet

### 📦 Cloner le projet

```bash
git clone https://github.com/abdoufk77/gestion-charite.git
cd gestion-charite
```

---

### ▶️ Option 1 — Lancement avec Maven (sans Docker)

```bash
mvn clean install
mvn spring-boot:run
```

---

### 🐳 Option 2 — Lancement avec Docker (recommandé)

> ⚠️ **Docker Desktop doit être lancé** avant d'exécuter ces commandes.

```bash
docker-compose up --build
```

Pour arrêter :
```bash
docker-compose down
```

---

### 🌐 Accès à l'application

| Service                   | URL                              |
|---------------------------|----------------------------------|
| Application               | http://localhost:8080            |
| Pgadmin (base de données) | http://localhost:5050/ |

> **Connexion H2 Console :**
> - Username : `admin@admin.com`
> - Password : `admin`

---

## 👥 Équipe

| Nom     | Rôle |
|---------|------|
| Abdou   | Développeur |
| kaoutar | Développeur |

---
