# BotView

Application web de **revue automatique de Pull Requests**. Elle se connecte à un dépôt GitHub (GitLab et Bitbucket prévus), reçoit un webhook à chaque événement de PR, analyse le code et publie une review.

- **Backend** : Micronaut + Maven, JDBC
- **Frontend** : Svelte + Vite, UI Bootstrap
- **Base de données** : DuckDB (fichier local)
- **Exposition du webhook** : ngrok

---

## Workflow cible

```
 GitHub / GitLab / Bitbucket (PR)
              |
              v
        Webhook (queue)
           |          |
           v          |
   Analyses statiques |
           |          |
           v          |
     Revue par LLM <--+
           |
           v
 Agrégation, déduplication, fusion
        |             |
        v             v
  Commentaire PR   Dashboard / Historique (DB)
```

---

## Prérequis

- JDK 27 
- Node.js 20+ et npm
- Maven (ou le wrapper `backend/mvnw`)
- Un compte [ngrok](https://ngrok.com) (gratuit) pour recevoir les webhooks en local
- Un token GitHub (voir plus bas)

---

## Variables d'environnement

| Variable  | Rôle |
|---|---|
| `GITHUB_TOKEN`  | Token d'accès à l'API GitHub |
| `GITLAB_TOKEN`  | Token GitLab |
| `BITBUCKET_TOKEN`    | Token Bitbucket |
| `NGROK_AUTHTOKEN` |  | Authentifie l'application auprès de ngrok |
| `NGROK_URL` | url recu de ngrok| 

Ces variables sont lues dans `backend/src/main/resources/application.yml` (`${GITHUB_TOKEN:}` etc.). Si une variable n'est pas définie, la valeur par défaut est vide. Pour GitHub, l'appel à l'API échouera alors avec une erreur d'authentification.

### GitHub : `GITHUB_TOKEN`

1. GitHub → **Settings → Developer settings → Personal access tokens**.
2. Créer un token **classic** avec les scopes :
   - `repo` (lire les dépôts et les PR)
   - `admin:repo_hook` (créer et supprimer les webhooks)
3. Copier le token (il n'est affiché qu'une fois).

### GitLab : `GITLAB_TOKEN` et Bitbucket : `BITBUCKET_TOKEN`

pareil que github

### ngrok : `NGROK_AUTHTOKEN`

Les webhooks GitHub doivent atteindre une URL publique. En local, ngrok crée un tunnel vers `http://localhost:8080`.

1. Créer un compte sur [dashboard.ngrok.com](https://dashboard.ngrok.com).
2. Récupérer l'authtoken dans **Your Authtoken**.
3. Le définir dans la variable `NGROK_AUTHTOKEN`. Le code utilise `Session.withAuthtokenFromEnv()`, qui lit exactement cette variable.
4. Récupérer un **domaine statique gratuit** dans **Domains** (par exemple `xxx-yyy-zzz.ngrok-free.dev`).

### ngrok : l'URL publique (domaine)

Pour désactiver ngrok (par exemple pour travailler sans webhook), mettre `ngrok.enabled: false` dans `application.yml`. Les tests le désactivent déjà via `application-test.yml`.

--- 

## Lancer le projet

Le frontend est déjà compilé dans `backend/src/main/resources/public` et il est servi directement par le backend. **Node.js n'est donc pas nécessaire pour utiliser l'application** : il suffit de lancer le backend. L'application est ensuite disponible sur http://localhost:8080.

Pense à définir les [variables d'environnement](#variables-denvironnement) avant de lancer.

### Option 1 : lancer le jar

```bash
cd backend
mvn package                       # génère le jar dans backend/target/
java -jar target/<nom-du-jar>.jar
```

Le dossier `data/` (base DuckDB) est créé à côté du jar. Le modèle LLM est téléchargé dans `./models/` par rapport au dossier depuis lequel on lance la commande (modifiable avec `LLM_MODEL_PATH`).

### Option 2 : lancer directement le backend avec Maven

```bash
cd backend
mvn mn:run
```

### Premier démarrage

Au premier lancement, le modèle LLM (plusieurs Go) est téléchargé en tâche de fond. L'application est utilisable pendant ce temps, mais la revue par LLM n'est disponible qu'une fois le modèle chargé.

--- 

## Structure du projet

```
botview/
├── backend/                  # Micronaut (Java, Maven)
│   └── src/main/java/fr/uge/
│       ├── controller/       # API REST (/api/...)
│       ├── git/              # Providers GitHub / GitLab / Bitbucket + DTO
│       ├── database/         # Requêtes JDBC DuckDB
│       ├── llm/              # Téléchargement et exécution du modèle
│       ├── nGrok/            # Tunnel ngrok au démarrage
│       └── DatabaseInitializer.java
├── frontend/                 # Svelte + Vite + Bootstrap
│   └── src/{pages,components,api,lib,types}
└── package.json              # scripts dev / build
```


## Choix techniques et problèmes rencontrés

### Clés étrangères et `UPDATE` dans DuckDB

#### Problème

DuckDB implémente un `UPDATE` comme un `DELETE` suivi d'un `INSERT`. Quand une table parent (ici `PULL_REQUEST`) est référencée par une clé étrangère dans d'autres tables (`REVIEW`, `MODIFIES`), la mise à jour d'une de ses lignes échoue : au moment du `DELETE` interne, des lignes enfants référencent encore l'ancienne ligne, ce qui viole la contrainte.

On obtenait donc l'erreur suivante dès qu'une PR avait déjà une review :

```
Constraint Error: Violates foreign key constraint because key "pull_request_id: 1"
is still referenced by a foreign key in a different table.
```

Il s'agit d'une limitation documentée de DuckDB sur les clés étrangères.

#### Solution de contournement

Les contraintes `REFERENCES` ont été supprimées dans les tables enfants, tout en conservant la colonne qui porte la référence (par exemple `pull_request_id`). Les jointures restent possibles et s'écrivent de la même façon, car un `JOIN` ne dépend pas de l'existence d'une contrainte.

#### Conséquence

La base ne garantit plus l'intégrité référentielle. C'est le code Java qui doit s'en charger, par exemple en supprimant les enregistrements enfants avant le parent.
 

