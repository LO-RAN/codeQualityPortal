#!/bin/sh
ANT_HOME=`dirname $0`/..

CLASSPATH=$ANT_HOME/lib/ant-launcher-1.6.5.jar:$ANT_HOME/lib/ant-1.6.5.jar
CLASSPATH=$CLASSPATH:$ANT_HOME/batch/lib/unifaceflowlauncher-1.0.jar
CLASSPATH=$CLASSPATH:$ANT_HOME/batch/lib/uniface_flow-1.0.jar
CLASSPATH=$CLASSPATH:$ANT_HOME/batch/lib/unet-1.0.jar
CLASSPATH=$CLASSPATH:$ANT_HOME/batch/lib/log4j-1.2.14.jar

$JAVA_HOME/bin/java -classpath $CLASSPATH -Dant.home=$ANT_HOME org.apache.tools.ant.Main  -buildfile $ANT_HOME/batch/buildAnalyse.xml

