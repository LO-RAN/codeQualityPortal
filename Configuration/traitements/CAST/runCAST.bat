@echo off


rem IMPORTANT : r�pertoire source EA pass� en param�tre

if "%1"=="" goto ERROR
set CAQS_SRC_DIR=%1

rem -------------------------------------------------------------------------------

rem r�pertoire dans lequel sont cr��s les batches CAST correspondant � l'analyse.
set CAST_BATCH_HOME=%CAQS_SRC_DIR%\CAST_batch

rem On appelle le batch CAST qui r�alise l'analyse des sources et la g�n�ration des fichiers pivots.
call %CAST_BATCH_HOME%\runAnalyse.bat
rem en cas d'anomalie, le batch appel� doit retourner un code d'erreur sup�rieur � 0
if errorlevel 1 echo **** ERROR : CAST analysis failed ****

goto END
:ERROR
echo ***** ERROR : parameters missing *****
echo usage : %0  sourceDirectory
:END
rem -------------------------------------------------------------------------------
