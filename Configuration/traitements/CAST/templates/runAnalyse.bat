echo *** Analyse de l'application et calcul des métriques de base
rem NB: %~dp0 retourne le répertoire dans lequel est stocké le batch courant.

call %~dp0ALL.bat
if errorlevel 1 echo **** ERROR ****


echo *** Calcul du snapshot
call %~dp0Snapshot.bat
if errorlevel 1 echo **** ERROR ****

echo *** Génération de fichiers pivots
call %~dp0Pivots.bat
if errorlevel 1 echo **** ERROR ****

