set JAVA_PATH=%JAVA_HOME%
"%JAVA_PATH%/bin/java.exe" -Xmx512m -cp "%JAVA_PATH%/lib/tools.jar;../traitements/genericParser/log4j-1.2.14.jar;../traitements/genericParser/;../traitements/genericParser/genericparser-1.0.jar;../traitements/genericParser/toolbox-2.5.jar" com.compuware.caqs.business.parser.GenericParser %1 %2 %3 %4 %5
