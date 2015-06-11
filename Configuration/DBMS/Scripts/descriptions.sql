UPDATE "FACTEUR" SET DESC_FACT = 'Cet objectif a pour but d''�valuer la capacit� de corriger � chaud l''application avec un minimum d''effets de bord.' WHERE  ID_FACT = 'MAIN';
UPDATE "FACTEUR" SET DESC_FACT = 'Cet objectif a pour but d''�valuer la facilit� d''ajout de fonctionnalit�s � l''application.' WHERE  ID_FACT = 'EVOL';
UPDATE "FACTEUR" SET DESC_FACT = 'Cet objectif a pour but d''�valuer la possibilit� de r�utiliser des portions de l''application dans d''autres applications.' WHERE  ID_FACT = 'REUT';
UPDATE "FACTEUR" SET DESC_FACT = 'Cet objectif a pour but d''�valuer l''effort pour atteindre la fiabilit� de l''application (effort de test en particulier).' WHERE  ID_FACT = 'FIAB';
UPDATE "FACTEUR" SET DESC_FACT = 'Cet objectif permet d''�valuer la capacit� de l''application � rester op�rationnelle (mont�e en charge, etc).' WHERE  ID_FACT = 'ROBU';




UPDATE "CRITERE" SET DESC_CRIT = 'L''utilisation syst�matique d''accolades am�liore la lisibilit� du code et �vite les erreurs lors de l''ajout d''instructions.' WHERE  ID_CRIT = 'NEEDBRACESCHECK';
UPDATE "CRITERE" SET DESC_CRIT = 'Liens �tablis dans le sens contraire de ce qui est pr�vu dans le diagramme d''architecture.' WHERE  ID_CRIT = 'ANTI_ARCHI_CS';
UPDATE "CRITERE" SET DESC_CRIT = 'Liens �tablis et non pr�vus dans le diagramme d''architecture.' WHERE  ID_CRIT = 'ANTI_ARCHI_NP';
UPDATE "CRITERE" SET DESC_CRIT = 'Antipattern d''architecture. Cette classe est consid�r�e comme le coeur de l''application. Ce n''est pas orient� objet.' WHERE  ID_CRIT = 'ANTI_BLOB';
UPDATE "CRITERE" SET DESC_CRIT = 'Copier-coller p�nalise la fiabilit� de l''application. La probabilit� pour que certains duplicata soient oubli�s est �lev�e.' WHERE  ID_CRIT = 'ANTI_COPIER_COLLER';
UPDATE "CRITERE" SET DESC_CRIT = 'Une m�thode ayant une fuite m�moire en alloue sans jamais la d�sallouer risquant ainsi de la saturer.' WHERE  ID_CRIT = 'ANTI_FMEM';
UPDATE "CRITERE" SET DESC_CRIT = 'Classe n''existant que le temps de lancer un processus. Compr�hension et r�utilisation du code deviennent tr�s difficile.' WHERE  ID_CRIT = 'ANTI_POLT';
UPDATE "CRITERE" SET DESC_CRIT = 'Classes utilitaires. Tr�s complexes, elles comprennent un grand nombre de m�thodes sans relation.' WHERE  ID_CRIT = 'ANTI_SAK';
UPDATE "CRITERE" SET DESC_CRIT = 'Le code d''au moins une m�thode de la classe poss�de une grande complexit� essentielle (non structur�, incompr�hensible, etc).' WHERE  ID_CRIT = 'ANTI_SPAG';
UPDATE "CRITERE" SET DESC_CRIT = 'L''utilisation de l''import doit �tre limit�e aux seules classes utilis�es.La syntaxe package.* est donc a proscrire.' WHERE  ID_CRIT = 'AVOIDSTARIMPORTCHECK';
UPDATE "CRITERE" SET DESC_CRIT = 'Fonction du nombre de parents (Classe ou interface - OOFANIN) et de la profondeur dans la hi�rarchie (DEPTH) de la classe.' WHERE  ID_CRIT = 'CHER';
UPDATE "CRITERE" SET DESC_CRIT = 'Fonction du nombre de classes dont d�pend la classe �tudi�e hors h�ritage (CBO) et du nombre de donn�es publiques (pub data).' WHERE  ID_CRIT = 'COBJ';
UPDATE "CRITERE" SET DESC_CRIT = 'Fonction du nombre de m�thodes appel�es (Call_Pair) et de la complexit� d''int�gration (iv(G)) d''une fonction.' WHERE  ID_CRIT = 'CTRA';
UPDATE "CRITERE" SET DESC_CRIT = 'Les d�clarations doivent �tre faites dans un ordre identique quelque soit la classe ou l''outil utilis�.' WHERE  ID_CRIT = 'DECLARATIONORDERCHECK';
UPDATE "CRITERE" SET DESC_CRIT = 'Fonction de la complexit� essentielle (ev(G)) de la m�thode.' WHERE  ID_CRIT = 'DEST';
UPDATE "CRITERE" SET DESC_CRIT = 'Des champs h�rit�s sont surcharg�s risquant d''amener � des disfonctionnement.' WHERE  ID_CRIT = 'HIDDENFIELDCHECK';
UPDATE "CRITERE" SET DESC_CRIT = 'Semblable au couplage traditionnel mais avec des seuils diff�rents.' WHERE  ID_CRIT = 'INTE';
UPDATE "CRITERE" SET DESC_CRIT = 'L''utilisation de la javadoc est indispensable pour tous les �l�ments r�utilisables (public).' WHERE  ID_CRIT = 'JAVADOCMISSING';
UPDATE "CRITERE" SET DESC_CRIT = 'Utilisation de valeurs num�riques dans le code (hors d�claration de constante) autre que 0 ou 1.' WHERE  ID_CRIT = 'MAGICNUMBERCHECK';
UPDATE "CRITERE" SET DESC_CRIT = 'Index de maintenabilit� : si MI3 inf�rieur � 85 ou MI4 inf�rieur ou �gal � 85, la note est de 1. Elle est de 4 sinon.' WHERE  ID_CRIT = 'MAIN';
UPDATE "CRITERE" SET DESC_CRIT = 'Fonction de la complexit� cyclomatique de la m�thode et de la complexit� essentielle de la m�thode.' WHERE  ID_CRIT = 'PLOG';
UPDATE "CRITERE" SET DESC_CRIT = 'Une m�thode non portable utilise des m�canismes sp�cifiques d''un syst�me d''exploitation.' WHERE  ID_CRIT = 'PORTABILITE';
UPDATE "CRITERE" SET DESC_CRIT = 'Le taux de couverture correspond au nombre de ligne de code ex�cuter par rapport au nombre de ligne de code �crites.' WHERE  ID_CRIT = 'TCOU';
UPDATE "CRITERE" SET DESC_CRIT = 'Fonction du nombre de lignes de code de la m�thode.' WHERE  ID_CRIT = 'TPRO';
UPDATE "CRITERE" SET DESC_CRIT = 'Les m�thodes ou variables priv�es ainsi que les variables locales non utilis�es sont mises en �vidence dans ce crit�re.' WHERE  ID_CRIT = 'UNUSEDLOCALORPRIVATE';




UPDATE "METRIQUE" SET DESC_MET = 'Classes utilitaires. Tr�s complexes, elles comprennent un grand nombre de m�thodes sans relation.' WHERE  ID_MET = 'SA_KNIFE';
UPDATE "METRIQUE" SET DESC_MET = 'Nombre d''appels d''une m�thode vers d''autres m�thodes.' WHERE  ID_MET = 'CALL_PAIR';
UPDATE "METRIQUE" SET DESC_MET = 'Nombre de classes dont une classe donn�e d�pend en dehors de l''h�ritage.' WHERE  ID_MET = 'CBO';
UPDATE "METRIQUE" SET DESC_MET = 'Taux de commentaires de traitements (commentaire dans le corps du code) d''une m�thode.' WHERE  ID_MET = 'COMMENTS';
UPDATE "METRIQUE" SET DESC_MET = 'Une m�thode est tr�s similaire � une autre, il est probable qu''elle soit issue d''un copier-coller.Une correction faite dans l''une doit �tre r�alis�e aussi dans l''autre. Une factorisation doit �tre envisag�e.' WHERE  ID_MET = 'COPY_PASTE';
UPDATE "METRIQUE" SET DESC_MET = 'C''est la profondeur d''h�ritage de la classe. Une classe ayant un seul parent et sans grand-parents a une profondeur de 2.' WHERE  ID_MET = 'DEPTH';
UPDATE "METRIQUE" SET DESC_MET = 'La complexit� essentielle quantifie dans quelle mesure une m�thode comprend du code non structur�. Par exemple saut hors d''une boucle, saut dans une boucle, saut hors ou dans une structure if.' WHERE  ID_MET = 'EVG';
UPDATE "METRIQUE" SET DESC_MET = 'La complexit� d''int�gration iv(G) est la complexit� du module r�duit � son graphe d''int�gration. Cela refl�te la complexit� avec laquelle le module appelle les modules directement subordonn�s.' WHERE  ID_MET = 'IVG';
UPDATE "METRIQUE" SET DESC_MET = 'Pour chaque �l�ment de donn�e de la classe, on calcule par quel pourcentage des m�thodes il est utilis�. On fait la moyenne de ces pourcentages et on le retranche de 100%.' WHERE  ID_MET = 'LOCM';
UPDATE "METRIQUE" SET DESC_MET = 'Nombre de classes d�riv�es.' WHERE  ID_MET = 'NOC';
UPDATE "METRIQUE" SET DESC_MET = 'Nombre de parents directs de la classe. Un Fanin sup�rieur � 1 indique le multi-h�ritage.' WHERE  ID_MET = 'OOFANIN';
UPDATE "METRIQUE" SET DESC_MET = 'Classe n''existant que le temps de lancer un processus. Compr�hension et r�utilisation du code deviennent tr�s difficile.' WHERE  ID_MET = 'POLTERGEIST';
UPDATE "METRIQUE" SET DESC_MET = 'Appel d''une m�thode vers elle m�me. Bien que la r�cursivit� ne soit pas condamnable a priori, il est important de la limiter et en tout les cas de la conna�tre.' WHERE  ID_MET = 'RECURSIVITE';
UPDATE "METRIQUE" SET DESC_MET = 'RFC est �gal au nombre de m�thodes de la classe plus le nombre de m�thodes accessibles aux objets de la classe par h�ritage.' WHERE  ID_MET = 'RFC';
UPDATE "METRIQUE" SET DESC_MET = 'Antipattern d''architecture. Cette classe est consid�r�e comme le c�ur de l''application. Ce n''est pas orient� objet.' WHERE  ID_MET = 'BLOB';
UPDATE "METRIQUE" SET DESC_MET = 'Le code d''au moins une m�thode de la classe poss�de une grande complexit� essentielle (non structur�, incompr�hensible, etc).' WHERE  ID_MET = 'SPAGHETTI';


UPDATE "METRIQUE" SET DESC_MET = 'La classe surcharge (override) equals() sans surcharger hashCode()' WHERE  ID_MET = 'R1';
UPDATE "METRIQUE" SET DESC_MET = 'equals() est red�fini (overload) sans �tre surcharg�' WHERE  ID_MET = 'R2';
UPDATE "METRIQUE" SET DESC_MET = 'La classe publique expose un champ public.' WHERE  ID_MET = 'R3';
UPDATE "METRIQUE" SET DESC_MET = 'Les conventions de nom pour Java ne sont pas respect�es.' WHERE  ID_MET = 'R4';
UPDATE "METRIQUE" SET DESC_MET = 'Le champ final statique public contient un objet susceptible de muter.' WHERE  ID_MET = 'R5';
UPDATE "METRIQUE" SET DESC_MET = 'La boucle est termin�e par une exception.' WHERE  ID_MET = 'R6';
UPDATE "METRIQUE" SET DESC_MET = 'Une m�thode surchargeable est invoqu�e dans le constructeur, clone() ou readObject().' WHERE  ID_MET = 'R7';
UPDATE "METRIQUE" SET DESC_MET = 'Une classe abstraite n''impl�mente pas l''interface.' WHERE  ID_MET = 'R8';
UPDATE "METRIQUE" SET DESC_MET = 'L''interface est utilis�e uniquement pour d�finir des constantes.' WHERE  ID_MET = 'R9';
UPDATE "METRIQUE" SET DESC_MET = 'La m�thode publique ne v�rifie pas le param�tre null.' WHERE  ID_MET = 'R10';
UPDATE "METRIQUE" SET DESC_MET = 'La liste des param�tres est plus longue que la recommandation donn�e dans les conventions.' WHERE  ID_MET = 'R11';
UPDATE "METRIQUE" SET DESC_MET = 'La classe impl�mente un Cloneable sans m�thode clone().' WHERE  ID_MET = 'R12';
UPDATE "METRIQUE" SET DESC_MET = 'Red�finition ambigu� de la m�thode.' WHERE  ID_MET = 'R13';
UPDATE "METRIQUE" SET DESC_MET = 'La m�thode retourne null au lieu d''un tableau de longueur z�ro.' WHERE  ID_MET = 'R14';
UPDATE "METRIQUE" SET DESC_MET = 'L''exception attrap�e est ignor�e.' WHERE  ID_MET = 'R15';
UPDATE "METRIQUE" SET DESC_MET = 'La classe �tend java.lang.error � la place de RuntimeException.' WHERE  ID_MET = 'R16';
UPDATE "METRIQUE" SET DESC_MET = 'Une m�thode surchargeable est invoqu�e dans un bloc synchronis�.' WHERE  ID_MET = 'R17';
UPDATE "METRIQUE" SET DESC_MET = 'wait() est invoqu� sans avoir utilis� de boucle d''attente' WHERE  ID_MET = 'R18';
UPDATE "METRIQUE" SET DESC_MET = 'La classe interne impl�mente un s�rialisable.' WHERE  ID_MET = 'R19';
UPDATE "METRIQUE" SET DESC_MET = 'La classe s�rialisable n''inclut pas de champ ''serialVersionUID''.' WHERE  ID_MET = 'R20';
UPDATE "METRIQUE" SET DESC_MET = 'finalize() n''appelle pas super.finalize().' WHERE  ID_MET = 'R21';
UPDATE "METRIQUE" SET DESC_MET = 'toString() n''est pas surcharg� dans la classe publique.' WHERE  ID_MET = 'R22';
UPDATE "METRIQUE" SET DESC_MET = 'equals() n''utilise pas instanceof pour v�rifier le type de param�tre.' WHERE  ID_MET = 'R23';
UPDATE "METRIQUE" SET DESC_MET = 'equals() ne v�rifie par l''�galit� avec ''this''.' WHERE  ID_MET = 'R24';
UPDATE "METRIQUE" SET DESC_MET = 'clone() n''appelle pas super.clone().' WHERE  ID_MET = 'R25';
UPDATE "METRIQUE" SET DESC_MET = 'readResolve() ou writeReplace() sont d�clar�s priv�s dans la classe S�rialisable.' WHERE  ID_MET = 'R26';
UPDATE "METRIQUE" SET DESC_MET = 'La classe de type Singleton s�rialisable a besoin d''une m�thode readResolve().' WHERE  ID_MET = 'R27';
UPDATE "METRIQUE" SET DESC_MET = 'L''objet JDBC n''appelle pas sa m�thode close().' WHERE  ID_MET = 'R28';

UPDATE "METRIQUE" SET DESC_MET = 'Nombre total de lignes' WHERE  ID_MET = 'NL';

UPDATE "CRITERE" SET DESC_CRIT = 'Fonction du taux de commentaire de traitement (intra proc�dures) et de la complexit� cyclomatique (v(G)) de la proc�dure.' WHERE  ID_CRIT = 'COMP';


