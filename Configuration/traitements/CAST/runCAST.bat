@echo off


rem IMPORTANT : répertoire source EA passé en paramètre

if "%1"=="" goto ERROR
set CAQS_SRC_DIR=%1

rem -------------------------------------------------------------------------------

rem répertoire dans lequel sont créés les batches CAST correspondant à l'analyse.
set CAST_BATCH_HOME=%CAQS_SRC_DIR%\CAST_batch

rem On appelle le batch CAST qui réalise l'analyse des sources et la génération des fichiers pivots.
call %CAST_BATCH_HOME%\runAnalyse.bat
rem en cas d'anomalie, le batch appelé doit retourner un code d'erreur supérieur à 0
if errorlevel 1 echo **** ERROR : CAST analysis failed ****

goto END
:ERROR
echo ***** ERROR : parameters missing *****
echo usage : %0  sourceDirectory
:END
rem -------------------------------------------------------------------------------
