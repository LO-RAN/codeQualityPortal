REM Copy du snapshot N-1
"C:\Program Files\CAST\5.1.2 for Oracle\ApmAdmin.exe" /ORACLE:RECCAQS /U:system /P:apec75 /Schema:APM_METRICS /copysnapshot:Snapshot_Caqs >%~dp0Logs\copySnapshot.log
if errorlevel 1 echo **** ERROR ****

REM Calcul du snapshot N
"C:\Program Files\CAST\5.1.2 for Oracle\ApmAdmin.exe" /ORACLE:RECCAQS /U:system /P:apec75 /Schema:APM_METRICS /full >%~dp0Logs\full.log
if errorlevel 1 echo **** ERROR ****
 
