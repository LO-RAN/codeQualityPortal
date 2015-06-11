#!/bin/sh
ANT_HOME=`dirname $0`/../..

CLASSPATH=$ANT_HOME/lib/ant-launcher-1.6.5.jar:$ANT_HOME/lib/ant-1.6.5.jar

$JAVA_HOME/bin/java -classpath $CLASSPATH -Dant.home=$ANT_HOME org.apache.tools.ant.Main  -buildfile `dirname $0`/build_report.xml $*

