REM -- Arg0: fichiers xml checkstyle
REM -- Arg1: Le path d'accès aux sources (D:\ReferentielRenault\AnalyseProd\sources\1_Audit_en_cours\GDI(Java)\src\)
REM -- Arg3: fichiers xml advisor
call extractcheckstyle %1 %2 AvoidUsingLiteralInformation V4 extract_V4.csv
call extractcheckstyle %1 %2 HiddenFieldCheck S2 extract_S2.csv
call extractcheckstyle %1 %2 AvoidStarImportCheck JV1 extract_JV1.csv
call extractadvisor %3 %2 EmptyCatchBlock R4 extract_R4.csv
call extractadvisor %3 %2 UnusedPrivateMethod UNUSED extract_CODE_MORT.csv
