echo *** Analyse de l'application et calcul des m�triques de base
rem NB: %~dp0 retourne le r�pertoire dans lequel est stock� le batch courant.

call %~dp0ALL.bat
if errorlevel 1 echo **** ERROR ****


echo *** Calcul du snapshot
call %~dp0Snapshot.bat
if errorlevel 1 echo **** ERROR ****

echo *** G�n�ration de fichiers pivots
call %~dp0Pivots.bat
if errorlevel 1 echo **** ERROR ****

