set JAVA_CMD=%1
set JAVA_LIB=%2
set PATH=%ADVISOR_HOME%\lib;%PATH%;
cd %ADVISOR_HOME%\bin
%JAVA_CMD% -DADVISOR_HOME="%ADVISOR_HOME%" -Djava.library.path="%PATH%" -Xmx1024m -cp %JAVA_LIB%;"%ADVISOR_HOME%\lib\DLM40JNI.jar;%ADVISOR_HOME%/bin/OptimalAdvisor.jar;../traitements/advisor/plugin/log4j-1.2.8.jar;../traitements/advisor/plugin/;../traitements/advisor/plugin/advisorplugin-1.2.jar" com.compuware.carscode.plugin.advisor.OptimalAdvisorAnalyser %3 %4 %5 ../traitements/advisor/plugin/configAdvisor.conf
