# Projet compagnie aérienne - V2

Corentin BRIAND G2 -- Travail individuel


## Ce qui a été conservé de la V1

- structure Maven
- classes métier de la V1 / V2
- CRUD en mémoire avec `Map`
- lecture / écriture de fichiers CSV
- tests unitaires JUnit
- compatibilité GitHub via `.gitignore`

## Nouveaux

### 1. Gestion plus réaliste du pilote

Un pilote n'a pas un seul vol définitif, mais une **liste de vols prévus**.
En revanche, il **ne peut pas être affecté à deux vols qui se chevauchent**.

La classe `Pilote` contient donc maintenant :
- une `List<Vol>` pour les vols prévus
- une vérification de disponibilité selon les horaires
- un refus d'affectation si un autre vol se superpose

### 2. Gestion plus réaliste du personnel cabine

Le personnel cabine possède lui aussi une liste de vols affectés avec vérification de disponibilité.

### 3. Utilisation de plusieurs structures de données

- `Map` / `LinkedHashMap` : stockage CRUD par identifiant
- `List` / `ArrayList` : collections métier (vols, réservations, équipage, passagers)
- `Deque` : utilisée comme file d'attente pour la **liste d'attente d'un vol complet**

### 4. Vol complet et liste d'attente

Si un vol est complet :
- la réservation est créée avec le statut `EN_ATTENTE`
- le passager est ajouté dans une file d'attente
- si une réservation confirmée est annulée, le premier passager en attente est promu


## Structure

```text
airline-v2/
├─ .gitignore
├─ pom.xml
├─ README.md
├─ src/
│  ├─ main/
│  │  ├─ java/com/compagnieaerienne/
│  │  └─ resources/data/vols.csv
│  └─ test/java/com/compagnieaerienne/
```

## Commandes Maven

Compiler :
```bash
mvn compile
```

Exécuter le projet :
```bash
mvn exec:java
```

Lancer les tests :
```bash
mvn test
```

## Tests présents

- disponibilité d'un avion
- disponibilité d'un pilote selon chevauchement horaire
- réservation confirmée
- réservation en attente si vol complet
- promotion depuis la liste d'attente après annulation
- refus d'affectation d'équipage si le pilote est déjà occupé
- import CSV des vols
