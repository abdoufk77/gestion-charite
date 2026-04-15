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

| Couche | Technologies |
|--------|------------|
| Backend | Spring Boot 3.x, Spring MVC, Spring Security, Spring Data JPA |
| Base de données | PostgreSQL / MySQL |
| Frontend | Thymeleaf |
| Outils | IntelliJ IDEA, Postman, Git, Maven |

---

## 🚀 Lancement

```bash
git clone https://github.com/abdoufk77/gestion-charite.git
mvn clean install
mvn spring-boot:run
```