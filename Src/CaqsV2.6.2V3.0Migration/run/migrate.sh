#!/bin/sh
java -cp .:alturalibDeployApplication.jar:commons-lang-2.0.jar:jcommon-0.9.5.jar:log4j-1.2.3.jar:migration262to30-1.0.jar:migration26to262-1.0.jar:ojdbc14.jar:toolbox-2.4.jar:xerces.jar com.compuware.carscode.migration.Migration262to30
