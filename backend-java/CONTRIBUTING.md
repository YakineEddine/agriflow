# 🤝 Guide de Contribution - Module 5 Collaborations

## Structure du module

Ce module implémente la gestion des demandes de collaboration entre agriculteurs.

### Architecture
- **entities/** : Modèles métier (CollabRequest, CollabApplication)
- **services/** : Logique business avec interfaces (IService, CollabRequestService, CollabApplicationService)
- **utils/** : Classes utilitaires (MyDatabase singleton)
- **tests/** : Application console CRUD (Main.java)

### User Stories (6 US - 23 story points)
1. US 5.1 : Publier une demande de collaboration (5 pts) ✅
2. US 5.2 : Consulter les demandes disponibles (3 pts) ✅
3. US 5.3 : Envoyer une candidature (4 pts) ✅
4. US 5.4 : Valider/rejeter demandes (admin) (3 pts) ✅
5. US 5.5 : Gérer mes candidatures (5 pts) ✅
6. US 5.6 : Recevoir notifications (3 pts) ✅

## Comment contribuer au repo partagé

### Prérequis
- Avoir cloné le repo partagé : `git clone https://github.com/MaatougAyoub/AgriFlow.git`
- Avoir les droits de collaborateur sur MaatougAyoub/AgriFlow

### Étapes de contribution

#### 1. Cloner le repo partagé
```bash
cd C:\Users\Home\Desktop
git clone https://github.com/MaatougAyoub/AgriFlow.git AgriFlow-Team
cd AgriFlow-Team
```

#### 2. Créer une branche feature
```bash
git checkout -b feature/module5-collaborations
```

#### 3. Copier le module
Copier le contenu de `backend-java/src/main/java/` vers :
```
AgriFlow-Team/backend-java/Module5-Collaborations/src/main/java/
```

Structure cible :
```
AgriFlow-Team/
└── backend-java/
    └── Module5-Collaborations/
        ├── pom.xml
        └── src/
            └── main/
                └── java/
                    ├── entities/
                    ├── services/
                    ├── tests/
                    └── utils/
```

#### 4. Adapter le pom.xml
Créer `Module5-Collaborations/pom.xml` :

```xml
<?xml version="1.0" encoding="UTF-8"?>
```

#### 5. Commit et push
```bash
git add backend-java/Module5-Collaborations/
git commit -m "feat(module5): Add Collaborations module with 3-tier architecture

- entities: CollabRequest, CollabApplication
- services: IService, CollabRequestService, CollabApplicationService  
- utils: MyDatabase (Singleton)
- tests: Main console CRUD application
- 6 user stories (23 story points)
- JDBC integration with MySQL"

git push -u origin feature/module5-collaborations
```

#### 6. Créer le Pull Request
1. Aller sur https://github.com/MaatougAyoub/AgriFlow
2. Cliquer sur "Compare & pull request"
3. Remplir la description du PR
4. Assigner les reviewers (MaatougAyoub + autres membres)
5. Créer le PR

## Base de données

### Tables utilisées par ce module
- `users` (partagée avec Module 1)
- `collab_requests` (spécifique Module 5)
- `collab_applications` (spécifique Module 5)

### Script SQL à intégrer
Voir `backend-java/src/main/resources/schema.sql` pour le script de création complet.

## Technologies
- Java 17
- JDBC pur (MySQL Connector/J 8.3.0)
- Maven
- MySQL 8.x (XAMPP)
- Architecture 3-tiers (entities/services/tests/utils)

## Tests
- Application console interactive (8 opérations CRUD)
- Connexion MySQL validée
- Persistance vérifiée dans phpMyAdmin

## Contact
- Développeur : YakineEddine
- Module : 5 - Collaborations
- Repo personnel : https://github.com/YakineEddine/agriflow
