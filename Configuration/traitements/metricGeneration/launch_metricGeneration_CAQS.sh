#!/bin/sh

java -classpath ../Traitements/metricGeneration/lib/checkstyle-all-4.3.jar:../Traitements/metricGeneration/lib/checkstyle-optional-4.3.jar:../Traitements/metricGeneration/lib/metricgeneration-1.1.jar com.compuware.caqs.metricGeneration.Main -c ../Traitements/metricGeneration/config/method_metrics.xml -r $1 -f xml -o $2/methodMetrics.xml
java -classpath ../Traitements/metricGeneration/lib/checkstyle-all-4.3.jar:../Traitements/metricGeneration/lib/checkstyle-optional-4.3.jar:../Traitements/metricGeneration/lib/metricgeneration-1.1.jar com.compuware.caqs.metricGeneration.Main -c ../Traitements/metricGeneration/config/class_metrics.xml -r $1 -f xml -o $2/classMetrics.xml
