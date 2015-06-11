@echo off
set PRG_DIR=%~dp0
set CLEAN_PRG=java -Xmx1024m -cp %PRG_DIR%unifaceProfiling2caqs.jar com.compuware.caqs.util.CleanTrace
set CALLS_PRG=java -Xmx1024m -cp %PRG_DIR%unifaceProfiling2caqs.jar com.compuware.caqs.util.Trace2Calls
set MATRIX_PRG=java -Xmx1024m -cp %PRG_DIR%unifaceProfiling2caqs.jar com.compuware.caqs.util.Calls2Matrix

echo processing %1...

IF NOT EXIST %1\reports MKDIR %1\reports
echo Cleaning trace...
%CLEAN_PRG% %1\src\profiling.txt %1\reports\profiling_clean.csv
echo Building calls...
%CALLS_PRG% %1\reports\profiling_clean.csv %1\reports
echo Building matrix...
%MATRIX_PRG% %1\reports

