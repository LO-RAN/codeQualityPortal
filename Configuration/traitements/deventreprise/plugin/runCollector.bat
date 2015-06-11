REM *****************************************
REM * Compuware Corporation
REM * One Campus Martius
REM * Detroit, Michigan 48226-5099
REM * This file contains information that is
REM * proprietary to Compuware Corporation
REM * and may not be copied duplicated,
REM * translated, transmitted, stored,
REM * retrieved or in any manner or by any 
REM * method conveyed or disclosed to a
REM * third party or parties without express 
REM * written permission from Compuware 
REM * Corporation.
REM * Copyright(C) 2007 Compuware Corporation
REM *****************************************
@ECHO OFF
@ECHO.

SETLOCAL

TITLE Metadata Analyzer Collector

CALL runTimeSiteValues.bat
CALL runTimeValues.bat Collector

IF "%1"=="-d" GOTO deleteColl
IF "%1"=="-D" GOTO deleteColl

SET DeleteCollected=false
IF "%1NULL"=="NULL" GOTO usageMsg
SET CollectionName=%1%
IF "%2NULL"=="NULL" GOTO defValues
IF "%3NULL"=="NULL" GOTO usageMsg
SET MDSrvrHost=%1%
SET MDSrvrPort=%2%
SET CollectionName=%3%
GOTO runCollection

:deleteColl
SET DeleteCollected=true
IF "%2NULL"=="NULL" GOTO usageMsg
SET CollectionName=%2%
IF "%3NULL"=="NULL" GOTO defValues
IF "%4NULL"=="NULL" GOTO usageMsg
SET MDSrvrHost=%2%
SET MDSrvrPort=%3%
SET CollectionName=%4%
GOTO runCollection

:defValues
SET MDSrvrHost=%MDS_HOST%
SET MDSrvrPort=%MDS_PORT%
GOTO runCollection

:runCollection
TITLE Metadata Analyzer Collection %CollectionName%
@ECHO Running collection %CollectionName%
java -Xmx1024m -cp "%WrkCP%" -DCWCFG="%CWCFG%" -DXPPCFG="%XPPCFG%" -DXWMACFG="%XWMACFG%" %METADATA_PKG%.collector.CollectionController %MDSrvrHost% %MDSrvrPort% %CollectionName% %DeleteCollected%
GOTO Complete

:usageMsg
@ECHO FATAL Invalid parameter list.
@ECHO FATAL runCollector syntax ([] denotes an optional parameter).
@ECHO FATAL
@ECHO FATAL   runCollector [-d] [MDSHost] [MDSPort] Collection
@ECHO FATAL
@ECHO FATAL   Parameters:
@ECHO FATAL     -d          Delete previously collected entities
@ECHO FATAL     MDSHost     Metadata Server host machine name
@ECHO FATAL     MDSPort     Metadata Server port number
@ECHO FATAL     Collection  Collection name
@ECHO FATAL
@ECHO FATAL Processing stopped.

:Complete
ENDLOCAL
