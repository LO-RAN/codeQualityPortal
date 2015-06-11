@echo off
set PRG_DIR=%~dp0
set PRG=java -Xmx1024m -cp %PRG_DIR%\view2exo.jar com.compuware.caqs.util.View2Exo

%PRG% users.csv roles.csv output.xml


