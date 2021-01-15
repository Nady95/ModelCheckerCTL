# Model-Checker CTL - *Systèmes complexes, M2 PLS*
`Model-Checker CTL` est une librairie Java vous permettant de vérifier si une [structure de Kripke](https://fr.wikipedia.org/wiki/Structure_de_Kripke) vérifie une [formule CTL](https://en.wikipedia.org/wiki/Computation_tree_logic).

Il a été réalisé dans le cadre d'un projet de **Systèmes complexes** en Master 2 Informatique (PLS) à l'Institut Galilée (USPN). Voici les membres du groupe y ayant participé :

- Nady SADDIK
- Rémi PHYU THANT THAR

##  Utilisation

### Dépendances et compilation

Afin de fonctionner correctement, `Model-Checker CTL` nécessite l'utilisation de la librairie [Gson](https://github.com/google/gson). Le fichier jar utilisé lors du développement correspondait à la version 2.3 est accessible en téléchargement [sur Maven](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.3/gson-2.3.jar).

### Comment s'en servir ?
L'utilisation de Model-Checker CTL est relativement simple. La structure de Kripke et la formule CTL *(voir la partie syntaxe)* à vérifier doivent être stockées dans un fichier json de la forme suivante :

    {
      "states": [
        {
          "name": "",
          "isInitial": "",
          "labels": [
            {
              "atom": "",
              "negation": ""
            },
            ...
          ]
        },
        ...
      ],
      "transitions": [
        {
          "stateA": "",
          "stateB": ""
        },
        ...
      ],
      "ctlformula": "..."
    }

Où :

- states : liste des états
- name : nom de l'état
- isInitial : booléen {true,false} indiquant si l'état est initial
- labels : liste des labels vérifiés par l'état
- atom : proposition atomique
- negation : booléen {true,false} indiquant si on veut la négation de la proposition atomique
- transitions : liste des transitions
- stateA : nom de l'état depuis lequel part la transition
- stateB : nom de l'état qui reçoit la transition
- ctlformula : formule CTL à vérifier

Une fois le fichier sauvegardé et le programme compilé, il suffit d'écrire :

    java nom_du_programme_compile chemin_absolu_vers_le_fichier_json

pour que le programme fasse son travail et retourne le résultat.

## Syntaxe
Soit phi, phi1, phi2 des formules CTL. Une formule CTL doit respecter la syntaxe suivante :
|Pour tous les chemins...|Il existe un chemin...| Formule atomique | Opérateurs booléens
|--|--|--|--|
| AF(phi) | EF(phi) | true | phi1 && phi2
| AG(phi) | EG(phi) | false | phi1 \|\| phi2
| A(phi1 U phi2) | E(phi1 U phi2) | [a-z][a-z0-9_]* | not(phi)
| AX(phi) | EX(phi) | | 