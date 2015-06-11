@echo off

set ANT_HOME=..
set ANT_CP=%ANT_HOME%\ant-launcher-1.6.5.jar;%ANT_HOME%\ant-1.6.5.jar;.\commons-logging.jar;.\importExport-1.3.jar;%TOMCATDIR%\common\endorsed\oracle-jdbc-1.4.jar;%TOMCATDIR%\common\endorsed\jtds-1.2.jar;%TOMCATDIR%\common\endorsed\hsqldb.jar;%TOMCATDIR%\common\endorsed\mysql-connector-java-5.0.3-bin.jar
java -Xmx1024m -classpath "%ANT_CP%" "-Dant.home=%ANT_HOME%" org.apache.tools.ant.Main %*

