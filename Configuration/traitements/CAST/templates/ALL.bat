REM Lancement du projet via le fichier ACX
"C:\Program Files\CAST\5.1.2 for Oracle\AnaMan.exe" -APP( -!NOGUI ) -LOGIN(,THEED\Administrator,) -LOAD(E:\CAQS_analyses\projets\jMeter\CAST_batch\project.acx) -RUN_CHECKED -EXIT 

REM Recharger le fichier ACX par defaut
"C:\Program Files\CAST\5.1.2 for Oracle\AnaMan.exe" -APP( -!NOGUI ) -LOGIN(,THEED\Administrator,) -LOAD(C:\CAST Implementation Framework\Implementation\Profile\RECCAQS_KB_CAQS_ANALYSE.acx) -EXIT 
