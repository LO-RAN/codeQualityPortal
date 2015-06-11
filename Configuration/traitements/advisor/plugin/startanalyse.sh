#!/bin/sh
export JAVA_PATH=$JAVA_HOME
export PATH=$ADVISOR_HOME/lib:$PATH:
# set home drive and directory to allow Advisor to find its configuration files...
cd $ADVISOR_HOME/bin

$JAVA_PATH/bin/java -DADVISOR_HOME=$ADVISOR_HOME -Djava.library.path=$PATH -Xmx500m -cp $JAVA_PATH/lib/tools.jar:$ADVISOR_HOME/lib/DLM40JNI.jar:$ADVISOR_HOME/bin/OptimalAdvisor.jar:/opt/Compuware/caqs/Traitements/advisor/plugin/log4j-1.2.8.jar:/opt/Compuware/caqs/Traitements/advisor/plugin/advisorplugin-1.2.jar com.compuware.carscode.plugin.advisor.OptimalAdvisorAnalyser $3 $4 $5 /opt/Compuware/caqs/Traitements/advisor/plugin/configAdvisor.conf
