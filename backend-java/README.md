# AGRIFLOW - Backend Java (Module 5: Collaborations)

## Prérequis
- **Java 17+**
- **Maven 3.6+**
- **MySQL 8.x** via **XAMPP**
- **IntelliJ IDEA** (recommandé)

## Configuration

### 1. Base de données
1. Démarre **XAMPP** → Lance MySQL
2. Ouvre **MySQL Workbench** ou **phpMyAdmin**
3. Exécute le script : `db/module5_collaborations.sql`
4. Vérifie que la base `agriflow` et les tables existent

### 2. Configuration JDBC
Le fichier `DBConnection.java` est configuré pour XAMPP par défaut :
- Host: `localhost:3306`
- User: `root`
- Password: *(vide)*

Si ton mot de passe MySQL est différent, modifie `DBConnection.java`.

## Lancer le projet

### Avec IntelliJ IDEA
1. Ouvre le dossier `backend-java` comme projet Maven
2. Laisse Maven télécharger les dépendances
3. Lance `Main.java` (clic droit → Run)

### Avec ligne de commande
```bash
cd backend-java
mvn clean compile exec:java -Dexec.mainClass="tn.esprit.agriflow.Main"
```

## Fonctionnalités (Séance 3 - JDBC CRUD)
- ✅ Créer/Lister/Modifier/Supprimer demandes de collaboration
- ✅ Créer/Lister/Modifier/Supprimer candidatures
- ✅ Menu console interactif
- ✅ Connexion JDBC MySQL (XAMPP)

## Structure du code
```
src/main/java/tn/esprit/agriflow/
├── config/
│   └── DBConnection.java          (Connexion MySQL singleton)
├── collab/
│   ├── CollabRequest.java         (Model demande)
│   ├── CollabApplication.java     (Model candidature)
│   ├── CollabRequestDAO.java      (CRUD demandes)
│   ├── CollabApplicationDAO.java  (CRUD candidatures)
│   └── CollabConsoleApp.java      (Application console)
└── Main.java                      (Point d'entrée)
```

## Auteur
**Membre 5** - Module Collaborations (recherche partenaires)  
PIDEV 2026 - ESPRIT

---

## 🤝 Contribution au projet collaboratif

Ce module est prêt à être intégré au repository partagé **MaatougAyoub/AgriFlow**.

### Fichiers de contribution préparés
- ✅ `CONTRIBUTING.md` : Guide de contribution détaillé
- ✅ `src/main/resources/module5-schema.sql` : Script SQL standalone
- ✅ Architecture 3-tiers conforme aux standards du projet
- ✅ Documentation complète (README + commentaires code)

### Pour contribuer
1. Lire le guide : [CONTRIBUTING.md](./CONTRIBUTING.md)
2. Suivre les étapes de clonage et branche feature
3. Copier le module vers le repo partagé
4. Créer une Pull Request

### Statut
- **Repository personnel** : `YakineEddine/agriflow` (backup + portfolio)
- **Repository collaboratif** : `MaatougAyoub/AgriFlow` (intégration équipe)
- **Branche de contribution** : `feature/module5-collaborations` (à créer)

### Commandes Git de contribution
```bash
# Cloner le repo partagé
git clone https://github.com/MaatougAyoub/AgriFlow.git AgriFlow-Team
cd AgriFlow-Team

# Créer branche feature
git checkout -b feature/module5-collaborations

# [Copier le code manuellement]

# Commit et push
git add backend-java/Module5-Collaborations/
git commit -m "feat(module5): Add Collaborations module"
git push -u origin feature/module5-collaborations

# Créer PR sur GitHub
```

### Checklist avant PR
- [ ] Code compile sans erreur (`mvn clean compile`)
- [ ] Tests manuels passent (console app)
- [ ] Script SQL testé dans phpMyAdmin
- [ ] Documentation à jour (README + CONTRIBUTING)
- [ ] Pas de données sensibles (mots de passe, tokens)
- [ ] Structure conforme au repo partagé
