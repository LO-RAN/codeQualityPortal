@echo off

set PRGDIR=%~dp0 

rem Computes the absolute path of the current installation
cd %~dp0 

rem Set the CATALINA_HOME and CATALINA_BASE to the current tomcat 
set CATALINA_HOME=%~dp0..
set CATALINA_BASE=%~dp0..

rem Set the bonita environment file
set BONITA_OPTS="-DBONITA_HOME=%CATALINA_HOME%\bonita"
set LOG_OPTS="-Djava.util.logging.config.file=%CATALINA_HOME%\conf\logging.properties"
set SECURITY_OPTS="-Djava.security.auth.login.config=%CATALINA_HOME%\conf\jaas.conf"

rem CAQS specific parameters 
set CAQS_OPTS="-Xshare:auto" "-XX:MaxPermSize=256m" "-Xms512m" "-Xmx1024m" "-XX:+HeapDumpOnOutOfMemoryError" "-Duser.language=en" "-Dfile.encoding=UTF-8" "-Djava.awt.headless=true" "-Dh2.bindAddress=127.0.0.1"

rem dynatrace agent settings
rem set DT_OPTS=-agentpath:d:/cpwr/dynaTrace/dynaTrace_4.0.0/agent/lib64/dtagent.dll=name=caqs,server=localhost

set JAVA_OPTS=%DT_OPTS% %CAQS_OPTS% %LOG_OPTS% %SECURITY_OPTS% %BONITA_OPTS% 
set JPDA_TRANSPORT=dt_socket
set JPDA_ADDRESS=8000

rem Launches the server
call catalina.bat %*
