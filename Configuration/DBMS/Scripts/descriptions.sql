UPDATE "FACTEUR" SET DESC_FACT = 'Cet objectif a pour but d''évaluer la capacité de corriger à chaud l''application avec un minimum d''effets de bord.' WHERE  ID_FACT = 'MAIN';
UPDATE "FACTEUR" SET DESC_FACT = 'Cet objectif a pour but d''évaluer la facilité d''ajout de fonctionnalités à l''application.' WHERE  ID_FACT = 'EVOL';
UPDATE "FACTEUR" SET DESC_FACT = 'Cet objectif a pour but d''évaluer la possibilité de réutiliser des portions de l''application dans d''autres applications.' WHERE  ID_FACT = 'REUT';
UPDATE "FACTEUR" SET DESC_FACT = 'Cet objectif a pour but d''évaluer l''effort pour atteindre la fiabilité de l''application (effort de test en particulier).' WHERE  ID_FACT = 'FIAB';
UPDATE "FACTEUR" SET DESC_FACT = 'Cet objectif permet d''évaluer la capacité de l''application à rester opérationnelle (montée en charge, etc).' WHERE  ID_FACT = 'ROBU';




UPDATE "CRITERE" SET DESC_CRIT = 'L''utilisation systématique d''accolades améliore la lisibilité du code et évite les erreurs lors de l''ajout d''instructions.' WHERE  ID_CRIT = 'NEEDBRACESCHECK';
UPDATE "CRITERE" SET DESC_CRIT = 'Liens établis dans le sens contraire de ce qui est prévu dans le diagramme d''architecture.' WHERE  ID_CRIT = 'ANTI_ARCHI_CS';
UPDATE "CRITERE" SET DESC_CRIT = 'Liens établis et non prévus dans le diagramme d''architecture.' WHERE  ID_CRIT = 'ANTI_ARCHI_NP';
UPDATE "CRITERE" SET DESC_CRIT = 'Antipattern d''architecture. Cette classe est considérée comme le coeur de l''application. Ce n''est pas orienté objet.' WHERE  ID_CRIT = 'ANTI_BLOB';
UPDATE "CRITERE" SET DESC_CRIT = 'Copier-coller pénalise la fiabilité de l''application. La probabilité pour que certains duplicata soient oubliés est élevée.' WHERE  ID_CRIT = 'ANTI_COPIER_COLLER';
UPDATE "CRITERE" SET DESC_CRIT = 'Une méthode ayant une fuite mémoire en alloue sans jamais la désallouer risquant ainsi de la saturer.' WHERE  ID_CRIT = 'ANTI_FMEM';
UPDATE "CRITERE" SET DESC_CRIT = 'Classe n''existant que le temps de lancer un processus. Compréhension et réutilisation du code deviennent très difficile.' WHERE  ID_CRIT = 'ANTI_POLT';
UPDATE "CRITERE" SET DESC_CRIT = 'Classes utilitaires. Très complexes, elles comprennent un grand nombre de méthodes sans relation.' WHERE  ID_CRIT = 'ANTI_SAK';
UPDATE "CRITERE" SET DESC_CRIT = 'Le code d''au moins une méthode de la classe possède une grande complexité essentielle (non structuré, incompréhensible, etc).' WHERE  ID_CRIT = 'ANTI_SPAG';
UPDATE "CRITERE" SET DESC_CRIT = 'L''utilisation de l''import doit être limitée aux seules classes utilisées.La syntaxe package.* est donc a proscrire.' WHERE  ID_CRIT = 'AVOIDSTARIMPORTCHECK';
UPDATE "CRITERE" SET DESC_CRIT = 'Fonction du nombre de parents (Classe ou interface - OOFANIN) et de la profondeur dans la hiérarchie (DEPTH) de la classe.' WHERE  ID_CRIT = 'CHER';
UPDATE "CRITERE" SET DESC_CRIT = 'Fonction du nombre de classes dont dépend la classe étudiée hors héritage (CBO) et du nombre de données publiques (pub data).' WHERE  ID_CRIT = 'COBJ';
UPDATE "CRITERE" SET DESC_CRIT = 'Fonction du nombre de méthodes appelées (Call_Pair) et de la complexité d''intégration (iv(G)) d''une fonction.' WHERE  ID_CRIT = 'CTRA';
UPDATE "CRITERE" SET DESC_CRIT = 'Les déclarations doivent être faites dans un ordre identique quelque soit la classe ou l''outil utilisé.' WHERE  ID_CRIT = 'DECLARATIONORDERCHECK';
UPDATE "CRITERE" SET DESC_CRIT = 'Fonction de la complexité essentielle (ev(G)) de la méthode.' WHERE  ID_CRIT = 'DEST';
UPDATE "CRITERE" SET DESC_CRIT = 'Des champs hérités sont surchargés risquant d''amener à des disfonctionnement.' WHERE  ID_CRIT = 'HIDDENFIELDCHECK';
UPDATE "CRITERE" SET DESC_CRIT = 'Semblable au couplage traditionnel mais avec des seuils différents.' WHERE  ID_CRIT = 'INTE';
UPDATE "CRITERE" SET DESC_CRIT = 'L''utilisation de la javadoc est indispensable pour tous les éléments réutilisables (public).' WHERE  ID_CRIT = 'JAVADOCMISSING';
UPDATE "CRITERE" SET DESC_CRIT = 'Utilisation de valeurs numériques dans le code (hors déclaration de constante) autre que 0 ou 1.' WHERE  ID_CRIT = 'MAGICNUMBERCHECK';
UPDATE "CRITERE" SET DESC_CRIT = 'Index de maintenabilité : si MI3 inférieur à 85 ou MI4 inférieur ou égal à 85, la note est de 1. Elle est de 4 sinon.' WHERE  ID_CRIT = 'MAIN';
UPDATE "CRITERE" SET DESC_CRIT = 'Fonction de la complexité cyclomatique de la méthode et de la complexité essentielle de la méthode.' WHERE  ID_CRIT = 'PLOG';
UPDATE "CRITERE" SET DESC_CRIT = 'Une méthode non portable utilise des mécanismes spécifiques d''un système d''exploitation.' WHERE  ID_CRIT = 'PORTABILITE';
UPDATE "CRITERE" SET DESC_CRIT = 'Le taux de couverture correspond au nombre de ligne de code exécuter par rapport au nombre de ligne de code écrites.' WHERE  ID_CRIT = 'TCOU';
UPDATE "CRITERE" SET DESC_CRIT = 'Fonction du nombre de lignes de code de la méthode.' WHERE  ID_CRIT = 'TPRO';
UPDATE "CRITERE" SET DESC_CRIT = 'Les méthodes ou variables privées ainsi que les variables locales non utilisées sont mises en évidence dans ce critère.' WHERE  ID_CRIT = 'UNUSEDLOCALORPRIVATE';




UPDATE "METRIQUE" SET DESC_MET = 'Classes utilitaires. Très complexes, elles comprennent un grand nombre de méthodes sans relation.' WHERE  ID_MET = 'SA_KNIFE';
UPDATE "METRIQUE" SET DESC_MET = 'Nombre d''appels d''une méthode vers d''autres méthodes.' WHERE  ID_MET = 'CALL_PAIR';
UPDATE "METRIQUE" SET DESC_MET = 'Nombre de classes dont une classe donnée dépend en dehors de l''héritage.' WHERE  ID_MET = 'CBO';
UPDATE "METRIQUE" SET DESC_MET = 'Taux de commentaires de traitements (commentaire dans le corps du code) d''une méthode.' WHERE  ID_MET = 'COMMENTS';
UPDATE "METRIQUE" SET DESC_MET = 'Une méthode est très similaire à une autre, il est probable qu''elle soit issue d''un copier-coller.Une correction faite dans l''une doit être réalisée aussi dans l''autre. Une factorisation doit être envisagée.' WHERE  ID_MET = 'COPY_PASTE';
UPDATE "METRIQUE" SET DESC_MET = 'C''est la profondeur d''héritage de la classe. Une classe ayant un seul parent et sans grand-parents a une profondeur de 2.' WHERE  ID_MET = 'DEPTH';
UPDATE "METRIQUE" SET DESC_MET = 'La complexité essentielle quantifie dans quelle mesure une méthode comprend du code non structuré. Par exemple saut hors d''une boucle, saut dans une boucle, saut hors ou dans une structure if.' WHERE  ID_MET = 'EVG';
UPDATE "METRIQUE" SET DESC_MET = 'La complexité d''intégration iv(G) est la complexité du module réduit à son graphe d''intégration. Cela reflète la complexité avec laquelle le module appelle les modules directement subordonnés.' WHERE  ID_MET = 'IVG';
UPDATE "METRIQUE" SET DESC_MET = 'Pour chaque élément de donnée de la classe, on calcule par quel pourcentage des méthodes il est utilisé. On fait la moyenne de ces pourcentages et on le retranche de 100%.' WHERE  ID_MET = 'LOCM';
UPDATE "METRIQUE" SET DESC_MET = 'Nombre de classes dérivées.' WHERE  ID_MET = 'NOC';
UPDATE "METRIQUE" SET DESC_MET = 'Nombre de parents directs de la classe. Un Fanin supérieur à 1 indique le multi-héritage.' WHERE  ID_MET = 'OOFANIN';
UPDATE "METRIQUE" SET DESC_MET = 'Classe n''existant que le temps de lancer un processus. Compréhension et réutilisation du code deviennent très difficile.' WHERE  ID_MET = 'POLTERGEIST';
UPDATE "METRIQUE" SET DESC_MET = 'Appel d''une méthode vers elle même. Bien que la récursivité ne soit pas condamnable a priori, il est important de la limiter et en tout les cas de la connaître.' WHERE  ID_MET = 'RECURSIVITE';
UPDATE "METRIQUE" SET DESC_MET = 'RFC est égal au nombre de méthodes de la classe plus le nombre de méthodes accessibles aux objets de la classe par héritage.' WHERE  ID_MET = 'RFC';
UPDATE "METRIQUE" SET DESC_MET = 'Antipattern d''architecture. Cette classe est considérée comme le cœur de l''application. Ce n''est pas orienté objet.' WHERE  ID_MET = 'BLOB';
UPDATE "METRIQUE" SET DESC_MET = 'Le code d''au moins une méthode de la classe possède une grande complexité essentielle (non structuré, incompréhensible, etc).' WHERE  ID_MET = 'SPAGHETTI';


UPDATE "METRIQUE" SET DESC_MET = 'La classe surcharge (override) equals() sans surcharger hashCode()' WHERE  ID_MET = 'R1';
UPDATE "METRIQUE" SET DESC_MET = 'equals() est redéfini (overload) sans être surchargé' WHERE  ID_MET = 'R2';
UPDATE "METRIQUE" SET DESC_MET = 'La classe publique expose un champ public.' WHERE  ID_MET = 'R3';
UPDATE "METRIQUE" SET DESC_MET = 'Les conventions de nom pour Java ne sont pas respectées.' WHERE  ID_MET = 'R4';
UPDATE "METRIQUE" SET DESC_MET = 'Le champ final statique public contient un objet susceptible de muter.' WHERE  ID_MET = 'R5';
UPDATE "METRIQUE" SET DESC_MET = 'La boucle est terminée par une exception.' WHERE  ID_MET = 'R6';
UPDATE "METRIQUE" SET DESC_MET = 'Une méthode surchargeable est invoquée dans le constructeur, clone() ou readObject().' WHERE  ID_MET = 'R7';
UPDATE "METRIQUE" SET DESC_MET = 'Une classe abstraite n''implémente pas l''interface.' WHERE  ID_MET = 'R8';
UPDATE "METRIQUE" SET DESC_MET = 'L''interface est utilisée uniquement pour définir des constantes.' WHERE  ID_MET = 'R9';
UPDATE "METRIQUE" SET DESC_MET = 'La méthode publique ne vérifie pas le paramètre null.' WHERE  ID_MET = 'R10';
UPDATE "METRIQUE" SET DESC_MET = 'La liste des paramètres est plus longue que la recommandation donnée dans les conventions.' WHERE  ID_MET = 'R11';
UPDATE "METRIQUE" SET DESC_MET = 'La classe implémente un Cloneable sans méthode clone().' WHERE  ID_MET = 'R12';
UPDATE "METRIQUE" SET DESC_MET = 'Redéfinition ambiguë de la méthode.' WHERE  ID_MET = 'R13';
UPDATE "METRIQUE" SET DESC_MET = 'La méthode retourne null au lieu d''un tableau de longueur zéro.' WHERE  ID_MET = 'R14';
UPDATE "METRIQUE" SET DESC_MET = 'L''exception attrapée est ignorée.' WHERE  ID_MET = 'R15';
UPDATE "METRIQUE" SET DESC_MET = 'La classe étend java.lang.error à la place de RuntimeException.' WHERE  ID_MET = 'R16';
UPDATE "METRIQUE" SET DESC_MET = 'Une méthode surchargeable est invoquée dans un bloc synchronisé.' WHERE  ID_MET = 'R17';
UPDATE "METRIQUE" SET DESC_MET = 'wait() est invoqué sans avoir utilisé de boucle d''attente' WHERE  ID_MET = 'R18';
UPDATE "METRIQUE" SET DESC_MET = 'La classe interne implémente un sérialisable.' WHERE  ID_MET = 'R19';
UPDATE "METRIQUE" SET DESC_MET = 'La classe sérialisable n''inclut pas de champ ''serialVersionUID''.' WHERE  ID_MET = 'R20';
UPDATE "METRIQUE" SET DESC_MET = 'finalize() n''appelle pas super.finalize().' WHERE  ID_MET = 'R21';
UPDATE "METRIQUE" SET DESC_MET = 'toString() n''est pas surchargé dans la classe publique.' WHERE  ID_MET = 'R22';
UPDATE "METRIQUE" SET DESC_MET = 'equals() n''utilise pas instanceof pour vérifier le type de paramètre.' WHERE  ID_MET = 'R23';
UPDATE "METRIQUE" SET DESC_MET = 'equals() ne vérifie par l''égalité avec ''this''.' WHERE  ID_MET = 'R24';
UPDATE "METRIQUE" SET DESC_MET = 'clone() n''appelle pas super.clone().' WHERE  ID_MET = 'R25';
UPDATE "METRIQUE" SET DESC_MET = 'readResolve() ou writeReplace() sont déclarés privés dans la classe Sérialisable.' WHERE  ID_MET = 'R26';
UPDATE "METRIQUE" SET DESC_MET = 'La classe de type Singleton sérialisable a besoin d''une méthode readResolve().' WHERE  ID_MET = 'R27';
UPDATE "METRIQUE" SET DESC_MET = 'L''objet JDBC n''appelle pas sa méthode close().' WHERE  ID_MET = 'R28';

UPDATE "METRIQUE" SET DESC_MET = 'Nombre total de lignes' WHERE  ID_MET = 'NL';

UPDATE "CRITERE" SET DESC_CRIT = 'Fonction du taux de commentaire de traitement (intra procédures) et de la complexité cyclomatique (v(G)) de la procédure.' WHERE  ID_CRIT = 'COMP';


